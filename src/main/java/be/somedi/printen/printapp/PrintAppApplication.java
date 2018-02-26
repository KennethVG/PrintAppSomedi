package be.somedi.printen.printapp;

import be.somedi.printen.printapp.config.PropertiesDev;
import be.somedi.printen.printapp.config.PropertiesProd;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Import({PropertiesDev.class, PropertiesProd.class})
@SpringBootApplication
public class PrintAppApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PrintAppApplication.class, args);
        //PrintPDFUtil reader = context.getBean(PrintPDFUtil.class);
        //reader.printAllPDFs();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer
    propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
