package com.libraryapis2.Author;

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
public class AuthorService {
	
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(AuthorService.class);
	
	private AuthorRepository authorRepository;
	
	

	public AuthorService(AuthorRepository authorRepository) {
		this.authorRepository = authorRepository;
	}



	public void addAuthor(Author authorToBeAdded, String traceId)
			throws LibraryResourceAlreadyExistException {
		
		logger.debug("TraceId: {}, Request to add Author:{}",traceId, authorToBeAdded);
		AuthorEntity authorEntity = new AuthorEntity(
			  authorToBeAdded.getFirstName(),
				authorToBeAdded.getLastName(),
				authorToBeAdded.getDateOfBirth(),
				authorToBeAdded.getGender()
				);
		
		AuthorEntity addedAuthor = null;
		
		try {
			addedAuthor = authorRepository.save(authorEntity);
		} catch (DataIntegrityViolationException e) {
			logger.error("TraceId: {}, author already exists!!", traceId, e);
			throw new LibraryResourceAlreadyExistException(traceId, "author already exists!!");
		}
		
		authorToBeAdded.setAuthorId(addedAuthor.getAuthorId());
		logger.info("TraceId: {}, Author added: {}", traceId, authorToBeAdded);
	}



	public Author getAuthor(Integer authorId, String traceId) throws LibraryResourceNotFoundException {
		
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorId);
		Author author = null;
		
		if(authorEntity.isPresent()) {
			
			AuthorEntity ae = authorEntity.get();
			author = createAuthorFromEntity(ae);
			
		}else {
			throw new LibraryResourceNotFoundException(traceId, "Author Id: " + authorId + " Not Found");
		}
		
		return author;
		
		
	}

	public void updateAuthor(Author authorToBeUpdated, String traceId) throws LibraryResourceNotFoundException {
		
		Optional<AuthorEntity> authorEntity = authorRepository.findById(authorToBeUpdated.getAuthorId());
		
		Author author = null;
		
		if(authorEntity.isPresent()) {
			
			AuthorEntity ae = authorEntity.get();
			if(authorToBeUpdated.getDateOfBirth()!= null) {
				ae.setDateOfBirth(authorToBeUpdated.getDateOfBirth());
				
			}
			if(authorToBeUpdated.getGender()!= null) {
				ae.setGender(authorToBeUpdated.getGender());
			}
			
			authorRepository.save(ae);
			authorToBeUpdated = createAuthorFromEntity(ae);
			
		}else {
			throw new LibraryResourceNotFoundException(traceId, "Author Id: " + authorToBeUpdated.getAuthorId() + " ");
		}
		
		
		
		
		
	}
	



public void deleteAuthor(Integer authorId, String traceId) throws LibraryResourceNotFoundException {
	
	try {
		authorRepository.deleteById(authorId);
	} catch(EmptyResultDataAccessException e) {
		logger.error("TraceId: {}, AuthorId:{} Not found", traceId, authorId, e);
		throw new LibraryResourceNotFoundException(traceId, "Author Id: " + authorId + " Notfound");
		
	}
	
}



public List<Author> searchAuthor(String firstName, String lastName, String traceId) {
	
	List<AuthorEntity> authorEntities = null;
	if(LibraryApiUtils.doesStringValueExist(firstName) && LibraryApiUtils.doesStringValueExist(lastName) ) {
		authorEntities = authorRepository.findByFirstNameAndLastNameContaining(firstName, lastName);
		
	} else if(LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName) ) {
		authorEntities = authorRepository.findByFirstNameContaining(firstName);
		} else if(!LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName) ) {
		authorEntities = authorRepository.findByLastNameContaining(lastName);
		}
		if(authorEntities != null && authorEntities.size() > 0) {
		return createAuthorForSearchResponse(authorEntities);
	}else {
		
		return Collections.emptyList();

	}

}


private Author createAuthorFromEntity(AuthorEntity ae) {
	
	return new Author(ae.getAuthorId(), ae.getFirstName(), ae.getLastName(), ae.getDateOfBirth(), ae.getGender());
}

private List<Author> createAuthorForSearchResponse(List<AuthorEntity> authorEntities) {
	
	
	return authorEntities.stream()
			.map(ae ->  new Author(ae.getAuthorId(), ae.getFirstName(), ae.getLastName(), ae.getDateOfBirth(), ae.getGender()))
			.collect(Collectors.toList());

}
	
	

}
