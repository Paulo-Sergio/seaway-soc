package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.Analise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AnaliseRepository extends JpaRepository<Analise, Long> {

    @Query("SELECT a FROM Analise a WHERE a.referencia = :referencia")
    Optional<Analise> findByReferencia(String referencia);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE analises", nativeQuery = true)
    void truncateTable();
}
