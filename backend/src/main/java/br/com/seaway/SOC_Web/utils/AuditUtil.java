package br.com.seaway.SOC_Web.utils;

import br.com.seaway.SOC_Web.dto.AuditResponse;
import br.com.seaway.SOC_Web.model.Audit;
import org.springframework.stereotype.Component;

@Component
public class AuditUtil {

    public AuditResponse createResponse(Audit audit) {
        return AuditResponse.builder()
                .id(audit.getId())
                .referencia(audit.getReferencia())
                .codigoLoja(Integer.parseInt(audit.getCodigoLoja()))
                .nomeLoja(audit.getNomeLoja())
                .gradeMinima(Integer.parseInt(audit.getGradeMinima()))
                .estoqueLoja(Integer.parseInt(audit.getEstoqueLoja()))
                .vendas10Dias(Integer.parseInt(audit.getVendas10Dias()))
                .vendaAcumulada(Integer.parseInt(audit.getVendaAcumulada()))
                .build();
    }
}
