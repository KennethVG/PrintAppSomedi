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
        assertEquals("", fullDoc);
    }
}