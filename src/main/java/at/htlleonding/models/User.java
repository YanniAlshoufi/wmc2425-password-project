package at.htlleonding.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @Email(message = "Please provide a valid email.")
    @Column(unique = true)
    private String email;

    @Max(72)
    private String passwordHash;

    @Max(30)
    @Length(message = "Your phone number cannot exceed 30 characters.")
    private String phoneNumber;
}