package com.libraryapis2.testUtils;

import java.util.Optional;

import com.libraryapis2.publisher.Publisher;
import com.libraryapis2.publisher.PublisherEntity;

public class LibraryApiTestUtil {
	
	public static Publisher createPublisher() { 
		return new Publisher(null, TestConstants.TEST_PUBLISHER_NAME,
				TestConstants.TEST_PUBLISHER_EMAIL, TestConstants.TEST_PUBLISHER_PHONE);
	}

	public static PublisherEntity createPublisherEntity() {
		return new PublisherEntity(TestConstants.TEST_PUBLISHER_NAME,
				TestConstants.TEST_PUBLISHER_EMAIL, TestConstants.TEST_PUBLISHER_PHONE);
		
	}

	public static Optional<PublisherEntity> createPublisherEntityOptional() {
		return Optional.of(createPublisherEntity());
	
	

}
}