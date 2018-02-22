package be.somedi.printen.printapp.util;

public enum DeleteItems {

    GEEN_VERSLAG("Geen verslag van specialist."), MAG_WEG("mag weg"), NIET_STUREN("niet sturen huisarts"),
    VRAAG_SPECIALIST("na vraag aan de specialist geen verslag beschikbaar"), PN("P.N."), BIJLAGE("Bijlage dossier " +
            "intern"), BRIEF_MAG_WEG("Deze brief mag weg"), VUL_AAN("vul_aan");


    private String text;

    DeleteItems(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
