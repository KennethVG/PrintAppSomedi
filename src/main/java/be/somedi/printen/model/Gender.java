package be.somedi.printen.model;

public enum Gender {

    M("Y"), V("X"), U("Z");

    private final String medidocGender;

    Gender(String medidocGender){
        this.medidocGender = medidocGender;
    }

    public String getMedidocGender() {
        return medidocGender;
    }
}
