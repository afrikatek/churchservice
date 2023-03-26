package com.afrikatek.churchservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.churchservice.IntegrationTest;
import com.afrikatek.churchservice.domain.BaptismHistory;
import com.afrikatek.churchservice.repository.BaptismHistoryRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link BaptismHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BaptismHistoryResourceIT {

    private static final Boolean DEFAULT_LUTHERAN = false;
    private static final Boolean UPDATED_LUTHERAN = true;

    private static final String DEFAULT_PREVIOUS_PARISH = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_PARISH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_BAPTISED = false;
    private static final Boolean UPDATED_BAPTISED = true;

    private static final LocalDate DEFAULT_BAPTISM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BAPTISM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_BAPTISED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BAPTISED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_CONFIRMED = false;
    private static final Boolean UPDATED_CONFIRMED = true;

    private static final LocalDate DEFAULT_CONFIRMATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONFIRMATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PARISH_CONFIRMED = "AAAAAAAAAA";
    private static final String UPDATED_PARISH_CONFIRMED = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MARRIED = false;
    private static final Boolean UPDATED_MARRIED = true;

    private static final LocalDate DEFAULT_MARRIAGE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MARRIAGE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PARISH_MARRIED_AT = "AAAAAAAAAA";
    private static final String UPDATED_PARISH_MARRIED_AT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/baptism-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BaptismHistoryRepository baptismHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBaptismHistoryMockMvc;

    private BaptismHistory baptismHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaptismHistory createEntity(EntityManager em) {
        BaptismHistory baptismHistory = new BaptismHistory()
            .lutheran(DEFAULT_LUTHERAN)
            .previousParish(DEFAULT_PREVIOUS_PARISH)
            .baptised(DEFAULT_BAPTISED)
            .baptismDate(DEFAULT_BAPTISM_DATE)
            .baptisedAt(DEFAULT_BAPTISED_AT)
            .confirmed(DEFAULT_CONFIRMED)
            .confirmationDate(DEFAULT_CONFIRMATION_DATE)
            .parishConfirmed(DEFAULT_PARISH_CONFIRMED)
            .married(DEFAULT_MARRIED)
            .marriageDate(DEFAULT_MARRIAGE_DATE)
            .parishMarriedAt(DEFAULT_PARISH_MARRIED_AT);
        return baptismHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BaptismHistory createUpdatedEntity(EntityManager em) {
        BaptismHistory baptismHistory = new BaptismHistory()
            .lutheran(UPDATED_LUTHERAN)
            .previousParish(UPDATED_PREVIOUS_PARISH)
            .baptised(UPDATED_BAPTISED)
            .baptismDate(UPDATED_BAPTISM_DATE)
            .baptisedAt(UPDATED_BAPTISED_AT)
            .confirmed(UPDATED_CONFIRMED)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .parishConfirmed(UPDATED_PARISH_CONFIRMED)
            .married(UPDATED_MARRIED)
            .marriageDate(UPDATED_MARRIAGE_DATE)
            .parishMarriedAt(UPDATED_PARISH_MARRIED_AT);
        return baptismHistory;
    }

    @BeforeEach
    public void initTest() {
        baptismHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createBaptismHistory() throws Exception {
        int databaseSizeBeforeCreate = baptismHistoryRepository.findAll().size();
        // Create the BaptismHistory
        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistory))
            )
            .andExpect(status().isCreated());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        BaptismHistory testBaptismHistory = baptismHistoryList.get(baptismHistoryList.size() - 1);
        assertThat(testBaptismHistory.getLutheran()).isEqualTo(DEFAULT_LUTHERAN);
        assertThat(testBaptismHistory.getPreviousParish()).isEqualTo(DEFAULT_PREVIOUS_PARISH);
        assertThat(testBaptismHistory.getBaptised()).isEqualTo(DEFAULT_BAPTISED);
        assertThat(testBaptismHistory.getBaptismDate()).isEqualTo(DEFAULT_BAPTISM_DATE);
        assertThat(testBaptismHistory.getBaptisedAt()).isEqualTo(DEFAULT_BAPTISED_AT);
        assertThat(testBaptismHistory.getConfirmed()).isEqualTo(DEFAULT_CONFIRMED);
        assertThat(testBaptismHistory.getConfirmationDate()).isEqualTo(DEFAULT_CONFIRMATION_DATE);
        assertThat(testBaptismHistory.getParishConfirmed()).isEqualTo(DEFAULT_PARISH_CONFIRMED);
        assertThat(testBaptismHistory.getMarried()).isEqualTo(DEFAULT_MARRIED);
        assertThat(testBaptismHistory.getMarriageDate()).isEqualTo(DEFAULT_MARRIAGE_DATE);
        assertThat(testBaptismHistory.getParishMarriedAt()).isEqualTo(DEFAULT_PARISH_MARRIED_AT);
    }

    @Test
    @Transactional
    void createBaptismHistoryWithExistingId() throws Exception {
        // Create the BaptismHistory with an existing ID
        baptismHistory.setId(1L);

        int databaseSizeBeforeCreate = baptismHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLutheranIsRequired() throws Exception {
        int databaseSizeBeforeTest = baptismHistoryRepository.findAll().size();
        // set the field null
        baptismHistory.setLutheran(null);

        // Create the BaptismHistory, which fails.

        restBaptismHistoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistory))
            )
            .andExpect(status().isBadRequest());

        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBaptismHistories() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        // Get all the baptismHistoryList
        restBaptismHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(baptismHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].lutheran").value(hasItem(DEFAULT_LUTHERAN.booleanValue())))
            .andExpect(jsonPath("$.[*].previousParish").value(hasItem(DEFAULT_PREVIOUS_PARISH)))
            .andExpect(jsonPath("$.[*].baptised").value(hasItem(DEFAULT_BAPTISED.booleanValue())))
            .andExpect(jsonPath("$.[*].baptismDate").value(hasItem(DEFAULT_BAPTISM_DATE.toString())))
            .andExpect(jsonPath("$.[*].baptisedAt").value(hasItem(DEFAULT_BAPTISED_AT.toString())))
            .andExpect(jsonPath("$.[*].confirmed").value(hasItem(DEFAULT_CONFIRMED.booleanValue())))
            .andExpect(jsonPath("$.[*].confirmationDate").value(hasItem(DEFAULT_CONFIRMATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].parishConfirmed").value(hasItem(DEFAULT_PARISH_CONFIRMED)))
            .andExpect(jsonPath("$.[*].married").value(hasItem(DEFAULT_MARRIED.booleanValue())))
            .andExpect(jsonPath("$.[*].marriageDate").value(hasItem(DEFAULT_MARRIAGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].parishMarriedAt").value(hasItem(DEFAULT_PARISH_MARRIED_AT)));
    }

    @Test
    @Transactional
    void getBaptismHistory() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        // Get the baptismHistory
        restBaptismHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, baptismHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(baptismHistory.getId().intValue()))
            .andExpect(jsonPath("$.lutheran").value(DEFAULT_LUTHERAN.booleanValue()))
            .andExpect(jsonPath("$.previousParish").value(DEFAULT_PREVIOUS_PARISH))
            .andExpect(jsonPath("$.baptised").value(DEFAULT_BAPTISED.booleanValue()))
            .andExpect(jsonPath("$.baptismDate").value(DEFAULT_BAPTISM_DATE.toString()))
            .andExpect(jsonPath("$.baptisedAt").value(DEFAULT_BAPTISED_AT.toString()))
            .andExpect(jsonPath("$.confirmed").value(DEFAULT_CONFIRMED.booleanValue()))
            .andExpect(jsonPath("$.confirmationDate").value(DEFAULT_CONFIRMATION_DATE.toString()))
            .andExpect(jsonPath("$.parishConfirmed").value(DEFAULT_PARISH_CONFIRMED))
            .andExpect(jsonPath("$.married").value(DEFAULT_MARRIED.booleanValue()))
            .andExpect(jsonPath("$.marriageDate").value(DEFAULT_MARRIAGE_DATE.toString()))
            .andExpect(jsonPath("$.parishMarriedAt").value(DEFAULT_PARISH_MARRIED_AT));
    }

    @Test
    @Transactional
    void getNonExistingBaptismHistory() throws Exception {
        // Get the baptismHistory
        restBaptismHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBaptismHistory() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();

        // Update the baptismHistory
        BaptismHistory updatedBaptismHistory = baptismHistoryRepository.findById(baptismHistory.getId()).get();
        // Disconnect from session so that the updates on updatedBaptismHistory are not directly saved in db
        em.detach(updatedBaptismHistory);
        updatedBaptismHistory
            .lutheran(UPDATED_LUTHERAN)
            .previousParish(UPDATED_PREVIOUS_PARISH)
            .baptised(UPDATED_BAPTISED)
            .baptismDate(UPDATED_BAPTISM_DATE)
            .baptisedAt(UPDATED_BAPTISED_AT)
            .confirmed(UPDATED_CONFIRMED)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .parishConfirmed(UPDATED_PARISH_CONFIRMED)
            .married(UPDATED_MARRIED)
            .marriageDate(UPDATED_MARRIAGE_DATE)
            .parishMarriedAt(UPDATED_PARISH_MARRIED_AT);

        restBaptismHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBaptismHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBaptismHistory))
            )
            .andExpect(status().isOk());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
        BaptismHistory testBaptismHistory = baptismHistoryList.get(baptismHistoryList.size() - 1);
        assertThat(testBaptismHistory.getLutheran()).isEqualTo(UPDATED_LUTHERAN);
        assertThat(testBaptismHistory.getPreviousParish()).isEqualTo(UPDATED_PREVIOUS_PARISH);
        assertThat(testBaptismHistory.getBaptised()).isEqualTo(UPDATED_BAPTISED);
        assertThat(testBaptismHistory.getBaptismDate()).isEqualTo(UPDATED_BAPTISM_DATE);
        assertThat(testBaptismHistory.getBaptisedAt()).isEqualTo(UPDATED_BAPTISED_AT);
        assertThat(testBaptismHistory.getConfirmed()).isEqualTo(UPDATED_CONFIRMED);
        assertThat(testBaptismHistory.getConfirmationDate()).isEqualTo(UPDATED_CONFIRMATION_DATE);
        assertThat(testBaptismHistory.getParishConfirmed()).isEqualTo(UPDATED_PARISH_CONFIRMED);
        assertThat(testBaptismHistory.getMarried()).isEqualTo(UPDATED_MARRIED);
        assertThat(testBaptismHistory.getMarriageDate()).isEqualTo(UPDATED_MARRIAGE_DATE);
        assertThat(testBaptismHistory.getParishMarriedAt()).isEqualTo(UPDATED_PARISH_MARRIED_AT);
    }

    @Test
    @Transactional
    void putNonExistingBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, baptismHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(baptismHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBaptismHistoryWithPatch() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();

        // Update the baptismHistory using partial update
        BaptismHistory partialUpdatedBaptismHistory = new BaptismHistory();
        partialUpdatedBaptismHistory.setId(baptismHistory.getId());

        partialUpdatedBaptismHistory
            .lutheran(UPDATED_LUTHERAN)
            .previousParish(UPDATED_PREVIOUS_PARISH)
            .baptised(UPDATED_BAPTISED)
            .parishConfirmed(UPDATED_PARISH_CONFIRMED)
            .married(UPDATED_MARRIED);

        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaptismHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaptismHistory))
            )
            .andExpect(status().isOk());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
        BaptismHistory testBaptismHistory = baptismHistoryList.get(baptismHistoryList.size() - 1);
        assertThat(testBaptismHistory.getLutheran()).isEqualTo(UPDATED_LUTHERAN);
        assertThat(testBaptismHistory.getPreviousParish()).isEqualTo(UPDATED_PREVIOUS_PARISH);
        assertThat(testBaptismHistory.getBaptised()).isEqualTo(UPDATED_BAPTISED);
        assertThat(testBaptismHistory.getBaptismDate()).isEqualTo(DEFAULT_BAPTISM_DATE);
        assertThat(testBaptismHistory.getBaptisedAt()).isEqualTo(DEFAULT_BAPTISED_AT);
        assertThat(testBaptismHistory.getConfirmed()).isEqualTo(DEFAULT_CONFIRMED);
        assertThat(testBaptismHistory.getConfirmationDate()).isEqualTo(DEFAULT_CONFIRMATION_DATE);
        assertThat(testBaptismHistory.getParishConfirmed()).isEqualTo(UPDATED_PARISH_CONFIRMED);
        assertThat(testBaptismHistory.getMarried()).isEqualTo(UPDATED_MARRIED);
        assertThat(testBaptismHistory.getMarriageDate()).isEqualTo(DEFAULT_MARRIAGE_DATE);
        assertThat(testBaptismHistory.getParishMarriedAt()).isEqualTo(DEFAULT_PARISH_MARRIED_AT);
    }

    @Test
    @Transactional
    void fullUpdateBaptismHistoryWithPatch() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();

        // Update the baptismHistory using partial update
        BaptismHistory partialUpdatedBaptismHistory = new BaptismHistory();
        partialUpdatedBaptismHistory.setId(baptismHistory.getId());

        partialUpdatedBaptismHistory
            .lutheran(UPDATED_LUTHERAN)
            .previousParish(UPDATED_PREVIOUS_PARISH)
            .baptised(UPDATED_BAPTISED)
            .baptismDate(UPDATED_BAPTISM_DATE)
            .baptisedAt(UPDATED_BAPTISED_AT)
            .confirmed(UPDATED_CONFIRMED)
            .confirmationDate(UPDATED_CONFIRMATION_DATE)
            .parishConfirmed(UPDATED_PARISH_CONFIRMED)
            .married(UPDATED_MARRIED)
            .marriageDate(UPDATED_MARRIAGE_DATE)
            .parishMarriedAt(UPDATED_PARISH_MARRIED_AT);

        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBaptismHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBaptismHistory))
            )
            .andExpect(status().isOk());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
        BaptismHistory testBaptismHistory = baptismHistoryList.get(baptismHistoryList.size() - 1);
        assertThat(testBaptismHistory.getLutheran()).isEqualTo(UPDATED_LUTHERAN);
        assertThat(testBaptismHistory.getPreviousParish()).isEqualTo(UPDATED_PREVIOUS_PARISH);
        assertThat(testBaptismHistory.getBaptised()).isEqualTo(UPDATED_BAPTISED);
        assertThat(testBaptismHistory.getBaptismDate()).isEqualTo(UPDATED_BAPTISM_DATE);
        assertThat(testBaptismHistory.getBaptisedAt()).isEqualTo(UPDATED_BAPTISED_AT);
        assertThat(testBaptismHistory.getConfirmed()).isEqualTo(UPDATED_CONFIRMED);
        assertThat(testBaptismHistory.getConfirmationDate()).isEqualTo(UPDATED_CONFIRMATION_DATE);
        assertThat(testBaptismHistory.getParishConfirmed()).isEqualTo(UPDATED_PARISH_CONFIRMED);
        assertThat(testBaptismHistory.getMarried()).isEqualTo(UPDATED_MARRIED);
        assertThat(testBaptismHistory.getMarriageDate()).isEqualTo(UPDATED_MARRIAGE_DATE);
        assertThat(testBaptismHistory.getParishMarriedAt()).isEqualTo(UPDATED_PARISH_MARRIED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, baptismHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(baptismHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBaptismHistory() throws Exception {
        int databaseSizeBeforeUpdate = baptismHistoryRepository.findAll().size();
        baptismHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBaptismHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(baptismHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BaptismHistory in the database
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBaptismHistory() throws Exception {
        // Initialize the database
        baptismHistoryRepository.saveAndFlush(baptismHistory);

        int databaseSizeBeforeDelete = baptismHistoryRepository.findAll().size();

        // Delete the baptismHistory
        restBaptismHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, baptismHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BaptismHistory> baptismHistoryList = baptismHistoryRepository.findAll();
        assertThat(baptismHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
