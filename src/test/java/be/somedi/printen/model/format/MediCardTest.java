package be.somedi.printen.model.format;

import be.somedi.printen.PrintAppApplication;
import be.somedi.printen.util.FormatUtil;
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
        ClassPathResource txtFile = new ClassPathResource("MSE_182670149_2976686_A4507.txt");
        mediCard.setPathToTxt(txtFile.getFile().toPath());
    }

    @Test
    public void buildDocument() {
        String date = FormatUtil.formatDate();
        String fullDoc = mediCard.buildDocument();
        assertEquals("PROTO BEGIN\n" +
                "\n" +
                "SOMEDI C.V.B.A.\n" +
                "Liersesteenweg 267\n" +
                "2220 Heist-op-den-Berg\n" +
                "Tel.: 015/25.89.11\n" +
                "\n" +
                date +
                "                    R182670149\n" +
                "          Vantrappen Greet\n" +
                "\n" +
                "          Van Aerde,Liliane\n" +
                "          Pastoor Van Eycklei 22\n" +
                "          2221 Booischot\n" +
                "          V               24/11/1933\n" +
                "\n" +
                "\n" +
                "--------------------------------------------------------------------------------\n" +
                "Dr. Vantrappen Greet\n" +
                "--------------------------------------------------------------------------------\n" +
                "\n" +
                "Geachte collega,\n" +
                "\n" +
                "Betreft : uw patiënt(e) Van Aerde Liliane geboren op 24/11/1933 \n" +
                " en wonende Pastoor Van Eycklei 22 te 2221 Booischot. \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670149 \n" +
                "\n" +
                "\n" +
                "Test met de lettergrootte van Cliniconnect. Te groot, te klein? Ideaal? \n" +
                "En het Lettertype? Is dit goed leesbaar? \n" +
                "\n" +
                "Met vriendelijke groeten,\n" +
                "Dr. Vantrappen Greet\n" +
                "\n" +
                "VOLLEDIG PROTOCOL", fullDoc);
    }
}
