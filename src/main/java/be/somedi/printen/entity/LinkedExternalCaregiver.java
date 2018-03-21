package be.somedi.printen.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@IdClass(LinkedExternalCaregiverPk.class)
@Table(name="dbo.Communication_LinkedExternalCaregiver")
public class LinkedExternalCaregiver {

    @Id
    private String externalId;
    @Id
    private String linkedId;

    public String getExternalId() {
        return externalId;
    }

    public String getLinkedId() {
        return linkedId;
    }
}

class LinkedExternalCaregiverPk implements Serializable {
    String externalId;
    String linkedId;
}
