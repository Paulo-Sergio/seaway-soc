package br.com.seaway.SOC_Web.controller;

import br.com.seaway.SOC_Web.dto.Cor01Response;
import br.com.seaway.SOC_Web.service.Cor01Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cores01")
@RequiredArgsConstructor
public class Cor01Controller {

    private final Cor01Service cor01Service;

    @GetMapping
    public ResponseEntity<List<Cor01Response>> findAll() {
        List<Cor01Response> response = cor01Service.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{referencia}")
    public ResponseEntity<List<Cor01Response>> findByReferencia(@PathVariable String referencia) {
        List<Cor01Response> response = cor01Service.findByReferencia(referencia);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{referencia}/codCor/{codCor}/classe/{classe}")
    public ResponseEntity<Void> updateClasse(
            @PathVariable String referencia,
            @PathVariable String codCor,
            @PathVariable String classe
    ) {
        cor01Service.updateClasse(referencia, codCor, classe);
        return ResponseEntity.noContent().build();
    }
}
