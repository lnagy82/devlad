package hu.tsystems.devlad.web.rest;

import hu.tsystems.devlad.DevladApp;

import hu.tsystems.devlad.domain.SkillRequest;
import hu.tsystems.devlad.repository.SkillRequestRepository;
import hu.tsystems.devlad.service.SkillRequestService;
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

import hu.tsystems.devlad.domain.enumeration.SkillRequestStatus;
/**
 * Test class for the SkillRequestResource REST controller.
 *
 * @see SkillRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevladApp.class)
public class SkillRequestResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_REQUESTOR = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTOR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final SkillRequestStatus DEFAULT_STATUS = SkillRequestStatus.REQUESTED;
    private static final SkillRequestStatus UPDATED_STATUS = SkillRequestStatus.DELETED;

    @Autowired
    private SkillRequestRepository skillRequestRepository;

    @Autowired
    private SkillRequestService skillRequestService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSkillRequestMockMvc;

    private SkillRequest skillRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SkillRequestResource skillRequestResource = new SkillRequestResource(skillRequestService);
        this.restSkillRequestMockMvc = MockMvcBuilders.standaloneSetup(skillRequestResource)
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
    public static SkillRequest createEntity(EntityManager em) {
        SkillRequest skillRequest = new SkillRequest()
                .created(DEFAULT_CREATED)
                .requestor(DEFAULT_REQUESTOR)
                .description(DEFAULT_DESCRIPTION)
                .status(DEFAULT_STATUS);
        return skillRequest;
    }

    @Before
    public void initTest() {
        skillRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkillRequest() throws Exception {
        int databaseSizeBeforeCreate = skillRequestRepository.findAll().size();

        // Create the SkillRequest

        restSkillRequestMockMvc.perform(post("/api/skill-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillRequest)))
            .andExpect(status().isCreated());

        // Validate the SkillRequest in the database
        List<SkillRequest> skillRequestList = skillRequestRepository.findAll();
        assertThat(skillRequestList).hasSize(databaseSizeBeforeCreate + 1);
        SkillRequest testSkillRequest = skillRequestList.get(skillRequestList.size() - 1);
        assertThat(testSkillRequest.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testSkillRequest.getRequestor()).isEqualTo(DEFAULT_REQUESTOR);
        assertThat(testSkillRequest.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSkillRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createSkillRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skillRequestRepository.findAll().size();

        // Create the SkillRequest with an existing ID
        SkillRequest existingSkillRequest = new SkillRequest();
        existingSkillRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillRequestMockMvc.perform(post("/api/skill-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSkillRequest)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SkillRequest> skillRequestList = skillRequestRepository.findAll();
        assertThat(skillRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillRequestRepository.findAll().size();
        // set the field null
        skillRequest.setCreated(null);

        // Create the SkillRequest, which fails.

        restSkillRequestMockMvc.perform(post("/api/skill-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillRequest)))
            .andExpect(status().isBadRequest());

        List<SkillRequest> skillRequestList = skillRequestRepository.findAll();
        assertThat(skillRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRequestorIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillRequestRepository.findAll().size();
        // set the field null
        skillRequest.setRequestor(null);

        // Create the SkillRequest, which fails.

        restSkillRequestMockMvc.perform(post("/api/skill-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillRequest)))
            .andExpect(status().isBadRequest());

        List<SkillRequest> skillRequestList = skillRequestRepository.findAll();
        assertThat(skillRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillRequestRepository.findAll().size();
        // set the field null
        skillRequest.setDescription(null);

        // Create the SkillRequest, which fails.

        restSkillRequestMockMvc.perform(post("/api/skill-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillRequest)))
            .andExpect(status().isBadRequest());

        List<SkillRequest> skillRequestList = skillRequestRepository.findAll();
        assertThat(skillRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillRequestRepository.findAll().size();
        // set the field null
        skillRequest.setStatus(null);

        // Create the SkillRequest, which fails.

        restSkillRequestMockMvc.perform(post("/api/skill-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillRequest)))
            .andExpect(status().isBadRequest());

        List<SkillRequest> skillRequestList = skillRequestRepository.findAll();
        assertThat(skillRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSkillRequests() throws Exception {
        // Initialize the database
        skillRequestRepository.saveAndFlush(skillRequest);

        // Get all the skillRequestList
        restSkillRequestMockMvc.perform(get("/api/skill-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].requestor").value(hasItem(DEFAULT_REQUESTOR.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSkillRequest() throws Exception {
        // Initialize the database
        skillRequestRepository.saveAndFlush(skillRequest);

        // Get the skillRequest
        restSkillRequestMockMvc.perform(get("/api/skill-requests/{id}", skillRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(skillRequest.getId().intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.requestor").value(DEFAULT_REQUESTOR.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSkillRequest() throws Exception {
        // Get the skillRequest
        restSkillRequestMockMvc.perform(get("/api/skill-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkillRequest() throws Exception {
        // Initialize the database
        skillRequestService.save(skillRequest);

        int databaseSizeBeforeUpdate = skillRequestRepository.findAll().size();

        // Update the skillRequest
        SkillRequest updatedSkillRequest = skillRequestRepository.findOne(skillRequest.getId());
        updatedSkillRequest
                .created(UPDATED_CREATED)
                .requestor(UPDATED_REQUESTOR)
                .description(UPDATED_DESCRIPTION)
                .status(UPDATED_STATUS);

        restSkillRequestMockMvc.perform(put("/api/skill-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSkillRequest)))
            .andExpect(status().isOk());

        // Validate the SkillRequest in the database
        List<SkillRequest> skillRequestList = skillRequestRepository.findAll();
        assertThat(skillRequestList).hasSize(databaseSizeBeforeUpdate);
        SkillRequest testSkillRequest = skillRequestList.get(skillRequestList.size() - 1);
        assertThat(testSkillRequest.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testSkillRequest.getRequestor()).isEqualTo(UPDATED_REQUESTOR);
        assertThat(testSkillRequest.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSkillRequest.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingSkillRequest() throws Exception {
        int databaseSizeBeforeUpdate = skillRequestRepository.findAll().size();

        // Create the SkillRequest

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSkillRequestMockMvc.perform(put("/api/skill-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillRequest)))
            .andExpect(status().isCreated());

        // Validate the SkillRequest in the database
        List<SkillRequest> skillRequestList = skillRequestRepository.findAll();
        assertThat(skillRequestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSkillRequest() throws Exception {
        // Initialize the database
        skillRequestService.save(skillRequest);

        int databaseSizeBeforeDelete = skillRequestRepository.findAll().size();

        // Get the skillRequest
        restSkillRequestMockMvc.perform(delete("/api/skill-requests/{id}", skillRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SkillRequest> skillRequestList = skillRequestRepository.findAll();
        assertThat(skillRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(SkillRequest.class);
//    }
}
