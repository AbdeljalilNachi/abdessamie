package com.betterfly.domain;

import com.betterfly.domain.enumeration.TypeProcessus;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * A ProcessusSMI.
 */
@Entity
@Table(name = "processus_smi")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "processussmi")
public class ProcessusSMI implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "processus")
    private String processus;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "version")
    private Integer version;

    @Column(name = "finalite")
    private String finalite;

    @Lob
    @Column(name = "fiche_processus")
    private byte[] ficheProcessus;

    @Column(name = "fiche_processus_content_type")
    private String ficheProcessusContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeProcessus type;

    @Column(name = "vigueur")
    private Boolean vigueur;

    @ManyToOne
    private User pilote;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessusSMI id(Long id) {
        this.id = id;
        return this;
    }

    public String getProcessus() {
        return this.processus;
    }

    public ProcessusSMI processus(String processus) {
        this.processus = processus;
        return this;
    }

    public void setProcessus(String processus) {
        this.processus = processus;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public ProcessusSMI date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getVersion() {
        return this.version;
    }

    public ProcessusSMI version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getFinalite() {
        return this.finalite;
    }

    public ProcessusSMI finalite(String finalite) {
        this.finalite = finalite;
        return this;
    }

    public void setFinalite(String finalite) {
        this.finalite = finalite;
    }

    public byte[] getFicheProcessus() {
        return this.ficheProcessus;
    }

    public ProcessusSMI ficheProcessus(byte[] ficheProcessus) {
        this.ficheProcessus = ficheProcessus;
        return this;
    }

    public void setFicheProcessus(byte[] ficheProcessus) {
        this.ficheProcessus = ficheProcessus;
    }

    public String getFicheProcessusContentType() {
        return this.ficheProcessusContentType;
    }

    public ProcessusSMI ficheProcessusContentType(String ficheProcessusContentType) {
        this.ficheProcessusContentType = ficheProcessusContentType;
        return this;
    }

    public void setFicheProcessusContentType(String ficheProcessusContentType) {
        this.ficheProcessusContentType = ficheProcessusContentType;
    }

    public TypeProcessus getType() {
        return this.type;
    }

    public ProcessusSMI type(TypeProcessus type) {
        this.type = type;
        return this;
    }

    public void setType(TypeProcessus type) {
        this.type = type;
    }

    public Boolean getVigueur() {
        return this.vigueur;
    }

    public ProcessusSMI vigueur(Boolean vigueur) {
        this.vigueur = vigueur;
        return this;
    }

    public void setVigueur(Boolean vigueur) {
        this.vigueur = vigueur;
    }

    public User getPilote() {
        return this.pilote;
    }

    public ProcessusSMI pilote(User user) {
        this.setPilote(user);
        return this;
    }

    public void setPilote(User user) {
        this.pilote = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessusSMI)) {
            return false;
        }
        return id != null && id.equals(((ProcessusSMI) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcessusSMI{" +
            "id=" + getId() +
            ", processus='" + getProcessus() + "'" +
            ", date='" + getDate() + "'" +
            ", version=" + getVersion() +
            ", finalite='" + getFinalite() + "'" +
            ", ficheProcessus='" + getFicheProcessus() + "'" +
            ", ficheProcessusContentType='" + getFicheProcessusContentType() + "'" +
            ", type='" + getType() + "'" +
            ", vigueur='" + getVigueur() + "'" +
            "}";
    }
}
