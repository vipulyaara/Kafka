const {BigQuery} = require("@google-cloud/bigquery");
const {getFirestore} = require("firebase-admin/firestore");
const {logger} = require("firebase-functions");

const bigquery = new BigQuery();
const db = getFirestore();

const getDateRange = (period) => {
  const now = new Date();
  let startDate;
  if (period === "weekly") {
    startDate = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000);
  } else if (period === "monthly") {
    startDate = new Date(now.getFullYear(), now.getMonth() - 1, now.getDate());
  } else {
    throw new Error("Invalid period specified");
  }
  return startDate.toISOString().slice(0, 10).replace(/-/g, "");
};

const queryBigQuery = async (query) => {
  try {
    const [rows] = await bigquery.query({query});
    return rows.map((row) => row.item_id);
  } catch (error) {
    logger.error("Error querying BigQuery:", error);
    throw error;
  }
};

const updateFirestore = async (docPath, itemIds) => {
  try {
    const docRef = db.doc(docPath);
    await docRef.set({itemIds});
    logger.info(`Updated ${docPath} with ${itemIds.length} items`);
  } catch (error) {
    logger.error(`Error updating Firestore document ${docPath}:`, error);
    throw error;
  }
};

module.exports = {getDateRange, queryBigQuery, updateFirestore};