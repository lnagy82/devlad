package hu.tsystems.devlad.repository;

import hu.tsystems.devlad.domain.LearnedSkill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LearnedSkill entity.
 */
@SuppressWarnings("unused")
public interface LearnedSkillRepository extends JpaRepository<LearnedSkill,Long> {

	Page<LearnedSkill> findAllByDeveloperId(Pageable pageable, Long developerId);
}
