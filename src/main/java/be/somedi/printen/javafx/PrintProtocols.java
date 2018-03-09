package be.somedi.printen.javafx;

public enum PrintProtocols {

    JA(true), NEE(false);

    private boolean value;

    PrintProtocols(boolean value) {
        this.value = value;
    }

    public boolean wenstPapierenProtocols() {
        return value;
    }
}
