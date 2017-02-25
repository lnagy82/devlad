package hu.tsystems.devlad.web.rest;

import hu.tsystems.devlad.DevladApp;

import hu.tsystems.devlad.domain.Developer;
import hu.tsystems.devlad.repository.DeveloperRepository;
import hu.tsystems.devlad.service.dto.DeveloperDTO;
import hu.tsystems.devlad.service.mapper.DeveloperMapper;
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
 * Test class for the DeveloperResource REST controller.
 *
 * @see DeveloperResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevladApp.class)
public class DeveloperResourceIntTest {

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXPERIENCE_POINTS = 1;
    private static final Integer UPDATED_EXPERIENCE_POINTS = 2;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DeveloperMapper developerMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDeveloperMockMvc;

    private Developer developer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            DeveloperResource developerResource = new DeveloperResource(developerRepository, developerMapper);
        this.restDeveloperMockMvc = MockMvcBuilders.standaloneSetup(developerResource)
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
    public static Developer createEntity(EntityManager em) {
        Developer developer = new Developer()
                 .identifier(DEFAULT_IDENTIFIER)
                .description(DEFAULT_DESCRIPTION)
                .level(DEFAULT_LEVEL)
                .experiencePoints(DEFAULT_EXPERIENCE_POINTS);
        return developer;
    }

    @Before
    public void initTest() {
        developer = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeveloper() throws Exception {
        int databaseSizeBeforeCreate = developerRepository.findAll().size();

        // Create the Developer
        DeveloperDTO developerDTO = developerMapper.developerToDeveloperDTO(developer);

        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isCreated());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeCreate + 1);
        Developer testDeveloper = developerList.get(developerList.size() - 1);
        assertThat(testDeveloper.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testDeveloper.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDeveloper.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testDeveloper.getExperiencePoints()).isEqualTo(DEFAULT_EXPERIENCE_POINTS);
    }

    @Test
    @Transactional
    public void createDeveloperWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = developerRepository.findAll().size();

        // Create the Developer with an existing ID
        Developer existingDeveloper = new Developer();
        existingDeveloper.setId(1L);
        DeveloperDTO existingDeveloperDTO = developerMapper.developerToDeveloperDTO(existingDeveloper);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingDeveloperDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = developerRepository.findAll().size();

        // Create the Developer, which fails.
        DeveloperDTO developerDTO = developerMapper.developerToDeveloperDTO(developer);

        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isBadRequest());

        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = developerRepository.findAll().size();
        // set the field null
        developer.setIdentifier(null);

        // Create the Developer, which fails.
        DeveloperDTO developerDTO = developerMapper.developerToDeveloperDTO(developer);

        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isBadRequest());

        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = developerRepository.findAll().size();
        // set the field null
        developer.setLevel(null);

        // Create the Developer, which fails.
        DeveloperDTO developerDTO = developerMapper.developerToDeveloperDTO(developer);

        restDeveloperMockMvc.perform(post("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isBadRequest());

        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDevelopers() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        // Get all the developerList
        restDeveloperMockMvc.perform(get("/api/developers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(developer.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].experiencePoints").value(hasItem(DEFAULT_EXPERIENCE_POINTS)));
    }

    @Test
    @Transactional
    public void getDeveloper() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);

        // Get the developer
        restDeveloperMockMvc.perform(get("/api/developers/{id}", developer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(developer.getId().intValue()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()))
            .andExpect(jsonPath("$.experiencePoints").value(DEFAULT_EXPERIENCE_POINTS));
    }

    @Test
    @Transactional
    public void getNonExistingDeveloper() throws Exception {
        // Get the developer
        restDeveloperMockMvc.perform(get("/api/developers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeveloper() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);
        int databaseSizeBeforeUpdate = developerRepository.findAll().size();

        // Update the developer
        Developer updatedDeveloper = developerRepository.findOne(developer.getId());
        updatedDeveloper
                .identifier(UPDATED_IDENTIFIER)
                .description(UPDATED_DESCRIPTION)
                .level(UPDATED_LEVEL)
                .experiencePoints(UPDATED_EXPERIENCE_POINTS);
        DeveloperDTO developerDTO = developerMapper.developerToDeveloperDTO(updatedDeveloper);

        restDeveloperMockMvc.perform(put("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isOk());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeUpdate);
        Developer testDeveloper = developerList.get(developerList.size() - 1);
        assertThat(testDeveloper.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testDeveloper.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDeveloper.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testDeveloper.getExperiencePoints()).isEqualTo(UPDATED_EXPERIENCE_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingDeveloper() throws Exception {
        int databaseSizeBeforeUpdate = developerRepository.findAll().size();

        // Create the Developer
        DeveloperDTO developerDTO = developerMapper.developerToDeveloperDTO(developer);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDeveloperMockMvc.perform(put("/api/developers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developerDTO)))
            .andExpect(status().isCreated());

        // Validate the Developer in the database
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDeveloper() throws Exception {
        // Initialize the database
        developerRepository.saveAndFlush(developer);
        int databaseSizeBeforeDelete = developerRepository.findAll().size();

        // Get the developer
        restDeveloperMockMvc.perform(delete("/api/developers/{id}", developer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Developer> developerList = developerRepository.findAll();
        assertThat(developerList).hasSize(databaseSizeBeforeDelete - 1);
    }

//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Developer.class);
//    }
}
