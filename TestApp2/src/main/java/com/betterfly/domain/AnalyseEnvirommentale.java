package com.betterfly.domain;

import com.betterfly.domain.enumeration.EnumFive;
import com.betterfly.domain.enumeration.Situation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A AnalyseEnvirommentale.
 */
@Entity
@Table(name = "analyse_envirommentale")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "analyseenvirommentale")
public class AnalyseEnvirommentale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "business_unit")
    private String businessUnit;

    @Column(name = "activite")
    private String activite;

    @Column(name = "aspect_environnemental")
    private String aspectEnvironnemental;

    @Column(name = "impact_environnemental")
    private String impactEnvironnemental;

    @Column(name = "competences_requises")
    private String competencesRequises;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation")
    private Situation situation;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequence")
    private EnumFive frequence;

    @Enumerated(EnumType.STRING)
    @Column(name = "sensibilite_milieu")
    private EnumFive sensibiliteMilieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "coefficient_maitrise")
    private EnumFive coefficientMaitrise;

    @Enumerated(EnumType.STRING)
    @Column(name = "gravite")
    private EnumFive gravite;

    @Column(name = "criticite")
    private Integer criticite;

    @Column(name = "maitrise_existante")
    private String maitriseExistante;

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

    public AnalyseEnvirommentale id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public AnalyseEnvirommentale date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBusinessUnit() {
        return this.businessUnit;
    }

    public AnalyseEnvirommentale businessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
        return this;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getActivite() {
        return this.activite;
    }

    public AnalyseEnvirommentale activite(String activite) {
        this.activite = activite;
        return this;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getAspectEnvironnemental() {
        return this.aspectEnvironnemental;
    }

    public AnalyseEnvirommentale aspectEnvironnemental(String aspectEnvironnemental) {
        this.aspectEnvironnemental = aspectEnvironnemental;
        return this;
    }

    public void setAspectEnvironnemental(String aspectEnvironnemental) {
        this.aspectEnvironnemental = aspectEnvironnemental;
    }

    public String getImpactEnvironnemental() {
        return this.impactEnvironnemental;
    }

    public AnalyseEnvirommentale impactEnvironnemental(String impactEnvironnemental) {
        this.impactEnvironnemental = impactEnvironnemental;
        return this;
    }

    public void setImpactEnvironnemental(String impactEnvironnemental) {
        this.impactEnvironnemental = impactEnvironnemental;
    }

    public String getCompetencesRequises() {
        return this.competencesRequises;
    }

    public AnalyseEnvirommentale competencesRequises(String competencesRequises) {
        this.competencesRequises = competencesRequises;
        return this;
    }

    public void setCompetencesRequises(String competencesRequises) {
        this.competencesRequises = competencesRequises;
    }

    public Situation getSituation() {
        return this.situation;
    }

    public AnalyseEnvirommentale situation(Situation situation) {
        this.situation = situation;
        return this;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public EnumFive getFrequence() {
        return this.frequence;
    }

    public AnalyseEnvirommentale frequence(EnumFive frequence) {
        this.frequence = frequence;
        return this;
    }

    public void setFrequence(EnumFive frequence) {
        this.frequence = frequence;
    }

    public EnumFive getSensibiliteMilieu() {
        return this.sensibiliteMilieu;
    }

    public AnalyseEnvirommentale sensibiliteMilieu(EnumFive sensibiliteMilieu) {
        this.sensibiliteMilieu = sensibiliteMilieu;
        return this;
    }

    public void setSensibiliteMilieu(EnumFive sensibiliteMilieu) {
        this.sensibiliteMilieu = sensibiliteMilieu;
    }

    public EnumFive getCoefficientMaitrise() {
        return this.coefficientMaitrise;
    }

    public AnalyseEnvirommentale coefficientMaitrise(EnumFive coefficientMaitrise) {
        this.coefficientMaitrise = coefficientMaitrise;
        return this;
    }

    public void setCoefficientMaitrise(EnumFive coefficientMaitrise) {
        this.coefficientMaitrise = coefficientMaitrise;
    }

    public EnumFive getGravite() {
        return this.gravite;
    }

    public AnalyseEnvirommentale gravite(EnumFive gravite) {
        this.gravite = gravite;
        return this;
    }

    public void setGravite(EnumFive gravite) {
        this.gravite = gravite;
    }

    public Integer getCriticite() {
        return this.criticite;
    }

    public AnalyseEnvirommentale criticite(Integer criticite) {
        this.criticite = criticite;
        return this;
    }

    public void setCriticite(Integer criticite) {
        this.criticite = criticite;
    }

    public String getMaitriseExistante() {
        return this.maitriseExistante;
    }

    public AnalyseEnvirommentale maitriseExistante(String maitriseExistante) {
        this.maitriseExistante = maitriseExistante;
        return this;
    }

    public void setMaitriseExistante(String maitriseExistante) {
        this.maitriseExistante = maitriseExistante;
    }

    public String getOrigine() {
        return this.origine;
    }

    public AnalyseEnvirommentale origine(String origine) {
        this.origine = origine;
        return this;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Action getAction() {
        return this.action;
    }

    public AnalyseEnvirommentale action(Action action) {
        this.setAction(action);
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getDelegue() {
        return this.delegue;
    }

    public AnalyseEnvirommentale delegue(User user) {
        this.setDelegue(user);
        return this;
    }

    public void setDelegue(User user) {
        this.delegue = user;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public AnalyseEnvirommentale processus(ProcessusSMI processusSMI) {
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
        if (!(o instanceof AnalyseEnvirommentale)) {
            return false;
        }
        return id != null && id.equals(((AnalyseEnvirommentale) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnalyseEnvirommentale{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", businessUnit='" + getBusinessUnit() + "'" +
            ", activite='" + getActivite() + "'" +
            ", aspectEnvironnemental='" + getAspectEnvironnemental() + "'" +
            ", impactEnvironnemental='" + getImpactEnvironnemental() + "'" +
            ", competencesRequises='" + getCompetencesRequises() + "'" +
            ", situation='" + getSituation() + "'" +
            ", frequence='" + getFrequence() + "'" +
            ", sensibiliteMilieu='" + getSensibiliteMilieu() + "'" +
            ", coefficientMaitrise='" + getCoefficientMaitrise() + "'" +
            ", gravite='" + getGravite() + "'" +
            ", criticite=" + getCriticite() +
            ", maitriseExistante='" + getMaitriseExistante() + "'" +
            ", origine='" + getOrigine() + "'" +
            "}";
    }
}
