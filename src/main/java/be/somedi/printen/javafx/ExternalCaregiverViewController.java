package be.somedi.printen.javafx;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.entity.LinkedExternalCaregiver;
import be.somedi.printen.mapper.FormatMapper;
import be.somedi.printen.model.UMFormat;
import be.somedi.printen.service.ExternalCaregiverService;
import be.somedi.printen.model.job.PrintJob;
import be.somedi.printen.service.LinkedExternalCargiverService;
import ch.qos.logback.core.util.ExecutorServiceUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Controller
public class ExternalCaregiverViewController {

    private final ExternalCaregiverService service;
    private final LinkedExternalCargiverService linkedExternalCargiverService;
    private final PrintJob printPDF;

    private ExecutorService executorService;
    private ExternalCaregiver caregiverToUpdate;
    private LinkedExternalCaregiver linkedExternalCaregiverToUpdate;

    @FXML
    private Label lblJobResult;
    @FXML
    private Label lblSearchResult;
    @FXML
    private Label lblPrint;
    @FXML
    private Label lblEPrint;
    @FXML
    private Label lblFormat;
    @FXML
    private Label lblRiziv;
    @FXML
    private Label lblKopie;

    @FXML
    private TextField txtExternalId;
    @FXML
    private TextField txtRizivAdres;
    @FXML
    private TextField txtKopie;

    @FXML
    private ChoiceBox<String> cbPrintProtocols;
    @FXML
    private ChoiceBox<String> cbEProtocols;
    @FXML
    private ChoiceBox<String> cbFormaat;

    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnVerwijder;


    @Autowired
    public ExternalCaregiverViewController(ExternalCaregiverService service, LinkedExternalCargiverService linkedExternalCargiverService, PrintJob printPDF) {
        this.service = service;
        this.linkedExternalCargiverService = linkedExternalCargiverService;
        this.printPDF = printPDF;
    }

    @FXML
    public void initialize() {

        lblSearchResult.setPrefHeight(85);
        lblSearchResult.setWrapText(true);
        lblSearchResult.setTextAlignment(TextAlignment.JUSTIFY);

        setVisibility(false);

        cbPrintProtocols.getItems().addAll("ja", "nee");
        cbEProtocols.getItems().addAll("ja", "nee");

        List<String> listOfFormats = Arrays.stream(UMFormat.values()).map(format -> format.name().toLowerCase()).collect
                (Collectors.toList());
        cbFormaat.getItems().addAll(listOfFormats);
    }

    @FXML
    private void updateDokter() {
        txtExternalId.clear();

        caregiverToUpdate.setPrintProtocols(cbPrintProtocols.getValue().equals("ja"));
        caregiverToUpdate.seteProtocols(cbEProtocols.getValue().equals("ja"));

        String selectedFormatValue = cbFormaat.getValue();
        caregiverToUpdate.setFormat(FormatMapper.mapToFormat(selectedFormatValue));
        caregiverToUpdate.setNihiiAddress(txtRizivAdres.getText());


        ExternalCaregiver ec = service.findByMnemonic(txtKopie.getText());

        int kopie = 0;
        if (ec != null) {
            if (linkedExternalCaregiverToUpdate == null) {
                linkedExternalCaregiverToUpdate = new LinkedExternalCaregiver();
            }
            linkedExternalCaregiverToUpdate.setExternalId(txtExternalId.getText());
            linkedExternalCaregiverToUpdate.setLinkedId(txtKopie.getText());
            kopie = linkedExternalCargiverService.updateLinkedExternalCaregiver(linkedExternalCaregiverToUpdate);
        }
        int result = service.updateExternalCaregiver(caregiverToUpdate);
        if (result == 1) {
            lblSearchResult.setText("Dokter " + caregiverToUpdate.getLastName() + " is met succes geüpdatet!");
            if (kopie == 1) {
                lblSearchResult.setText("Dokter " + caregiverToUpdate.getLastName() + " is met succes geüpdatet! Kopie naar  Dr. " + ec.getLastName() + " is in orde!");
            }
        } else
            lblSearchResult.setText("Update mislukt!");
    }

    @FXML
    private void zoekDokter() {
        String externalId = txtExternalId.getText();
        String kopie = txtKopie.getText();
        if (externalId == null || externalId.equals(""))
            lblSearchResult.setText("Vul Mnemonic in!");

        caregiverToUpdate = service.findByMnemonic(externalId);

        if (caregiverToUpdate != null) {
            lblSearchResult.setText(caregiverToUpdate.getExternalID() + " " + caregiverToUpdate.getLastName() + " " +
                    caregiverToUpdate.getFirstName());
            setVisibility(true);

            cbPrintProtocols.setValue(caregiverToUpdate.getPrintProtocols() ? "ja" : "nee");
            cbEProtocols.setValue(caregiverToUpdate.geteProtocols() ? "ja" : "nee");
            cbFormaat.setValue(caregiverToUpdate.getFormat().name().toLowerCase());
            txtRizivAdres.setText(caregiverToUpdate.getNihiiAddress() != null ? caregiverToUpdate.getNihiiAddress() :
                    caregiverToUpdate.getNihii());

            linkedExternalCaregiverToUpdate = linkedExternalCargiverService.findLinkedIdByExternalId(caregiverToUpdate.getExternalID());
            txtKopie.setText(linkedExternalCaregiverToUpdate != null ? linkedExternalCaregiverToUpdate.getLinkedId() : "");

        } else {
            lblSearchResult.setText("Deze mnemonic bestaat niet in de Cliniconnect database");
        }
        if (kopie == null || !kopie.equalsIgnoreCase("")) {
            btnVerwijder.setDisable(true);
        }
    }

    @FXML
    public void verwijderKopie() {
        String kopie = txtKopie.getText();
        if (kopie == null || !kopie.equalsIgnoreCase("")) {
            LinkedExternalCaregiver linkedExternalCaregiver = new LinkedExternalCaregiver();
            linkedExternalCaregiver.setLinkedId(kopie);
            linkedExternalCaregiver.setExternalId(txtExternalId.getText());
            linkedExternalCargiverService.deleteLinkedExternalCaregiver(linkedExternalCaregiver);
            lblSearchResult.setText("Kopie naar andere dokter succesvol verwijderd!");
            txtKopie.clear();
        } else {
            lblSearchResult.setText("Deze dokter wenst op dit moment geen kopie.");
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
        btnUpdate.setVisible(visible);
        btnVerwijder.setVisible(visible);

        lblPrint.setVisible(visible);
        lblEPrint.setVisible(visible);
        lblKopie.setVisible(visible);
        lblFormat.setVisible(visible);
        lblRiziv.setVisible(visible);

        cbFormaat.setVisible(visible);
        cbPrintProtocols.setVisible(visible);
        cbEProtocols.setVisible(visible);

        txtRizivAdres.setVisible(visible);
        txtKopie.setVisible(visible);
    }
}
