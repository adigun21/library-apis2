package com.libraryapis2.Author;

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
import com.libraryapis2.exception.LibraryResourceBadRequestException;
import com.libraryapis2.exception.LibraryResourceNotFoundException;
import com.libraryapis2.exception.LibraryResourceUnauthorizedException;
import com.libraryapis2.util.LibraryApiUtils;

@RestController
@RequestMapping(path = "/v1/authors")
public class AuthorController {
	
	private static Logger logger = LoggerFactory.getLogger(AuthorController.class); 
	private AuthorService authorService;
	
	
	
	public AuthorController(AuthorService authorService) {
		this.authorService = authorService;
	}
	
	
	@GetMapping(path = "/{authorId}")
	public ResponseEntity<?> getAuthor(@PathVariable Integer authorId,
										  @RequestHeader(value = "TraceId", defaultValue = "") String traceId) 
												  throws LibraryResourceNotFoundException {
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		
		return new ResponseEntity<>(authorService.getAuthor(authorId, traceId), HttpStatus.OK);
	

}
	@PostMapping
	public ResponseEntity<?> addAuthor(@Valid @RequestBody Author author,
			                              @RequestHeader(value = "TraceId", defaultValue = "") String traceId,
			                              @RequestHeader(value = "Authorization") String bearerToken)
			                           throws LibraryResourceAlreadyExistException, LibraryResourceUnauthorizedException {
		
		
		logger.debug("Request to add Author:{}", author);
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		logger.debug("Added TraceId: {}",traceId);
		
		if(!LibraryApiUtils.isUserAdmin(bearerToken)) {
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) + " attempted to add a Author. Disallowed because user is not Admin");
            throw new LibraryResourceUnauthorizedException(traceId, "User not allowed to Add a Author");
        }
	    authorService.addAuthor(author, traceId);
		
		logger.debug("Returning response for TraceId:{}",traceId);
		return new ResponseEntity<>(author, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{authorId}")
	public ResponseEntity<?> updateAuthor(@PathVariable Integer authorId, @Valid @RequestBody Author author,
			                                 @RequestHeader(value = "TraceId", defaultValue = "") String traceId,
			                                 @RequestHeader(value = "Authorization") String bearerToken)
			                                throws LibraryResourceNotFoundException, LibraryResourceUnauthorizedException {
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
			logger.debug("Added TraceId: {}", traceId);
			
		if(!LibraryApiUtils.isUserAdmin(bearerToken)) {
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) + " attempted to add a Author. Disallowed because user is not Admin");
            throw new LibraryResourceUnauthorizedException(traceId, "User not allowed to Add a Author");
        }
			author.setAuthorId(authorId);
			authorService.updateAuthor(author, traceId);
		
		return new ResponseEntity<>(author, HttpStatus.OK);
	

}
	
	@DeleteMapping(path = "/{authorId}")
	public ResponseEntity<?> deleteAuthor(@PathVariable Integer authorId,
			                                 @RequestHeader(value = "TraceId", defaultValue = "") String traceId,
			                                 @RequestHeader(value = "Authorization") String bearerToken)
			                               throws LibraryResourceNotFoundException, LibraryResourceUnauthorizedException {
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		if(!LibraryApiUtils.isUserAdmin(bearerToken)) {
            logger.error(LibraryApiUtils.getUserIdFromClaim(bearerToken) + " attempted to delete a Author. Disallowed because user is not Admin");
            throw new LibraryResourceUnauthorizedException(traceId, "User not allowed to Add a Author");
        }
        logger.debug("Added TraceId: {}", traceId);
		
			authorService.deleteAuthor(authorId, traceId);
		
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
	
	
}
	
	@GetMapping(path = "/search")
	public ResponseEntity<?> searchAuthor(@RequestParam String firstName, @RequestParam String lastName,
			                                 @RequestHeader(value = "TraceId", defaultValue = "") String traceId)
			                                		 throws LibraryResourceBadRequestException{
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		
		
		if(!LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName)) {
			logger.error("TraceId: {}, Please enter at least one search Authors!!!", traceId);
			throw new LibraryResourceBadRequestException(traceId, "please enter name for search Author.");
			
			
		}
			logger.error("Returning response for TraceId: {}", traceId);
			return new ResponseEntity<>(authorService.searchAuthor(firstName, lastName, traceId), HttpStatus.OK);
		}
		
		
	
	
}