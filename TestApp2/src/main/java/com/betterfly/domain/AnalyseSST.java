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
 * A AnalyseSST.
 */
@Entity
@Table(name = "analyse_sst")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "analysesst")
public class AnalyseSST implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "buisness_unit")
    private String buisnessUnit;

    @Column(name = "unite_travail")
    private String uniteTravail;

    @Column(name = "danger")
    private String danger;

    @Column(name = "risque")
    private String risque;

    @Column(name = "competence")
    private String competence;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation")
    private Situation situation;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequence")
    private EnumFive frequence;

    @Enumerated(EnumType.STRING)
    @Column(name = "duree_exposition")
    private EnumFive dureeExposition;

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

    public AnalyseSST id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public AnalyseSST date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBuisnessUnit() {
        return this.buisnessUnit;
    }

    public AnalyseSST buisnessUnit(String buisnessUnit) {
        this.buisnessUnit = buisnessUnit;
        return this;
    }

    public void setBuisnessUnit(String buisnessUnit) {
        this.buisnessUnit = buisnessUnit;
    }

    public String getUniteTravail() {
        return this.uniteTravail;
    }

    public AnalyseSST uniteTravail(String uniteTravail) {
        this.uniteTravail = uniteTravail;
        return this;
    }

    public void setUniteTravail(String uniteTravail) {
        this.uniteTravail = uniteTravail;
    }

    public String getDanger() {
        return this.danger;
    }

    public AnalyseSST danger(String danger) {
        this.danger = danger;
        return this;
    }

    public void setDanger(String danger) {
        this.danger = danger;
    }

    public String getRisque() {
        return this.risque;
    }

    public AnalyseSST risque(String risque) {
        this.risque = risque;
        return this;
    }

    public void setRisque(String risque) {
        this.risque = risque;
    }

    public String getCompetence() {
        return this.competence;
    }

    public AnalyseSST competence(String competence) {
        this.competence = competence;
        return this;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public Situation getSituation() {
        return this.situation;
    }

    public AnalyseSST situation(Situation situation) {
        this.situation = situation;
        return this;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public EnumFive getFrequence() {
        return this.frequence;
    }

    public AnalyseSST frequence(EnumFive frequence) {
        this.frequence = frequence;
        return this;
    }

    public void setFrequence(EnumFive frequence) {
        this.frequence = frequence;
    }

    public EnumFive getDureeExposition() {
        return this.dureeExposition;
    }

    public AnalyseSST dureeExposition(EnumFive dureeExposition) {
        this.dureeExposition = dureeExposition;
        return this;
    }

    public void setDureeExposition(EnumFive dureeExposition) {
        this.dureeExposition = dureeExposition;
    }

    public EnumFive getCoefficientMaitrise() {
        return this.coefficientMaitrise;
    }

    public AnalyseSST coefficientMaitrise(EnumFive coefficientMaitrise) {
        this.coefficientMaitrise = coefficientMaitrise;
        return this;
    }

    public void setCoefficientMaitrise(EnumFive coefficientMaitrise) {
        this.coefficientMaitrise = coefficientMaitrise;
    }

    public EnumFive getGravite() {
        return this.gravite;
    }

    public AnalyseSST gravite(EnumFive gravite) {
        this.gravite = gravite;
        return this;
    }

    public void setGravite(EnumFive gravite) {
        this.gravite = gravite;
    }

    public Integer getCriticite() {
        return this.criticite;
    }

    public AnalyseSST criticite(Integer criticite) {
        this.criticite = criticite;
        return this;
    }

    public void setCriticite(Integer criticite) {
        this.criticite = criticite;
    }

    public String getMaitriseExistante() {
        return this.maitriseExistante;
    }

    public AnalyseSST maitriseExistante(String maitriseExistante) {
        this.maitriseExistante = maitriseExistante;
        return this;
    }

    public void setMaitriseExistante(String maitriseExistante) {
        this.maitriseExistante = maitriseExistante;
    }

    public String getOrigine() {
        return this.origine;
    }

    public AnalyseSST origine(String origine) {
        this.origine = origine;
        return this;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Action getAction() {
        return this.action;
    }

    public AnalyseSST action(Action action) {
        this.setAction(action);
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getDelegue() {
        return this.delegue;
    }

    public AnalyseSST delegue(User user) {
        this.setDelegue(user);
        return this;
    }

    public void setDelegue(User user) {
        this.delegue = user;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public AnalyseSST processus(ProcessusSMI processusSMI) {
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
        if (!(o instanceof AnalyseSST)) {
            return false;
        }
        return id != null && id.equals(((AnalyseSST) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnalyseSST{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", buisnessUnit='" + getBuisnessUnit() + "'" +
            ", uniteTravail='" + getUniteTravail() + "'" +
            ", danger='" + getDanger() + "'" +
            ", risque='" + getRisque() + "'" +
            ", competence='" + getCompetence() + "'" +
            ", situation='" + getSituation() + "'" +
            ", frequence='" + getFrequence() + "'" +
            ", dureeExposition='" + getDureeExposition() + "'" +
            ", coefficientMaitrise='" + getCoefficientMaitrise() + "'" +
            ", gravite='" + getGravite() + "'" +
            ", criticite=" + getCriticite() +
            ", maitriseExistante='" + getMaitriseExistante() + "'" +
            ", origine='" + getOrigine() + "'" +
            "}";
    }
}
