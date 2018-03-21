package be.somedi.printen.util;

import be.somedi.printen.model.UMFormat;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;

import static be.somedi.printen.util.TxtUtil.countNumberOfLines;
import static org.junit.Assert.*;

public class TxtUtilTest {

    @Test
    public void testGeenVerslag() throws IOException {
        ClassPathResource resNotToPrint = new ClassPathResource("geenVerslag.txt");
        Path path = resNotToPrint.getFile().toPath();
        boolean toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        assertTrue(toPrint);
    }

    @Test
    public void testLetterToPrint() throws IOException {
        ClassPathResource resToPrint = new ClassPathResource("print.txt");
        Path path = resToPrint.getFile().toPath();
        boolean toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        assertFalse(toPrint);
    }

    @Test
    public void testLetterToPatient() throws IOException {
        ClassPathResource resNotToPrint = new ClassPathResource("patient.txt");
        Path path = resNotToPrint.getFile().toPath();
        boolean toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        assertTrue(toPrint);
    }

    @Test
    public void testVulAan() throws IOException {
        ClassPathResource resNotToPrint = new ClassPathResource("vulAan.txt");
        Path path = resNotToPrint.getFile().toPath();
        boolean toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        assertTrue(toPrint);
    }

    @Test
    public void testGetBody() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("print.txt");
        Path path = txtFile.getFile().toPath();
        String result = TxtUtil.getBodyOfTxt(path, UMFormat.MEDIDOC);
        assertFalse(result.isEmpty());

        String expected = "Betreft : uw pati~ent(e) Florentie Agnes geboren op 30/07/1950   en wonende\n" +
                "Sint Jansstraat 39/3 te 3118 Werchter.\n" +
                "\n" +
                "Consultatie : 21/02/2018 met referentienr : 183150264\n" +
                "\n" +
                "* BESPREKING:\n" +
                "Gunstige evolutie.\n" +
                "Pati~ente mag progressief haar activiteiten opdrijven. Dit is normaal dat\n" +
                " zij";

        assertEquals(expected, result);
    }

    @Test
    public void testGetBodyWithLongLine() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("MSE_182670160_2976684_A9564.txt");
        Path path = txtFile.getFile().toPath();
        String result = TxtUtil.getBodyOfTxt(path, UMFormat.MEDIDOC);
        String expected = "Betreft : uw patiënt(e) Van Hool Theodora geboren op 30/06/1954 \n" +
                " en wonende Dr.J.Vermylenstraat 14 te 2223 Schriek. \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670160 \n" +
                "\n" +
                "\n" +
                "Deze brief zou ook naar Jamar moete gaan. Wordt hier aparte XML voor \n" +
                "aangemaakt?";
        assertEquals(expected, result);
    }

    @Test
    public void testGetBodyWithSummaryMedidoc() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("besluit.txt");
        Path path = txtFile.getFile().toPath();
        String result = TxtUtil.getBodyOfTxt(path, UMFormat.MEDIDOC);
        String expected = "Betreft :Â uw patiÃ«nt(e) Van Vliet Alexandra geboren op 11/10/2006Â   en\n" +
                " wonende\n" +
                "Kerkhofstraat 43 te 2220 Heist-op-den-Berg.Â \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670181\n" +
                "\n" +
                "* ANAMNESE:\n" +
                "Â klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "Â klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "Â klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "\n" +
                " * klinisch onderzoekklinisch onderzoek\n" +
                "\n" +
                " * BESLUIT:\n" +
                "]blablablabla\n" +
                "]blablablablablablablablablablablablabla blablablablablablablablablabl\n" +
                "] ablablablablablablablablabl ablablablablablablab labla\n" +
                "]Einde besluit";
        assertEquals(expected, result);
    }

    @Test
    public void testGetBodyWithSummaryMedar() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("besluit.txt");
        Path path = txtFile.getFile().toPath();
        String result = TxtUtil.getBodyOfTxt(path, UMFormat.MEDAR);
        assertEquals("Betreft :Â uw patiÃ«nt(e) Van Vliet Alexandra geboren op 11/10/2006Â   en\n" +
                " wonende\n" +
                "Kerkhofstraat 43 te 2220 Heist-op-den-Berg.Â \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670181\n" +
                "\n" +
                "* ANAMNESE:\n" +
                "Â klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "Â klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "Â klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "\n" +
                " * klinisch onderzoekklinisch onderzoek\n" +
                "\n" +
                "/CONCL\n" +
                " * BESLUIT:\n" +
                " blablablabla\n" +
                " blablablablablablablablablablablablabla blablablablablablablablablabl\n" +
                " ablablablablablablablablabl ablablablablablablab labla\n" +
                " Einde besluit", result);
    }

    @Test
    public void testGetBodyWithLongSummaryMedidoc() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("langBesluit.txt");
        Path path = txtFile.getFile().toPath();
        String result = TxtUtil.getBodyOfTxt(path, UMFormat.MEDIDOC);
        String expected = "Betreft :Â uw patiÃ«nt(e) Van Vliet Alexandra geboren op 11/10/2006Â   en\n" +
                " wonende\n" +
                "Kerkhofstraat 43 te 2220 Heist-op-den-Berg.Â \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670181\n" +
                "\n" +
                "* ANAMNESE:\n" +
                "Â klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "Â klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "Â klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "\n" +
                " * klinisch onderzoekklinisch onderzoek\n" +
                "\n" +
                " * BESLUIT:\n" +
                "]blablablabla\n" +
                "]blablablablablablablablablablablablabla blablablablablablablablablabl\n" +
                "] ablablablablablablablablabl ablablablablablablab labla\n" +
                "]Dit is een heel\n" +
                "]maar echt heeel\n" +
                "]heel\n" +
                "]heel\n" +
                " lang\n" +
                " besluit. OK!\n" +
                " Einde besluit";
        assertEquals(expected, result);
    }

    @Test
    public void testGetMnemonic() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("MSE_182670181_2976687_A4407.txt");
        Path path = txtFile.getFile().toPath();
        String mnemnonic = TxtUtil.getMnemnonic(path);
        assertEquals("A4407", mnemnonic);
    }

    @Test
    public void testGetTextAfterKeyword() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("MSE_182670181_2976687_A4407.txt");
        Path path = txtFile.getFile().toPath();

        String pc = TxtUtil.getExternalIdAfterPC(path);
        assertEquals("V1416402", pc);

        String ua = TxtUtil.getMnemonicAfterUA(path);
        assertEquals("A6979", ua);

        String pn = TxtUtil.getNameAfterPN(path);
        assertEquals("Van Vliet", pn);

        String pv = TxtUtil.getFirstNameAfterPV(path);
        assertEquals("Alexandra", pv);

        String ps = TxtUtil.getStreetWithNumberAfterPS(path);
        assertEquals("Kerkhofstraat 43", ps);

        String pp = TxtUtil.getZipCodeAfterPP(path);
        assertEquals("2220", pp);

        String pa = TxtUtil.getCityAfterPA(path);
        assertEquals("Heist-op-den-Berg", pa);

        String pd = TxtUtil.getBirthDateAtferPD(path);
        assertEquals("11102006", pd);

        String ud = TxtUtil.getDateOfResearchAfterUD(path);
        assertEquals("04012018", ud);
    }

    @Test
    public void testCountNumberOfLines() {
        String doc = "Hello World" +
                "\nHello Mars " +
                "\nHello Everyone " +
                "\nHello myself";
        assertEquals(4L, countNumberOfLines(doc));
    }

}
