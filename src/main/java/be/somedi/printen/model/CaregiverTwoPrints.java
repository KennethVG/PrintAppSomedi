package be.somedi.printen.model;

public enum CaregiverTwoPrints {

    S4183("Van Olmen"), S7872("Wildiers"), S6339("Christiaens"), S6222("Ilegems"), S7686("Van den Bogaert");

    private String name;

    CaregiverTwoPrints(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
