package hu.tsystems.devlad.web.rest;

import hu.tsystems.devlad.DevladApp;

import hu.tsystems.devlad.domain.Accomplishment;
import hu.tsystems.devlad.repository.AccomplishmentRepository;
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
 * Test class for the AccomplishmentResource REST controller.
 *
 * @see AccomplishmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevladApp.class)
public class AccomplishmentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXPERIENCE_POINTS = 1;
    private static final Integer UPDATED_EXPERIENCE_POINTS = 2;

    @Autowired
    private AccomplishmentRepository accomplishmentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAccomplishmentMockMvc;

    private Accomplishment accomplishment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
            AccomplishmentResource accomplishmentResource = new AccomplishmentResource(accomplishmentRepository);
        this.restAccomplishmentMockMvc = MockMvcBuilders.standaloneSetup(accomplishmentResource)
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
    public static Accomplishment createEntity(EntityManager em) {
        Accomplishment accomplishment = new Accomplishment()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .experiencePoints(DEFAULT_EXPERIENCE_POINTS);
        return accomplishment;
    }

    @Before
    public void initTest() {
        accomplishment = createEntity(em);
    }

    @Test
    @Transactional
    public void createAccomplishment() throws Exception {
        int databaseSizeBeforeCreate = accomplishmentRepository.findAll().size();

        // Create the Accomplishment

        restAccomplishmentMockMvc.perform(post("/api/accomplishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accomplishment)))
            .andExpect(status().isCreated());

        // Validate the Accomplishment in the database
        List<Accomplishment> accomplishmentList = accomplishmentRepository.findAll();
        assertThat(accomplishmentList).hasSize(databaseSizeBeforeCreate + 1);
        Accomplishment testAccomplishment = accomplishmentList.get(accomplishmentList.size() - 1);
        assertThat(testAccomplishment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAccomplishment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAccomplishment.getExperiencePoints()).isEqualTo(DEFAULT_EXPERIENCE_POINTS);
    }

    @Test
    @Transactional
    public void createAccomplishmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accomplishmentRepository.findAll().size();

        // Create the Accomplishment with an existing ID
        Accomplishment existingAccomplishment = new Accomplishment();
        existingAccomplishment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccomplishmentMockMvc.perform(post("/api/accomplishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAccomplishment)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Accomplishment> accomplishmentList = accomplishmentRepository.findAll();
        assertThat(accomplishmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accomplishmentRepository.findAll().size();
        // set the field null
        accomplishment.setName(null);

        // Create the Accomplishment, which fails.

        restAccomplishmentMockMvc.perform(post("/api/accomplishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accomplishment)))
            .andExpect(status().isBadRequest());

        List<Accomplishment> accomplishmentList = accomplishmentRepository.findAll();
        assertThat(accomplishmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = accomplishmentRepository.findAll().size();
        // set the field null
        accomplishment.setDescription(null);

        // Create the Accomplishment, which fails.

        restAccomplishmentMockMvc.perform(post("/api/accomplishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accomplishment)))
            .andExpect(status().isBadRequest());

        List<Accomplishment> accomplishmentList = accomplishmentRepository.findAll();
        assertThat(accomplishmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExperiencePointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = accomplishmentRepository.findAll().size();
        // set the field null
        accomplishment.setExperiencePoints(null);

        // Create the Accomplishment, which fails.

        restAccomplishmentMockMvc.perform(post("/api/accomplishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accomplishment)))
            .andExpect(status().isBadRequest());

        List<Accomplishment> accomplishmentList = accomplishmentRepository.findAll();
        assertThat(accomplishmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAccomplishments() throws Exception {
        // Initialize the database
        accomplishmentRepository.saveAndFlush(accomplishment);

        // Get all the accomplishmentList
        restAccomplishmentMockMvc.perform(get("/api/accomplishments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accomplishment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].experiencePoints").value(hasItem(DEFAULT_EXPERIENCE_POINTS)));
    }

    @Test
    @Transactional
    public void getAccomplishment() throws Exception {
        // Initialize the database
        accomplishmentRepository.saveAndFlush(accomplishment);

        // Get the accomplishment
        restAccomplishmentMockMvc.perform(get("/api/accomplishments/{id}", accomplishment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(accomplishment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.experiencePoints").value(DEFAULT_EXPERIENCE_POINTS));
    }

    @Test
    @Transactional
    public void getNonExistingAccomplishment() throws Exception {
        // Get the accomplishment
        restAccomplishmentMockMvc.perform(get("/api/accomplishments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccomplishment() throws Exception {
        // Initialize the database
        accomplishmentRepository.saveAndFlush(accomplishment);
        int databaseSizeBeforeUpdate = accomplishmentRepository.findAll().size();

        // Update the accomplishment
        Accomplishment updatedAccomplishment = accomplishmentRepository.findOne(accomplishment.getId());
        updatedAccomplishment
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .experiencePoints(UPDATED_EXPERIENCE_POINTS);

        restAccomplishmentMockMvc.perform(put("/api/accomplishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAccomplishment)))
            .andExpect(status().isOk());

        // Validate the Accomplishment in the database
        List<Accomplishment> accomplishmentList = accomplishmentRepository.findAll();
        assertThat(accomplishmentList).hasSize(databaseSizeBeforeUpdate);
        Accomplishment testAccomplishment = accomplishmentList.get(accomplishmentList.size() - 1);
        assertThat(testAccomplishment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAccomplishment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccomplishment.getExperiencePoints()).isEqualTo(UPDATED_EXPERIENCE_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingAccomplishment() throws Exception {
        int databaseSizeBeforeUpdate = accomplishmentRepository.findAll().size();

        // Create the Accomplishment

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAccomplishmentMockMvc.perform(put("/api/accomplishments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(accomplishment)))
            .andExpect(status().isCreated());

        // Validate the Accomplishment in the database
        List<Accomplishment> accomplishmentList = accomplishmentRepository.findAll();
        assertThat(accomplishmentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAccomplishment() throws Exception {
        // Initialize the database
        accomplishmentRepository.saveAndFlush(accomplishment);
        int databaseSizeBeforeDelete = accomplishmentRepository.findAll().size();

        // Get the accomplishment
        restAccomplishmentMockMvc.perform(delete("/api/accomplishments/{id}", accomplishment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Accomplishment> accomplishmentList = accomplishmentRepository.findAll();
        assertThat(accomplishmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(Accomplishment.class);
//    }
}
