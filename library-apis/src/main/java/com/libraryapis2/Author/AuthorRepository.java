package com.libraryapis2.Author;

import java.util.List;

import org.hamcrest.Matcher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Integer>{

	List<AuthorEntity> findByFirstNameAndLastNameContaining(String firstName, String lastName);

	List<AuthorEntity> findByFirstNameContaining(String firstName);

	List<AuthorEntity> findByLastNameContaining(String lastName);
	
	
	
	

	

}
