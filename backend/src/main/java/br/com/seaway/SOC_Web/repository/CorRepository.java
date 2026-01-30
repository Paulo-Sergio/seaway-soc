package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.Cor;
import br.com.seaway.SOC_Web.model.Previsao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CorRepository extends JpaRepository<Cor, Long> {

    @Query("SELECT c FROM Cor c WHERE c.referencia = :referencia")
    List<Cor> findByReferencia(String referencia);
}
