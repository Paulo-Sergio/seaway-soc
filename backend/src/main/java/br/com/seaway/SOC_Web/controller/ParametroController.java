package br.com.seaway.SOC_Web.controller;

import br.com.seaway.SOC_Web.dto.ParametroResponse;
import br.com.seaway.SOC_Web.dto.PrevisaoResponse;
import br.com.seaway.SOC_Web.service.ParametroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parametros")
@RequiredArgsConstructor
public class ParametroController {

    private final ParametroService parametroService;

    @GetMapping
    public ResponseEntity<ParametroResponse> findAll() {
        ParametroResponse response = parametroService.findAll();
        return ResponseEntity.ok(response);
    }
}
