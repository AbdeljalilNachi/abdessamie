package com.betterfly.domain;

import com.betterfly.domain.enumeration.Mois;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A ResultatIndicateur.
 */
@Entity
@Table(name = "resultat_indicateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "resultatindicateur")
public class ResultatIndicateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "mois")
    private Mois mois;

    @Column(name = "cible")
    private Float cible;

    @Column(name = "resultat")
    private Float resultat;

    @ManyToOne
    @JsonIgnoreProperties(value = { "resultats", "indicateur" }, allowSetters = true)
    private ResultIndicateurs resultIndicateurs;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResultatIndicateur id(Long id) {
        this.id = id;
        return this;
    }

    public Mois getMois() {
        return this.mois;
    }

    public ResultatIndicateur mois(Mois mois) {
        this.mois = mois;
        return this;
    }

    public void setMois(Mois mois) {
        this.mois = mois;
    }

    public Float getCible() {
        return this.cible;
    }

    public ResultatIndicateur cible(Float cible) {
        this.cible = cible;
        return this;
    }

    public void setCible(Float cible) {
        this.cible = cible;
    }

    public Float getResultat() {
        return this.resultat;
    }

    public ResultatIndicateur resultat(Float resultat) {
        this.resultat = resultat;
        return this;
    }

    public void setResultat(Float resultat) {
        this.resultat = resultat;
    }

    public ResultIndicateurs getResultIndicateurs() {
        return this.resultIndicateurs;
    }

    public ResultatIndicateur resultIndicateurs(ResultIndicateurs resultIndicateurs) {
        this.setResultIndicateurs(resultIndicateurs);
        return this;
    }

    public void setResultIndicateurs(ResultIndicateurs resultIndicateurs) {
        this.resultIndicateurs = resultIndicateurs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultatIndicateur)) {
            return false;
        }
        return id != null && id.equals(((ResultatIndicateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultatIndicateur{" +
            "id=" + getId() +
            ", mois='" + getMois() + "'" +
            ", cible=" + getCible() +
            ", resultat=" + getResultat() +
            "}";
    }
}
