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
        ClassPathResource txtFile = new ClassPathResource("MSE_182670160_2976684_A9564.txt");
        medar.setPathToTxt(txtFile.getFile().toPath());
    }

    @Test
    public void buildDocument() {
        String fullDoc = medar.buildDocument();
        String date = FormatUtil.formatDate();
        assertEquals("/FROM     : Vantrappen|Prins de Lignestr 1 B0101|3001|Heverlee||1/06979/12/414\n" +
                "/TO       : Vekemans|Hoogstraat 13|2223|Schriek||1/19564/37/004\n" +
                "/SUBJECT  : Theodora|Van Hool|Dr.J.Vermylenstraat 14|2223|Schriek|19540630|V\n" +
                "/INFO     : Created on " +
                date +
                "|MDR182670160\n" +
                "\n" +
                "/TITLE Raadpleging Dr. Vantrappen\n" +
                "/DATE " +
                date +
                "\n" +
                "Geachte collega,\n" +
                "\n" +
                "/DESCR\n" +
                "Betreft : uw patiënt(e) Van Hool Theodora geboren op 30/06/1954 \n" +
                " en wonende Dr.J.Vermylenstraat 14 te 2223 Schriek. \n" +
                "\n" +
                " Consultatie : 04/01/2018 met referentienr : 182670160 \n" +
                "\n" +
                "\n" +
                "Deze brief zou ook naar Jamar moete gaan. Wordt hier aparte XML voor \n" +
                "aangemaakt?\n" +
                "/END\n" +
                "Met vriendelijke groeten,\n" +
                "Dr. Vantrappen Greet\n", fullDoc);
    }
}