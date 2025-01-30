package fr.univtln.bruno.samples.jpa.model.users;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import fr.univtln.bruno.samples.jpa.model.Loan;

/**
 * Represents a User entity in the library management system.
 * This class is mapped to the "users" table in the database using JPA annotations.
 * '@Entity' annotation indicates that this class is a JPA entity
 * '@Table' specifies the table name in the database
 * The class uses Lombok annotations to automatically generate getters, setters,
 * constructors, and builder pattern implementation.
 * @author Emmanuel Bruno
 * @version 0.0.1
 * @since 0.0.1
 *
 * @see Loan
 */
@Entity
@Table(name="users")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  /**
   * The unique identifier for the user entity.
   * This ID is automatically generated using a sequence generator named "user_seq".
   * The sequence is configured with an allocation size of 1, meaning it will increment by 1 for each new user.
   */
  @Id
  @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
  private Long id;

  /**
   * The name of the user.
   * This field cannot be null as specified by the @Column annotation.
   */
  @Column(nullable = false)
  private String name;

  /**
   * The email address of the user.
   * This field is required and must be unique across all users in the system.
   * `@Column` annotation ensures that:
   * - The email cannot be null (nullable = false)
   * - The email must be unique in the database (unique = true)
   */
  @Column(nullable = false, unique = true)
  private String email;

  /**
   * The collection of loans associated with this user.
   * This is a bidirectional relationship where one user can have multiple loans.
   * The 'mappedBy' attribute indicates that the 'user' field in the Loan entity
   * owns the relationship.
   */
  @OneToMany(mappedBy = "user")
  private Set<Loan> loans;
}
