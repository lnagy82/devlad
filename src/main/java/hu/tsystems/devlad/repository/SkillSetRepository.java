package hu.tsystems.devlad.repository;

import hu.tsystems.devlad.domain.SkillSet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SkillSet entity.
 */
@SuppressWarnings("unused")
public interface SkillSetRepository extends JpaRepository<SkillSet,Long> {

}
