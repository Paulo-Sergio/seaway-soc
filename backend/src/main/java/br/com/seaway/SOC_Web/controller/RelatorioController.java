package br.com.seaway.SOC_Web.controller;

import br.com.seaway.SOC_Web.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/ordens-corte/pdf")
    public ResponseEntity<byte[]> gerarRelatorioPDF(
            @RequestParam(defaultValue = "Sistema") String usuario
    ) throws Exception {
        LocalDate data = LocalDate.now();

        // Deixa a IllegalArgumentException propagar para o GlobalExceptionHandler
        byte[] relatorio = relatorioService.gerarRelatorioOrdemCorte(data, usuario);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "ordem-corte-" + data + ".pdf");

        return new ResponseEntity<>(relatorio, headers, HttpStatus.OK);
    }
}
