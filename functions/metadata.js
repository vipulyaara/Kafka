/**
 * make this curl request to fetch following fields from internet archive.
 * 
 * https://archive.org/advancedsearch.php?q=kafka&fl%5B%5D=collection&fl%5B%5D=creator&fl%5B%5D=description&fl%5B%5D=downloads&fl%5B%5D=genre&fl%5B%5D=language&fl%5B%5D=mediatype&fl%5B%5D=name&fl%5B%5D=subject&sort%5B%5D=&sort%5B%5D=&sort%5B%5D=&rows=50&page=1&output=json&callback=callback&save=yes
 * (use similar format to fetch these response fields but use another url to fetch response by giving item ids as below)
 * API to fetch items using ids:

    https://archive.org/advancedsearch.php?q=identifier:(mehdi-hassan OR the-judgement_202408 OR in-the-penal-colony)&output=json&rows=200&page=1&sort=-downloads
 * 

response = {"responseHeader":{"status":0,"QTime":44,"params":{"query":"(( ( (title:kafka^100 OR salients:kafka^50 OR subject:kafka^25 OR description:kafka^15 OR collection:kafka^10 OR language:kafka^10 OR text:kafka^1) ) AND !collection:(podcasts OR radio OR uspto))^2 OR ( ( (title:kafka^100 OR salients:kafka^50 OR subject:kafka^25 OR description:kafka^15 OR collection:kafka^10 OR language:kafka^10 OR text:kafka^1) ) AND collection:(podcasts OR radio OR uspto))^0.5)","qin":"kafka","fields":"collection,creator,description,downloads,genre,language,mediatype,name,subject","wt":"json","rows":"50","json.wrf":"callback","start":0}},"response":{"numFound":4737,"start":0,"docs":[{"collection":["opensource_audio","community"],"creator":"Kafka","description":"Kafka - Obra dos Sonhos (1989) - mp3 320kbps","downloads":77,"mediatype":"audio","subject":["kafka","kafka banda","kafka band","m\u00fasica brasileira","raro"]},{"collection":["opensource_audio","community"],"creator":"Franz Kafka","description":"Micro-Fiction by Franz Kafka. Recorded by Kafka Archives. Please note that this audio has inconsistent pauses due to an error in recording.","downloads":156,"language":"eng","mediatype":"audio","subject":["micro-fiction","short story","Kafka Archives"]}
 * 
 * save this response in firestore collection called metadata.
 * 
 */

const functions = require('firebase-functions');
const admin = require('firebase-admin');
const axios = require('axios');

const db = admin.firestore();

const removeUndefined = (obj) => {
  return Object.fromEntries(
    Object.entries(obj).filter(([_, v]) => v !== undefined)
  );
};


exports.fetchAndSaveMetadata = functions.runWith({
  timeoutSeconds: 540, // 9 minutes, maximum for Blaze plan
  memory: '1GB'
}).https.onRequest(async (req, res) => {
  // Define a constant set of item IDs
  const itemIds = 'mushayre-jaun,khushboo-parveen-shakir,meaning_201910,shayad-jaun-eliya,janan-janan-audio-book,ShivKumarBatalviHindYugm,yaani-ghazals,shiv-kumar-bbatalvi,adabi-interviews,gumaan-ghazals,BalKavitaChooheKiDilliYatraRamdhariSinghDinkar,ghazals_ghalib_0809_librivox,satya-ke-sath-mere-prayog,LekinByJaunElia,baileyscafe00nayl,baileyscafe000nayl,ost-english-metamorphosis-by-franz-kafka,ShaidByJaunElia,in.ernet.dli.2015.63318,franz-kafka-the-trial,selectedshortsto00fran,godan-by-munshi-premchand-gyannidhi.com,franzkafkasmetam00bloo,in.ernet.dli.2015.499492,poems_1706_librivox,MahaGeetaByOshoInHindiPart1,12RulesForLifeJordanB.PetersonAnAntidoteToChaos,in-the-company-of-a-poet-by-kabir-nasreen-munni,osho-literature-in-hindi,HindiBook-ashtavakraGeeta.pdf,rand-ayn-the-fountainhead,StoriesByPremchand,norwegianwood00mura,KafanPremchand'
  const ids = itemIds.split(',');

  let batch = db.batch();
  let batchCount = 0;
  const MAX_BATCH_SIZE = 500; // Firestore limit

  
  const chunkSize = 200;
  const itemIdChunks = [];
  for (let i = 0; i < ids.length; i += chunkSize) {
    itemIdChunks.push(ids.slice(i, i + chunkSize));
  }

  console.log(`Split item IDs into ${itemIdChunks.length} chunks`);

  // Process each chunk
  for (let i = 0; i < itemIdChunks.length; i++) {
    const chunk = itemIdChunks[i];
    console.log(`Processing chunk ${i + 1} of ${itemIdChunks.length}`);

    const chunkQuery = `identifier:(${chunk.join(' OR ')})`;
    const chunkUrl = `https://archive.org/advancedsearch.php?q=${encodeURIComponent(chunkQuery)}&rows=200&page=1&output=json&sort=-downloads`;

    console.log(`Fetching metadata for chunk ${i + 1} from URL:`, chunkUrl);

    try {
      const chunkResponse = await axios.get(chunkUrl);
      const chunkDocs = chunkResponse.data.response.docs;

      console.log(`Fetched metadata for ${chunkDocs.length} items in chunk ${i + 1}`);

      // Process the chunk docs
      for (const doc of chunkDocs) {
        console.log(`Processing metadata for item: ${doc.identifier}`);
        const docRef = db.collection('content_metadata').doc(doc.identifier);
        
        // Pick specific fields you want to save and remove undefined values
        const fieldsToSave = removeUndefined({
          identifier: doc.identifier,
          title: doc.title,
          creator: doc.creator,
          description: doc.description,
          downloads: doc.downloads,
          mediatype: doc.mediatype,
          subject: doc.subject,
          collection: doc.collection,
          language: doc.language
          // Add any other fields you want to save
        });

        batch.set(docRef, fieldsToSave);
        batchCount++;

        // If batch size reaches limit, commit and reset
        if (batchCount === MAX_BATCH_SIZE) {
          await batch.commit();
          console.log(`Committed batch with ${batchCount} operations`);
          batch = db.batch();
          batchCount = 0;
        }
      }

    } catch (error) {
      console.error(`Error processing chunk ${i + 1}:`, error);
    }
  }

  // Commit any remaining operations in the final batch
  if (batchCount > 0) {
    try {
      await batch.commit();
      console.log(`Committed final batch with ${batchCount} operations`);
    } catch (error) {
      console.error('Error committing final batch:', error);
    }
  }

  console.log('Finished processing all chunks');
});

// Export the fetchAndSaveMetadata function
module.exports = {
  fetchAndSaveMetadata: exports.fetchAndSaveMetadata
};



