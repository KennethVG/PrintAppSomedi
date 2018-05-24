package be.somedi.printen.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOUtilTest {

    private Path result;

    @Test
    public void writeFileToError() throws IOException {
        ClassPathResource resNotToPrint = new ClassPathResource("geenVerslag.txt");
        Path path = resNotToPrint.getFile().toPath();
        System.out.println(path);
        result = IOUtil.writeFileToError(path.getParent(), path, "Geen verslag van specialist");
        Assert.assertTrue(Files.exists(result));

    }

    @Test
    public void writeFileToUM() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("MSE_182670181_2976687_A4407.txt");
        Path path = txtFile.getFile().toPath();
        result = IOUtil.writeFileToUM(path.getParent(), "A4407", "182670181", "REP", "Hello World");
        Assert.assertTrue(Files.exists(result));
    }

//    @After
//    public void removeFiles() throws IOException {
//        Files.delete(result);
//    }
}