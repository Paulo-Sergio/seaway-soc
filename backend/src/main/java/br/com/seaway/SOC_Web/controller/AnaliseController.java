package br.com.seaway.SOC_Web.controller;

import br.com.seaway.SOC_Web.dto.AnaliseResponse;
import br.com.seaway.SOC_Web.service.AnaliseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analises")
@RequiredArgsConstructor
public class AnaliseController {

    private final AnaliseService analiseService;

    @GetMapping
    public ResponseEntity<List<AnaliseResponse>> findAll() {
        List<AnaliseResponse> response = analiseService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{referencia}")
    public ResponseEntity<AnaliseResponse> findByReferencia(@PathVariable String referencia) {
        AnaliseResponse response = analiseService.findByReferencia(referencia);
        return ResponseEntity.ok(response);
    }
}
