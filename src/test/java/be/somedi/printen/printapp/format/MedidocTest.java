package be.somedi.printen.printapp.format;

import be.somedi.printen.printapp.PrintAppApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PrintAppApplication.class)
@TestPropertySource({"/application.properties", "/application-dev.properties", "/path-dev.properties"})
@ActiveProfiles("DEV")
public class MedidocTest {

    @Autowired
    private Medidoc medidoc;

    @Test
    public void testFormatRiziv() {
        String riziv = "11261797003";
        String result = Medidoc.formatRiziv(riziv);
        assertEquals("1/12617/97/003", result);
    }

    @Test
    public void testformatStringWithBlanks() {
        String expected = "Kenneth                 ";
        String result = Medidoc.formatStringWithBlanks("Kenneth", 24);
        assertEquals(expected.length(), result.length());
        assertEquals(expected, result);
    }

    @Test
    public void testformatStringWithMaxChars() {
        String expected = "MijnBelachelijkL";
        String result = Medidoc.formatStringWithMaxChars("MijnBelachelijkLangeVoornaam", 16);
        assertEquals(expected, result);

        assertEquals("Jos", Medidoc.formatStringWithMaxChars("Jos", 16));
    }

    @Test
    public void testFormatDate() {
        LocalDateTime ldt = LocalDateTime.of(2018, Month.APRIL, 8, 8, 22);
        assertEquals("1804080822", Medidoc.formatDateAndTime(ldt));

        String date = "30071962";
        String result = Medidoc.formatDate(date);
        assertEquals("620730", result);
    }

    @Test
    public void testFormatGender() {
        String female = "V-198000";
        String male = "M151581";
        assertEquals("X", Medidoc.formatGender(female));
        assertEquals("Y", Medidoc.formatGender(male));
    }

    @Test
    public void testBuildHeading() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("MSE_182670181_2976687_A4407.txt");
        Path path = txtFile.getFile().toPath();
        String heading = medidoc.buildHeading(path);
        LocalDateTime ldt = LocalDateTime.now();

        String expected = "1/14407/53/003\n" +
                "Snellings               Marleen\n" +
                "P.A. Liersesteenweg                267\n" +
                "2220          Heist-op-den-Berg\n" +
                "015/249174\n" +
                "\n" +
                Medidoc.formatDateAndTime(ldt) +
                "\n" +
                "1/06979/12/414\n" +
                "Vantrappen              Greet";
        assertEquals(expected, heading);

//        String headingLetter = medidoc.buildHeadingLetter(path);
//        System.out.println(headingLetter);
//
//        String expectedHeadingLetter= "";



    }
}
