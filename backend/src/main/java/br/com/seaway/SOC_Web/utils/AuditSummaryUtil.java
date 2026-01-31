package br.com.seaway.SOC_Web.utils;

import br.com.seaway.SOC_Web.dto.AuditSummaryResponse;
import br.com.seaway.SOC_Web.model.AuditSummary;
import org.springframework.stereotype.Component;

@Component
public class AuditSummaryUtil {

    public AuditSummaryResponse createResponse(AuditSummary audit) {
        return AuditSummaryResponse.builder()
                .id(audit.getId())
                .referencia(audit.getReferencia())
                .previsaoTotal(Integer.parseInt(audit.getPrevisaoTotal()))
                .ocEmAberto(Integer.parseInt(audit.getOcEmAberto()))
                .estoqueFabrica(Integer.parseInt(audit.getEstoqueFabrica()))
                .estoqueLojaFinal(Integer.parseInt(audit.getEstoqueLojaFinal()))
                .pedidosExpFacDesp(Integer.parseInt(audit.getPedidosExpFacDesp()))
                .saldoPedidoAnterior(Integer.parseInt(audit.getSaldoPedidoAnterior()))
                .build();
    }
}
