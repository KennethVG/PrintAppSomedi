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
    private String nihii;
    private String zip;
    private String city;
    private String streetWithNumber;
    private String phone;
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

    public String getNihii() {
        return nihii;
    }

    public void setNihii(String nihii) {
        this.nihii = nihii;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetWithNumber() {
        return streetWithNumber;
    }

    public void setStreetWithNumber(String streetWithNumber) {
        this.streetWithNumber = streetWithNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
                ", nihii='" + nihii + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", streetWithNumber='" + streetWithNumber + '\'' +
                ", phone='" + phone + '\'' +
                ", printProtocols=" + printProtocols +
                '}';
    }
}
