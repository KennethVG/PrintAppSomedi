package be.somedi.printen.printapp.model;

import javax.persistence.*;

@Entity
@Table(name = "externalcaregiver")
public class ExternalCaregiver {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String externalID;

    private String firstName;

    private String lastName;

    private Boolean printProtocols;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getPrintProtocols() {
        return printProtocols;
    }

    public void setPrintProtocols(Boolean printProtocols) {
        this.printProtocols = printProtocols;
    }

    @Override
    public String toString() {
        return "ExternalCaregiver{" +
                "id=" + id +
                ", externalID='" + externalID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", printProtocols=" + printProtocols +
                '}';
    }
}
