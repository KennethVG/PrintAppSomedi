package be.somedi.printen.entity;

import javax.persistence.*;

@Entity
@Table(name = "dbo.Global_Patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String externalId;

    @Column(name = "person_id")
    private Long personId;

    public Long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public Long getPersonId() {
        return personId;
    }
}