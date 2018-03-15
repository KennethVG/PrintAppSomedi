package be.somedi.printen.model;

public enum PrintProtocols {

    JA(true), NEE(false);

    private final boolean value;

    PrintProtocols(boolean value) {
        this.value = value;
    }

    public boolean wenstPapierenProtocols() {
        return value;
    }
}
