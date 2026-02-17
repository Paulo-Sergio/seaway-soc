package br.com.seaway.SOC_Web.controller;

import br.com.seaway.SOC_Web.dto.Cor03Response;
import br.com.seaway.SOC_Web.service.Cor03Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cores03")
@RequiredArgsConstructor
public class Cor03Controller {

    private final Cor03Service cor03Service;

    @GetMapping("{referencia}/codCor/{codCor}")
    public ResponseEntity<List<Cor03Response>> findByReferenciaAndCor(
            @PathVariable String referencia, @PathVariable String codCor
    ) {
        List<Cor03Response> response = cor03Service.findByReferenciaAndCor(referencia, codCor);
        return ResponseEntity.ok(response);
    }
}
