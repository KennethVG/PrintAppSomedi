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
public class MedarTest {

    @Autowired
    private BaseFormat medar;

    @Before
    public void init() throws IOException {
        ClassPathResource txtFile = new ClassPathResource("MSE_182670149_2976686_A4507.txt");
        medar.setPathToTxt(txtFile.getFile().toPath());
    }

    @Test
    public void buildDocument() {
        String fullDoc = medar.buildDocument(null);
        String date = FormatUtil.formatDate();
        assertEquals("/FROM     : Vantrappen|Prins de Lignestr 1 B0101|3001|Heverlee||1/06979/12/414\n" +
                "/TO       : Bijdekerke|Schrieksesteenweg 16|2221|Booischot||1/14507/50/004\n" +
                "/SUBJECT  : Liliane|Van Aerde|Pastoor Van Eycklei 22|2221|Booischot|19331124|V\n" +
                "/INFO     : Created on " +
                date +
                "|MDR182670149\n" +
                "\n" +
                "/TITLE Raadpleging Dr. Vantrappen\n" +
                "/DATE " +
                date +
                "\n" +
                "Geachte collega,\n" +
                "\n" +
                "/DESCR\n" +
                "Betreft : uw patiënt(e) Van Aerde Liliane geboren op 24/11/1933 \n" +
                " en wonende Pastoor Van Eycklei 22 te 2221 Booischot. \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670149 \n" +
                "\n" +
                "\n" +
                "Test met de lettergrootte van Cliniconnect. Te groot, te klein? Ideaal? \n" +
                "En het Lettertype? Is dit goed leesbaar? \n" +
                "/END\n" +
                "Met vriendelijke groeten,\n" +
                "Dr. Vantrappen Greet\n", fullDoc);
    }
}