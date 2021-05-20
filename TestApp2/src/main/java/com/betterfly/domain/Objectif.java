package com.betterfly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Objectif.
 */
@Entity
@Table(name = "objectif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "objectif")
public class Objectif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "axedelapolitiqueqse")
    private String axedelapolitiqueqse;

    @Column(name = "objectifqse")
    private String objectifqse;

    @Column(name = "origine")
    private String origine;

    @ManyToOne
    private Action action;

    @ManyToOne
    private User delegue;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pilote" }, allowSetters = true)
    private ProcessusSMI processus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "responsableDeCalcul", "processus" }, allowSetters = true)
    private IndicateurSMI indicateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Objectif id(Long id) {
        this.id = id;
        return this;
    }

    public String getAxedelapolitiqueqse() {
        return this.axedelapolitiqueqse;
    }

    public Objectif axedelapolitiqueqse(String axedelapolitiqueqse) {
        this.axedelapolitiqueqse = axedelapolitiqueqse;
        return this;
    }

    public void setAxedelapolitiqueqse(String axedelapolitiqueqse) {
        this.axedelapolitiqueqse = axedelapolitiqueqse;
    }

    public String getObjectifqse() {
        return this.objectifqse;
    }

    public Objectif objectifqse(String objectifqse) {
        this.objectifqse = objectifqse;
        return this;
    }

    public void setObjectifqse(String objectifqse) {
        this.objectifqse = objectifqse;
    }

    public String getOrigine() {
        return this.origine;
    }

    public Objectif origine(String origine) {
        this.origine = origine;
        return this;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Action getAction() {
        return this.action;
    }

    public Objectif action(Action action) {
        this.setAction(action);
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getDelegue() {
        return this.delegue;
    }

    public Objectif delegue(User user) {
        this.setDelegue(user);
        return this;
    }

    public void setDelegue(User user) {
        this.delegue = user;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public Objectif processus(ProcessusSMI processusSMI) {
        this.setProcessus(processusSMI);
        return this;
    }

    public void setProcessus(ProcessusSMI processusSMI) {
        this.processus = processusSMI;
    }

    public IndicateurSMI getIndicateur() {
        return this.indicateur;
    }

    public Objectif indicateur(IndicateurSMI indicateurSMI) {
        this.setIndicateur(indicateurSMI);
        return this;
    }

    public void setIndicateur(IndicateurSMI indicateurSMI) {
        this.indicateur = indicateurSMI;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Objectif)) {
            return false;
        }
        return id != null && id.equals(((Objectif) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Objectif{" +
            "id=" + getId() +
            ", axedelapolitiqueqse='" + getAxedelapolitiqueqse() + "'" +
            ", objectifqse='" + getObjectifqse() + "'" +
            ", origine='" + getOrigine() + "'" +
            "}";
    }
}
