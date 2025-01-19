package ru.mudan.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mudan.entity.Registration;

public interface RegistrationRepository extends CrudRepository<Registration, Long> {
}
