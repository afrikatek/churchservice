package com.afrikatek.churchservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.churchservice.IntegrationTest;
import com.afrikatek.churchservice.domain.Ministry;
import com.afrikatek.churchservice.repository.MinistryRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link MinistryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MinistryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ministries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MinistryRepository ministryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMinistryMockMvc;

    private Ministry ministry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ministry createEntity(EntityManager em) {
        Ministry ministry = new Ministry().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return ministry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ministry createUpdatedEntity(EntityManager em) {
        Ministry ministry = new Ministry().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return ministry;
    }

    @BeforeEach
    public void initTest() {
        ministry = createEntity(em);
    }

    @Test
    @Transactional
    void createMinistry() throws Exception {
        int databaseSizeBeforeCreate = ministryRepository.findAll().size();
        // Create the Ministry
        restMinistryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ministry)))
            .andExpect(status().isCreated());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeCreate + 1);
        Ministry testMinistry = ministryList.get(ministryList.size() - 1);
        assertThat(testMinistry.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMinistry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createMinistryWithExistingId() throws Exception {
        // Create the Ministry with an existing ID
        ministry.setId(1L);

        int databaseSizeBeforeCreate = ministryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMinistryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ministry)))
            .andExpect(status().isBadRequest());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ministryRepository.findAll().size();
        // set the field null
        ministry.setName(null);

        // Create the Ministry, which fails.

        restMinistryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ministry)))
            .andExpect(status().isBadRequest());

        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMinistries() throws Exception {
        // Initialize the database
        ministryRepository.saveAndFlush(ministry);

        // Get all the ministryList
        restMinistryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ministry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getMinistry() throws Exception {
        // Initialize the database
        ministryRepository.saveAndFlush(ministry);

        // Get the ministry
        restMinistryMockMvc
            .perform(get(ENTITY_API_URL_ID, ministry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ministry.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMinistry() throws Exception {
        // Get the ministry
        restMinistryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMinistry() throws Exception {
        // Initialize the database
        ministryRepository.saveAndFlush(ministry);

        int databaseSizeBeforeUpdate = ministryRepository.findAll().size();

        // Update the ministry
        Ministry updatedMinistry = ministryRepository.findById(ministry.getId()).get();
        // Disconnect from session so that the updates on updatedMinistry are not directly saved in db
        em.detach(updatedMinistry);
        updatedMinistry.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMinistryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMinistry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMinistry))
            )
            .andExpect(status().isOk());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeUpdate);
        Ministry testMinistry = ministryList.get(ministryList.size() - 1);
        assertThat(testMinistry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMinistry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingMinistry() throws Exception {
        int databaseSizeBeforeUpdate = ministryRepository.findAll().size();
        ministry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMinistryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ministry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministry))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMinistry() throws Exception {
        int databaseSizeBeforeUpdate = ministryRepository.findAll().size();
        ministry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ministry))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMinistry() throws Exception {
        int databaseSizeBeforeUpdate = ministryRepository.findAll().size();
        ministry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ministry)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMinistryWithPatch() throws Exception {
        // Initialize the database
        ministryRepository.saveAndFlush(ministry);

        int databaseSizeBeforeUpdate = ministryRepository.findAll().size();

        // Update the ministry using partial update
        Ministry partialUpdatedMinistry = new Ministry();
        partialUpdatedMinistry.setId(ministry.getId());

        partialUpdatedMinistry.name(UPDATED_NAME);

        restMinistryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMinistry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMinistry))
            )
            .andExpect(status().isOk());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeUpdate);
        Ministry testMinistry = ministryList.get(ministryList.size() - 1);
        assertThat(testMinistry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMinistry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateMinistryWithPatch() throws Exception {
        // Initialize the database
        ministryRepository.saveAndFlush(ministry);

        int databaseSizeBeforeUpdate = ministryRepository.findAll().size();

        // Update the ministry using partial update
        Ministry partialUpdatedMinistry = new Ministry();
        partialUpdatedMinistry.setId(ministry.getId());

        partialUpdatedMinistry.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restMinistryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMinistry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMinistry))
            )
            .andExpect(status().isOk());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeUpdate);
        Ministry testMinistry = ministryList.get(ministryList.size() - 1);
        assertThat(testMinistry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMinistry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingMinistry() throws Exception {
        int databaseSizeBeforeUpdate = ministryRepository.findAll().size();
        ministry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMinistryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ministry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ministry))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMinistry() throws Exception {
        int databaseSizeBeforeUpdate = ministryRepository.findAll().size();
        ministry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ministry))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMinistry() throws Exception {
        int databaseSizeBeforeUpdate = ministryRepository.findAll().size();
        ministry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMinistryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ministry)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ministry in the database
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMinistry() throws Exception {
        // Initialize the database
        ministryRepository.saveAndFlush(ministry);

        int databaseSizeBeforeDelete = ministryRepository.findAll().size();

        // Delete the ministry
        restMinistryMockMvc
            .perform(delete(ENTITY_API_URL_ID, ministry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ministry> ministryList = ministryRepository.findAll();
        assertThat(ministryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
