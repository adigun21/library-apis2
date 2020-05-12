package com.libraryapis2.publisher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryapis2.exception.LibraryResourceAlreadyExistException;
import com.libraryapis2.exception.LibraryResourceNotFoundException;

@RestController
@RequestMapping(path = "/v1/publishers")
public class PublisherController {
	
	private PublisherService publisherService;
	
	
	
	public PublisherController(PublisherService publisherService) {
		this.publisherService = publisherService;
	}
	
	
	@GetMapping(path = "/{publisherId}")
	public ResponseEntity<?> getPublisher(@PathVariable Integer publisherId) {
		
		Publisher publisher = null;
		
		try {
			publisher = publisherService.getPublisher(publisherId);
		} catch(LibraryResourceNotFoundException e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(publisher, HttpStatus.OK);
	

}
	@PostMapping
	public ResponseEntity<?> addPublisher(@RequestBody Publisher publisher){
		try {
			 publisherService.addPublisher(publisher);
		} catch (LibraryResourceAlreadyExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<>(publisher, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{publisherId}")
	public ResponseEntity<?> updatePublisher(@PathVariable Integer publisherId, @RequestBody Publisher publisher) {
		
		
		try {
			publisher.setPublisherId(publisherId);
			publisherService.updatePublisher(publisher);
		} catch(LibraryResourceNotFoundException e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(publisher, HttpStatus.OK);
	

}
	
	@DeleteMapping(path = "/{publisherId}")
	public ResponseEntity<?> deletePublisher(@PathVariable Integer publisherId) {
		
		
		try {
			publisherService.deletePublisher(publisherId);
		} catch(LibraryResourceNotFoundException e) {
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	
	
}
	
}