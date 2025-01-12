package ru.mudan.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.mudan.entity.AppUser;

public interface UserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}
