package be.somedi.printen.printapp.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;

public class TxtUtilTest {

    @Test
    public void testGeenVerslag() throws IOException {
        ClassPathResource resNotToPrint= new ClassPathResource("geenVerslag.txt");
        Path path = resNotToPrint.getFile().toPath();
        boolean toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        Assert.assertTrue(toPrint);
    }

    @Test
    public void testLetterToPrint() throws IOException {
        ClassPathResource resToPrint = new ClassPathResource("print.txt");
        Path path = resToPrint.getFile().toPath();
        boolean toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        Assert.assertFalse(toPrint);
    }

    @Test
    public void testLetterToPatient() throws IOException {
        ClassPathResource resNotToPrint= new ClassPathResource("patient.txt");
        Path path = resNotToPrint.getFile().toPath();
        boolean toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        Assert.assertTrue(toPrint);

    }

    @Test
    public void testVulAan() throws IOException {
        ClassPathResource resNotToPrint = new ClassPathResource("vulAan.txt");
        Path path = resNotToPrint.getFile().toPath();
        boolean toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        Assert.assertTrue(toPrint);
    }
}
