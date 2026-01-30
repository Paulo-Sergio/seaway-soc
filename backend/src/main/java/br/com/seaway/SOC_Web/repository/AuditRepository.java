package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

    @Query("SELECT a FROM Audit a WHERE a.referencia = :referencia")
    List<Audit> findByReferencia(String referencia);
}
