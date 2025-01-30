package fr.univtln.bruno.samples.jpa.model.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
/**
 * Represents a Book entity that extends the Document class.
 * This class maintains information about books including their ISBN and page count.
 * - @Entity annotation indicates that this class is a JPA entity
 * - @Table annotation specifies the table name in the database as "books"
 * The class uses Lombok annotations to automatically generate:
 * - Builders (@SuperBuilder)
 * - All-args constructor (@AllArgsConstructor)
 * - Getters (@Getter)
 * - Setters (@Setter)
 * - No-args constructor (@NoArgsConstructor) with protected access
 */
@Entity
@Table(name = "books")

@SuperBuilder
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Book extends Document {

  /**
   * The ISBN can be either ISBN-10 or ISBN-13.
   */
  @NotBlank
  private String isbn;

  /**
   * The total number of pages contained in the book, which indicates its length.
   */
  @Positive
  private int pages;

}
