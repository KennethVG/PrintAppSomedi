package be.somedi.printen.javafx;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.model.print.PrintPDFUtil;
import ch.qos.logback.core.util.ExecutorServiceUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Controller
public class ExternalCaregiverViewController {

    private final ExternalCaregiverService service;
    private final PrintPDFUtil printPDF;

    private ExecutorService executorService;
    private ExternalCaregiver caregiverToUpdate;
    private List<String> listOfPrintValues;

    @FXML
    private Label lblJobResult;

    @FXML
    private Label lblSearchResult;

    @FXML
    private Label lblInfo;

    @FXML
    private TextField txtExternalId;

    @FXML
    private ChoiceBox<String> cbPrintProtocols;

    @FXML
    private Button updateDokter;


    @Autowired
    public ExternalCaregiverViewController(ExternalCaregiverService service, PrintPDFUtil printPDF) {
        this.service = service;
        this.printPDF = printPDF;
    }

    @FXML
    public void initialize(){
        listOfPrintValues = Arrays.stream(PrintProtocols.values()).map(pp-> pp.name().toLowerCase()).collect(Collectors.toList());
        cbPrintProtocols.getItems().addAll(listOfPrintValues);
    }

    @FXML
    private void updateDokter() {
        String selectedValue = cbPrintProtocols.getValue();
        if (selectedValue.equals("ja")) {
            caregiverToUpdate.setPrintProtocols(true);
        } else {
            caregiverToUpdate.setPrintProtocols(false);
        }

        int result = service.updatePrintProtocols(caregiverToUpdate);
        String text = caregiverToUpdate.getPrintProtocols() ? " wenst vanaf nu papieren protocols" : " wenst vanaf nu GEEN papieren protocols meer";
        if (result == 1) {
            lblSearchResult.setText("Dokter " + caregiverToUpdate.getLastName() + text);
        } else
            lblSearchResult.setText("Update mislukt!");
    }

    @FXML
    private void zoekDokter() {
        String externalId = txtExternalId.getText();
        if (externalId == null || externalId.equals(""))
            lblSearchResult.setText("Vul Mnemonic in!");

        caregiverToUpdate = service.findByMnemonic(externalId);

        if (caregiverToUpdate != null) {
            lblSearchResult.setText(caregiverToUpdate.getExternalID() + " " + caregiverToUpdate.getLastName() + " " + caregiverToUpdate.getFirstName());
            cbPrintProtocols.setVisible(true);
            updateDokter.setVisible(true);
            lblInfo.setVisible(true);

            cbPrintProtocols.setValue(caregiverToUpdate.getPrintProtocols() ? listOfPrintValues.get(0) : listOfPrintValues.get(1));
        } else {
            lblSearchResult.setText("Deze mnemonic bestaat niet in de Cliniconnect database");
        }
    }

    @FXML
    private void startPrintJob() {
        Thread startJob = new Thread(printPDF::startPrintJob);
        executorService = ExecutorServiceUtil.newExecutorService();
        executorService.submit(startJob);
        lblJobResult.setText("Printjob is bezig ...");
    }

    @FXML
    private void stopPrintJob() {
        printPDF.stopPrintJob();
        executorService.shutdownNow();
        lblJobResult.setText("Geen printjob bezig!");
    }
}
