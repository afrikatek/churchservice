package com.afrikatek.churchservice.domain;

import com.afrikatek.churchservice.domain.enumeration.AddressType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "street", nullable = false)
    private String street;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "city", length = 50, nullable = false)
    private String city;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "province", length = 50, nullable = false)
    private String province;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "country", length = 50, nullable = false)
    private String country;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    private AddressType addressType;

    @Size(min = 3, max = 15)
    @Column(name = "telephone_work", length = 15)
    private String telephoneWork;

    @Size(min = 3, max = 15)
    @Column(name = "telephone_home", length = 15)
    private String telephoneHome;

    @Size(min = 3, max = 15)
    @Column(name = "cellphone", length = 15)
    private String cellphone;

    @ManyToOne
    @JsonIgnoreProperties(value = { "baptismHistory", "user", "addresses", "league", "ministry" }, allowSetters = true)
    private Profile profile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Address id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return this.street;
    }

    public Address street(String street) {
        this.setStreet(street);
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public Address city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    public Address province(String province) {
        this.setProvince(province);
        return this;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return this.country;
    }

    public Address country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public AddressType getAddressType() {
        return this.addressType;
    }

    public Address addressType(AddressType addressType) {
        this.setAddressType(addressType);
        return this;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getTelephoneWork() {
        return this.telephoneWork;
    }

    public Address telephoneWork(String telephoneWork) {
        this.setTelephoneWork(telephoneWork);
        return this;
    }

    public void setTelephoneWork(String telephoneWork) {
        this.telephoneWork = telephoneWork;
    }

    public String getTelephoneHome() {
        return this.telephoneHome;
    }

    public Address telephoneHome(String telephoneHome) {
        this.setTelephoneHome(telephoneHome);
        return this;
    }

    public void setTelephoneHome(String telephoneHome) {
        this.telephoneHome = telephoneHome;
    }

    public String getCellphone() {
        return this.cellphone;
    }

    public Address cellphone(String cellphone) {
        this.setCellphone(cellphone);
        return this;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Address profile(Profile profile) {
        this.setProfile(profile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", street='" + getStreet() + "'" +
            ", city='" + getCity() + "'" +
            ", province='" + getProvince() + "'" +
            ", country='" + getCountry() + "'" +
            ", addressType='" + getAddressType() + "'" +
            ", telephoneWork='" + getTelephoneWork() + "'" +
            ", telephoneHome='" + getTelephoneHome() + "'" +
            ", cellphone='" + getCellphone() + "'" +
            "}";
    }
}
