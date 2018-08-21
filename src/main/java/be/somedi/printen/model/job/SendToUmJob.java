package be.somedi.printen.model.job;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.model.format.BaseFormat;
import be.somedi.printen.model.format.Medar;
import be.somedi.printen.model.format.MediCard;
import be.somedi.printen.model.format.Medidoc;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class SendToUmJob {

    private final BeanFactory beans;

    @Value("${path-um-medidoc}")
    private Path PATH_TO_UM_MEDIDOC;
    @Value("${path-um-medar}")
    private Path PATH_TO_UM_MEDAR;
    @Value("${path-um-medicard}")
    private Path PATH_TO_UM_MEDICARD;

    @Autowired
    public SendToUmJob(BeanFactory beans) {
        this.beans = beans;
    }

    public boolean formatAndSend(ExternalCaregiver caregiver, Path pathToTxt) {
        switch (caregiver.getFormat()) {
            case MEDIDOC:
                return makeFiles(beans.getBean(Medidoc.class), pathToTxt, caregiver, PATH_TO_UM_MEDIDOC);
            case MEDAR:
                return makeFiles(beans.getBean(Medar.class), pathToTxt, caregiver, PATH_TO_UM_MEDAR);
            case MEDICARD:
                return makeFiles(beans.getBean(MediCard.class), pathToTxt, caregiver, PATH_TO_UM_MEDICARD);
            default:
                return true;
        }
    }

    private boolean makeFiles(BaseFormat baseFormat, Path pathToTxt, ExternalCaregiver caregiver,  Path pathToUm){
        baseFormat.setPathToTxt(pathToTxt);
        Path repFile = baseFormat.makeRepFile(pathToUm, caregiver);
        Path adrFile = baseFormat.makeAdrFile(pathToUm, caregiver);
        return Files.exists(repFile) && Files.exists(adrFile);
    }

}
