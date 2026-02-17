package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE parametros", nativeQuery = true)
    void truncateTable();
}
