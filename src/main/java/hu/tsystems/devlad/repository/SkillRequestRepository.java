package hu.tsystems.devlad.repository;

import hu.tsystems.devlad.domain.SkillRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SkillRequest entity.
 */
@SuppressWarnings("unused")
public interface SkillRequestRepository extends JpaRepository<SkillRequest,Long> {

}
