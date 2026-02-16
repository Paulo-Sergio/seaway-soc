package br.com.seaway.SOC_Web.repository;

import br.com.seaway.SOC_Web.model.Cor01;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Cor01Repository extends JpaRepository<Cor01, Long> {

    @Query("SELECT c FROM Cor01 c WHERE c.referencia = :referencia")
    List<Cor01> findByReferencia(String referencia);

    @Query("SELECT c FROM Cor01 c WHERE c.referencia = :referencia AND c.codigoCor = :codCor")
    Optional<Cor01> findByReferenciaAndCodCor(String referencia, String codCor);

    @Query("SELECT c FROM Cor01 c WHERE c.classe IS NOT NULL AND TRIM(c.classe) <> ''")
    List<Cor01> findNotNullClasse();

    @Query("SELECT c FROM Cor01 c WHERE c.referencia = :referencia AND c.classe = :classe")
    List<Cor01> findByReferenciaAndClasse(String referencia, String classe);
}
