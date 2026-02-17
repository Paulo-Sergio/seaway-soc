package br.com.seaway.SOC_Web.controller.file;

import br.com.seaway.SOC_Web.service.file.*;
import br.com.seaway.SOC_Web.service.file.Cor03FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FilesController {

    private final PrevisaoFileService previsaoFileService;

    private final AnaliseFileService analiseFileService;

    private final Cor01FileService cor01FileService;

    private final AuditFileService auditFileService;

    private final ParametroFileService parametroFileService;

    private final Cor03FileService cor03FileService;

    private final OutputFilesService outputFilesService;

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processFiles() throws IOException {
        previsaoFileService.processFixedPathFile();
        analiseFileService.processFixedPathFile();
        cor01FileService.processFixedPathFile();
        auditFileService.processFixedPathFile();
        parametroFileService.processFixedPathFile();
        cor03FileService.processFixedPathFile();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Arquivo do caminho fixo processado com sucesso");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/export-outputs")
    public ResponseEntity<Void> processOutput() {
        outputFilesService.exportRemanejados();
        outputFilesService.exportClasses();
        outputFilesService.exportSOC();
        return ResponseEntity.noContent().build();
    }
}
