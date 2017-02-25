package hu.tsystems.devlad.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A LearnedSkill.
 */
@Entity
@Table(name = "learned_skill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LearnedSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "learned", nullable = false)
    private ZonedDateTime learned;

    @NotNull
    @Column(name = "signed", nullable = false)
    private String signed;

    @ManyToOne(optional = false)
    @NotNull
    private Developer developer;

    @ManyToOne(optional = false)
    @NotNull
    private Skill skill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getLearned() {
        return learned;
    }

    public LearnedSkill learned(ZonedDateTime learned) {
        this.learned = learned;
        return this;
    }

    public void setLearned(ZonedDateTime learned) {
        this.learned = learned;
    }

    public String getSigned() {
        return signed;
    }

    public LearnedSkill signed(String signed) {
        this.signed = signed;
        return this;
    }

    public void setSigned(String signed) {
        this.signed = signed;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public LearnedSkill developer(Developer developer) {
        this.developer = developer;
        return this;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public Skill getSkill() {
        return skill;
    }

    public LearnedSkill skill(Skill skill) {
        this.skill = skill;
        return this;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LearnedSkill learnedSkill = (LearnedSkill) o;
        if (learnedSkill.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, learnedSkill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LearnedSkill{" +
            "id=" + id +
            ", learned='" + learned + "'" +
            ", signed='" + signed + "'" +
            '}';
    }
}
