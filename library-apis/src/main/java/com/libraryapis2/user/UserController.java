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
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
                                          @RequestHeader(value = "Authorization") String bearerToken)
            throws LibraryResourceNotFoundException, LibraryResourceUnauthorizedException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        return new ResponseEntity<>(userService.getUser(userId, traceId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addUser(@Valid @RequestBody User user,
                                          @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws LibraryResourceAlreadyExistException {

        logger.debug("Request to add User: {}", user);
        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);
        userService.addUser(user, traceId);

        logger.debug("Returning response for TraceId: {}", traceId);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Integer userId,
                                             @Valid @RequestBody User user,
                                             @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
                                             @RequestHeader(value = "Authorization") String bearerToken)
            throws LibraryResourceNotFoundException, LibraryResourceUnauthorizedException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        if(!LibraryApiUtils.isUserAdmin(bearerToken)) {
            logger.error("Trace Id: {}, even an admin user is not allowed to update a user's details", traceId);
            throw new LibraryResourceUnauthorizedException(traceId, "Even an admin is not allowed to update a user's details");
        }
        
        int userIdInClaim = LibraryApiUtils.getUserIdFromClaim(bearerToken);
        if(userIdInClaim !=userId) {
        	logger.error("Trace Id: {}, UserId {} not allowed to update the details of another user {} ", traceId, userIdInClaim, userId);
            throw new LibraryResourceUnauthorizedException(traceId, "Not allowed to update the details of another user");
        }
        
    
        user.setUserId(userId);
        userService.updateUser(user, traceId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer userId,
                                             @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId,
                                             @RequestHeader(value = "Authorization") String bearerToken)
            throws LibraryResourceNotFoundException, LibraryResourceUnauthorizedException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        logger.debug("Added TraceId: {}", traceId);

        if(LibraryApiUtils.isUserAdmin(bearerToken)) {
            logger.error("Trace Id: {}, even an admin user is not allowed to delete a user", traceId);
            throw new LibraryResourceUnauthorizedException(traceId, "Even an admin user is not allowed to delete a user");
        }
        
        int userIdInClaim = LibraryApiUtils.getUserIdFromClaim(bearerToken);
        if(userIdInClaim != userId) {
            logger.error("Trace Id: {}, UserId {} not allowed to delete another user {} ", traceId, userIdInClaim, userId);
            throw new LibraryResourceUnauthorizedException(traceId, "Not allowed to delete another user");
        }
        
        userService.deleteUser(userId, traceId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<?> searchUser(@RequestParam String firstName, @RequestParam String lastName,
                                             @RequestHeader(value = "Trace-Id", defaultValue = "") String traceId)
            throws LibraryResourceBadRequestException {

        if(!LibraryApiUtils.doesStringValueExist(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        if(!LibraryApiUtils.doesStringValueExist(firstName) && !LibraryApiUtils.doesStringValueExist(lastName)) {
            logger.error("TraceId: {}, Please enter at least one search criteria to search Users!!", traceId);
            throw new LibraryResourceBadRequestException(traceId, "Please enter a name to search User.");
        }

        return new ResponseEntity<>(userService.searchUser(firstName, lastName, traceId), HttpStatus.OK);
    }
}