package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.Cor01;
import br.com.seaway.SOC_Web.model.Cor03;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Cor03Repository extends JpaRepository<Cor03, Long> {

    @Query("SELECT c FROM Cor03 c WHERE c.referencia = :referencia")
    List<Cor01> findByReferencia(String referencia);
}
