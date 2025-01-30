package fr.univtln.bruno.samples.jpa.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import fr.univtln.bruno.samples.jpa.model.documents.Document;
import fr.univtln.bruno.samples.jpa.model.users.User;

/**
 * Represents a loan transaction in a library system.
 * This entity manages the relationship between users and documents for borrowing purposes.
 *
 * @author Emmanuel Bruno
 * @version 0.0.1
 * @since 0.0.1
 */
@Entity
@Table(name = "loans")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

  /**
   * The unique identifier for the Loan entity.
   * This ID is automatically generated using a sequence generator named "loan_seq"
   * with a sequence name of "loan_sequence" and allocation size of 1.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_seq")
  @SequenceGenerator(name = "loan_seq", sequenceName = "loan_sequence", allocationSize = 1)
  private Long id;

  /**
   * The user who borrowed the item.
   * This field represents a many-to-one relationship between Loan and User entities,
   * where multiple loans can be associated with a single user.
   */
  @ManyToOne
  private User user;

  /**
   * The document associated with this loan.
   * This represents a many-to-one relationship between Loan and Document entities,
   * where multiple loans can be associated with a single document.
   */
  @ManyToOne
  private Document document;

  @Column(name = "due_date", nullable = false)
  private LocalDate dueDate;

  /**
   * The return date of the loan.
   * This date represents when the borrowed item was or should be returned.
   * Stored in the database column "return_date".
   */
  @Column(name = "return_date")
  private LocalDate returnDate;
}
