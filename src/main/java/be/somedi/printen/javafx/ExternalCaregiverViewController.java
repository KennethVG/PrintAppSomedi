package be.somedi.printen.javafx;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.mapper.FormatMapper;
import be.somedi.printen.model.PrintProtocols;
import be.somedi.printen.model.UMFormat;
import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.model.job.PrintJob;
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
    private final PrintJob printPDF;

    private ExecutorService executorService;
    private ExternalCaregiver caregiverToUpdate;
    private List<String> listOfPrintValues;

    @FXML
    private Label lblJobResult;
    @FXML
    private Label lblSearchResult;
    @FXML
    private Label lblPrint;
    @FXML
    private Label lblFormat;
    @FXML
    private Label lblRiziv;

    @FXML
    private TextField txtExternalId;
    @FXML
    private TextField txtRizivAdres;

    @FXML
    private ChoiceBox<String> cbPrintProtocols;
    @FXML
    private ChoiceBox<String> cbFormaat;

    @FXML
    private Button updateDokter;


    @Autowired
    public ExternalCaregiverViewController(ExternalCaregiverService service, PrintJob printPDF) {
        this.service = service;
        this.printPDF = printPDF;
    }

    @FXML
    public void initialize() {

        setVisibility(false);

        listOfPrintValues = Arrays.stream(PrintProtocols.values()).map(pp -> pp.name().toLowerCase()).collect
                (Collectors.toList());
        cbPrintProtocols.getItems().addAll(listOfPrintValues);

        List<String> listOfFormats = Arrays.stream(UMFormat.values()).map(format -> format.name().toLowerCase()).collect
                (Collectors.toList());
        cbFormaat.getItems().addAll(listOfFormats);
    }

    @FXML
    private void updateDokter() {
        String selectedPrintValue = cbPrintProtocols.getValue();
        if (selectedPrintValue.equals("ja")) {
            caregiverToUpdate.setPrintProtocols(true);
        } else {
            caregiverToUpdate.setPrintProtocols(false);
        }

        String selectedFormatValue = cbFormaat.getValue();
        caregiverToUpdate.setFormat(FormatMapper.mapToFormat(selectedFormatValue));
        caregiverToUpdate.setNihiiAddress(txtRizivAdres.getText());

        int result = service.updateExternalCaregiver(caregiverToUpdate);
        if (result == 1) {
            lblSearchResult.setText("Dokter " + caregiverToUpdate.getLastName() + " is met succes geüpdatet!");
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
            lblSearchResult.setText(caregiverToUpdate.getExternalID() + " " + caregiverToUpdate.getLastName() + " " +
                    caregiverToUpdate.getFirstName());
            setVisibility(true);

            cbPrintProtocols.setValue(caregiverToUpdate.getPrintProtocols() ? listOfPrintValues.get(0) :
                    listOfPrintValues.get(1));
            cbFormaat.setValue(caregiverToUpdate.getFormat().name().toLowerCase());
            txtRizivAdres.setText(caregiverToUpdate.getNihiiAddress() != null ? caregiverToUpdate.getNihiiAddress() :
                    caregiverToUpdate.getNihii());

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

    private void setVisibility(boolean visible) {
        cbPrintProtocols.setVisible(visible);
        updateDokter.setVisible(visible);
        lblPrint.setVisible(visible);

        lblFormat.setVisible(visible);
        lblRiziv.setVisible(visible);
        cbFormaat.setVisible(visible);
        txtRizivAdres.setVisible(visible);
    }
}
