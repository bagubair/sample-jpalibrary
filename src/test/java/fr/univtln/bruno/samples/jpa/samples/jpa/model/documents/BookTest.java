package fr.univtln.bruno.samples.jpa.samples.jpa.model.documents;

import fr.univtln.bruno.samples.jpa.model.documents.Book;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

  // ValidatorFactory and Validator are used to validate the Book entity
  private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private final Validator validator = factory.getValidator();

  // Tests creating a Book with valid data
  @Test
  void createBookWithValidData() {
    Book book = Book.builder()
      .isbn("978-3-16-148410-0")
      .pages(300)
      .build();
    assertEquals("978-3-16-148410-0", book.getIsbn());
    assertEquals(300, book.getPages());
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertTrue(violations.isEmpty()); // No constraint violations expected
  }

  // Parameterized test for creating a Book with invalid ISBN values
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  void createBookWithInvalidIsbn(String isbn) {
    Book book = Book.builder()
      .isbn(isbn)
      .pages(300)
      .build();
    assertEquals(isbn, book.getIsbn());
    assertEquals(300, book.getPages());
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertFalse(violations.isEmpty()); // Constraint violations expected
  }

  // Parameterized test for creating a Book with invalid page numbers
  @ParameterizedTest
  @ValueSource(ints = {0, -1})
  void createBookWithInvalidPages(int pages) {
    Book book = Book.builder()
      .isbn("978-3-16-148410-0")
      .pages(pages)
      .build();
    assertEquals("978-3-16-148410-0", book.getIsbn());
    assertEquals(pages, book.getPages());
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertFalse(violations.isEmpty()); // Constraint violations expected
  }
}
