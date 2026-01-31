package br.com.seaway.SOC_Web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditSummaryResponse {

    private Long id;
    private String referencia;
    private int previsaoTotal;
    private int ocEmAberto;
    private int estoqueFabrica;
    private int estoqueLojaFinal;
    private int pedidosExpFacDesp;
    private int saldoPedidoAnterior;
}
