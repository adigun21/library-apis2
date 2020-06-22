package com.libraryapis2.book;

import java.util.List;

import org.hamcrest.Matcher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, Integer>{
	List<BookEntity> findByTitleContaining(String title);
    BookEntity findByIsbn(String isbn);


	

}
