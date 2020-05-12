package com.libraryapis2.publisher;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.libraryapis2.exception.LibraryResourceAlreadyExistException;
import com.libraryapis2.exception.LibraryResourceNotFoundException;
import com.libraryapis2.util.LibraryApiUtils;

@Service
public class PublisherService {
	
	private PublisherRepository publisherRepository;
	
	

	public PublisherService(PublisherRepository publisherRepository) {
		this.publisherRepository = publisherRepository;
	}



	public void addPublisher(Publisher publisherToBeAdded)
			throws LibraryResourceAlreadyExistException {
		
		PublisherEntity publisherEntity = new PublisherEntity(
				publisherToBeAdded.getName(),
				publisherToBeAdded.getEmailId(),
				publisherToBeAdded.getPhoneNumber()
				);
		
		PublisherEntity addedPublisher = null;
		
		try {
			addedPublisher = publisherRepository.save(publisherEntity);
		} catch (DataIntegrityViolationException e) {
			throw new LibraryResourceAlreadyExistException("publisher already exists!!");
		}
		
		publisherToBeAdded.setPublisherId(addedPublisher.getPublisherid());
	
	}



	public Publisher getPublisher(Integer publisherId) throws LibraryResourceNotFoundException {
		
		Optional<PublisherEntity> publisherEntity = publisherRepository.findById(publisherId);
		Publisher publisher = null;
		
		if(publisherEntity.isPresent()) {
			
			PublisherEntity pe = publisherEntity.get();
			publisher = createPublisherFromEntity(pe);
			
		}else {
			throw new LibraryResourceNotFoundException(" " + publisherId + " ");
		}
		
		return publisher;
		
		
	}

	public void updatePublisher(Publisher publisherToBeUpdated) throws LibraryResourceNotFoundException {
		
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
			throw new LibraryResourceNotFoundException(" " + publisherToBeUpdated.getPublisherId() + " ");
		}
		
		
		
		
		
	}
	
private Publisher createPublisherFromEntity(PublisherEntity pe) {
		
		return new Publisher(pe.getPublisherid(), pe.getName(), pe.getEmailId(), pe.getPhoneNumber());
	}

	
	
	

}