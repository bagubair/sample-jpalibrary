package fr.univtln.bruno.samples.jpa.samples.jpa.model.documents;

import fr.univtln.bruno.samples.jpa.model.documents.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class BookIT {

  private EntityManagerFactory entityManagerFactory;
  private EntityManager entityManager;

  @BeforeEach
  void setUp() {
    entityManagerFactory = Persistence.createEntityManagerFactory("testjpaUnit");
    entityManager = entityManagerFactory.createEntityManager();
  }

  @AfterEach
  void tearDown() {
    if (entityManager != null) {
      entityManager.close();
    }
    if (entityManagerFactory != null) {
      entityManagerFactory.close();
    }
  }

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

  @Test
  void persistBookWithZeroPages() {
    Book book = Book.builder()
      .isbn("978-3-16-148410-0")
      .pages(0)
      .build();

    entityManager.getTransaction().begin();
    Exception exception = assertThrows(jakarta.persistence.RollbackException.class, () -> {
      entityManager.persist(book);
      entityManager.getTransaction().commit();
    });
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertInstanceOf(jakarta.validation.ConstraintViolationException.class, cause);
    entityManager.getTransaction().rollback();
  }

  @Test
  void persistBookWithNegativePages() {
    Book book = Book.builder()
      .isbn("978-3-16-148410-0")
      .pages(-100)
      .build();

    entityManager.getTransaction().begin();
    Exception exception = assertThrows(jakarta.persistence.RollbackException.class, () -> {
      entityManager.persist(book);
      entityManager.getTransaction().commit();
    });
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertInstanceOf(jakarta.validation.ConstraintViolationException.class, cause);
    entityManager.getTransaction().rollback();
  }

  @Test
  void persistBookWithBlankIsbn() {
    Book book = Book.builder()
      .isbn("")
      .pages(300)
      .build();

    entityManager.getTransaction().begin();
    Exception exception = assertThrows(jakarta.persistence.RollbackException.class, () -> {
      entityManager.persist(book);
      entityManager.getTransaction().commit();
    });
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertInstanceOf(jakarta.validation.ConstraintViolationException.class, cause);
    entityManager.getTransaction().rollback();
  }

  @Test
  void persistBookWithNullIsbn() {
    Book book = Book.builder()
      .isbn(null)
      .pages(300)
      .build();

    entityManager.getTransaction().begin();
    Exception exception = assertThrows(jakarta.persistence.RollbackException.class, () -> {
      entityManager.persist(book);
      entityManager.getTransaction().commit();
    });
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertInstanceOf(jakarta.validation.ConstraintViolationException.class, cause);
    entityManager.getTransaction().rollback();
  }

}
