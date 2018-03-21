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
        BaseFormat baseFormat;
        switch (caregiver.getFormat()) {
            case MEDIDOC:
                baseFormat = beans.getBean(Medidoc.class);
                baseFormat.setPathToTxt(pathToTxt);
                Path repFile = baseFormat.makeRepFile(PATH_TO_UM_MEDIDOC, caregiver);
                Path adrFile = baseFormat.makeAdrFile(PATH_TO_UM_MEDIDOC, caregiver);
                return Files.exists(repFile) && Files.exists(adrFile);
            case MEDAR:
                baseFormat = beans.getBean(Medar.class);
                baseFormat.setPathToTxt(pathToTxt);
                Path repFileMedar = baseFormat.makeRepFile(PATH_TO_UM_MEDAR, caregiver);
                Path adrFileMedar = baseFormat.makeAdrFile(PATH_TO_UM_MEDAR, caregiver);
                return Files.exists(repFileMedar) && Files.exists(adrFileMedar);
            case MEDICARD:
                baseFormat = beans.getBean(MediCard.class);
                baseFormat.setPathToTxt(pathToTxt);
                Path repFileMedicard = baseFormat.makeRepFile(PATH_TO_UM_MEDICARD, caregiver);
                Path adrFileMedicard = baseFormat.makeAdrFile(PATH_TO_UM_MEDICARD, caregiver);
                return Files.exists(repFileMedicard) && Files.exists(adrFileMedicard);
            default:
                return true;
        }
    }
}
