package com.libraryapis2.publisher;

import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libraryapis2.exception.LibraryResourceAlreadyExistException;
import com.libraryapis2.exception.LibraryResourceNotFoundException;
import com.libraryapis2.util.LibraryApiUtils;

@RestController
@RequestMapping(path = "/v1/publishers")
public class PublisherController {
	
	private static Logger logger = LoggerFactory.getLogger(PublisherController.class); 
	private PublisherService publisherService;
	
	
	
	public PublisherController(PublisherService publisherService) {
		this.publisherService = publisherService;
	}
	
	
	@GetMapping(path = "/{publisherId}")
	public ResponseEntity<?> getPublisher(@PathVariable Integer publisherId,
										  @RequestHeader(value = "TraceId", defaultValue = "") String traceId) {
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		
		Publisher publisher = null;
		
		try {
			publisher = publisherService.getPublisher(publisherId, traceId);
		} catch(LibraryResourceNotFoundException e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(publisher, HttpStatus.OK);
	

}
	@PostMapping
	public ResponseEntity<?> addPublisher(@Valid @RequestBody Publisher publisher,
			                              @RequestHeader(value = "TraceId", defaultValue = "") String traceId){
		
		
		logger.debug("Request to add Publisher:{}", publisher);
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		logger.debug("Added TraceId: {}",traceId);
		
		try {
			 publisherService.addPublisher(publisher, traceId);
		} catch (LibraryResourceAlreadyExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		logger.debug("Returning response for TraceId:{}",traceId);
		return new ResponseEntity<>(publisher, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{publisherId}")
	public ResponseEntity<?> updatePublisher(@PathVariable Integer publisherId, @Valid @RequestBody Publisher publisher,
			                                 @RequestHeader(value = "TraceId", defaultValue = "") String traceId) {
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		
		try {
			publisher.setPublisherId(publisherId);
			publisherService.updatePublisher(publisher, traceId);
		} catch(LibraryResourceNotFoundException e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(publisher, HttpStatus.OK);
	

}
	
	@DeleteMapping(path = "/{publisherId}")
	public ResponseEntity<?> deletePublisher(@PathVariable Integer publisherId,
			                                 @RequestHeader(value = "TraceId", defaultValue = "") String traceId) {
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		
		
		try {
			publisherService.deletePublisher(publisherId, traceId);
		} catch(LibraryResourceNotFoundException e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	
	
}
	
	@GetMapping(path = "/search")
	public ResponseEntity<?> searchPublisher(@RequestParam String name,
			                                 @RequestHeader(value = "TraceId", defaultValue = "") String traceId) {
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		
		
		if(!LibraryApiUtils.doesStringValueExist(name)) {
			
			return new ResponseEntity<>("please enter name for search Publisher", HttpStatus.BAD_REQUEST);
		}
			
			return new ResponseEntity<>(publisherService.searchPublisher(name, traceId), HttpStatus.OK);
		}
		
		
	
	
}