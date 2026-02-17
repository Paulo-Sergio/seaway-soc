package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.Cor03;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Cor03Repository extends JpaRepository<Cor03, Long> {

    @Query("""
            SELECT c FROM Cor03 c
            WHERE c.referencia = :referencia
            AND c.codigoCor = :codCor
            """)
    List<Cor03> findByReferencia(String referencia, String codCor);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE cores03", nativeQuery = true)
    void truncateTable();
}
