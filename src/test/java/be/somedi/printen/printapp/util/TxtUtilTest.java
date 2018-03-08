package be.somedi.printen.printapp.util;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class TxtUtilTest {

    @Test
    public void testGeenVerslag() throws IOException {
        ClassPathResource resNotToPrint= new ClassPathResource("geenVerslag.txt");
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
        ClassPathResource resNotToPrint= new ClassPathResource("patient.txt");
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
    public void testGetBody()  throws IOException {
        ClassPathResource txtFile = new ClassPathResource("print.txt");
        Path path = txtFile.getFile().toPath();
        String result = TxtUtil.getBodyOfTxt(path);
        assertFalse(result.isEmpty());

        String expected = "Betreft : uw pati~ent(e) Florentie Agnes geboren op 30/07/1950   en wonende\n" +
                "Sint Jansstraat 39/3 te 3118 Werchter.\n" +
                "\n" +
                "Consultatie : 21/02/2018 met referentienr : 183150264\n" +
                "\n" +
                "* BESPREKING:\n" +
                "Gunstige evolutie.\n" +
                "Pati~ente mag progressief haar activiteiten opdrijven. Dit is normaal dat zij\n" +
                "\n" +
                "Met vriendelijke groeten,";

        assertEquals(expected, result);
    }

    @Test
    public void testGetMnemonic() throws IOException{
        ClassPathResource txtFile = new ClassPathResource("MSE_182670181_2976687_A4407.txt");
        Path path = txtFile.getFile().toPath();
        String mnemnonic = TxtUtil.getMnemnonic(path);
        assertEquals("A4407", mnemnonic);
    }


}
