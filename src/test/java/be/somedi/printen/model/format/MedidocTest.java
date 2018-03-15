package be.somedi.printen.model.format;

import be.somedi.printen.PrintAppApplication;
import org.junit.Before;
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

import static be.somedi.printen.util.FormatUtil.formatDateAndTime;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PrintAppApplication.class)
@TestPropertySource({"/application.properties", "/application-dev.properties", "/path-dev.properties"})
@ActiveProfiles("DEV")
public class MedidocTest {

    @Autowired
    private Medidoc medidoc;

    private Path path;

    @Before
    public void init() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("MSE_182670181_2976687_A4407.txt");
        path = txtFile.getFile().toPath();
    }

    @Test
    public void testBuildHeading() {
        String heading = medidoc.buildHeading(path);

        String expected = "1/14407/53/003\n" +
                "Snellings               Marleen\n" +
                "P.A. Liersesteenweg                267\n" +
                "2220          Heist-op-den-Berg\n" +
                "015/249174\n" +
                "\n" +
                formatDateAndTime(LocalDateTime.now()) +
                "\n" +
                "1/06979/12/414\n" +
                "Vantrappen              Greet";
        assertEquals(expected, heading);
    }

    @Test
    public void testBuildHeadingLetter() {
        String headingLetter = medidoc.buildHeadingLetter(path);
        String expectedHeadingLetter= "#A06101136812\n" +
                "Van Vliet               Alexandra\n" +
                "061011\n" +
                "X\n" +
                "180104\n" +
                "182670181     \n" +
                "\n";
        assertEquals(expectedHeadingLetter, headingLetter);
    }

    @Test
    public void testBody() {
        String body = medidoc.buildBody(path);
        String expectedBodyOfLetter = "#Rb\n" +
                "!Onderzoek\n" +
                "Geachte collega,\n" +
                "\n" +
                "Betreft : uw patiënt(e) Van Vliet Alexandra geboren op 11/10/2006   en\n" +
                " wonende\n" +
                "Kerkhofstraat 43 te 2220 Heist-op-den-Berg. \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670181\n" +
                "\n" +
                "* ANAMNESE:\n" +
                " klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                " klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                " klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "\n" +
                " * klinisch onderzoekklinisch onderzoek\n" +
                "\n" +
                "Met vriendelijke groeten,\n" +
                "Marleen Snellings\n" +
                "#R/\n" +
                "#A/";
        assertEquals(expectedBodyOfLetter, body);
    }

    @Test
    public void buildDocument(){

        String fullDoc = medidoc.buildDocument(path);
        String expected = "1/14407/53/003\n" +
                "Snellings               Marleen\n" +
                "P.A. Liersesteenweg                267\n" +
                "2220          Heist-op-den-Berg\n" +
                "015/249174\n" +
                "\n" +
                formatDateAndTime(LocalDateTime.now()) +
                "\n" +
                "1/06979/12/414\n" +
                "Vantrappen              Greet\n" +
                "#A06101136812\n" +
                "Van Vliet               Alexandra\n" +
                "061011\n" +
                "X\n" +
                "180104\n" +
                "182670181     \n" +
                "\n" +
                "#Rb\n" +
                "!Onderzoek\n" +
                "Geachte collega,\n" +
                "\n" +
                "Betreft : uw patiënt(e) Van Vliet Alexandra geboren op 11/10/2006   en\n" +
                " wonende\n" +
                "Kerkhofstraat 43 te 2220 Heist-op-den-Berg. \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670181\n" +
                "\n" +
                "* ANAMNESE:\n" +
                " klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                " klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                " klinisch onderzoek klinisch onderzoek klinisch onderzoek klinisch\n" +
                " onderzoek een veel te lange lijn, alleszins langer dan 75 karakters.\n" +
                "\n" +
                " * klinisch onderzoekklinisch onderzoek\n" +
                "\n" +
                "Met vriendelijke groeten,\n" +
                "Marleen Snellings\n" +
                "#R/\n" +
                "#A/\n" +
                "#/41";
        assertEquals(expected, fullDoc);

    }
}
