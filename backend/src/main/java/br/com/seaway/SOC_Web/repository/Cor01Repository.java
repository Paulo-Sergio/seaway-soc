package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.Cor01;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Cor01Repository extends JpaRepository<Cor01, Long> {

    @Query("SELECT c FROM Cor c WHERE c.referencia = :referencia")
    List<Cor01> findByReferencia(String referencia);
}
