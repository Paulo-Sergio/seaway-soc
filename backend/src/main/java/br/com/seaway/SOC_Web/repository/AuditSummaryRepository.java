package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.AuditSummary;
import br.com.seaway.SOC_Web.model.Previsao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditSummaryRepository extends JpaRepository<AuditSummary, Long> {

    @Query("SELECT a FROM AuditSummary a WHERE a.referencia = :referencia")
    Optional<AuditSummary> findByReferencia(String referencia);
}
