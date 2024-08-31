const { getDateRange, queryBigQuery, updateFirestore } = require('../utils/bigQueryUtils');
const { logger } = require("firebase-functions");

const calculateTopItems = async (eventName, period, docName) => {
  const startDate = getDateRange(period);
  const query = `
    SELECT
      (SELECT value.string_value FROM UNNEST(event_params) WHERE key = 'item_id') AS item_id,
      COUNT(*) AS count
    FROM
      \`kafka-books.analytics_195726967.events_*\`
    WHERE
      event_name = '${eventName}'
      AND event_date >= '${startDate}'
    GROUP BY
      item_id
    ORDER BY
      count DESC
    LIMIT 100
  `;

  try {
    const itemIds = await queryBigQuery(query);
    await updateFirestore(`recommendations/${docName}`, itemIds);
    logger.info(`Successfully updated ${docName} with ${itemIds.length} items`);
    return itemIds;
  } catch (error) {
    logger.error(`Error calculating top ${eventName} items for ${period}:`, error);
    throw error;
  }
};

const calculateTopReadWeekly = () => calculateTopItems('read_item', 'weekly', 'top_read_weekly');
const calculateTopPlayedWeekly = () => calculateTopItems('play_item', 'weekly', 'top_played_weekly');
const calculateTopReadMonthly = () => calculateTopItems('read_item', 'monthly', 'top_read_monthly');
const calculateTopPlayedMonthly = () => calculateTopItems('play_item', 'monthly', 'top_played_monthly');
const calculateTopFavoritedWeekly = () => calculateTopItems('add_favorite', 'weekly', 'top_favorited_items');

module.exports = {
  calculateTopReadWeekly,
  calculateTopPlayedWeekly,
  calculateTopReadMonthly,
  calculateTopPlayedMonthly,
  calculateTopFavoritedWeekly
};