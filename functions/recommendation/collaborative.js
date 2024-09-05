/**
 * 
 * write an algorithm to calculate the similarity between users based on their interaction history.
 * collaborative filtering
 *
 * The user interactions are in bigquery, as fetched by the trending algorithm. The events we are interested in are read_item, play_item, add_favorite.
 * 
 * The item metadata is in firestore under item_metadata/{item_id}.
 * the important fields are: item_id, title, creator, subjects, language, collection, format and description (natural langauge).
 * 
 * Save the results in firestore under user_recommendations/{user_id}/{recommendation_id}.
 * 
 * We want to run this algorithm once a week only for the users that have been active in 30 days.
 * Make it efficient in terms of bigquery usage and billing. we can query common things once and store them in firestore.
 * 
 */

const { BigQuery } = require('@google-cloud/bigquery');
const { getFirestore } = require('firebase-admin/firestore');
const { logger } = require('firebase-functions');

const bigquery = new BigQuery();
const db = getFirestore();

const EVENTS_OF_INTEREST = ['read_item', 'play_item', 'add_favorite'];
const ACTIVE_USER_THRESHOLD_DAYS = 60;
const SIMILARITY_THRESHOLD = 0.45;

async function calculateCollaborativeRecommendations() {
    try {
      // Step 1: Get active users from the last 30 days
      const activeUsers = await getActiveUsers();
  
      if (activeUsers.length === 0) {
        logger.info('No active users found');
        return;
      }

      // Step 1.5: Log active users
    //   logger.info('Active users retrieved:', JSON.stringify(activeUsers, null, 2));
  
      // Step 2: Get user interactions for active users
      const userInteractions = await getUserInteractions(activeUsers);

      // Step 2.5: Log user interactions
      logger.info('User interactions retrieved:', JSON.stringify(userInteractions, null, 2));
  
      // Step 3: Calculate user similarity
      const userSimilarities = calculateUserSimilarity(userInteractions);

      // Step 3.5: Log user similarities
      logger.info('User similarities calculated:', JSON.stringify(userSimilarities, null, 2));
  
      // Step 4: Generate recommendations for each user
      for (const userId of activeUsers) {
        if (userId && typeof userId === 'string' && userId.trim() !== '') {
        //   logger.info(`Generating recommendations for user: ${userId}`);
          const recommendations = await generateRecommendations(userId, userSimilarities, userInteractions);
          if (recommendations.length > 5) {
            logger.info(`Generated ${recommendations.length} recommendations for user: ${userId}`);
            await saveRecommendations(userId, recommendations);
          }
          
        } else {
          logger.warn(`Skipping invalid userId: ${userId}`);
        }
      }
  
      logger.info('Collaborative filtering recommendations completed successfully');
    } catch (error) {
      logger.error('Error in collaborative filtering:', error);
    }
  }

async function getActiveUsers() {
    const query = `
      SELECT
        user_pseudo_id AS user_id
      FROM (
        SELECT
          user_pseudo_id,
          COUNT(*) AS open_item_detail_count
        FROM
          \`kafka-books.analytics_195726967.events_*\`
        WHERE
          _TABLE_SUFFIX BETWEEN FORMAT_DATE('%Y%m%d', DATE_SUB(CURRENT_DATE(), INTERVAL ${ACTIVE_USER_THRESHOLD_DAYS} DAY))
            AND FORMAT_DATE('%Y%m%d', CURRENT_DATE())
          AND event_name = 'open_item_detail'
        GROUP BY
          user_pseudo_id
        HAVING
          open_item_detail_count > 5
      ) AS active_users
      WHERE
        user_pseudo_id IN (
          SELECT DISTINCT
            user_pseudo_id
          FROM
            \`kafka-books.analytics_195726967.events_*\`
          WHERE
            _TABLE_SUFFIX BETWEEN FORMAT_DATE('%Y%m%d', DATE_SUB(CURRENT_DATE(), INTERVAL ${ACTIVE_USER_THRESHOLD_DAYS} DAY))
              AND FORMAT_DATE('%Y%m%d', CURRENT_DATE())
            AND event_name IN (${EVENTS_OF_INTEREST.map(event => `'${event}'`).join(',')})
        )
      LIMIT 50
    `;

    // Log the query before execution
    logger.info('Executing BigQuery query for getActiveUsers:', query);
  
    const [rows] = await bigquery.query({ query });
    return rows.map(row => row.user_id);
  }

  async function getUserInteractions(activeUsers) {
    const query = `
    SELECT
      user_pseudo_id AS user_id,
      event_name,
      (SELECT value.string_value FROM UNNEST(event_params) WHERE key = 'item_id') AS item_id,
      COUNT(*) AS interaction_count
    FROM
      \`kafka-books.analytics_195726967.events_*\`
    WHERE
      PARSE_DATE('%Y%m%d', _TABLE_SUFFIX) BETWEEN DATE_SUB(CURRENT_DATE(), INTERVAL ${ACTIVE_USER_THRESHOLD_DAYS} DAY) AND CURRENT_DATE()
      AND event_name IN (${EVENTS_OF_INTEREST.map(event => `'${event}'`).join(',')})
      AND user_pseudo_id IN (${activeUsers.map(user => `'${user}'`).join(',')})
      AND user_pseudo_id IS NOT NULL
    GROUP BY
      user_id, event_name, item_id
    HAVING
      item_id IS NOT NULL
  `;
  
    // Log the query for debugging
    logger.info('getUserInteractions query:', query);
  
    const [rows] = await bigquery.query({ query });
  
    // Log the raw results
    logger.info('Raw getUserInteractions results:', JSON.stringify(rows));
  
    return rows.reduce((acc, row) => {
      if (!acc[row.user_id]) acc[row.user_id] = {};
      if (!acc[row.user_id][row.item_id]) acc[row.user_id][row.item_id] = 0;
      acc[row.user_id][row.item_id] += row.interaction_count;
      return acc;
    }, {});
  }

function calculateUserSimilarity(userInteractions) {
  const similarities = {};
  const users = Object.keys(userInteractions);

  for (let i = 0; i < users.length; i++) {
    for (let j = i + 1; j < users.length; j++) {
      const user1 = users[i];
      const user2 = users[j];
      const similarity = calculateCosineSimilarity(userInteractions[user1], userInteractions[user2]);

      if (similarity > SIMILARITY_THRESHOLD) {
        if (!similarities[user1]) similarities[user1] = [];
        if (!similarities[user2]) similarities[user2] = [];
        similarities[user1].push({ user: user2, score: similarity });
        similarities[user2].push({ user: user1, score: similarity });
      }
    }
  }

  return similarities;
}

function calculateCosineSimilarity(user1Interactions, user2Interactions) {
  let dotProduct = 0;
  let magnitude1 = 0;
  let magnitude2 = 0;

  for (const itemId in user1Interactions) {
    const user1Count = user1Interactions[itemId];
    const user2Count = user2Interactions[itemId] || 0;
    dotProduct += user1Count * user2Count;
    magnitude1 += user1Count * user1Count;
  }

  for (const itemId in user2Interactions) {
    const count = user2Interactions[itemId];
    magnitude2 += count * count;
  }

  magnitude1 = Math.sqrt(magnitude1);
  magnitude2 = Math.sqrt(magnitude2);

  return dotProduct / (magnitude1 * magnitude2);
}

async function generateRecommendations(userId, userSimilarities, userInteractions) {
  const similarUsers = userSimilarities[userId] || [];
  const recommendations = {};

  for (const { user: similarUser, score } of similarUsers) {
    for (const itemId in userInteractions[similarUser]) {
      if (!userInteractions[userId][itemId]) {
        if (!recommendations[itemId]) recommendations[itemId] = 0;
        recommendations[itemId] += score * userInteractions[similarUser][itemId];
      }
    }
  }

  const sortedRecommendations = Object.entries(recommendations)
    .sort((a, b) => b[1] - a[1])
    .slice(0, 50)
    .map(([itemId, score]) => ({ itemId, score }));

  return sortedRecommendations;
}

async function saveRecommendations(userId, recommendations) {
    if (!userId || typeof userId !== 'string' || userId.trim() === '') {
      logger.error(`Invalid userId: ${userId}`);
      return;
    }
  
    const userRecommendationsRef = db.collection('user_recommendations').doc(userId).collection('collaborative');
  
    try {
      const batch = db.batch();
  
      for (const rec of recommendations) {
        const docRef = userRecommendationsRef.doc(rec.itemId);
        batch.set(docRef, {
          score: rec.score
        });
      }
  
      await batch.commit();
  
      logger.info(`Saved ${recommendations.length} recommendations for user ${userId}`);
    } catch (error) {
      logger.error(`Error saving recommendations for user ${userId}:`, error);
    }
  }

module.exports = {
  calculateCollaborativeRecommendations,
};
