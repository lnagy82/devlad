package hu.tsystems.devlad.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import hu.tsystems.devlad.service.dto.LearnedSkillDTO;
import hu.tsystems.devlad.service.dto.TutorDTO;

@Repository
public class LearnedSkillRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	public Page<LearnedSkillDTO> findAllByDeveloperId(Pageable pageable, Long developerId) {

		Long total = //
				((BigInteger) em //
						.createNativeQuery("SELECT count(*) FROM " + "(SELECT s.id FROM learned_skill ls "
								+ "INNER JOIN developer d ON ls.developer_id = d.id "
								+ "INNER JOIN skill s ON ls.skill_id = s.id " //
								+ "WHERE d.id = ?1 " //
								+ "GROUP BY s.id, s.name) skillz") //
						.setParameter(1, developerId) //
						.getSingleResult()).longValue();

		@SuppressWarnings("unchecked")
		List<Object[]> list = //
				em //
						.createNativeQuery(
								"SELECT s.id, s.name, string_agg(ls.level, ', '), sum(ls.exp) FROM learned_skill ls "
										+ "INNER JOIN developer d ON ls.developer_id = d.id "
										+ "INNER JOIN skill s ON ls.skill_id = s.id " //
										+ "WHERE d.id = ?1 " //
										+ "GROUP BY s.id, s.name " //
										+ "ORDER BY sum(ls.exp) DESC") //
						.setParameter(1, developerId) //
						.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

		List<LearnedSkillDTO> listDTO = new ArrayList<LearnedSkillDTO>();

		for (Object[] o : list) {
			listDTO.add(new LearnedSkillDTO(o));
		}

		return new PageImpl<LearnedSkillDTO>(listDTO, pageable, total);
	}

	public Page<TutorDTO> findAllTutor(Pageable pageable) {
		Long total = //
				((BigInteger) em //
						.createNativeQuery("SELECT count(distinct skill_id) FROM learned_skill") //
						.getSingleResult()).longValue();

		@SuppressWarnings("unchecked")
		List<Object[]> list = //
				em //
						.createNativeQuery("SELECT id, name, string_agg(identifier, ', ') " //
								+ "FROM (SELECT s.id, s.name, d.identifier, sum(ls.exp) xp, row_number() over(partition by s.name order by sum(ls.exp) DESC) as rank "
								+ "FROM learned_skill ls " //
								+ "INNER JOIN developer d ON ls.developer_id = d.id "
								+ "INNER JOIN skill s ON ls.skill_id = s.id " //
								+ "GROUP BY s.id, s.name, d.identifier " //
								+ "ORDER BY name, xp DESC) s " //
								+ "WHERE s.rank < 6 " //
								+ "GROUP BY id, name " + "ORDER BY name") //
						.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

		List<TutorDTO> listDTO = new ArrayList<TutorDTO>();

		for (Object[] o : list) {
			listDTO.add(new TutorDTO(o));
		}

		return new PageImpl<TutorDTO>(listDTO, pageable, total);
	}

}
