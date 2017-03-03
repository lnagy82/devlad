package hu.tsystems.devlad.web.rest;

import hu.tsystems.devlad.DevladApp;

import hu.tsystems.devlad.domain.TeamMember;
import hu.tsystems.devlad.domain.Team;
import hu.tsystems.devlad.domain.Developer;
import hu.tsystems.devlad.repository.TeamMemberRepository;
import hu.tsystems.devlad.service.TeamMemberService;
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
 * Test class for the TeamMemberResource REST controller.
 *
 * @see TeamMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevladApp.class)
public class TeamMemberResourceIntTest {

    private static final String DEFAULT_ASSIGNMENT = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNMENT = "BBBBBBBBBB";

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTeamMemberMockMvc;

    private TeamMember teamMember;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TeamMemberResource teamMemberResource = new TeamMemberResource(teamMemberService);
        this.restTeamMemberMockMvc = MockMvcBuilders.standaloneSetup(teamMemberResource)
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
    public static TeamMember createEntity(EntityManager em) {
        TeamMember teamMember = new TeamMember()
                .assignment(DEFAULT_ASSIGNMENT);
        // Add required entity
        Team team = TeamResourceIntTest.createEntity(em);
        em.persist(team);
        em.flush();
        teamMember.setTeam(team);
        // Add required entity
        Developer developer = DeveloperResourceIntTest.createEntity(em);
        em.persist(developer);
        em.flush();
        teamMember.setDeveloper(developer);
        return teamMember;
    }

    @Before
    public void initTest() {
        teamMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeamMember() throws Exception {
        int databaseSizeBeforeCreate = teamMemberRepository.findAll().size();

        // Create the TeamMember

        restTeamMemberMockMvc.perform(post("/api/team-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamMember)))
            .andExpect(status().isCreated());

        // Validate the TeamMember in the database
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeCreate + 1);
        TeamMember testTeamMember = teamMemberList.get(teamMemberList.size() - 1);
        assertThat(testTeamMember.getAssignment()).isEqualTo(DEFAULT_ASSIGNMENT);
    }

    @Test
    @Transactional
    public void createTeamMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamMemberRepository.findAll().size();

        // Create the TeamMember with an existing ID
        TeamMember existingTeamMember = new TeamMember();
        existingTeamMember.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamMemberMockMvc.perform(post("/api/team-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTeamMember)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTeamMembers() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get all the teamMemberList
        restTeamMemberMockMvc.perform(get("/api/team-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teamMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].assignment").value(hasItem(DEFAULT_ASSIGNMENT.toString())));
    }

    @Test
    @Transactional
    public void getTeamMember() throws Exception {
        // Initialize the database
        teamMemberRepository.saveAndFlush(teamMember);

        // Get the teamMember
        restTeamMemberMockMvc.perform(get("/api/team-members/{id}", teamMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teamMember.getId().intValue()))
            .andExpect(jsonPath("$.assignment").value(DEFAULT_ASSIGNMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTeamMember() throws Exception {
        // Get the teamMember
        restTeamMemberMockMvc.perform(get("/api/team-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeamMember() throws Exception {
        // Initialize the database
        teamMemberService.save(teamMember);

        int databaseSizeBeforeUpdate = teamMemberRepository.findAll().size();

        // Update the teamMember
        TeamMember updatedTeamMember = teamMemberRepository.findOne(teamMember.getId());
        updatedTeamMember
                .assignment(UPDATED_ASSIGNMENT);

        restTeamMemberMockMvc.perform(put("/api/team-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeamMember)))
            .andExpect(status().isOk());

        // Validate the TeamMember in the database
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeUpdate);
        TeamMember testTeamMember = teamMemberList.get(teamMemberList.size() - 1);
        assertThat(testTeamMember.getAssignment()).isEqualTo(UPDATED_ASSIGNMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingTeamMember() throws Exception {
        int databaseSizeBeforeUpdate = teamMemberRepository.findAll().size();

        // Create the TeamMember

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTeamMemberMockMvc.perform(put("/api/team-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teamMember)))
            .andExpect(status().isCreated());

        // Validate the TeamMember in the database
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTeamMember() throws Exception {
        // Initialize the database
        teamMemberService.save(teamMember);

        int databaseSizeBeforeDelete = teamMemberRepository.findAll().size();

        // Get the teamMember
        restTeamMemberMockMvc.perform(delete("/api/team-members/{id}", teamMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TeamMember> teamMemberList = teamMemberRepository.findAll();
        assertThat(teamMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

//    @Test
//    public void equalsVerifier() throws Exception {
//        TestUtil.equalsVerifier(TeamMember.class);
//    }
}
