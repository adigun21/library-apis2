package com.libraryapis2.publisher;

import java.util.List;

import org.hamcrest.Matcher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends CrudRepository<PublisherEntity, Integer>{
	List<PublisherEntity> findByNameContaining(String name);

	

}
