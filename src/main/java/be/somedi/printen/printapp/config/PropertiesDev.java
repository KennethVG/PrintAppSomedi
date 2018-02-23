package be.somedi.printen.printapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("path-dev.properties")
@Profile("dev")
public class PropertiesDev {

}
