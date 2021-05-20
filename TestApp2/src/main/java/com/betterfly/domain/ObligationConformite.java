package com.betterfly.domain;

import com.betterfly.domain.enumeration.Rubrique;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A ObligationConformite.
 */
@Entity
@Table(name = "obligation_conformite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "obligationconformite")
public class ObligationConformite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "rubrique")
    private Rubrique rubrique;

    @Column(name = "reference")
    private String reference;

    @Column(name = "num")
    private Integer num;

    @Column(name = "exigence")
    private String exigence;

    @Column(name = "applicable")
    private Boolean applicable;

    @Column(name = "conforme")
    private Boolean conforme;

    @Column(name = "statut")
    private Integer statut;

    @Column(name = "observation")
    private String observation;

    @Column(name = "origine")
    private String origine;

    @ManyToOne
    private Action action;

    @ManyToOne
    private User delegue;

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

    public ObligationConformite id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ObligationConformite date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Rubrique getRubrique() {
        return this.rubrique;
    }

    public ObligationConformite rubrique(Rubrique rubrique) {
        this.rubrique = rubrique;
        return this;
    }

    public void setRubrique(Rubrique rubrique) {
        this.rubrique = rubrique;
    }

    public String getReference() {
        return this.reference;
    }

    public ObligationConformite reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getNum() {
        return this.num;
    }

    public ObligationConformite num(Integer num) {
        this.num = num;
        return this;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getExigence() {
        return this.exigence;
    }

    public ObligationConformite exigence(String exigence) {
        this.exigence = exigence;
        return this;
    }

    public void setExigence(String exigence) {
        this.exigence = exigence;
    }

    public Boolean getApplicable() {
        return this.applicable;
    }

    public ObligationConformite applicable(Boolean applicable) {
        this.applicable = applicable;
        return this;
    }

    public void setApplicable(Boolean applicable) {
        this.applicable = applicable;
    }

    public Boolean getConforme() {
        return this.conforme;
    }

    public ObligationConformite conforme(Boolean conforme) {
        this.conforme = conforme;
        return this;
    }

    public void setConforme(Boolean conforme) {
        this.conforme = conforme;
    }

    public Integer getStatut() {
        return this.statut;
    }

    public ObligationConformite statut(Integer statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }

    public String getObservation() {
        return this.observation;
    }

    public ObligationConformite observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getOrigine() {
        return this.origine;
    }

    public ObligationConformite origine(String origine) {
        this.origine = origine;
        return this;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Action getAction() {
        return this.action;
    }

    public ObligationConformite action(Action action) {
        this.setAction(action);
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getDelegue() {
        return this.delegue;
    }

    public ObligationConformite delegue(User user) {
        this.setDelegue(user);
        return this;
    }

    public void setDelegue(User user) {
        this.delegue = user;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public ObligationConformite processus(ProcessusSMI processusSMI) {
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
        if (!(o instanceof ObligationConformite)) {
            return false;
        }
        return id != null && id.equals(((ObligationConformite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ObligationConformite{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", rubrique='" + getRubrique() + "'" +
            ", reference='" + getReference() + "'" +
            ", num=" + getNum() +
            ", exigence='" + getExigence() + "'" +
            ", applicable='" + getApplicable() + "'" +
            ", conforme='" + getConforme() + "'" +
            ", statut=" + getStatut() +
            ", observation='" + getObservation() + "'" +
            ", origine='" + getOrigine() + "'" +
            "}";
    }
}
