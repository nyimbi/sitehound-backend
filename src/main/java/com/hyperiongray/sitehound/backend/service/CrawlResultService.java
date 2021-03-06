package com.hyperiongray.sitehound.backend.service;

import com.hyperiongray.sitehound.backend.repository.CrawledIndexRepository;
import com.hyperiongray.sitehound.backend.repository.impl.elasticsearch.api.BroadCrawlContextDto;
import com.hyperiongray.sitehound.backend.repository.impl.elasticsearch.api.TrainingCrawlContextDto;
import com.hyperiongray.sitehound.backend.repository.impl.mongo.GenericCrawlMongoRepository;
import com.hyperiongray.sitehound.backend.repository.impl.mongo.translator.BroadCrawlContextDtoTranslator;
import com.hyperiongray.sitehound.backend.repository.impl.mongo.translator.TrainingCrawlContextDtoTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * Created by tomas on 2/9/16.
 */
@Service
public class CrawlResultService{

	@Autowired private CrawledIndexRepository analyzedCrawlResultDtoIndexRepository;
	@Autowired private GenericCrawlMongoRepository genericCrawlMongoRepository;
//	@Autowired private AquariumResultTranslator aquariumResultTranslator;
	@Autowired private TrainingCrawlContextDtoTranslator trainingCrawlContextDtoTranslator;
	@Autowired private BroadCrawlContextDtoTranslator broadCrawlContextDtoTranslator;

//	@Deprecated
//	public void save(AquariumInput aquariumInput, AquariumInternal aquariumInternal, NlpOutput nlpOutput){
//		Map<String, Object> document = aquariumResultTranslator.translateToDocument(aquariumInput, aquariumInternal, nlpOutput);
//		genericCrawlMongoRepository.save(aquariumInput.getMetadata().getCrawlType(), aquariumInput.getMetadata().getWorkspace(), document);
//	}

	public void save(BroadCrawlContextDto broadCrawlContextDto) throws IOException{

		// update ES index
		String hashKey = analyzedCrawlResultDtoIndexRepository.upsert(broadCrawlContextDto.getCrawlRequestDto().getUrl(), broadCrawlContextDto.getCrawlRequestDto().getWorkspace(), broadCrawlContextDto.getCrawlRequestDto().getCrawlEntityType(), broadCrawlContextDto.getAnalyzedCrawlResultDto());

		// update mongo index
		Map<String, Object> document = broadCrawlContextDtoTranslator.translate(broadCrawlContextDto);
		document.put("hashKey", hashKey);
		genericCrawlMongoRepository.save(broadCrawlContextDto.getCrawlType(), broadCrawlContextDto.getCrawlRequestDto().getWorkspace(), document);
	}

	public void save(TrainingCrawlContextDto trainingCrawlContextDto) throws IOException{

		// update ES index
		String hashKey = analyzedCrawlResultDtoIndexRepository.upsert(trainingCrawlContextDto.getCrawlRequestDto().getUrl(), trainingCrawlContextDto.getCrawlRequestDto().getWorkspace(), trainingCrawlContextDto.getCrawlRequestDto().getCrawlEntityType(), trainingCrawlContextDto.getAnalyzedCrawlResultDto());

		// update mongo index
		Map<String, Object> document = trainingCrawlContextDtoTranslator.translate(trainingCrawlContextDto);
		document.put("hashKey", hashKey);
		genericCrawlMongoRepository.save(trainingCrawlContextDto.getCrawlType(), trainingCrawlContextDto.getCrawlRequestDto().getWorkspace(), document);
	}


}
