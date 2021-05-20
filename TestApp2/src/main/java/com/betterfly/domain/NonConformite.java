package com.betterfly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A NonConformite.
 */
@Entity
@Table(name = "non_conformite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "nonconformite")
public class NonConformite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @Column(name = "causes_potentielles")
    private String causesPotentielles;

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

    public NonConformite id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public NonConformite date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public NonConformite description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCausesPotentielles() {
        return this.causesPotentielles;
    }

    public NonConformite causesPotentielles(String causesPotentielles) {
        this.causesPotentielles = causesPotentielles;
        return this;
    }

    public void setCausesPotentielles(String causesPotentielles) {
        this.causesPotentielles = causesPotentielles;
    }

    public String getOrigine() {
        return this.origine;
    }

    public NonConformite origine(String origine) {
        this.origine = origine;
        return this;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Action getAction() {
        return this.action;
    }

    public NonConformite action(Action action) {
        this.setAction(action);
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getDelegue() {
        return this.delegue;
    }

    public NonConformite delegue(User user) {
        this.setDelegue(user);
        return this;
    }

    public void setDelegue(User user) {
        this.delegue = user;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public NonConformite processus(ProcessusSMI processusSMI) {
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
        if (!(o instanceof NonConformite)) {
            return false;
        }
        return id != null && id.equals(((NonConformite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NonConformite{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", causesPotentielles='" + getCausesPotentielles() + "'" +
            ", origine='" + getOrigine() + "'" +
            "}";
    }
}
