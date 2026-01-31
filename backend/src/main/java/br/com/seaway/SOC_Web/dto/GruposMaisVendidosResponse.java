package br.com.seaway.SOC_Web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GruposMaisVendidosResponse {

    private String descricaoGrupo;
    private Double totalVendas;

    public GruposMaisVendidosResponse(String descricaoGrupo, Double totalVendas) {
        this.descricaoGrupo = descricaoGrupo;
        this.totalVendas = totalVendas;
    }
}
