package fr.univtln.bruno.samples.jpa.samples.jpa.model.documents;

import fr.univtln.bruno.samples.jpa.model.documents.Book;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

  private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private final Validator validator = factory.getValidator();

  @Test
  void createBookWithValidData() {
    Book book = Book.builder()
      .isbn("978-3-16-148410-0")
      .pages(300)
      .build();
    assertEquals("978-3-16-148410-0", book.getIsbn());
    assertEquals(300, book.getPages());
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertTrue(violations.isEmpty());
  }

  @Test
  void createBookWithNullIsbn() {
    Book book = Book.builder()
      .isbn(null)
      .pages(300)
      .build();
    assertNull(book.getIsbn());
    assertEquals(300, book.getPages());
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertFalse(violations.isEmpty());
  }

  @Test
  void createBookWithZeroPages() {
    Book book = Book.builder()
      .isbn("978-3-16-148410-0")
      .pages(0)
      .build();
    assertEquals("978-3-16-148410-0", book.getIsbn());
    assertEquals(0, book.getPages());
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertFalse(violations.isEmpty());
  }

  @Test
  void createBookWithNegativePages() {
    Book book = Book.builder()
      .isbn("978-3-16-148410-0")
      .pages(-1)
      .build();
    assertEquals("978-3-16-148410-0", book.getIsbn());
    assertEquals(-1, book.getPages());
    Set<ConstraintViolation<Book>> violations = validator.validate(book);
    assertFalse(violations.isEmpty());
  }
}
