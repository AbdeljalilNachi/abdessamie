package com.betterfly.domain;

import com.betterfly.domain.enumeration.Standard;
import com.betterfly.domain.enumeration.StatutAudit;
import com.betterfly.domain.enumeration.TypeAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A Audit.
 */
@Entity
@Table(name = "audit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "audit")
public class Audit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date_audit")
    private LocalDate dateAudit;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_audit")
    private TypeAudit typeAudit;

    @Enumerated(EnumType.STRING)
    @Column(name = "standard")
    private Standard standard;

    @Column(name = "i_d")
    private String iD;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutAudit statut;

    @Column(name = "conclusion")
    private String conclusion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pilote" }, allowSetters = true)
    private ProcessusSMI processus;

    @ManyToOne
    private User auditeur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Audit id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDateAudit() {
        return this.dateAudit;
    }

    public Audit dateAudit(LocalDate dateAudit) {
        this.dateAudit = dateAudit;
        return this;
    }

    public void setDateAudit(LocalDate dateAudit) {
        this.dateAudit = dateAudit;
    }

    public TypeAudit getTypeAudit() {
        return this.typeAudit;
    }

    public Audit typeAudit(TypeAudit typeAudit) {
        this.typeAudit = typeAudit;
        return this;
    }

    public void setTypeAudit(TypeAudit typeAudit) {
        this.typeAudit = typeAudit;
    }

    public Standard getStandard() {
        return this.standard;
    }

    public Audit standard(Standard standard) {
        this.standard = standard;
        return this;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public String getiD() {
        return this.iD;
    }

    public Audit iD(String iD) {
        this.iD = iD;
        return this;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public StatutAudit getStatut() {
        return this.statut;
    }

    public Audit statut(StatutAudit statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(StatutAudit statut) {
        this.statut = statut;
    }

    public String getConclusion() {
        return this.conclusion;
    }

    public Audit conclusion(String conclusion) {
        this.conclusion = conclusion;
        return this;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public Audit processus(ProcessusSMI processusSMI) {
        this.setProcessus(processusSMI);
        return this;
    }

    public void setProcessus(ProcessusSMI processusSMI) {
        this.processus = processusSMI;
    }

    public User getAuditeur() {
        return this.auditeur;
    }

    public Audit auditeur(User user) {
        this.setAuditeur(user);
        return this;
    }

    public void setAuditeur(User user) {
        this.auditeur = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Audit)) {
            return false;
        }
        return id != null && id.equals(((Audit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Audit{" +
            "id=" + getId() +
            ", dateAudit='" + getDateAudit() + "'" +
            ", typeAudit='" + getTypeAudit() + "'" +
            ", standard='" + getStandard() + "'" +
            ", iD='" + getiD() + "'" +
            ", statut='" + getStatut() + "'" +
            ", conclusion='" + getConclusion() + "'" +
            "}";
    }
}
