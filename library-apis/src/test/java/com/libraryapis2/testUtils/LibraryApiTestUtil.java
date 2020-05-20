package com.libraryapis2.testUtils;

import java.time.LocalDate;
import java.util.Optional;

import com.libraryapis2.model.common.Gender;
import com.libraryapis2.Author.Author;
import com.libraryapis2.Author.AuthorEntity;
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
	public static Author createAuthor() {
		return new Author(null, TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(30),Gender.Female);
	}
	
	public static AuthorEntity createAuthorEntity() {
		return  new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(30), Gender.Female);
	}

	public static Optional<AuthorEntity> createAuthorEntityOptional() {
		return Optional.of(createAuthorEntity());
	}

	
}