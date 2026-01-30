package br.com.seaway.SOC_Web.utils;

import br.com.seaway.SOC_Web.dto.PrevisaoResponse;
import br.com.seaway.SOC_Web.model.Analise;
import br.com.seaway.SOC_Web.model.Previsao;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PrevisaoUtil {

    public PrevisaoResponse createResponse(Previsao previsao) {
        // Definindo o formato da data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return PrevisaoResponse.builder()
                .id(previsao.getId())
                .genero(previsao.getGenero())
                .tipo(previsao.getTipo())
                .classificacao(previsao.getClassificacao())
                .descricaoProduto(previsao.getDescricaoProduto())
                .referencia(previsao.getReferencia())
                .descricaoProduto(previsao.getDescricaoProduto())
                .venda(Integer.parseInt(previsao.getVenda()))
                .indice(Double.parseDouble(previsao.getIndice()))
                .vendasDezDias(Integer.parseInt(previsao.getVendasDezDias()))
                .calculadoLoja(Integer.parseInt(previsao.getCalculadoLoja()))
                .informadoLoja(Integer.parseInt(previsao.getInformadoLoja()))
                .informadoAtacado(Integer.parseInt(previsao.getInformadoAtacado()))
                .calculadoAtacado(Integer.parseInt(previsao.getCalculadoAtacado()))
                .informadoTotal(Integer.parseInt(previsao.getInformadoTotal()))
                .sugestaoOc(Integer.parseInt(previsao.getSugestaoOc()))
                .dataSugestao(LocalDate.parse(previsao.getDataSugestao(), formatter))
                .sugestaoAnterior(Integer.parseInt(previsao.getSugestaoAnterior()))
                .ocEmAberto(Integer.parseInt(previsao.getOcEmAberto()))
                .pedidoAtender(Integer.parseInt(previsao.getPedidoAtender()))
                .tipoProduto(Integer.parseInt(previsao.getTipoProduto()))
                .temPromocao(previsao.getTemPromocao())
                .agrupa(Integer.parseInt(previsao.getAgrupa()))
                .giro(Integer.parseInt(previsao.getGiro()))
                .bloqueado(previsao.getBloqueado())
                .codigoColecao(previsao.getCodigoColecao())
                .nomeColecao(previsao.getNomeColecao())
                .build();
    }
}
