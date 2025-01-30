package fr.univtln.bruno.samples.jpa.model.documents;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;


/**
 * Represents a document entity in the system.
 * This is a base class for different types of documents using joined inheritance strategy.
 *
 * @author Emmanuel Bruno
 * @version 0.0.1
 * @since 0.0.1
 *
 * The class includes basic document attributes such as:
 * - A unique identifier
 * - A title
 * - A publication date
 * - A list of authors
 * The table mapping is done using JPA annotations with a joined inheritance strategy,
 * allowing for specialized document types to extend this base class.
 * The many-to-many relationship with authors is managed through a join table named "document_author".
 * This class uses Lombok annotations to reduce boilerplate code:
 * - @SuperBuilder for hierarchical builder pattern
 * - @AllArgsConstructor and @NoArgsConstructor for constructors
 * - @Getter and @Setter for accessor methods
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="documents")

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
@Setter
public class Document {

  /**
   * The unique identifier for the document entity.
   * This ID is automatically generated using a sequence generator named "document_seq".
   * The sequence is configured with an allocation size of 1, meaning it will increment by 1 for each new document.
   */
  @Id
  @SequenceGenerator(name = "document_seq", sequenceName = "document_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "document_seq")
  private Long id;

  /**
   * The title of the document.
   */
  private String title;

  /**
   * The publication date of the document.
   */
  private LocalDate publicationDate;

  /**
   * The list of authors associated with the document.
   * This is a many-to-many relationship managed by the "document_author" join table.
   */
  @ManyToMany
  @JoinTable(name = "document_author",
    joinColumns = @JoinColumn(name = "document_id"),
    inverseJoinColumns = @JoinColumn(name = "author_id"))
  private List<Author> authors;
}



