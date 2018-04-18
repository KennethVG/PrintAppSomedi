package be.somedi.printen.entity;

import javax.persistence.*;

@Entity
@Table(name = "dbo.PersonalInfo_Person")
//@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    private String inss;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getId() {
        return id;
    }

    public String getInss() {
        return inss;
    }
}