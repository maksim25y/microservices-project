package ru.mudan.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mudan.entity.AppUser;

public interface UserRepository extends CrudRepository<AppUser, Long> {
}
