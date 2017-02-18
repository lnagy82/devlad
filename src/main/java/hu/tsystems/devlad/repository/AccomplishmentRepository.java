package hu.tsystems.devlad.repository;

import hu.tsystems.devlad.domain.Accomplishment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Accomplishment entity.
 */
@SuppressWarnings("unused")
public interface AccomplishmentRepository extends JpaRepository<Accomplishment,Long> {

}
