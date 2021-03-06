package com.hyperiongray.sitehound.backend.repository.impl.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.hyperiongray.sitehound.backend.service.crawler.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by tomas on 11/16/15.
 */
@Repository
public class CrawlJobRepository{

	@Autowired
	private MongoRepository mongoRepository;

	private static final String CRAWL_JOB_COLLECTION_NAME = "crawl_job";
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlJobRepository.class);

	public boolean updateJobStatus(String jobId, Constants.JobStatus status){
		LOGGER.info("About to update crawlJob:" + jobId);
		MongoCollection<Document> collection = mongoRepository.getDatabase().getCollection(CRAWL_JOB_COLLECTION_NAME);
		Bson filter = new BasicDBObject("_id", new ObjectId(jobId)).append("status", "QUEUED");
		UpdateResult updateResult=collection.updateOne(filter,
				new BasicDBObject("$set", new BasicDBObject("status", status.toString())));
		LOGGER.info("Finished to update crawlJob:" + jobId);
		return updateResult.getModifiedCount() == 1L;
	}

	public boolean isJobActive(String jobId){
		MongoCollection<Document> collection = mongoRepository.getDatabase().getCollection(CRAWL_JOB_COLLECTION_NAME);
		Bson filter = new BasicDBObject("_id", new ObjectId(jobId));
		FindIterable<Document> iterable = collection.find(filter);
		MongoCursor<Document> iterator = iterable.iterator();
//		List<TrainedCrawledUrl> trainedCrawledUrls= new LinkedList<>();
//		while(iterator.hasNext()){
//			trainedCrawledUrls.add(translate(iterator.next()));
//		}
//		return trainedCrawledUrls;
		if(iterator.hasNext()){
			String status= iterator.next().getString("status");
			if(Constants.JobStatus.STOPPED.toString().equalsIgnoreCase(status)){
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
	}

}
