package be.somedi.printen.controller;

import be.somedi.printen.entity.ExternalCaregiver;
import be.somedi.printen.mapper.FormatMapper;
import be.somedi.printen.model.job.PrintJob;
import be.somedi.printen.service.ExternalCaregiverService;
import ch.qos.logback.core.util.ExecutorServiceUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping("/caregiver")
@CrossOrigin
public class ExternalCaregiverRestController {

    private final ExternalCaregiverService externalCaregiverService;
    private final PrintJob printJob;

    public ExternalCaregiverRestController(ExternalCaregiverService externalCaregiverService, PrintJob printJob) {
        this.externalCaregiverService = externalCaregiverService;
        this.printJob = printJob;
    }

    @GetMapping("/{mnemonic}")
    public ResponseEntity findExternalCaregiverByMnemonic(@PathVariable String mnemonic) {
        return ResponseEntity.ok(externalCaregiverService.findByMnemonic(mnemonic));
    }

    @PostMapping("/print/{mnemonic}")
    public ResponseEntity updatePrintProtocolsOfExternalCaregiver(@PathVariable String mnemonic, @RequestBody Boolean printprotocols) {
        ExternalCaregiver caregiver = externalCaregiverService.findByMnemonic(mnemonic);
        caregiver.setPrintProtocols(printprotocols);
        return ResponseEntity.ok(externalCaregiverService.updateExternalCaregiver(caregiver));
    }

    @PostMapping("/address/{mnemonic}")
    public ResponseEntity updateAddressOfExternalCaregiver(@PathVariable String mnemonic, @RequestBody String address) {
        ExternalCaregiver caregiver = externalCaregiverService.findByMnemonic(mnemonic);
        caregiver.setNihiiAddress(address);
        return ResponseEntity.ok(externalCaregiverService.updateExternalCaregiver(caregiver));
    }

    @PostMapping("/format/{mnemonic}")
    public ResponseEntity updateFormatOfExternalCaregiver(@PathVariable String mnemonic, @RequestBody String format) {
        ExternalCaregiver caregiver = externalCaregiverService.findByMnemonic(mnemonic);
        caregiver.setFormat(FormatMapper.mapToFormat(format));
        return ResponseEntity.ok(externalCaregiverService.updateExternalCaregiver(caregiver));
    }

    @GetMapping("/start")
    public ResponseEntity startPrintJob() {
        Thread startJob = new Thread(printJob::startPrintJob);
        ExecutorService executorService = ExecutorServiceUtil.newExecutorService();
        executorService.submit(startJob);
        return ResponseEntity.ok("{\"result\":\"Job is bezig!\"}");
    }

    @GetMapping("/stop")
    public ResponseEntity stopPrintJob() {
        printJob.stopPrintJob();
        return ResponseEntity.ok("{\"result\":\"Job is gestopt!\"}");
    }
}
