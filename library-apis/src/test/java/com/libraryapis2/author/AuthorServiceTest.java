package com.libraryapis2.author;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.libraryapis2.Author.Author;
import com.libraryapis2.Author.AuthorEntity;
import com.libraryapis2.Author.AuthorRepository;
import com.libraryapis2.Author.AuthorService;
import com.libraryapis2.exception.LibraryResourceAlreadyExistException;
import com.libraryapis2.exception.LibraryResourceNotFoundException;
import com.libraryapis2.model.common.Gender;
import com.libraryapis2.testUtils.LibraryApiTestUtil;
import com.libraryapis2.testUtils.TestConstants;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceTest {
	

	@Mock
	AuthorRepository authorRepository;
	
	AuthorService authorService;

	@Before
	public  void setUp() throws Exception {
		authorService = new AuthorService(authorRepository);
	}

	
	@Test
	public void AddAuthor_success() throws LibraryResourceAlreadyExistException {
		
		when(authorRepository.save(any(AuthorEntity.class)))
		.thenReturn(LibraryApiTestUtil.createAuthorEntity());
		Author author = LibraryApiTestUtil.createAuthor();
		authorService.addAuthor(author, TestConstants.API_TRACE_ID);
		
		verify(authorRepository, times(1)).save(any(AuthorEntity.class));
		assertNotNull(author.getAuthorId());
		assertTrue(author.getFirstName().equals(TestConstants.TEST_AUTHOR_FIRST_NAME));
		
	}

	@Test(expected = LibraryResourceAlreadyExistException.class)
	public void AddAuthor_failure() throws LibraryResourceAlreadyExistException {
		
		doThrow(DataIntegrityViolationException.class).when(authorRepository).save(any(AuthorEntity.class));
		Author author = LibraryApiTestUtil.createAuthor();
		authorService.addAuthor(author, TestConstants.API_TRACE_ID);
		verify(authorRepository, times(1)).save(any(AuthorEntity.class));
		}
	


	@Test
	public void getAuthor_success() throws LibraryResourceNotFoundException {
		
		when(authorRepository.findById(anyInt()))
			.thenReturn(LibraryApiTestUtil.createAuthorEntityOptional());
		Author author = authorService.getAuthor(123,TestConstants.API_TRACE_ID);
		
		verify(authorRepository, times(1)).findById(123);
		assertNotNull(author);
		assertNotNull(author.getAuthorId());
		
		
		
	}

	@Test(expected = LibraryResourceNotFoundException.class)
	public void getAuthor_failure() throws LibraryResourceNotFoundException {
		
		when(authorRepository.findById(anyInt()))
		.thenReturn(Optional.empty());
		authorService.getAuthor(123,TestConstants.API_TRACE_ID);
		verify(authorRepository, times(1)).findById(123);
		
		
		
	}

		@Test
	public void UpdateAuthor_success() throws LibraryResourceAlreadyExistException, LibraryResourceNotFoundException {
		
		
		when(authorRepository.save(any(AuthorEntity.class)))
		.thenReturn(LibraryApiTestUtil.createAuthorEntity());
		Author author = LibraryApiTestUtil.createAuthor();
		authorService.addAuthor(author, TestConstants.API_TRACE_ID);
		verify(authorRepository, times(1)).save(any(AuthorEntity.class));
		
		LocalDate updateDob = author.getDateOfBirth().minusMonths(5);
		author.setDateOfBirth(updateDob);
		
		when(authorRepository.findById(anyInt()))
		.thenReturn(LibraryApiTestUtil.createAuthorEntityOptional());
		authorService.updateAuthor(author, TestConstants.API_TRACE_ID);
		
		verify(authorRepository, times(1)).findById(author.getAuthorId());
		verify(authorRepository, times(2)).save(any(AuthorEntity.class));
		
		assertTrue(author.getDateOfBirth().isEqual(updateDob));
		
		
	}

	@Test
	public void deleteAuthor_success() throws LibraryResourceNotFoundException {
		
		doNothing().when(authorRepository).deleteById(123);
		authorService. deleteAuthor(123,TestConstants.API_TRACE_ID);
		verify(authorRepository, times(1)).deleteById(123);
		
	}
	
	@Test(expected = LibraryResourceNotFoundException.class)
	public void deleteAuthor_failure() throws LibraryResourceNotFoundException {
		
		doThrow(EmptyResultDataAccessException.class).when(authorRepository).deleteById(123);
		authorService. deleteAuthor(123,TestConstants.API_TRACE_ID);
		verify(authorRepository, times(1)).deleteById(123);
		
	}

	@Test
	public void SearchAuthor_success_firstname_lastname() {
		
		List<AuthorEntity>authorEntityList = Arrays.asList(
			    new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "a", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(30), Gender.Female),
				new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "b", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(32),Gender.Male));
						
		when(authorRepository.findByFirstNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME))
			.thenReturn(authorEntityList);
		
		List<Author> authors = authorService.searchAuthor(TestConstants.TEST_AUTHOR_FIRST_NAME, "", TestConstants.API_TRACE_ID);
		
		verify(authorRepository, times(1)).findByFirstNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME);
		assertEquals(authorEntityList.size(),authors.size());
		assertEquals(authorEntityList.size(),authors.stream()
				
				.filter(author -> author.getFirstName().contains(TestConstants.TEST_AUTHOR_FIRST_NAME)).count());
		
	}
	
	@Test
	public void SearchAuthor_success_firstname() {
		
		List<AuthorEntity>authorEntityList = Arrays.asList(
			    new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "a", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(30), Gender.Female),
				new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "b", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(32), Gender.Male),
				new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "c", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(34), Gender.Male)
						
				);
						
		when(authorRepository.findByFirstNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME))
			.thenReturn(authorEntityList);
		
		List<Author> authors = authorService.searchAuthor(TestConstants.TEST_AUTHOR_FIRST_NAME, "", TestConstants.API_TRACE_ID);
		
		verify(authorRepository, times(1)).findByFirstNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME);
		assertEquals(authorEntityList.size(),authors.size());
		assertEquals(authorEntityList.size(),authors.stream()
				
				.filter(author -> author.getFirstName().contains(TestConstants.TEST_AUTHOR_FIRST_NAME)).count());
		
	}
	
	@Test
	public void SearchAuthor_success_lastname() {
		
		List<AuthorEntity>authorEntityList = Arrays.asList(
			    new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "a", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(30), Gender.Female),
				new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "b", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(32), Gender.Male),
				new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "c", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(34), Gender.Male),
				new AuthorEntity(TestConstants.TEST_AUTHOR_FIRST_NAME + "d", TestConstants.TEST_AUTHOR_LAST_NAME, LocalDate.now().minusYears(34), Gender.Male));
						
		when(authorRepository.findByLastNameContaining(TestConstants.TEST_AUTHOR_LAST_NAME))
			.thenReturn(authorEntityList);
		
		List<Author> authors = authorService.searchAuthor(" ",TestConstants.TEST_AUTHOR_LAST_NAME, TestConstants.API_TRACE_ID);
		
		verify(authorRepository, times(1)).findByLastNameContaining(TestConstants.TEST_AUTHOR_LAST_NAME);
		assertEquals(authorEntityList.size(),authors.size());
		assertEquals(authorEntityList.size(),authors.stream()
				.filter(author -> author.getLastName().contains(TestConstants.TEST_AUTHOR_LAST_NAME)).count());
		
	}
	
	
	
	@Test
	public void SearchAuthor_failure() {
		
		when(authorRepository.findByFirstNameAndLastNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME))
			.thenReturn(Collections.emptyList());
		
		List<Author> authors = authorService.searchAuthor(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME, TestConstants.API_TRACE_ID);
		
		verify(authorRepository, times(1)).findByFirstNameAndLastNameContaining(TestConstants.TEST_AUTHOR_FIRST_NAME, TestConstants.TEST_AUTHOR_LAST_NAME);
		assertEquals(0,authors.size());
		
	}


}
