package fr.univtln.bruno.samples.jpa.model.documents;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.Embeddable;
import lombok.*;
/**
 * Represents a postal address that can be embedded in other entities.
 * This class is designed to be used as an embeddable component in JPA entities.
 * The class implements Serializable to support serialization of the address data.
 * It uses Lombok annotations to reduce boilerplate code:
 * - @Setter: Generates setters for all fields
 * - @Getter: Generates getters for all fields
 * - @Builder: Implements the Builder pattern
 * - @AllArgsConstructor: Generates a constructor with all fields
 * - @NoArgsConstructor: Generates a protected no-args constructor for JPA
 *
 * @author Emmanuel Bruno
 * @version 0.0.1
 * @since 0.0.1
 */
@Embeddable

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Address implements Serializable {

  /**
   * The serial version UID for serialization.
   * Change this value if you modify the class.
   */
  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * The street of the address.
   */
  private String street;

  /**
   * The city of the address.
   */
  private String city;

  /**
   * The country of the address.
   */
  private String country;
}
