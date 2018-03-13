package be.somedi.printen.entity;

import javax.persistence.*;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String externalId;

    @Column(name = "person_id")
    private int personId;

    public Long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public int getPersonId() {
        return personId;
    }
}
