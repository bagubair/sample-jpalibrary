package fr.univtln.bruno.samples.jpa;

import fr.univtln.bruno.samples.jpa.model.Loan;
import fr.univtln.bruno.samples.jpa.model.documents.Author;
import fr.univtln.bruno.samples.jpa.model.documents.Document;
import fr.univtln.bruno.samples.jpa.model.users.User;
import fr.univtln.bruno.samples.jpa.model.utils.DataGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

///import com.github.javafaker.Book;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;



/**
 * Main application class that manages the JPA EntityManagerFactory and H2 database server.
 * This class is responsible for:
 * - Starting the H2 database server
 * - Creating and managing the EntityManagerFactory
 * - Providing access to the EntityManagerFactory through a static method
 * - Handling proper shutdown of resources through a shutdown hook
 * - Generating sample data and performing basic JPA queries
 * The class uses static initialization to set up the database server and EntityManagerFactory,
 * ensuring they are available throughout the application's lifecycle.
 * The H2 database server is started with TCP connections allowed from other hosts,
 * and the EntityManagerFactory is created using the "tpJakartaUnit" persistence unit.
 */
@Slf4j
public class App {

  private static final EntityManagerFactory emf;
  private static final Server h2Server;
  private static final Server webServer;
/* 
  private static final class DatabaseConfig {
    private static final String DB_ARGS_TCP[] = { "-tcpAllowOthers", "-pgAllowOthers", "-ifNotExists" };
    private static final String DB_ARGS_WEB[] = { "-webAllowOthers", "-webPort", "9090" };
    private static final String PERSISTENCE_UNIT = "tpJakartaUnit";
  }
*/

  static {
    Server tryH2Server = null;
    Server tryWebServer = null;
    EntityManagerFactory tryEmf = null;

    try {
      // Start H2 database server
      tryH2Server = Server.createTcpServer(DatabaseConfig.DB_ARGS_TCP).start();
      log.info("H2 database server started and connection is open.");
      log.info("URL (h2): {}", tryH2Server.getURL());

      // Start H2 web server
      tryWebServer = Server.createWebServer(DatabaseConfig.DB_ARGS_WEB).start();
      log.info("URL (web): {}", tryWebServer.getURL());

      // Create EntityManagerFactory

      tryEmf = Persistence.createEntityManagerFactory(DatabaseConfig.PERSISTENCE_UNIT);

    } catch (SQLException e) {
      log.error("Failed to start H2 database server.", e);
      System.exit(0);
    }

    h2Server = tryH2Server;
    webServer = tryWebServer;
    emf = tryEmf;

    // Stop the H2 database server on shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      emf.close();
      log.info("EMF closed");

      h2Server.stop();
      webServer.stop();
      log.info("H2 database server stopped.");
    }));

  }

  public static EntityManagerFactory getEntityManagerFactory() {
    return emf;
  }

  /**
   * Main method to start the application.
   *
   * @param args Command line arguments
   */
  public static void main(String[] args) {

    // Generate sample data
    try (DataGenerator dataGenerator =
           DataGenerator.builder()
             .entityManagerFactory(getEntityManagerFactory())
             .bookCount(2000)
             .authorCount(180)
             .userCount(1000)
             .loanCount(2000)
             .build()) {
      dataGenerator.generateData();
    }

    // Perform basic JPA queries
    try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
      entityManager.createQuery("select a from Author a", Author.class)
        .setFirstResult(0)
        .setMaxResults(10)
        .getResultStream()
        .map(Object::toString)
        .forEach(log::info);
    }

    try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
      entityManager.createQuery("select d from Document d LEFT JOIN d.authors a WHERE a.id = :authorId", Document.class)
        .setParameter("authorId", 1L)
        .setFirstResult(0)
        .getResultStream()
        .map(Object::toString)
        .forEach(log::info);
    }

    try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
      entityManager.createQuery("SELECT l FROM Loan l WHERE l.returnDate IS NULL OR l.dueDate > :today", Loan.class)
        .setParameter("today", LocalDate.now())  // Utilisation de la date actuelle
        .setFirstResult(0)
        .setMaxResults(10)
        .getResultStream()
        .map( l -> l.getDocument().getTitle())
        .forEach(log::info);
    }
    try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
      List<Object[]> results = entityManager.createQuery(
          "SELECT l.user, COUNT(l) FROM Loan l GROUP BY l.user", Object[].class)
          .setFirstResult(0)
          .setMaxResults(10)
          .getResultList();  // On récupère la liste complète
  
      results.forEach(row -> {
          User user = (User) row[0];
          Long loanCount = (Long) row[1];
          log.info(user.getName() + " a " + loanCount + " emprunts.");
      });
  }
  

    emf.close();
    h2Server.stop();
    webServer.stop();
  }

  private static final class DatabaseConfig {
    private static final String[] DB_ARGS_TCP = {"-tcpAllowOthers", "-ifNotExists"};
    private static final String[] DB_ARGS_WEB = {"-webAllowOthers"};

    private static final String PERSISTENCE_UNIT = "tpJakartaUnit";
  }
  
}
