package com.afrikatek.churchservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BaptismHistory.
 */
@Entity
@Table(name = "baptism_history")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BaptismHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "lutheran", nullable = false)
    private Boolean lutheran;

    @Size(min = 3, max = 20)
    @Column(name = "previous_parish", length = 20)
    private String previousParish;

    @Column(name = "baptised")
    private Boolean baptised;

    @Column(name = "baptism_date")
    private LocalDate baptismDate;

    @Column(name = "baptised_at")
    private LocalDate baptisedAt;

    @Column(name = "confirmed")
    private Boolean confirmed;

    @Column(name = "confirmation_date")
    private LocalDate confirmationDate;

    @Size(min = 3, max = 20)
    @Column(name = "parish_confirmed", length = 20)
    private String parishConfirmed;

    @Column(name = "married")
    private Boolean married;

    @Column(name = "marriage_date")
    private LocalDate marriageDate;

    @Size(min = 3, max = 20)
    @Column(name = "parish_married_at", length = 20)
    private String parishMarriedAt;

    @JsonIgnoreProperties(value = { "baptismHistory", "user", "addresses", "league", "ministry" }, allowSetters = true)
    @OneToOne(mappedBy = "baptismHistory")
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BaptismHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLutheran() {
        return this.lutheran;
    }

    public BaptismHistory lutheran(Boolean lutheran) {
        this.setLutheran(lutheran);
        return this;
    }

    public void setLutheran(Boolean lutheran) {
        this.lutheran = lutheran;
    }

    public String getPreviousParish() {
        return this.previousParish;
    }

    public BaptismHistory previousParish(String previousParish) {
        this.setPreviousParish(previousParish);
        return this;
    }

    public void setPreviousParish(String previousParish) {
        this.previousParish = previousParish;
    }

    public Boolean getBaptised() {
        return this.baptised;
    }

    public BaptismHistory baptised(Boolean baptised) {
        this.setBaptised(baptised);
        return this;
    }

    public void setBaptised(Boolean baptised) {
        this.baptised = baptised;
    }

    public LocalDate getBaptismDate() {
        return this.baptismDate;
    }

    public BaptismHistory baptismDate(LocalDate baptismDate) {
        this.setBaptismDate(baptismDate);
        return this;
    }

    public void setBaptismDate(LocalDate baptismDate) {
        this.baptismDate = baptismDate;
    }

    public LocalDate getBaptisedAt() {
        return this.baptisedAt;
    }

    public BaptismHistory baptisedAt(LocalDate baptisedAt) {
        this.setBaptisedAt(baptisedAt);
        return this;
    }

    public void setBaptisedAt(LocalDate baptisedAt) {
        this.baptisedAt = baptisedAt;
    }

    public Boolean getConfirmed() {
        return this.confirmed;
    }

    public BaptismHistory confirmed(Boolean confirmed) {
        this.setConfirmed(confirmed);
        return this;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public LocalDate getConfirmationDate() {
        return this.confirmationDate;
    }

    public BaptismHistory confirmationDate(LocalDate confirmationDate) {
        this.setConfirmationDate(confirmationDate);
        return this;
    }

    public void setConfirmationDate(LocalDate confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getParishConfirmed() {
        return this.parishConfirmed;
    }

    public BaptismHistory parishConfirmed(String parishConfirmed) {
        this.setParishConfirmed(parishConfirmed);
        return this;
    }

    public void setParishConfirmed(String parishConfirmed) {
        this.parishConfirmed = parishConfirmed;
    }

    public Boolean getMarried() {
        return this.married;
    }

    public BaptismHistory married(Boolean married) {
        this.setMarried(married);
        return this;
    }

    public void setMarried(Boolean married) {
        this.married = married;
    }

    public LocalDate getMarriageDate() {
        return this.marriageDate;
    }

    public BaptismHistory marriageDate(LocalDate marriageDate) {
        this.setMarriageDate(marriageDate);
        return this;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    public String getParishMarriedAt() {
        return this.parishMarriedAt;
    }

    public BaptismHistory parishMarriedAt(String parishMarriedAt) {
        this.setParishMarriedAt(parishMarriedAt);
        return this;
    }

    public void setParishMarriedAt(String parishMarriedAt) {
        this.parishMarriedAt = parishMarriedAt;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        if (this.profile != null) {
            this.profile.setBaptismHistory(null);
        }
        if (profile != null) {
            profile.setBaptismHistory(this);
        }
        this.profile = profile;
    }

    public BaptismHistory profile(Profile profile) {
        this.setProfile(profile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaptismHistory)) {
            return false;
        }
        return id != null && id.equals(((BaptismHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BaptismHistory{" +
            "id=" + getId() +
            ", lutheran='" + getLutheran() + "'" +
            ", previousParish='" + getPreviousParish() + "'" +
            ", baptised='" + getBaptised() + "'" +
            ", baptismDate='" + getBaptismDate() + "'" +
            ", baptisedAt='" + getBaptisedAt() + "'" +
            ", confirmed='" + getConfirmed() + "'" +
            ", confirmationDate='" + getConfirmationDate() + "'" +
            ", parishConfirmed='" + getParishConfirmed() + "'" +
            ", married='" + getMarried() + "'" +
            ", marriageDate='" + getMarriageDate() + "'" +
            ", parishMarriedAt='" + getParishMarriedAt() + "'" +
            "}";
    }
}
