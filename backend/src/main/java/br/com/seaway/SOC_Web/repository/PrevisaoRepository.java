package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.Previsao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrevisaoRepository extends JpaRepository<Previsao, Long> {

    @Query("SELECT p FROM Previsao p WHERE p.referencia = :referencia")
    Optional<Previsao> findByReferencia(String referencia);
}
