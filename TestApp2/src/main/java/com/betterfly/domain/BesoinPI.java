package com.betterfly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A BesoinPI.
 */
@Entity
@Table(name = "besoin_pi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "besoinpi")
public class BesoinPI implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_identification")
    private LocalDate dateIdentification;

    @Column(name = "pi_pertinentes")
    private String piPertinentes;

    @Column(name = "pertinente")
    private Boolean pertinente;

    @Column(name = "prise_en_charge")
    private Boolean priseEnCharge;

    @Column(name = "afficher")
    private Boolean afficher;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pilote" }, allowSetters = true)
    private ProcessusSMI processus;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BesoinPI id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateIdentification() {
        return this.dateIdentification;
    }

    public BesoinPI dateIdentification(LocalDate dateIdentification) {
        this.dateIdentification = dateIdentification;
        return this;
    }

    public void setDateIdentification(LocalDate dateIdentification) {
        this.dateIdentification = dateIdentification;
    }

    public String getPiPertinentes() {
        return this.piPertinentes;
    }

    public BesoinPI piPertinentes(String piPertinentes) {
        this.piPertinentes = piPertinentes;
        return this;
    }

    public void setPiPertinentes(String piPertinentes) {
        this.piPertinentes = piPertinentes;
    }

    public Boolean getPertinente() {
        return this.pertinente;
    }

    public BesoinPI pertinente(Boolean pertinente) {
        this.pertinente = pertinente;
        return this;
    }

    public void setPertinente(Boolean pertinente) {
        this.pertinente = pertinente;
    }

    public Boolean getPriseEnCharge() {
        return this.priseEnCharge;
    }

    public BesoinPI priseEnCharge(Boolean priseEnCharge) {
        this.priseEnCharge = priseEnCharge;
        return this;
    }

    public void setPriseEnCharge(Boolean priseEnCharge) {
        this.priseEnCharge = priseEnCharge;
    }

    public Boolean getAfficher() {
        return this.afficher;
    }

    public BesoinPI afficher(Boolean afficher) {
        this.afficher = afficher;
        return this;
    }

    public void setAfficher(Boolean afficher) {
        this.afficher = afficher;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public BesoinPI processus(ProcessusSMI processusSMI) {
        this.setProcessus(processusSMI);
        return this;
    }

    public void setProcessus(ProcessusSMI processusSMI) {
        this.processus = processusSMI;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BesoinPI)) {
            return false;
        }
        return id != null && id.equals(((BesoinPI) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BesoinPI{" +
            "id=" + getId() +
            ", dateIdentification='" + getDateIdentification() + "'" +
            ", piPertinentes='" + getPiPertinentes() + "'" +
            ", pertinente='" + getPertinente() + "'" +
            ", priseEnCharge='" + getPriseEnCharge() + "'" +
            ", afficher='" + getAfficher() + "'" +
            "}";
    }
}
