/**
 * Content-based filtering recommendation system
 * 
 * This algorithm recommends items to users based on the similarity between item features
 * and user preferences. It analyzes the metadata of items that a user has interacted with
 * and suggests new items with similar characteristics.
 * 
 * Key steps:
 * 1. Build user profiles based on their interaction history
 * 2. Extract and process item features from metadata
 * 3. Calculate similarity between user profiles and item features
 * 4. Generate recommendations based on similarity scores
 * 5. Store recommendations in Firestore for quick access
 * 
 * The algorithm should consider various item attributes such as:
 * - Subjects
 * - Creator
 * - Language
 * - Collection
 * - Format
 * - Description (using natural language processing techniques)
 * 
 * metadata is stored in firestore under metdata/{item_id}/
 * 
 * To optimize performance and reduce costs:
 * - Run the algorithm periodically (e.g., weekly) for active users
 * - Use efficient similarity calculation methods
 * - Leverage caching mechanisms to store intermediate results
 * - keep biquery and firestore
 * - store user profiles in firestore if that's more efficient
 * 
 * make it a firebase cloud function called onrequest
 * 
 * TODO: Implement the content-based filtering algorithm
 */
const functions = require('firebase-functions');
const admin = require('firebase-admin');
const natural = require('natural');
const { BigQuery } = require('@google-cloud/bigquery');

const db = admin.firestore();
const bigquery = new BigQuery();

const EVENTS_OF_INTEREST = ['read_item', 'play_item', 'add_favorite'];
const ACTIVE_USER_THRESHOLD_DAYS = 30;
const SIMILARITY_THRESHOLD = 0.3;
const MAX_RECOMMENDATIONS = 50;

async function generateContentBasedRecommendations() {
  const activeUsers = await getActiveUsers();
  console.log(`Found ${activeUsers.length} active users for content-based recommendations`);
  
  let allUserItemIds = {};

  for (const userId of activeUsers) {
    const userInteractions = await getUserInteractions(userId);
    allUserItemIds[userId] = userInteractions.map(interaction => interaction.item_id);
    
    const userProfile = await buildUserProfile(userId);
    console.log(`Built user profile for user ${userId}:`, JSON.stringify(userProfile, null, 2));
    const recommendations = await generateUserRecommendations(userId, userProfile);
    console.log(`Generated ${recommendations.length} recommendations for user ${userId}`);
    if (recommendations.length > 0) {
      await saveRecommendations(userId, recommendations);
      console.log(`Saved ${recommendations.length} recommendations for user ${userId}`);
    } else {
      console.log(`No recommendations generated for user ${userId}. Skipping save.`);
    }
  }

  // Consolidate all item IDs across all users
  const allItemIds = new Set();
  for (const itemIds of Object.values(allUserItemIds)) {
    itemIds.forEach(id => allItemIds.add(id));
  }

  // Log all item IDs in a single, comma-separated line
  console.log('All item IDs:');
  console.log([...allItemIds].join(','));

  // Log all user IDs in a single, comma-separated line
  console.log('All user IDs:');
  console.log(Object.keys(allUserItemIds).join(','));
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
      LIMIT 500
    `;

  const [rows] = await bigquery.query({ query });
  return rows.map(row => row.user_id);
  // For testing purposes, return a fixed set of user IDs
  // return [
  //   '2fc6c7a7e7e101d923f017cc4df1f786',
  //   '29643dbf9a3146472e0d9ef74de5b1aa',
  //   'a9896783fb0aa798f6f80b389bfd83d8',
  //   '81bf680912fce21ecaf97ae0681fbd41',
  //   '9b01d4fa48d1af95a158e1bf58537fb0'
  // ];
}

async function buildUserProfile(userId) {
  const userInteractions = await getUserInteractions(userId);
  console.log(`Retrieved ${userInteractions.length} interactions for user ${userId}`);
  const profile = {
    subject: {},
    creator: {},
    language: {},
    collection: {},
    mediatype: {},
  };
  
  for (const interaction of userInteractions) {
    const itemMetadata = await getItemMetadata(interaction.item_id);
    console.log(`Processing item ${interaction.item_id}`);
    if (!itemMetadata) {
      console.log(`No metadata found for item ${interaction.item_id}`);
    } else {
      console.log(`Retrieved metadata for item ${interaction.item_id}:`, JSON.stringify(itemMetadata, null, 2));
    }
    if (itemMetadata) {
      updateProfileCounts(profile.subject, itemMetadata.subject);
      updateProfileCounts(profile.creator, itemMetadata.creator);
      updateProfileCounts(profile.language, itemMetadata.language);
      updateProfileCounts(profile.collection, itemMetadata.collection);
      updateProfileCounts(profile.mediatype, itemMetadata.mediatype);
      
      const keywords = itemMetadata.description.flatMap(extractKeywords);
      updateProfileCounts(profile.keywords, keywords);
    }
  }

  normalizeProfile(profile);
  return profile;
}

async function getUserInteractions(userId) {
  const query = `
    SELECT
      user_pseudo_id AS user_id,
      (SELECT value.string_value FROM UNNEST(event_params) WHERE key = 'item_id') AS item_id,
      COUNT(*) AS interaction_count
    FROM
      \`kafka-books.analytics_195726967.events_*\`
    WHERE
      _TABLE_SUFFIX BETWEEN FORMAT_DATE('%Y%m%d', DATE_SUB(CURRENT_DATE(), INTERVAL ${ACTIVE_USER_THRESHOLD_DAYS} DAY))
        AND FORMAT_DATE('%Y%m%d', CURRENT_DATE())
      AND event_name IN (${EVENTS_OF_INTEREST.map(event => `'${event}'`).join(',')})
      AND user_pseudo_id = @userId
    GROUP BY
      user_id, item_id
    HAVING
      item_id IS NOT NULL
  `;

  const options = {
    query: query,
    params: { userId: userId }
  };

  const [rows] = await bigquery.query(options);
  return rows.map(row => ({ item_id: row.item_id }));
}

async function getItemMetadata(itemId) {
  const docRef = db.collection('metadata').doc(itemId);
  const doc = await docRef.get();
  return doc.exists ? doc.data() : null;
}

function updateProfileCounts(profileSection, values) {
  for (const value of values) {
    if (value) {
      profileSection[value] = (profileSection[value] || 0) + 1;
    }
  }
}

function extractKeywords(text) {
  const tokenizer = new natural.WordTokenizer();
  const tokens = tokenizer.tokenize(text);
  return tokens.filter(token => token.length > 3);
}

function normalizeProfile(profile) {
  for (const section in profile) {
    const total = Object.values(profile[section]).reduce((sum, count) => sum + count, 0);
    for (const key in profile[section]) {
      profile[section][key] /= total;
    }
  }
}

async function generateUserRecommendations(userId, userProfile) {
  const allItems = await getAllItems();
  const scoredItems = [];

  for (const item of allItems) {
    if (!(await hasUserInteracted(userId, item.id))) {
      const similarity = calculateSimilarity(userProfile, item);
      if (similarity >= SIMILARITY_THRESHOLD) {
        scoredItems.push({ itemId: item.id, score: similarity });
      }
    }
  }

  scoredItems.sort((a, b) => b.score - a.score);
  return scoredItems.slice(0, MAX_RECOMMENDATIONS);
}

async function getAllItems() {
  const snapshot = await db.collection('metadata').get();
  return snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
}

async function hasUserInteracted(userId, itemId) {
  const query = `
    SELECT COUNT(*) as count
    FROM \`kafka-books.analytics_195726967.events_*\`
    WHERE user_id = @userId
    AND item_id = @itemId
    AND event_name IN ('read_item', 'play_item', 'add_favorite')
    LIMIT 1
  `;

  const options = {
    query: query,
    params: { userId: userId, itemId: itemId }
  };

  const [rows] = await bigquery.query(options);
  return rows[0].count > 0;
}

function calculateSimilarity(userProfile, item) {
  let similarity = 0;
  const weights = {
    subject: 0.3,
    creator: 0.2,
    mediatype: 0.2,
    language: 0.1,
    collection: 0.1,
  };

  for (const feature in weights) {
    similarity += weights[feature] * calculateFeatureSimilarity(userProfile[feature], item[feature]);
  }

  return similarity;
}

function calculateFeatureSimilarity(userFeatures, itemFeature) {
  if (Array.isArray(itemFeature)) {
    return itemFeature.reduce((sum, value) => sum + (userFeatures[value] || 0), 0) / itemFeature.length;
  } else if (typeof itemFeature === 'string') {
    return userFeatures[itemFeature] || 0;
  }
  return 0;
}

async function saveRecommendations(userId, recommendations) {
  const userRecommendationsRef = db.collection('user_recommendations').doc(userId);
  await userRecommendationsRef.set({
    contentBased: recommendations
  }, { merge: true });
}

module.exports = {
  generateContentBasedRecommendations,
};