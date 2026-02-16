package br.com.seaway.SOC_Web.utils;

import br.com.seaway.SOC_Web.dto.PrevisaoResponse;
import br.com.seaway.SOC_Web.dto.RelatorioOCResponse;
import br.com.seaway.SOC_Web.model.Analise;
import br.com.seaway.SOC_Web.model.Cor01;
import br.com.seaway.SOC_Web.model.Previsao;
import br.com.seaway.SOC_Web.repository.AnaliseRepository;
import br.com.seaway.SOC_Web.repository.Cor01Repository;
import br.com.seaway.SOC_Web.repository.PrevisaoRepository;
import br.com.seaway.SOC_Web.service.PrevisaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PrevisaoUtil {

    @Autowired
    private AnaliseRepository analiseRepository;

    @Autowired
    private AnaliseUtil analiseUtil;

    @Autowired
    private Cor01Repository cor01Repository;

    public PrevisaoResponse createResponse(Previsao previsao) {
        Optional<Analise> opAnalise = analiseRepository.findByReferencia(previsao.getReferencia());

        return PrevisaoResponse.builder()
                .id(previsao.getId())
                .genero(previsao.getGenero())
                .tipo(previsao.getTipo())
                .classificacao(previsao.getClassificacao())
                .descricaoGrupo(previsao.getDescricaoGrupo())
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
                .dataSugestao(SeawayUtil.parseData(previsao.getDataSugestao()))
                .sugestaoAnterior(Integer.parseInt(previsao.getSugestaoAnterior()))
                .ocEmAberto(Integer.parseInt(previsao.getOcEmAberto()))
                .pedidoAtender(Integer.parseInt(previsao.getPedidoAtender()))
                .tipoProduto(Integer.parseInt(previsao.getTipoProduto()))
                .temPromocao(previsao.getTemPromocao())
                .agrupa(SeawayUtil.parseInt(previsao.getAgrupa()))
                .giro(SeawayUtil.parseInt(previsao.getGiro()))
                .bloqueado(previsao.getBloqueado())
                .codigoColecao(previsao.getCodigoColecao())
                .nomeColecao(previsao.getNomeColecao())
                .remanejar(previsao.getRemanejar())
                .prioridade(previsao.getPrioridade())
                .analise(opAnalise.map(analise -> analiseUtil.createResponse(analise)).orElse(null))
                .build();
    }

    public RelatorioOCResponse createResponseRelatorioOC(Previsao previsao) {
        List<Cor01> coresClasseA = cor01Repository.findByReferenciaAndClasse(previsao.getReferencia(), "A");
        List<Cor01> coresClasseB = cor01Repository.findByReferenciaAndClasse(previsao.getReferencia(), "B");
        List<Cor01> coresClasseC = cor01Repository.findByReferenciaAndClasse(previsao.getReferencia(), "C");

        return RelatorioOCResponse.builder()
                .descricaoGrupo(previsao.getDescricaoGrupo())
                .referencia(previsao.getReferencia())
                .venda(Integer.parseInt(previsao.getVenda()))
                .indice(Double.parseDouble(previsao.getIndice()))
                .vendasDezDias(Integer.parseInt(previsao.getVendasDezDias()))
                .calculadoLoja(Integer.parseInt(previsao.getCalculadoLoja()))
                .informadoLoja(Integer.parseInt(previsao.getInformadoLoja()))
                .informadoAtacado(Integer.parseInt(previsao.getInformadoAtacado()))
                .calculadoAtacado(Integer.parseInt(previsao.getCalculadoAtacado()))
                .informadoTotal(Integer.parseInt(previsao.getInformadoTotal()))
                .sugestaoOc(Integer.parseInt(previsao.getSugestaoOc()))
                .dataSugestao(previsao.getDataSugestao())
                .remanejar(previsao.getRemanejar())
                .prioridade(previsao.getPrioridade())

                .coresPreferenciais(coresClasseA.stream()
                        .map(Cor01::getNomeCor)
                        .collect(Collectors.joining(", ")))

                .coresNaoPreferenciais(coresClasseB.stream()
                        .map(Cor01::getNomeCor)
                        .collect(Collectors.joining(", ")))

                .coresBloqueadas(coresClasseC.stream()
                        .map(Cor01::getNomeCor)
                        .collect(Collectors.joining(", ")))

                .build();
    }
}
