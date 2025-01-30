package fr.univtln.bruno.samples.jpa;

import fr.univtln.bruno.samples.jpa.model.documents.Author;
import fr.univtln.bruno.samples.jpa.model.utils.DataGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;

import java.sql.SQLException;

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
 *
 * @author Your Name
 * @version 1.0
 * @see EntityManagerFactory
 * @see Server
 */
@Slf4j
public class App {

  private static final EntityManagerFactory emf;

  private static final Server dbServer;

  private static final class DatabaseConfig {
    private static final String DB_ARGS[] = {"-tcpAllowOthers","-webAllowOthers","-pgAllowOthers","-ifNotExists"};
    private static final String PERSISTENCE_UNIT = "tpJakartaUnit";
  }

  static {
    Server tryServer = null;
    EntityManagerFactory tryEmf = null;

    try {
      tryServer = Server.createTcpServer(DatabaseConfig.DB_ARGS).start();
      log.info("{}", "H2 database server started and connection is open.");
      log.info("{}", "URL: " + tryServer.getURL());
      tryEmf = Persistence.createEntityManagerFactory(DatabaseConfig.PERSISTENCE_UNIT);

    } catch (SQLException e) {
      log.error("{}", "Failed to start H2 database server.", e);
      System.exit(0);
    }

    dbServer = tryServer;
    emf = tryEmf;
    // Stop the H2 database server
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      emf.close();
      log.info("{}", "EMF closed");
      dbServer.stop();
      log.info("{}", "H2 database server stopped.");
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

    try(DataGenerator dataGenerator =
    DataGenerator.builder()
      .entityManagerFactory(getEntityManagerFactory())
      .bookCount(2000)
      .authorCount(180)
      .loanCount(2000)
      .build()) {
      dataGenerator.generateData();
      }

    try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
      entityManager.createQuery("select a from Author a", Author.class)
        .setFirstResult(0)
        .setMaxResults(10)
        .getResultStream()
        .map(Object::toString)
        .forEach(log::info);
    }
  }
}
