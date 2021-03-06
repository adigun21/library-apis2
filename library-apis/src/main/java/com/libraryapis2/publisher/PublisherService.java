package com.libraryapis2.publisher;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.libraryapis2.exception.LibraryResourceAlreadyExistException;
import com.libraryapis2.exception.LibraryResourceNotFoundException;
import com.libraryapis2.util.LibraryApiUtils;


@Service
public class PublisherService {
	
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(PublisherService.class);
	
	private PublisherRepository publisherRepository;
	
	

	public PublisherService(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}



	public void addPublisher(Publisher publisherToBeAdded, String traceId)
			throws LibraryResourceAlreadyExistException {
		
		logger.debug("TraceId: {}, Request to add Publisher:{}",traceId, publisherToBeAdded);
		PublisherEntity publisherEntity = new PublisherEntity(
				publisherToBeAdded.getName(),
				publisherToBeAdded.getEmailId(),
				publisherToBeAdded.getPhoneNumber()
				);
		
		PublisherEntity addedPublisher = null;
		
		try {
			addedPublisher = publisherRepository.save(publisherEntity);
		} catch (DataIntegrityViolationException e) {
			logger.error("TraceId: {}, publisher already exists!!", traceId, e);
			throw new LibraryResourceAlreadyExistException(traceId, "publisher already exists!!");
		}
		
		publisherToBeAdded.setPublisherId(addedPublisher.getPublisherid());
		logger.info("TraceId: {}, Publisher added: {}", traceId, publisherToBeAdded);
	}



	public Publisher getPublisher(Integer publisherId, String traceId) throws LibraryResourceNotFoundException {
		
		Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherId);
		Publisher publisher = null;
		
		if(publisherEntity.isPresent()) {
			
			PublisherEntity pe = publisherEntity.get();
			publisher = createPublisherFromEntity(pe);
			
		}else {
			throw new LibraryResourceNotFoundException(traceId, "Publisher Id: " + publisherId + " Not Found");
		}
		
		return publisher;
		
		
	}

	public void updatePublisher(Publisher publisherToBeUpdated, String traceId) throws LibraryResourceNotFoundException {
		
		Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherToBeUpdated.getPublisherId());
		Publisher publisher = null;
		
		if(publisherEntity.isPresent()) {
			
			PublisherEntity pe = publisherEntity.get();
			if(LibraryApiUtils.doesStringValueExist(publisherToBeUpdated.getEmailId())) {
				pe.setEmailId(publisherToBeUpdated.getEmailId());
				
			}
			if(LibraryApiUtils.doesStringValueExist(publisherToBeUpdated.getPhoneNumber())) {
				pe.setPhoneNumber(publisherToBeUpdated.getPhoneNumber());
			}
			
			publisherRepository.save(pe);
			publisherToBeUpdated = createPublisherFromEntity(pe);
			
		}else {
			throw new LibraryResourceNotFoundException(traceId, "Publisher Id: " + publisherToBeUpdated.getPublisherId() + " ");
		}
		
		
		
		
		
	}
	



public void deletePublisher(Integer publisherId, String traceId) throws LibraryResourceNotFoundException {
	
	try {
		publisherRepository.deleteById(publisherId);
	} catch(EmptyResultDataAccessException e) {
		logger.error("TraceId: {}, Publisher Id:{} Not found", traceId, publisherId, e);
		throw new LibraryResourceNotFoundException(traceId, "Publisher Id: " + publisherId + " Notfound");
		
	}
	
}



public List<Publisher> searchPublisher(String name, String traceId) {
	
	List<PublisherEntity> publisherEntities = null;
	if(LibraryApiUtils.doesStringValueExist(name)) {
		publisherEntities = publisherRepository.findByNameContaining(name);
	}
	if(publisherEntities != null && publisherEntities.size() > 0) {
		return createPublisherForSearchResponse(publisherEntities);
	}else {
		return Collections.emptyList();
	}
}

private List<Publisher> createPublisherForSearchResponse(List<PublisherEntity> publisherEntities) {
	
	
	return publisherEntities.stream()
			.map(pe -> createPublisherFromEntity(pe))
			.collect(Collectors.toList());
}



private Publisher createPublisherFromEntity(PublisherEntity pe) {
	
	return new Publisher(pe.getPublisherid(), pe.getName(), pe.getEmailId(), pe.getPhoneNumber());
}

	
	

}
