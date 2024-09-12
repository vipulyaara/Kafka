const {onRequest} = require("firebase-functions/v2/https");
const functions = require("firebase-functions");

// The Firebase Admin SDK to access Firestore.
const {initializeApp} = require("firebase-admin/app");

initializeApp();

const runtimeOpts = {
  memory: '2GiB',
  timeoutSeconds: 540 // 9 minutes (max allowed)
};

const {
  calculateTopReadWeekly,
  calculateTopPlayedWeekly,
  calculateTopReadMonthly,
  calculateTopPlayedMonthly,
  calculateTopFavoritedWeekly,
  calculateTopFavoritedMonthly,
} = require("./recommendation/trending");

// Weekly scheduled function
exports.weeklyUpdateTopItems = functions.pubsub
  .schedule("every monday 00:00")
  .timeZone("UTC")
  .onRun(async (context) => {
    try {
      await Promise.all([
        calculateTopReadWeekly(),
        calculateTopPlayedWeekly(),
        calculateTopFavoritedWeekly(),
      ]);
      console.log("Weekly top items update completed successfully");
    } catch (error) {
      console.error("Error in weekly top items update:", error);
    }
  });

// Monthly scheduled function
exports.monthlyUpdateTopItems = functions.pubsub
  .schedule("1 of month 00:00")
  .timeZone("UTC")
  .onRun(async (context) => {
    try {
      await Promise.all([
        calculateTopReadMonthly(),
        calculateTopPlayedMonthly(),
        calculateTopFavoritedMonthly(),
      ]);
      console.log("Monthly top items update completed successfully");
    } catch (error) {
      console.error("Error in monthly top items update:", error);
    }
  });

// http://127.0.0.1:5001/kafka-books/us-central1/manualUpdateTopItems?type=weekly
exports.manualUpdateTopItems = onRequest(async (req, res) => {
  const {type} = req.query;

  try {
    switch (type) {
      case "weekly":
        await Promise.all([
          calculateTopReadWeekly(),
          calculateTopPlayedWeekly(),
          calculateTopFavoritedWeekly(),
        ]);
        break;
      case "monthly":
        await Promise.all([
          calculateTopReadMonthly(),
          calculateTopPlayedMonthly(),
          calculateTopFavoritedMonthly(),
        ]);
        break;
      default:
        throw new Error("Invalid update type specified");
    }
    res.json({result: `${type} top items update completed successfully`});
  } catch (error) {
    console.error(`Error in manual ${type} top items update:`, error);
    res.status(500).json({
      error: `An error occurred during ${type} top items update`,
    });
  }
});

// Import the calculateCollaborativeRecommendations function
const { calculateCollaborativeRecommendations } = require('./recommendation/collaborative');

// http://127.0.0.1:5001/kafka-books/us-central1/manualCollaborativeRecommendations
exports.manualCollaborativeRecommendations = onRequest(runtimeOpts, async (req, res) => {
  try {
    await calculateCollaborativeRecommendations();
    res.json({ result: 'Collaborative recommendations calculation completed successfully' });
  } catch (error) {
    console.error('Error in manual collaborative recommendations calculation:', error);
    res.status(500).json({
      error: 'An error occurred during collaborative recommendations calculation',
    });
  }
});

// Import the calculateContentBasedRecommendations function
const { generateContentBasedRecommendations } = require('./recommendation/content_filtering');

// http://127.0.0.1:5001/kafka-books/us-central1/manualContentBasedRecommendations
exports.manualContentBasedRecommendations = onRequest(runtimeOpts, async (req, res) => {
  try {
    await generateContentBasedRecommendations();
    res.json({ result: 'Content-based recommendations calculation completed successfully' });
  } catch (error) {
    console.error('Error in manual content-based recommendations calculation:', error);
    res.status(500).json({
      error: 'An error occurred during content-based recommendations calculation',
    });
  }
});

// Import the fetchAndSaveMetadata function
const { fetchAndSaveMetadata } = require('./metadata');

// http://127.0.0.1:5001/kafka-books/us-central1/manualFetchAndSaveMetadata
exports.manualFetchAndSaveMetadata = onRequest(async (req, res) => {
  try {
    await fetchAndSaveMetadata(req, res);
  } catch (error) {
    console.error('Error in manual fetch and save metadata:', error);
    res.status(500).json({
      error: 'An error occurred during fetch and save metadata',
    });
  }
});

