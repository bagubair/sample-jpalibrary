package fr.univtln.bruno.samples.jpa.samples.jpa.model.documents;

import fr.univtln.bruno.samples.jpa.model.documents.Book;
import jakarta.persistence.*;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BookIT {

  private static EntityManagerFactory entityManagerFactory;
  private EntityManager entityManager;

  // Provides invalid ISBN values including null for parameterized tests
  static Stream<String> invalidIsbnProvider() {
    return Stream.of("", " ", null);
  }

  // Sets up the EntityManagerFactory before all tests
  @BeforeAll
  static void setUpBeforeAll() {
    entityManagerFactory = Persistence.createEntityManagerFactory("testjpaUnit");
  }

  // Closes the EntityManagerFactory after all tests
  @AfterAll
  static void tearDownAfterAll() {
    if (entityManagerFactory != null) {
      entityManagerFactory.close();
    }
  }

  // Sets up the EntityManager before each test
  @BeforeEach
  void setUp() {
    entityManager = entityManagerFactory.createEntityManager();
  }

  // Closes the EntityManager after each test
  @AfterEach
  void tearDown() {
    if (entityManager != null) {
      entityManager.close();
    }
  }

  // Tests persisting and retrieving a valid Book entity
  @Test
  void persistAndRetrieveBook() {
    Book book = Book.builder()
      .isbn("978-3-16-148410-0")
      .pages(300)
      .build();

    entityManager.getTransaction().begin();
    entityManager.persist(book);
    entityManager.getTransaction().commit();

    entityManager.clear();

    Book retrievedBook = entityManager.find(Book.class, book.getId());
    assertNotNull(retrievedBook);
    assertEquals("978-3-16-148410-0", retrievedBook.getIsbn());
    assertEquals(300, retrievedBook.getPages());
  }

  // Parameterized test for persisting a Book with invalid page numbers
  @ParameterizedTest
  @ValueSource(ints = {0, -100})
  void persistBookWithInvalidPages(int pages) {
    Book book = Book.builder()
      .isbn("978-3-16-148410-0")
      .pages(pages)
      .build();

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    entityManager.persist(book);
    Exception exception = assertThrows(RollbackException.class, transaction::commit);

    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertInstanceOf(ConstraintViolationException.class, cause);
    entityManager.getTransaction().rollback();
  }

  // Parameterized test for persisting a Book with invalid ISBN values
  @ParameterizedTest
  @MethodSource("invalidIsbnProvider")
  void persistBookWithInvalidIsbn(String isbn) {
    Book book = Book.builder()
      .isbn(isbn)
      .pages(300)
      .build();

    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    entityManager.persist(book);
    Exception exception = assertThrows(RollbackException.class, transaction::commit);
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertInstanceOf(ConstraintViolationException.class, cause);
    entityManager.getTransaction().rollback();
  }
}
