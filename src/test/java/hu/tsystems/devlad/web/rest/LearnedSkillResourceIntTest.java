package hu.tsystems.devlad.web.rest;

import hu.tsystems.devlad.DevladApp;

import hu.tsystems.devlad.domain.LearnedSkill;
import hu.tsystems.devlad.domain.Developer;
import hu.tsystems.devlad.domain.Skill;
import hu.tsystems.devlad.repository.LearnedSkillRepository;
import hu.tsystems.devlad.service.LearnedSkillService;
import hu.tsystems.devlad.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static hu.tsystems.devlad.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LearnedSkillResource REST controller.
 *
 * @see LearnedSkillResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevladApp.class)
public class LearnedSkillResourceIntTest {

    private static final ZonedDateTime DEFAULT_LEARNED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LEARNED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SIGNED = "AAAAAAAAAA";
    private static final String UPDATED_SIGNED = "BBBBBBBBBB";

    @Autowired
    private LearnedSkillRepository learnedSkillRepository;

    @Autowired
    private LearnedSkillService learnedSkillService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLearnedSkillMockMvc;

    private LearnedSkill learnedSkill;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LearnedSkillResource learnedSkillResource = new LearnedSkillResource(learnedSkillService);
        this.restLearnedSkillMockMvc = MockMvcBuilders.standaloneSetup(learnedSkillResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LearnedSkill createEntity(EntityManager em) {
        LearnedSkill learnedSkill = new LearnedSkill()
                .learned(DEFAULT_LEARNED)
                .signed(DEFAULT_SIGNED);
        // Add required entity
        Developer developer = DeveloperResourceIntTest.createEntity(em);
        em.persist(developer);
        em.flush();
        learnedSkill.setDeveloper(developer);
        // Add required entity
        Skill skill = SkillResourceIntTest.createEntity(em);
        em.persist(skill);
        em.flush();
        learnedSkill.setSkill(skill);
        return learnedSkill;
    }

    @Before
    public void initTest() {
        learnedSkill = createEntity(em);
    }

    @Test
    @Transactional
    public void createLearnedSkill() throws Exception {
        int databaseSizeBeforeCreate = learnedSkillRepository.findAll().size();

        // Create the LearnedSkill

        restLearnedSkillMockMvc.perform(post("/api/learned-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnedSkill)))
            .andExpect(status().isCreated());

        // Validate the LearnedSkill in the database
        List<LearnedSkill> learnedSkillList = learnedSkillRepository.findAll();
        assertThat(learnedSkillList).hasSize(databaseSizeBeforeCreate + 1);
        LearnedSkill testLearnedSkill = learnedSkillList.get(learnedSkillList.size() - 1);
        assertThat(testLearnedSkill.getLearned()).isEqualTo(DEFAULT_LEARNED);
        assertThat(testLearnedSkill.getSigned()).isEqualTo(DEFAULT_SIGNED);
    }

    @Test
    @Transactional
    public void createLearnedSkillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = learnedSkillRepository.findAll().size();

        // Create the LearnedSkill with an existing ID
        LearnedSkill existingLearnedSkill = new LearnedSkill();
        existingLearnedSkill.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLearnedSkillMockMvc.perform(post("/api/learned-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingLearnedSkill)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LearnedSkill> learnedSkillList = learnedSkillRepository.findAll();
        assertThat(learnedSkillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLearnedIsRequired() throws Exception {
        int databaseSizeBeforeTest = learnedSkillRepository.findAll().size();
        // set the field null
        learnedSkill.setLearned(null);

        // Create the LearnedSkill, which fails.

        restLearnedSkillMockMvc.perform(post("/api/learned-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnedSkill)))
            .andExpect(status().isBadRequest());

        List<LearnedSkill> learnedSkillList = learnedSkillRepository.findAll();
        assertThat(learnedSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSignedIsRequired() throws Exception {
        int databaseSizeBeforeTest = learnedSkillRepository.findAll().size();
        // set the field null
        learnedSkill.setSigned(null);

        // Create the LearnedSkill, which fails.

        restLearnedSkillMockMvc.perform(post("/api/learned-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnedSkill)))
            .andExpect(status().isBadRequest());

        List<LearnedSkill> learnedSkillList = learnedSkillRepository.findAll();
        assertThat(learnedSkillList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLearnedSkills() throws Exception {
        // Initialize the database
        learnedSkillRepository.saveAndFlush(learnedSkill);

        // Get all the learnedSkillList
        restLearnedSkillMockMvc.perform(get("/api/learned-skills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(learnedSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].learned").value(hasItem(sameInstant(DEFAULT_LEARNED))))
            .andExpect(jsonPath("$.[*].signed").value(hasItem(DEFAULT_SIGNED.toString())));
    }

    @Test
    @Transactional
    public void getLearnedSkill() throws Exception {
        // Initialize the database
        learnedSkillRepository.saveAndFlush(learnedSkill);

        // Get the learnedSkill
        restLearnedSkillMockMvc.perform(get("/api/learned-skills/{id}", learnedSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(learnedSkill.getId().intValue()))
            .andExpect(jsonPath("$.learned").value(sameInstant(DEFAULT_LEARNED)))
            .andExpect(jsonPath("$.signed").value(DEFAULT_SIGNED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLearnedSkill() throws Exception {
        // Get the learnedSkill
        restLearnedSkillMockMvc.perform(get("/api/learned-skills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLearnedSkill() throws Exception {
        // Initialize the database
        learnedSkillService.save(learnedSkill);

        int databaseSizeBeforeUpdate = learnedSkillRepository.findAll().size();

        // Update the learnedSkill
        LearnedSkill updatedLearnedSkill = learnedSkillRepository.findOne(learnedSkill.getId());
        updatedLearnedSkill
                .learned(UPDATED_LEARNED)
                .signed(UPDATED_SIGNED);

        restLearnedSkillMockMvc.perform(put("/api/learned-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLearnedSkill)))
            .andExpect(status().isOk());

        // Validate the LearnedSkill in the database
        List<LearnedSkill> learnedSkillList = learnedSkillRepository.findAll();
        assertThat(learnedSkillList).hasSize(databaseSizeBeforeUpdate);
        LearnedSkill testLearnedSkill = learnedSkillList.get(learnedSkillList.size() - 1);
        assertThat(testLearnedSkill.getLearned()).isEqualTo(UPDATED_LEARNED);
        assertThat(testLearnedSkill.getSigned()).isEqualTo(UPDATED_SIGNED);
    }

    @Test
    @Transactional
    public void updateNonExistingLearnedSkill() throws Exception {
        int databaseSizeBeforeUpdate = learnedSkillRepository.findAll().size();

        // Create the LearnedSkill

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLearnedSkillMockMvc.perform(put("/api/learned-skills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(learnedSkill)))
            .andExpect(status().isCreated());

        // Validate the LearnedSkill in the database
        List<LearnedSkill> learnedSkillList = learnedSkillRepository.findAll();
        assertThat(learnedSkillList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLearnedSkill() throws Exception {
        // Initialize the database
        learnedSkillService.save(learnedSkill);

        int databaseSizeBeforeDelete = learnedSkillRepository.findAll().size();

        // Get the learnedSkill
        restLearnedSkillMockMvc.perform(delete("/api/learned-skills/{id}", learnedSkill.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LearnedSkill> learnedSkillList = learnedSkillRepository.findAll();
        assertThat(learnedSkillList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LearnedSkill.class);
    }
}
