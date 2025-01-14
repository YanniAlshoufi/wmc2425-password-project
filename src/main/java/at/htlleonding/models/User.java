package at.htlleonding.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @Email
    private String email;

    @Size(min = 1, max = 72)
    private String passwordHash;

    @Size(min = 1, max = 50)
    private String phoneNumber;
}