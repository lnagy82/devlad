package hu.tsystems.devlad.web.rest;

import hu.tsystems.devlad.DevladApp;

import hu.tsystems.devlad.domain.SkillSet;
import hu.tsystems.devlad.repository.SkillSetRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SkillSetResource REST controller.
 *
 * @see SkillSetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevladApp.class)
public class SkillSetResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private SkillSetRepository skillSetRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSkillSetMockMvc;

    private SkillSet skillSet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            SkillSetResource skillSetResource = new SkillSetResource(skillSetRepository);
        this.restSkillSetMockMvc = MockMvcBuilders.standaloneSetup(skillSetResource)
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
    public static SkillSet createEntity(EntityManager em) {
        SkillSet skillSet = new SkillSet()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION);
        return skillSet;
    }

    @Before
    public void initTest() {
        skillSet = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkillSet() throws Exception {
        int databaseSizeBeforeCreate = skillSetRepository.findAll().size();

        // Create the SkillSet

        restSkillSetMockMvc.perform(post("/api/skill-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillSet)))
            .andExpect(status().isCreated());

        // Validate the SkillSet in the database
        List<SkillSet> skillSetList = skillSetRepository.findAll();
        assertThat(skillSetList).hasSize(databaseSizeBeforeCreate + 1);
        SkillSet testSkillSet = skillSetList.get(skillSetList.size() - 1);
        assertThat(testSkillSet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSkillSet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createSkillSetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skillSetRepository.findAll().size();

        // Create the SkillSet with an existing ID
        SkillSet existingSkillSet = new SkillSet();
        existingSkillSet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillSetMockMvc.perform(post("/api/skill-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSkillSet)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SkillSet> skillSetList = skillSetRepository.findAll();
        assertThat(skillSetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillSetRepository.findAll().size();
        // set the field null
        skillSet.setName(null);

        // Create the SkillSet, which fails.

        restSkillSetMockMvc.perform(post("/api/skill-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillSet)))
            .andExpect(status().isBadRequest());

        List<SkillSet> skillSetList = skillSetRepository.findAll();
        assertThat(skillSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillSetRepository.findAll().size();
        // set the field null
        skillSet.setDescription(null);

        // Create the SkillSet, which fails.

        restSkillSetMockMvc.perform(post("/api/skill-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillSet)))
            .andExpect(status().isBadRequest());

        List<SkillSet> skillSetList = skillSetRepository.findAll();
        assertThat(skillSetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSkillSets() throws Exception {
        // Initialize the database
        skillSetRepository.saveAndFlush(skillSet);

        // Get all the skillSetList
        restSkillSetMockMvc.perform(get("/api/skill-sets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getSkillSet() throws Exception {
        // Initialize the database
        skillSetRepository.saveAndFlush(skillSet);

        // Get the skillSet
        restSkillSetMockMvc.perform(get("/api/skill-sets/{id}", skillSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(skillSet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSkillSet() throws Exception {
        // Get the skillSet
        restSkillSetMockMvc.perform(get("/api/skill-sets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkillSet() throws Exception {
        // Initialize the database
        skillSetRepository.saveAndFlush(skillSet);
        int databaseSizeBeforeUpdate = skillSetRepository.findAll().size();

        // Update the skillSet
        SkillSet updatedSkillSet = skillSetRepository.findOne(skillSet.getId());
        updatedSkillSet
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION);

        restSkillSetMockMvc.perform(put("/api/skill-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSkillSet)))
            .andExpect(status().isOk());

        // Validate the SkillSet in the database
        List<SkillSet> skillSetList = skillSetRepository.findAll();
        assertThat(skillSetList).hasSize(databaseSizeBeforeUpdate);
        SkillSet testSkillSet = skillSetList.get(skillSetList.size() - 1);
        assertThat(testSkillSet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSkillSet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingSkillSet() throws Exception {
        int databaseSizeBeforeUpdate = skillSetRepository.findAll().size();

        // Create the SkillSet

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSkillSetMockMvc.perform(put("/api/skill-sets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillSet)))
            .andExpect(status().isCreated());

        // Validate the SkillSet in the database
        List<SkillSet> skillSetList = skillSetRepository.findAll();
        assertThat(skillSetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSkillSet() throws Exception {
        // Initialize the database
        skillSetRepository.saveAndFlush(skillSet);
        int databaseSizeBeforeDelete = skillSetRepository.findAll().size();

        // Get the skillSet
        restSkillSetMockMvc.perform(delete("/api/skill-sets/{id}", skillSet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SkillSet> skillSetList = skillSetRepository.findAll();
        assertThat(skillSetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillSet.class);
    }
}
