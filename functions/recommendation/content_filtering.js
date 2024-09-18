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

let userInteractionsCache = {};

async function generateContentBasedRecommendations() {
  const activeUsers = await getActiveUsers();
  console.log(`Found ${activeUsers.length} active users for content-based recommendations`);
  
  const allUserItemIds = {};

  // Batch query for user interactions
  const userInteractions = await batchGetUserInteractions(activeUsers);
  
  for (const userId of activeUsers) {
    const existingRecommendations = await getUserRecommendations(userId);
    if (existingRecommendations && existingRecommendations.length > 0) {
      console.log(`User ${userId} already has recommendations. Skipping.`);
      continue;
    }

    const userProfile = await buildUserProfile(userId, userInteractions[userId]);
    console.log(`Built user profile for user ${userId}:`, JSON.stringify(userProfile, null, 2));
    const recommendations = await generateUserRecommendations(userId, userProfile);
    console.log(`Generated ${recommendations.length} recommendations for user ${userId}`);
    if (recommendations.length > 0) {
      await saveRecommendations(userId, recommendations);
      console.log(`Saved ${recommendations.length} recommendations for user ${userId}`);
      
      // Add user's item IDs to allUserItemIds
      allUserItemIds[userId] = recommendations.map(rec => rec.itemId);
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
        user_id
      FROM (
        SELECT
          user_id,
          COUNT(*) AS open_item_detail_count
        FROM
          \`kafka-books.analytics_195726967.events_*\`
        WHERE
          _TABLE_SUFFIX BETWEEN FORMAT_DATE('%Y%m%d', DATE_SUB(CURRENT_DATE(), INTERVAL ${ACTIVE_USER_THRESHOLD_DAYS} DAY))
            AND FORMAT_DATE('%Y%m%d', CURRENT_DATE())
          AND event_name = 'open_item_detail'
        GROUP BY
          user_id
        HAVING
          open_item_detail_count > 5
      ) AS active_users
      WHERE
        user_id IN (
          SELECT DISTINCT
            user_id
          FROM
            \`kafka-books.analytics_195726967.events_*\`
          WHERE
            _TABLE_SUFFIX BETWEEN FORMAT_DATE('%Y%m%d', DATE_SUB(CURRENT_DATE(), INTERVAL ${ACTIVE_USER_THRESHOLD_DAYS} DAY))
              AND FORMAT_DATE('%Y%m%d', CURRENT_DATE())
            AND event_name IN (${EVENTS_OF_INTEREST.map(event => `'${event}'`).join(',')})
        )
      LIMIT 1000
    `;

    const [rows] = await bigquery.query({ query });
    const activeUsers = rows.map(row => row.user_id);
    
    // Add the specific user ID if it's not already in the list
    const specificUserId = 'P34Oi9eyXNPQxWYM98e7VCoUSeF2';
    if (!activeUsers.includes(specificUserId)) {
      activeUsers.push(specificUserId);
    }
  
    return activeUsers;
}

async function buildUserProfile(userId, interactions) {
  console.log(`Retrieved ${interactions.length} interactions for user ${userId}`);
  const profile = {
    subject: {},
    creator: {},
    language: {},
    collection: {},
    mediatype: {},
  };
  
  for (const interaction of interactions) {
    const itemMetadata = await getItemMetadata(interaction.item_id);
    console.log(`Processing item ${interaction.item_id}`);
    if (!itemMetadata) {
      console.log(`No metadata found for item ${interaction.item_id}`);
    } else {
      console.log(`Retrieved metadata for item ${interaction.item_id}:`, JSON.stringify(itemMetadata, null, 2));
    }
    if (itemMetadata) {
      updateProfileCounts(profile.subject, itemMetadata.subject);
      updateProfileCounts(profile.creator, Array.isArray(itemMetadata.creator) ? itemMetadata.creator : [itemMetadata.creator]);
      updateProfileCounts(profile.language, Array.isArray(itemMetadata.language) ? itemMetadata.language : [itemMetadata.language]);
      updateProfileCounts(profile.collection, itemMetadata.collection);
      updateProfileCounts(profile.mediatype, [itemMetadata.mediaType]);
      
      // const keywords = extractKeywords(itemMetadata.description);
      // updateProfileCounts(profile.keywords, keywords);
    }
  }

  normalizeProfile(profile);
  return profile;
}

async function batchGetUserInteractions(userIds) {
  // First, try to get interactions from Firestore
  const firestoreInteractions = await batchGetFirestoreInteractions(userIds);
  
  // Identify users who need BigQuery lookup
  const usersNeedingBigQuery = userIds.filter(userId => !firestoreInteractions[userId]);
  
  // If all users have Firestore data, return it
  if (usersNeedingBigQuery.length === 0) {
    return firestoreInteractions;
  }
  
  // Perform BigQuery batch query for remaining users
  const bigQueryInteractions = await batchGetBigQueryInteractions(usersNeedingBigQuery);
  
  // Merge Firestore and BigQuery results
  const allInteractions = { ...firestoreInteractions, ...bigQueryInteractions };
  
  // Store BigQuery results in Firestore for future use
  await batchSaveInteractionsToFirestore(bigQueryInteractions);
  
  return allInteractions;
}

async function batchGetFirestoreInteractions(userIds) {
  const batch = db.batch();
  const refs = userIds.map(userId => db.collection('user_interactions').doc(userId));
  const docs = await db.getAll(...refs);
  
  return docs.reduce((acc, doc, index) => {
    if (doc.exists) {
      acc[userIds[index]] = doc.data().interactions || [];
    }
    return acc;
  }, {});
}

async function batchGetBigQueryInteractions(userIds) {
  const query = `
    SELECT
      user_id,
      ARRAY_AGG(STRUCT(item_id, interaction_count)) AS interactions
    FROM (
      SELECT
        user_id,
        (SELECT value.string_value FROM UNNEST(event_params) WHERE key = 'item_id') AS item_id,
        COUNT(*) AS interaction_count
      FROM
        \`kafka-books.analytics_195726967.events_*\`
      WHERE
        _TABLE_SUFFIX BETWEEN FORMAT_DATE('%Y%m%d', DATE_SUB(CURRENT_DATE(), INTERVAL ${ACTIVE_USER_THRESHOLD_DAYS} DAY))
          AND FORMAT_DATE('%Y%m%d', CURRENT_DATE())
        AND event_name IN (${EVENTS_OF_INTEREST.map(event => `'${event}'`).join(',')})
        AND user_id IN UNNEST(@userIds)
      GROUP BY
        user_id, item_id
      HAVING
        item_id IS NOT NULL
    )
    GROUP BY user_id
  `;

  const options = {
    query: query,
    params: { userIds: userIds }
  };

  const [rows] = await bigquery.query(options);
  
  return rows.reduce((acc, row) => {
    acc[row.user_id] = row.interactions.map(i => ({ item_id: i.item_id }));
    return acc;
  }, {});
}

async function batchSaveInteractionsToFirestore(interactions) {
  const batch = db.batch();
  
  for (const [userId, userInteractions] of Object.entries(interactions)) {
    const docRef = db.collection('user_interactions').doc(userId);
    batch.set(docRef, { interactions: userInteractions });
  }
  
  await batch.commit();
}

async function getUserInteractions(userId) {
  // Check if interactions are already in cache
  if (userInteractionsCache[userId]) {
    return Array.from(userInteractionsCache[userId]).map(item_id => ({ item_id }));
  }

  // If not in cache, fetch from Firestore first
  const userInteractionsRef = db.collection('user_interactions').doc(userId);
  const doc = await userInteractionsRef.get();

  if (doc.exists) {
    const interactions = doc.data().interactions || [];
    userInteractionsCache[userId] = new Set(interactions.map(i => i.item_id));
    return interactions;
  }

  // If not in Firestore, fall back to BigQuery
  const query = `
    SELECT
      user_id,
      (SELECT value.string_value FROM UNNEST(event_params) WHERE key = 'item_id') AS item_id,
      COUNT(*) AS interaction_count
    FROM
      \`kafka-books.analytics_195726967.events_*\`
    WHERE
      _TABLE_SUFFIX BETWEEN FORMAT_DATE('%Y%m%d', DATE_SUB(CURRENT_DATE(), INTERVAL ${ACTIVE_USER_THRESHOLD_DAYS} DAY))
        AND FORMAT_DATE('%Y%m%d', CURRENT_DATE())
      AND event_name IN (${EVENTS_OF_INTEREST.map(event => `'${event}'`).join(',')})
      AND user_id = @userId
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
  const interactions = rows.map(row => ({ item_id: row.item_id }));

  // Cache the results
  userInteractionsCache[userId] = new Set(interactions.map(i => i.item_id));

  // Store in Firestore for future use
  await userInteractionsRef.set({ interactions });

  return interactions;
}

async function getItemMetadata(itemId) {
  const docRef = db.collection('content_metadata').doc(itemId);
  const doc = await docRef.get();
  return doc.exists ? doc.data() : null;
}

function updateProfileCounts(profileSection, values) {
  if (!Array.isArray(values)) {
    values = [values];
  }
  for (const value of values) {
    if (value && typeof value === 'string') {
      profileSection[value] = (profileSection[value] || 0) + 1;
    }
  }
}

function extractKeywords(text) {
  if (typeof text !== 'string') {
    return [];
  }
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
  if (!userInteractionsCache[userId]) {
    await getUserInteractions(userId);
  }

  const allItems = await getAllItems();
  const scoredItems = [];

  for (const item of allItems) {
    if (!hasUserInteracted(userId, item.id)) {
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
  const snapshot = await db.collection('content_metadata').get();
  return snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
}

function hasUserInteracted(userId, itemId) {
  // Check if user interactions are in cache
  if (userInteractionsCache[userId]) {
    return userInteractionsCache[userId].has(itemId);
  }
  
  // If not in cache, return false (interactions will be loaded later)
  return false;
}

function calculateSimilarity(userProfile, item) {
  let similarity = 0;
  const weights = {
    subject: 0.2,
    creator: 0.1,
    mediatype: 0.2,
    language: 0.3,
    collection: 0.03,
  };

  for (const feature in weights) {
    let itemFeature = item[feature === 'mediatype' ? 'mediaType' : feature];
    similarity += weights[feature] * calculateFeatureSimilarity(userProfile[feature], itemFeature);
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

async function getUserRecommendations(userId) {
  const userRecommendationsRef = db.collection('user_recommendations').doc(userId);
  const doc = await userRecommendationsRef.get();
  if (doc.exists) {
    return doc.data().contentBased || [];
  }
  return [];
}

const EXCLUDED_ITEM_IDS = [
  'behan-ne-chhote-bhai-se-choot-chudwa-kar-maja',
  // Add more item IDs to exclude as needed
];

async function saveRecommendations(userId, recommendations) {
  // Only save recommendations if there are more than 3
  if (recommendations.length <= 3) {
    console.log(`Not saving recommendations for user ${userId} as there are only ${recommendations.length} items.`);
    return;
  }

  const userRecommendationsRef = db.collection('user_recommendations').doc(userId).collection('contentBased');
  
  const batch = db.batch();
  let savedCount = 0;

  for (const rec of recommendations) {
    if (!EXCLUDED_ITEM_IDS.includes(rec.itemId)) {
      const docRef = userRecommendationsRef.doc(rec.itemId);
      batch.set(docRef, {
        score: rec.score
      });
      savedCount++;
    }
  }

  await batch.commit();
  console.log(`Saved ${savedCount} content-based recommendations for user ${userId} (${recommendations.length - savedCount} excluded)`);
}

module.exports = {
  generateContentBasedRecommendations,
};