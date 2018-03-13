package be.somedi.printen.entity;

import javax.persistence.*;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String inss;

    public Long getId() {
        return id;
    }

    public String getInss() {
        return inss;
    }
}
