package be.somedi.printen.model.job;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.model.UMFormat;
import be.somedi.printen.model.format.BaseFormat;
import be.somedi.printen.model.format.HealthOne;
import be.somedi.printen.model.format.Medidoc;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

import static be.somedi.printen.model.UMFormat.HEALTH_ONE;
import static be.somedi.printen.model.UMFormat.MEDIDOC;

@Component
public class SendToUmJob {

    @Value("${path-um}")
    private Path PATH_TO_UM;
    private final BeanFactory beans;

    @Autowired
    public SendToUmJob(BeanFactory beans) {
        this.beans = beans;
    }

    public boolean formatAndSend(ExternalCaregiver caregiver, Path pathToTxt) {
        BaseFormat baseFormat;
        switch (caregiver.getFormat()) {
            case MEDIDOC:
                baseFormat = beans.getBean(Medidoc.class);
                baseFormat.setPathToTxt(pathToTxt);
                Medidoc medidoc = (Medidoc) baseFormat;
                Path repFile = medidoc.makeRepFile();
                Path adrFile = medidoc.makeAdrFile();
                return Files.exists(repFile) && Files.exists(adrFile);
            case HEALTH_ONE:
                baseFormat = beans.getBean(HealthOne.class);
                baseFormat.setPathToTxt(pathToTxt);
                return true;
            default:
                return true;
        }
    }
}
