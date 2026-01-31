package br.com.seaway.SOC_Web.controller;

import br.com.seaway.SOC_Web.dto.GruposMaisVendidosResponse;
import br.com.seaway.SOC_Web.dto.PrevisaoResponse;
import br.com.seaway.SOC_Web.service.PrevisaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/previsoes")
@RequiredArgsConstructor
public class PrevisaoController {

    private final PrevisaoService previsaoService;

    @GetMapping("grupos")
    public ResponseEntity<List<String>> findAllGrupos() {
        List<String> response = previsaoService.findAllGrupos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("byDescricaoGrupo")
    public ResponseEntity<List<PrevisaoResponse>> findByDescricaoGrupo(@RequestParam String descricaoGrupo) {
        List<PrevisaoResponse> response = previsaoService.findByDescricaoGrupo(descricaoGrupo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/gruposMaisVendidos")
    public List<GruposMaisVendidosResponse> getDescricaoGruposEVendasSum() {
        return previsaoService.obterDescricaoGruposEVendasSum();
    }
}
