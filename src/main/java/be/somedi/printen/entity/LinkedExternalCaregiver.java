package be.somedi.printen.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@IdClass(LinkedExternalCaregiverPk.class)
@Table(name="dbo.Communication_LinkedExternalCaregiver")
public class LinkedExternalCaregiver {

    @Id
    @Column(unique = true)
    private String externalId;
    @Id
    private String linkedId;

    public String getExternalId() {
        return externalId;
    }

    public String getLinkedId() {
        return linkedId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }
}

class LinkedExternalCaregiverPk implements Serializable {
    private String externalId;
    private String linkedId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedExternalCaregiverPk that = (LinkedExternalCaregiverPk) o;
        return Objects.equals(externalId, that.externalId) &&
                Objects.equals(linkedId, that.linkedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalId, linkedId);
    }
}
