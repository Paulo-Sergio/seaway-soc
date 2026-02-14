package br.com.seaway.SOC_Web.controller.file;

import br.com.seaway.SOC_Web.service.file.*;
import br.com.seaway.SOC_Web.service.file.Cor03FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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

    @PostMapping("/process")
    public ResponseEntity<String> processFiles() {
        try {
            previsaoFileService.processFixedPathFile();
            analiseFileService.processFixedPathFile();
            cor01FileService.processFixedPathFile();
            auditFileService.processFixedPathFile();
            parametroFileService.processFixedPathFile();
            cor03FileService.processFixedPathFile();
            return ResponseEntity.ok("Arquivo do caminho fixo processado com sucesso");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Falha ao processar arquivo: " + e.getMessage());
        }
    }
}
