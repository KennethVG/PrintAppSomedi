package be.somedi.printen.printapp;

import be.somedi.printen.printapp.util.ReadPDFUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PrintAppApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PrintAppApplication.class, args);
        ReadPDFUtil reader = context.getBean(ReadPDFUtil.class);
        reader.printAllPDFs();
    }
}
