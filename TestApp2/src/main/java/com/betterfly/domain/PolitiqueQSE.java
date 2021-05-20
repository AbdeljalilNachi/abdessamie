package com.betterfly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A PolitiqueQSE.
 */
@Entity
@Table(name = "politique_qse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "politiqueqse")
public class PolitiqueQSE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "axe_politique_qse")
    private String axePolitiqueQSE;

    @Column(name = "objectif_qse")
    private String objectifQSE;

    @Column(name = "vigueur")
    private Boolean vigueur;

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

    public PolitiqueQSE id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PolitiqueQSE date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAxePolitiqueQSE() {
        return this.axePolitiqueQSE;
    }

    public PolitiqueQSE axePolitiqueQSE(String axePolitiqueQSE) {
        this.axePolitiqueQSE = axePolitiqueQSE;
        return this;
    }

    public void setAxePolitiqueQSE(String axePolitiqueQSE) {
        this.axePolitiqueQSE = axePolitiqueQSE;
    }

    public String getObjectifQSE() {
        return this.objectifQSE;
    }

    public PolitiqueQSE objectifQSE(String objectifQSE) {
        this.objectifQSE = objectifQSE;
        return this;
    }

    public void setObjectifQSE(String objectifQSE) {
        this.objectifQSE = objectifQSE;
    }

    public Boolean getVigueur() {
        return this.vigueur;
    }

    public PolitiqueQSE vigueur(Boolean vigueur) {
        this.vigueur = vigueur;
        return this;
    }

    public void setVigueur(Boolean vigueur) {
        this.vigueur = vigueur;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public PolitiqueQSE processus(ProcessusSMI processusSMI) {
        this.setProcessus(processusSMI);
        return this;
    }

    public void setProcessus(ProcessusSMI processusSMI) {
        this.processus = processusSMI;
    }

    public IndicateurSMI getIndicateur() {
        return this.indicateur;
    }

    public PolitiqueQSE indicateur(IndicateurSMI indicateurSMI) {
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
        if (!(o instanceof PolitiqueQSE)) {
            return false;
        }
        return id != null && id.equals(((PolitiqueQSE) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolitiqueQSE{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", axePolitiqueQSE='" + getAxePolitiqueQSE() + "'" +
            ", objectifQSE='" + getObjectifQSE() + "'" +
            ", vigueur='" + getVigueur() + "'" +
            "}";
    }
}
