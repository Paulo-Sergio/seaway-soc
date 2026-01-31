package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.dto.AnaliseResponse;
import br.com.seaway.SOC_Web.dto.AuditResponse;
import br.com.seaway.SOC_Web.dto.AuditSummaryResponse;
import br.com.seaway.SOC_Web.model.Analise;
import br.com.seaway.SOC_Web.model.Audit;
import br.com.seaway.SOC_Web.model.AuditSummary;
import br.com.seaway.SOC_Web.repository.AuditRepository;
import br.com.seaway.SOC_Web.repository.AuditSummaryRepository;
import br.com.seaway.SOC_Web.utils.AuditSummaryUtil;
import br.com.seaway.SOC_Web.utils.AuditUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditRepository auditRepository;

    private final AuditSummaryRepository auditSummaryRepository;

    private final AuditUtil auditUtil;

    private final AuditSummaryUtil auditSummaryUtil;

    public List<AuditResponse> findAllAudits() {
        List<Audit> audits = auditRepository.findAll();
        return audits.stream().map(auditUtil::createResponse)
                .toList();
    }

    public List<AuditSummaryResponse> findAllAuditsSummary() {
        List<AuditSummary> auditsSummary = auditSummaryRepository.findAll();
        return auditsSummary.stream().map(auditSummaryUtil::createResponse)
                .toList();
    }

    public List<AuditResponse> findByReferenciaAudit(String referencia) {
        List<Audit> audits = auditRepository.findByReferencia(referencia);
        return audits.stream().map(auditUtil::createResponse)
                .toList();
    }

    public AuditSummaryResponse findByReferenciaAuditSummary(String referencia) {
        return auditSummaryRepository.findByReferencia(referencia)
                .map(auditSummaryUtil::createResponse)
                .orElseThrow(() -> new EntityNotFoundException("AuditSummary não encontrada para a referência: " + referencia));
    }
}
