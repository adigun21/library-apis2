package com.libraryapis2.user;

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
@RequestMapping(path = "/v1/users")
public class UserController {
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class); 
	
	private UserService userService;
	
	
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	
	@GetMapping(path = "/{userId}")
	public ResponseEntity<?> getUser(@PathVariable Integer userId,
										  @RequestHeader(value = "TraceId", defaultValue = "") String traceId)
										
												  throws LibraryResourceNotFoundException {
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		
		
		return new ResponseEntity<>(userService.getUser(userId, traceId), HttpStatus.OK);
	

}
	@PostMapping
	public ResponseEntity<?> addUser(@Valid @RequestBody User user,
			                              @RequestHeader(value = "TraceId", defaultValue = "") String traceId)
			                            		  throws LibraryResourceAlreadyExistException{
		
		
		logger.debug("Request to add User:{}", user);
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		logger.debug("Added TraceId: {}",traceId);
	    userService.addUser(user, traceId);
		
		logger.debug("Returning response for TraceId:{}",traceId);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	@PutMapping(path = "/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable Integer userId, @Valid @RequestBody User user,
			                                 @RequestHeader(value = "TraceId", defaultValue = "") String traceId)
			                               throws LibraryResourceNotFoundException {
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
			user.setUserId(userId);
			userService.updateUser(user, traceId);
			logger.debug("Returning response for TraceId: {}", traceId);
			return new ResponseEntity<>(user, HttpStatus.OK);
	

}
	
	@DeleteMapping(path = "/{userId}")
	
	public ResponseEntity<?> deleteUser(@PathVariable Integer userId,
			                                 @RequestHeader(value = "TraceId", defaultValue = "") String traceId) 
			                                throws LibraryResourceNotFoundException{
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		
			userService.deleteUser(userId, traceId);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
			                                	
}
	
	@GetMapping(path = "/search")
	public ResponseEntity<?> searchUser(@RequestParam String firstName, @RequestParam String lastName,
			                                 @RequestHeader(value = "TraceId", defaultValue = "") String traceId)
			                                		 throws LibraryResourceBadRequestException{
		
		if(!LibraryApiUtils.doesStringValueExist(traceId)) {
			traceId = UUID.randomUUID().toString();
			}
		
		
		if(!LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName)) {
			logger.error("TraceId: {}, Please enter at least one search critteria to search for users!!!", traceId);
			throw new LibraryResourceBadRequestException(traceId, "please enter name for search User.");
			
			
		}
			logger.debug("Returning response for TraceId: {}", traceId);
			return new ResponseEntity<>(userService.searchUser(firstName, lastName, traceId), HttpStatus.OK);
		}
		
		
	
	
}