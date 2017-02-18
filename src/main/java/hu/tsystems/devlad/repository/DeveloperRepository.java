package hu.tsystems.devlad.repository;

import hu.tsystems.devlad.domain.Developer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Developer entity.
 */
@SuppressWarnings("unused")
public interface DeveloperRepository extends JpaRepository<Developer,Long> {

}
