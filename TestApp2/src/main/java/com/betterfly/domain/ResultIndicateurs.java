package com.betterfly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A ResultIndicateurs.
 */
@Entity
@Table(name = "result_indicateurs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "resultindicateurs")
public class ResultIndicateurs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "annee")
    private Integer annee;

    @Column(name = "observation")
    private String observation;

    @OneToMany(mappedBy = "resultIndicateurs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "resultIndicateurs" }, allowSetters = true)
    private Set<ResultatIndicateur> resultats = new HashSet<>();

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

    public ResultIndicateurs id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getAnnee() {
        return this.annee;
    }

    public ResultIndicateurs annee(Integer annee) {
        this.annee = annee;
        return this;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getObservation() {
        return this.observation;
    }

    public ResultIndicateurs observation(String observation) {
        this.observation = observation;
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Set<ResultatIndicateur> getResultats() {
        return this.resultats;
    }

    public ResultIndicateurs resultats(Set<ResultatIndicateur> resultatIndicateurs) {
        this.setResultats(resultatIndicateurs);
        return this;
    }

    public ResultIndicateurs addResultats(ResultatIndicateur resultatIndicateur) {
        this.resultats.add(resultatIndicateur);
        resultatIndicateur.setResultIndicateurs(this);
        return this;
    }

    public ResultIndicateurs removeResultats(ResultatIndicateur resultatIndicateur) {
        this.resultats.remove(resultatIndicateur);
        resultatIndicateur.setResultIndicateurs(null);
        return this;
    }

    public void setResultats(Set<ResultatIndicateur> resultatIndicateurs) {
        if (this.resultats != null) {
            this.resultats.forEach(i -> i.setResultIndicateurs(null));
        }
        if (resultatIndicateurs != null) {
            resultatIndicateurs.forEach(i -> i.setResultIndicateurs(this));
        }
        this.resultats = resultatIndicateurs;
    }

    public IndicateurSMI getIndicateur() {
        return this.indicateur;
    }

    public ResultIndicateurs indicateur(IndicateurSMI indicateurSMI) {
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
        if (!(o instanceof ResultIndicateurs)) {
            return false;
        }
        return id != null && id.equals(((ResultIndicateurs) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultIndicateurs{" +
            "id=" + getId() +
            ", annee=" + getAnnee() +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
