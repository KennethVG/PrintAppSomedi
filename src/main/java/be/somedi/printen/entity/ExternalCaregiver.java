package be.somedi.printen.entity;

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
    private String nihii;
    private String zip;
    private String city;
    private String streetWithNumber;
    private String phone;
    private Boolean printProtocols;
    private String copyToExternalID;
    private String format;

    public Long getId() {
        return id;
    }

    public String getExternalID() {
        return externalID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNihii() {
        return nihii;
    }

    public String getZip() {
        return zip;
    }

    public String getCity() {
        return city;
    }

    public String getStreetWithNumber() {
        return streetWithNumber;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getPrintProtocols() {
        return printProtocols;
    }

    public void setPrintProtocols(Boolean printProtocols) {
        this.printProtocols = printProtocols;
    }

    public String getCopyToExternalID() {
        return copyToExternalID;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String toString() {
        return "ExternalCaregiver{" +
                "id=" + id +
                ", externalID='" + externalID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nihii='" + nihii + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", streetWithNumber='" + streetWithNumber + '\'' +
                ", phone='" + phone + '\'' +
                ", printProtocols=" + printProtocols +
                ", copyToExternalID='" + copyToExternalID + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}
