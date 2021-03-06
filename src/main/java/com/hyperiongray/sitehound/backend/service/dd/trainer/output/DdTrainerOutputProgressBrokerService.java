package com.hyperiongray.sitehound.backend.service.dd.trainer.output;

import com.hyperiongray.sitehound.backend.service.JsonMapper;
import com.hyperiongray.sitehound.backend.service.crawler.BrokerService;
import com.hyperiongray.sitehound.backend.kafka.api.dto.dd.trainer.output.DdTrainerOutputProgress;
import com.hyperiongray.sitehound.backend.repository.impl.mongo.DdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

/**
 * Created by tomas on 28/09/16.
 */
@Service
public class DdTrainerOutputProgressBrokerService implements BrokerService {

    @Autowired private DdRepository ddRepository;


    private static final Logger LOGGER = LoggerFactory.getLogger(DdTrainerOutputProgressBrokerService.class);

    @Override
    public void process(String jsonInput, Semaphore semaphore){

        try{
            LOGGER.info("DdTrainerOutputPagesBrokerService consumer Permits:" + semaphore.availablePermits());
            LOGGER.debug("Receiving response: " + jsonInput);
            JsonMapper<DdTrainerOutputProgress> jsonMapper= new JsonMapper();
            DdTrainerOutputProgress ddTrainerOutputProgress = jsonMapper.toObject(jsonInput, DdTrainerOutputProgress.class);
            ddRepository.saveProgress(ddTrainerOutputProgress);
        }
        catch(Exception e){
            LOGGER.error("ERROR:" + jsonInput, e);
        }
        finally{
            LOGGER.info("DdTrainerOutputPagesBrokerService Consumer Permits (one will be released now): " + semaphore.availablePermits());
            semaphore.release();
        }
    }
}
