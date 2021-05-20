package com.betterfly.domain;

import com.betterfly.domain.enumeration.TypeConstatAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A ConstatAudit.
 */
@Entity
@Table(name = "constat_audit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "constataudit")
public class ConstatAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeConstatAudit type;

    @Column(name = "constat")
    private String constat;

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
    @JsonIgnoreProperties(value = { "processus", "auditeur" }, allowSetters = true)
    private Audit audit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConstatAudit id(Long id) {
        this.id = id;
        return this;
    }

    public TypeConstatAudit getType() {
        return this.type;
    }

    public ConstatAudit type(TypeConstatAudit type) {
        this.type = type;
        return this;
    }

    public void setType(TypeConstatAudit type) {
        this.type = type;
    }

    public String getConstat() {
        return this.constat;
    }

    public ConstatAudit constat(String constat) {
        this.constat = constat;
        return this;
    }

    public void setConstat(String constat) {
        this.constat = constat;
    }

    public String getOrigine() {
        return this.origine;
    }

    public ConstatAudit origine(String origine) {
        this.origine = origine;
        return this;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Action getAction() {
        return this.action;
    }

    public ConstatAudit action(Action action) {
        this.setAction(action);
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getDelegue() {
        return this.delegue;
    }

    public ConstatAudit delegue(User user) {
        this.setDelegue(user);
        return this;
    }

    public void setDelegue(User user) {
        this.delegue = user;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public ConstatAudit processus(ProcessusSMI processusSMI) {
        this.setProcessus(processusSMI);
        return this;
    }

    public void setProcessus(ProcessusSMI processusSMI) {
        this.processus = processusSMI;
    }

    public Audit getAudit() {
        return this.audit;
    }

    public ConstatAudit audit(Audit audit) {
        this.setAudit(audit);
        return this;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstatAudit)) {
            return false;
        }
        return id != null && id.equals(((ConstatAudit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConstatAudit{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", constat='" + getConstat() + "'" +
            ", origine='" + getOrigine() + "'" +
            "}";
    }
}
