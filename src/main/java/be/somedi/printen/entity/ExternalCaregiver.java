package be.somedi.printen.entity;

import be.somedi.printen.model.UMFormat;

import javax.persistence.*;

@Entity
@Table(name = "dbo.Communication_ExternalCaregiver")
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
    private String title;
    private Boolean printProtocols;
    private String copyToExternalID;
    private String nihiiAddress;

    @Enumerated(EnumType.STRING)
    private UMFormat format;

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

    public String getTitle() {
        return title;
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

    public String getNihiiAddress() {
        return nihiiAddress;
    }

    public UMFormat getFormat() {
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
                ", title='" + title + '\'' +
                ", printProtocols=" + printProtocols +
                ", copyToExternalID='" + copyToExternalID + '\'' +
                ", nihiiAddress='" + nihiiAddress + '\'' +
                ", format=" + format +
                '}';
    }
}
