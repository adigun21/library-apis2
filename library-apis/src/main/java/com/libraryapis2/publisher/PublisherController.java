package com.libraryapis2.publisher;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryapis2.publisher.model.Publisher;

@RestController
@RequestMapping(path = "/v1/publishers")
public class PublisherController {
	
	@GetMapping(path = "/{publisherId}")
	public Publisher getPublisher(@PathVariable String publisherId) {
		return new Publisher(publisherId, "Prentice Hall Publishers", "rentice@email.com", "1234-456-789");
	
	

}

}