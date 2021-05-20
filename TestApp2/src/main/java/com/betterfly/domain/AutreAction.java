package com.betterfly.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A AutreAction.
 */
@Entity
@Table(name = "autre_action")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "autreaction")
public class AutreAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "origine_action")
    private String origineAction;

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

    public AutreAction id(Long id) {
        this.id = id;
        return this;
    }

    public String getOrigineAction() {
        return this.origineAction;
    }

    public AutreAction origineAction(String origineAction) {
        this.origineAction = origineAction;
        return this;
    }

    public void setOrigineAction(String origineAction) {
        this.origineAction = origineAction;
    }

    public String getOrigine() {
        return this.origine;
    }

    public AutreAction origine(String origine) {
        this.origine = origine;
        return this;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Action getAction() {
        return this.action;
    }

    public AutreAction action(Action action) {
        this.setAction(action);
        return this;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getDelegue() {
        return this.delegue;
    }

    public AutreAction delegue(User user) {
        this.setDelegue(user);
        return this;
    }

    public void setDelegue(User user) {
        this.delegue = user;
    }

    public ProcessusSMI getProcessus() {
        return this.processus;
    }

    public AutreAction processus(ProcessusSMI processusSMI) {
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
        if (!(o instanceof AutreAction)) {
            return false;
        }
        return id != null && id.equals(((AutreAction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AutreAction{" +
            "id=" + getId() +
            ", origineAction='" + getOrigineAction() + "'" +
            ", origine='" + getOrigine() + "'" +
            "}";
    }
}
