package hu.tsystems.devlad.repository;

import hu.tsystems.devlad.domain.TeamMember;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TeamMember entity.
 */
@SuppressWarnings("unused")
public interface TeamMemberRepository extends JpaRepository<TeamMember,Long> {

}
