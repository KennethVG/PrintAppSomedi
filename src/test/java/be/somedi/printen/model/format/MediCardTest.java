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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PrintAppApplication.class)
@TestPropertySource({"/application.properties", "/application-dev.properties", "/path-dev.properties"})
@ActiveProfiles("dev")
public class MediCardTest {

    @Autowired
    private BaseFormat mediCard;

    @Before
    public void init() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("MSE_182670160_2976684_A9564.txt");
        mediCard.setPathToTxt(txtFile.getFile().toPath());
    }

    @Test
    public void buildDocument() {

        String fullDoc = mediCard.buildDocument();
        assertEquals("PROTO BEGIN\n" +
                "\n" +
                "SOMEDI C.V.B.A.\n" +
                "Liersesteenweg 267\n" +
                "2220 Heist-op-den-Berg\n" +
                "Tel.: 015/25.89.11\n" +
                "\n" +
                "21/03/2018                    R182670160\n" +
                "          Vantrappen Greet\n" +
                "\n" +
                "          Van Hool,Theodora\n" +
                "          Dr.J.Vermylenstraat 14\n" +
                "          2223 Schriek\n" +
                "          V               30/06/1954\n" +
                "\n" +
                "\n" +
                "--------------------------------------------------------------------------------\n" +
                "Dr. Vantrappen Greet\n" +
                "--------------------------------------------------------------------------------\n" +
                "\n" +
                "Geachte collega,\n" +
                "\n" +
                "Betreft : uw patiënt(e) Van Hool Theodora geboren op 30/06/1954 \n" +
                " en wonende Dr.J.Vermylenstraat 14 te 2223 Schriek. \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670160 \n" +
                "\n" +
                "\n" +
                "Deze brief zou ook naar Jamar moete gaan. Wordt hier aparte XML voor \n" +
                "aangemaakt?\n" +
                "\n" +
                "Met vriendelijke groeten,\n" +
                "Dr. Vantrappen Greet\n" +
                "\n" +
                "VOLLEDIG PROTOCOL", fullDoc);
    }
}
