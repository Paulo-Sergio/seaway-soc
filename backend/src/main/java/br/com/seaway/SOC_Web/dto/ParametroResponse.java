package br.com.seaway.SOC_Web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ParametroResponse {

    private Long id;
    private LocalDate dataCalculo;
    private LocalTime horaCalculo;
    private String codColecao;
    private String nomeColecao;
}
