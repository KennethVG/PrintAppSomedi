package be.somedi.printen.model;

public enum Gender {

    MALE("Y"), FEMALE("X"), UNDEFINED("Z");

    private String abbreviation;

    Gender(String abbreviation){
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
