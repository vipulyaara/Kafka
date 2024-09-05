const {getDateRange, queryBigQuery} = require("../utils/bigQueryUtils");
const {getFirestore} = require("firebase-admin/firestore");
const {logger} = require("firebase-functions");

const db = getFirestore();

const calculateTopItems = async (eventName, period, docName) => {
  const startDate = getDateRange(period);
  const query = `
    SELECT
      (SELECT value.string_value FROM UNNEST(event_params) WHERE key = 'item_id') AS item_id,
      COUNT(*) AS count
    FROM
      \`kafka-books.analytics_195726967.events_*\`
    WHERE
      event_name = "${eventName}"
      AND event_date >= "${startDate}"
    GROUP BY
      item_id
    ORDER BY
      count DESC
    LIMIT 50
  `;

  try {
    const itemIds = await queryBigQuery(query);

    // Update recommendations document
    // await updateFirestore(`recommendations/${docName}`, itemIds);
   
    // Check if the document exists
    const docRef = db.doc(`homepage-collection/${docName}`);
    const docSnapshot = await docRef.get();
    
    if (docSnapshot.exists) {
      const itemIdsString = itemIds
        .filter((id) => id != null)
        .join(", ");

      await docRef.update({itemIds: itemIdsString});
      logger.info(`Updated existing homepage-collection/${docName} with first item ID`);
    } else {
      logger.warn(`Document homepage-collection/${docName} does not exist. Skipping update.`);
    }
    
    logger.info(`Successfully updated ${docName} with ${itemIds.length} items`);
    return itemIds;
  } catch (error) {
    logger.error(`Error calculating top ${eventName} items for ${period}:`, error);
    throw error;
  }
};

const calculateTopReadWeekly = () => calculateTopItems("read_item", "weekly", "top_read_weekly");
const calculateTopPlayedWeekly = () => calculateTopItems("play_item", "weekly", "top_played_weekly");
const calculateTopReadMonthly = () => calculateTopItems("read_item", "monthly", "top_read_monthly");
const calculateTopPlayedMonthly = () => calculateTopItems("play_item", "monthly", "top_played_monthly");
const calculateTopFavoritedWeekly = () => calculateTopItems("add_favorite", "weekly", "top_favorited_weekly");
const calculateTopFavoritedMonthly = () => calculateTopItems("add_favorite", "monthly", "top_favorited_monthly");

module.exports = {
  calculateTopReadWeekly,
  calculateTopPlayedWeekly,
  calculateTopReadMonthly,
  calculateTopPlayedMonthly,
  calculateTopFavoritedWeekly,
  calculateTopFavoritedMonthly,
};