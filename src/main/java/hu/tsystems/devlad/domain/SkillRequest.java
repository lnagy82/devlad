package hu.tsystems.devlad.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import hu.tsystems.devlad.domain.enumeration.SkillRequestStatus;

/**
 * A SkillRequest.
 */
@Entity
@Table(name = "skill_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SkillRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @NotNull
    @Column(name = "requestor", nullable = false)
    private String requestor;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SkillRequestStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public SkillRequest created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getRequestor() {
        return requestor;
    }

    public SkillRequest requestor(String requestor) {
        this.requestor = requestor;
        return this;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getDescription() {
        return description;
    }

    public SkillRequest description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SkillRequestStatus getStatus() {
        return status;
    }

    public SkillRequest status(SkillRequestStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(SkillRequestStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SkillRequest skillRequest = (SkillRequest) o;
        if (skillRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, skillRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SkillRequest{" +
            "id=" + id +
            ", created='" + created + "'" +
            ", requestor='" + requestor + "'" +
            ", description='" + description + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
