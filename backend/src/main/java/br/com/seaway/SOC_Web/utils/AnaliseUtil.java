package br.com.seaway.SOC_Web.utils;

import br.com.seaway.SOC_Web.dto.AnaliseResponse;
import br.com.seaway.SOC_Web.model.Analise;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AnaliseUtil {

    public AnaliseResponse createResponse(Analise analise) {
        // Definindo o formato da data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return AnaliseResponse.builder()
                .id(analise.getId())
                .genero(analise.getGenero())
                .tipo(analise.getTipo())
                .classificacao(analise.getClassificacao())
                .descricaoProduto(analise.getDescricaoProduto())
                .referencia(analise.getReferencia())
                .descricaoProduto(analise.getDescricaoProduto())
                .venda(Integer.parseInt(analise.getVenda()))
                .indice(Double.parseDouble(analise.getIndice()))
                .vendasDezDias(Integer.parseInt(analise.getVendasDezDias()))
                .calculadoLoja(Integer.parseInt(analise.getCalculadoLoja()))
                .informadoLoja(Integer.parseInt(analise.getInformadoLoja()))
                .informadoAtacado(Integer.parseInt(analise.getInformadoAtacado()))
                .calculadoAtacado(Integer.parseInt(analise.getCalculadoAtacado()))
                .informadoTotal(Integer.parseInt(analise.getInformadoTotal()))
                .sugestaoOc(Integer.parseInt(analise.getSugestaoOc()))
                .dataSugestao(LocalDate.parse(analise.getDataSugestao(), formatter))
                .sugestaoAnterior(Integer.parseInt(analise.getSugestaoAnterior()))
                .ocEmAberto(Integer.parseInt(analise.getOcEmAberto()))
                .pedidoAtender(Integer.parseInt(analise.getPedidoAtender()))
                .tipoProduto(Integer.parseInt(analise.getTipoProduto()))
                .temPromocao(analise.getTemPromocao())
                .agrupa(Integer.parseInt(analise.getAgrupa()))
                .giro(Integer.parseInt(analise.getGiro()))
                .bloqueado(analise.getBloqueado())
                .estoqueFabrica(Integer.parseInt(analise.getEstoqueFabrica()))
                .build();
    }
}
