package at.htlleonding.users;

import at.htlleonding.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@ApplicationScoped
@Transactional
public class UsersRepository implements PanacheRepositoryBase<User, UUID> {
}
