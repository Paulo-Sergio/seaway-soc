package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.dto.GruposMaisVendidosResponse;
import br.com.seaway.SOC_Web.model.Previsao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrevisaoRepository extends JpaRepository<Previsao, Long> {

    @Query("SELECT p FROM Previsao p WHERE p.descricaoGrupo = :descricaoGrupo")
    List<Previsao> findByDescricaoGrupo(String descricaoGrupo);

    @Query("SELECT p FROM Previsao p WHERE p.referencia = :referencia")
    Optional<Previsao> findByReferencia(String referencia);

    @Query("SELECT DISTINCT p.descricaoGrupo FROM Previsao p")
    List<String> findAllDistinctDescricaoGrupo();

    @Query("""
                SELECT p.descricaoGrupo, SUM(CAST(p.vendasDezDias AS double))
                FROM Previsao p
                GROUP BY p.descricaoGrupo
                ORDER BY SUM(CAST(p.vendasDezDias AS double)) DESC
            """)
    List<GruposMaisVendidosResponse> findDescricaoGrupoAndVendasSum();

    @Query("SELECT p FROM Previsao p WHERE p.remanejar IS NOT NULL")
    List<Previsao> findNotNullRemanejar();

    @Query("SELECT p FROM Previsao p " +
            "WHERE LENGTH(p.dataSugestao) = 10 " +
            "AND p.dataSugestao NOT LIKE '%  %'")
    List<Previsao> findValidDatesSugestao();
}
