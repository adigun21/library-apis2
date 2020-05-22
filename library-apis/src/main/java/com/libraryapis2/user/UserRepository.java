package com.libraryapis2.user;

import java.util.List;

import org.hamcrest.Matcher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>{
	
	UserEntity findByUsername(String username);

	List<UserEntity> findByFirstNameAndLastNameContaining(String firstName, String lastName);

	List<UserEntity> findByFirstNameContaining(String firstName);

	List<UserEntity> findByLastNameContaining(String lastName);
	
	
	
	

	

}
