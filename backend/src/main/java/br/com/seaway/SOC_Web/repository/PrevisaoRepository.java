package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.dto.GruposMaisVendidosResponse;
import br.com.seaway.SOC_Web.model.Previsao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrevisaoRepository extends JpaRepository<Previsao, Long> {

    @Query("SELECT p FROM Previsao p WHERE p.descricaoGrupo = :descricaoGrupo ORDER BY p.vendasDezDias DESC")
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

    @Query("SELECT p FROM Previsao p WHERE p.dataSugestao = :dataHoje")
    List<Previsao> findValidDatesSugestao(String dataHoje);

    @Query("SELECT p FROM Previsao p WHERE p.dataSugestao = :data AND p.descricaoGrupo = :descricaoGrupo ORDER BY p.vendasDezDias DESC ")
    List<Previsao> findByDataHojeSoc(String data, String descricaoGrupo);

    @Query("SELECT p FROM Previsao p WHERE p.descricaoGrupo = :descricaoGrupo " +
            "ORDER BY (CASE WHEN p.agrupa IS NULL OR p.agrupa = '' THEN 1 ELSE 0 END) ASC, " +
            "p.agrupa DESC, p.vendasDezDias DESC")
    List<Previsao> findByDescricaoGrupoComAgrupar(String descricaoGrupo);

    /** Consultar para os relatórios */
    @Query("SELECT p FROM Previsao p WHERE p.dataSugestao = :data ORDER BY p.descricaoGrupo, COALESCE(p.prioridade, 3)")
    List<Previsao> findByData(String data);

    @Query("SELECT p FROM Previsao p WHERE p.remanejar = :tipo ORDER BY p.descricaoGrupo, COALESCE(p.prioridade, 3)")
    List<Previsao> findRemanejados(String tipo);
    /** FIM - Consultar para os relatórios */

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE previsoes", nativeQuery = true)
    void truncateTable();
}
