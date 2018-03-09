package be.somedi.printen.model.job;

import be.somedi.printen.model.format.Medidoc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class SendToUmJob {

    @Value("${path-um}")
    private Path PATH_TO_UM;

    private final Medidoc medidoc;

    public SendToUmJob(Medidoc medidoc) {
        this.medidoc = medidoc;
    }

    public boolean formatAndSend(Path pathToTxt){
        Path file = medidoc.makeFile(pathToTxt);
        return Files.exists(file);
    }
}
