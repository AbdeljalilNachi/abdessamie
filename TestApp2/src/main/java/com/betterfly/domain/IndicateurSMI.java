package com.betterfly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A IndicateurSMI.
 */
@Entity
@Table(name = "indicateur_smi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "indicateursmi")
public class IndicateurSMI implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_identification")
    private LocalDate dateIdentification;

    @Column(name = "indicateur")
    private String indicateur;

    @Column(name = "formule_calcul")
    private String formuleCalcul;

    @Column(name = "cible")
    private Float cible;

    @Column(name = "seuil_tolerance")
    private Float seuilTolerance;

    @Column(name = "unite")
    private String unite;

    @Column(name = "periodicite")
    private String periodicite;

    @Column(name = "observations")
    private String observations;

    @Column(name = "vigueur")
    private Boolean vigueur;

    @ManyToOne
    private User responsableDeCalcul;

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

    public IndicateurSMI id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateIdentification() {
        return this.dateIdentification;
    }

    public IndicateurSMI dateIdentification(LocalDate dateIdentification) {
        this.dateIdentification = dateIdentification;
        return this;
    }

    public void setDateIdentification(LocalDate dateIdentification) {
        this.dateIdentification = dateIdentification;
    }

    public String getIndicateur() {
        return this.indicateur;
    }

    public IndicateurSMI indicateur(String indicateur) {
        this.indicateur = indicateur;
        return this;
    }

    public void setIndicateur(String indicateur) {
        this.indicateur = indicateur;
    }

    public String getFormuleCalcul() {
        return this.formuleCalcul;
    }

    public IndicateurSMI formuleCalcul(String formuleCalcul) {
        this.formuleCalcul = formuleCalcul;
        return this;
    }

    public void setFormuleCalcul(String formuleCalcul) {
        this.formuleCalcul = formuleCalcul;
    }

    public Float getCible() {
        return this.cible;
    }

    public IndicateurSMI cible(Float cible) {
        this.cible = cible;
        return this;
    }

    public void setCible(Float cible) {
        this.cible = cible;
    }

    public Float getSeuilTolerance() {
        return this.seuilTolerance;
    }

    public IndicateurSMI seuilTolerance(Float seuilTolerance) {
        this.seuilTolerance = seuilTolerance;
        return this;
    }

    public void setSeuilTolerance(Float seuilTolerance) {
        this.seuilTolerance = seuilTolerance;
    }

    public String getUnite() {
        return this.unite;
    }

    public IndicateurSMI unite(String unite) {
        this.unite = unite;
        return this;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getPeriodicite() {
        return this.periodicite;
    }

    public IndicateurSMI periodicite(String periodicite) {
        this.periodicite = periodicite;
        return this;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public String getObservations() {
        return this.observations;
    }

    public IndicateurSMI observations(String observations) {
        this.observations = observations;
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Boolean getVigueur() {
        return this.vigueur;
    }

    public IndicateurSMI vigueur(Boolean vigueur) {
        this.vigueur = vigueur;
        return this;
    }

    public void setVigueur(Boolean vigueur) {
        this.vigueur = vigueur;
    }

    public User getResponsableDeCalcul() {
        return this.responsableDeCalcul;
    }

    public IndicateurSMI responsableDeCalcul(User user) {
        this.setResponsableDeCalcul(user);
        return this;
    }

    public void setResponsableDeCalcul(User user) {
        this.responsableDeCalcul = user;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public IndicateurSMI processus(ProcessusSMI processusSMI) {
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
        if (!(o instanceof IndicateurSMI)) {
            return false;
        }
        return id != null && id.equals(((IndicateurSMI) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IndicateurSMI{" +
            "id=" + getId() +
            ", dateIdentification='" + getDateIdentification() + "'" +
            ", indicateur='" + getIndicateur() + "'" +
            ", formuleCalcul='" + getFormuleCalcul() + "'" +
            ", cible=" + getCible() +
            ", seuilTolerance=" + getSeuilTolerance() +
            ", unite='" + getUnite() + "'" +
            ", periodicite='" + getPeriodicite() + "'" +
            ", observations='" + getObservations() + "'" +
            ", vigueur='" + getVigueur() + "'" +
            "}";
    }
}
