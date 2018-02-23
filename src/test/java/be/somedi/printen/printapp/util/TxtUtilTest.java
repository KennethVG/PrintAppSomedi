package be.somedi.printen.printapp.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;

public class TxtUtilTest {

    @Test
    public void testIsPathToPrint() throws IOException {
        ClassPathResource resNotToPrint = new ClassPathResource("vulAan.txt");
        Path path = resNotToPrint.getFile().toPath();
        boolean toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        Assert.assertTrue(toPrint);

        resNotToPrint= new ClassPathResource("geenVerslag.txt");
        path = resNotToPrint.getFile().toPath();
        toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        Assert.assertTrue(toPrint);

        ClassPathResource resToPrint = new ClassPathResource("print.txt");
        path = resToPrint.getFile().toPath();
        toPrint = TxtUtil.isPathWithLetterNotToPrint(path);
        Assert.assertFalse(toPrint);
    }
}
