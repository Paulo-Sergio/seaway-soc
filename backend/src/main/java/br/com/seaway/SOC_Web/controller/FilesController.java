package br.com.seaway.SOC_Web.controller;

import br.com.seaway.SOC_Web.model.Previsao;
import br.com.seaway.SOC_Web.service.file.*;
import br.com.seaway.SOC_Web.service.PrevisaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FilesController {

    private final PrevisaoFileService previsaoFileService;

    private final AnaliseFileService analiseFileService;

    private final CorFileService corFileService;

    private final AuditFileService auditFileService;

    private final ParametroFileService parametroFileService;

    private final PrevisaoService previsaoService;

    @PostMapping("/process")
    public ResponseEntity<String> processFiles() {
        try {
            previsaoFileService.processFixedPathFile();
            analiseFileService.processFixedPathFile();
            corFileService.processFixedPathFile();
            auditFileService.processFixedPathFile();
            parametroFileService.processFixedPathFile();
            return ResponseEntity.ok("Arquivo do caminho fixo processado com sucesso");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Falha ao processar arquivo: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Previsao>> getAllPrevisoes() {
        List<Previsao> previsoes = previsaoService.findAll();
        return ResponseEntity.ok(previsoes);
    }
}
