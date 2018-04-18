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
import java.time.LocalDateTime;

import static be.somedi.printen.util.FormatUtil.formatDateAndTime;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = PrintAppApplication.class)
@TestPropertySource({"/application.properties", "/application-dev.properties", "/path-dev.properties"})
@ActiveProfiles("dev")
public class MedidocTest {

    @Autowired
    private BaseFormat medidoc;

    @Before
    public void init() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("MSE_182670149_2976686_A4507.txt");
        medidoc.setPathToTxt(txtFile.getFile().toPath());
    }

    @Test
    public void buildDocument() {
        String fullDoc = medidoc.buildDocument();
        assertEquals("1/06979/12/414\n" +
                "Vantrappen              Greet\n" +
                "P.A. Liersesteenweg                267\n" +
                "2220          Heist-op-den-Berg\n" +
                "016/236904\n" +
                "\n" +
                formatDateAndTime(LocalDateTime.now()) + "\n" +
                "1/14507/50/004\n" +
                "Bijdekerke              Erik\n" +
                "#A33112416480\n" +
                "Van Aerde               Liliane\n" +
                "19331124\n" +
                "X\n" +
                "20180104\n" +
                "182670149     \n" +
                "\n" +
                "#Rb\n" +
                "!Onderzoek\n" +
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
                "#R/\n" +
                "#A/\n" +
                "#/34", fullDoc);
    }
}
