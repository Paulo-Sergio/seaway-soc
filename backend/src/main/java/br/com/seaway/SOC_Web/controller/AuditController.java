package br.com.seaway.SOC_Web.controller;

import br.com.seaway.SOC_Web.dto.AuditResponse;
import br.com.seaway.SOC_Web.dto.AuditSummaryResponse;
import br.com.seaway.SOC_Web.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audits")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public ResponseEntity<List<AuditResponse>> findAllAudits() {
        List<AuditResponse> response = auditService.findAllAudits();
        return ResponseEntity.ok(response);
    }

    @GetMapping("summary")
    public ResponseEntity<List<AuditSummaryResponse>> findAllAuditsSummary() {
        List<AuditSummaryResponse> response = auditService.findAllAuditsSummary();
        return ResponseEntity.ok(response);
    }

    @GetMapping("{referencia}")
    public ResponseEntity<List<AuditResponse>> findByReferenciaAudit(@PathVariable String referencia) {
        List<AuditResponse> response = auditService.findByReferenciaAudit(referencia);
        return ResponseEntity.ok(response);
    }

    @GetMapping("summary/{referencia}")
    public ResponseEntity<AuditSummaryResponse> findByReferenciaAuditSummary(@PathVariable String referencia) {
        AuditSummaryResponse response = auditService.findByReferenciaAuditSummary(referencia);
        return ResponseEntity.ok(response);
    }
}
