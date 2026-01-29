package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.AuditSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditSummaryRepository extends JpaRepository<AuditSummary, Long> {


}
