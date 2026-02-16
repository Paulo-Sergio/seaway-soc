package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.dto.GruposMaisVendidosResponse;
import br.com.seaway.SOC_Web.dto.PrevisaoResponse;
import br.com.seaway.SOC_Web.model.Previsao;
import br.com.seaway.SOC_Web.repository.PrevisaoRepository;
import br.com.seaway.SOC_Web.utils.PrevisaoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrevisaoService {

    private final PrevisaoRepository previsaoRepository;

    private final PrevisaoUtil previsaoUtil;

    public List<String> findAllGrupos() {
        return previsaoRepository.findAllDistinctDescricaoGrupo();
    }

    public List<PrevisaoResponse> findByDescricaoGrupo(String descricaoGrupo) {
        List<Previsao> previsoes = previsaoRepository.findByDescricaoGrupo(descricaoGrupo);
        return previsoes.stream().map(previsaoUtil::createResponse)
                .toList();
    }

    public List<GruposMaisVendidosResponse> obterDescricaoGruposEVendasSum() {
        return previsaoRepository.findDescricaoGrupoAndVendasSum();
    }

    public void updateRemanejar(String referencia, String remanejar) {
        Optional<Previsao> optional = previsaoRepository.findByReferencia(referencia);
        if (optional.isPresent()) {
            Previsao previsao = optional.get();
            previsao.setRemanejar(remanejar.equals("NA") ? null :  remanejar);
            previsaoRepository.save(previsao);
        }
    }

    public void updateSugestaoOc(String referencia, String sugestaoOc) {
        Optional<Previsao> optional = previsaoRepository.findByReferencia(referencia);
        if (optional.isPresent()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            Previsao previsao = optional.get();
            previsao.setSugestaoOc(String.format("%04d", Integer.parseInt(sugestaoOc)));

            if (Integer.parseInt(sugestaoOc) <= 0) {
                previsao.setDataSugestao(null);
            } else {
                previsao.setDataSugestao(LocalDate.now().format(formatter));
            }
            previsaoRepository.save(previsao);
        }
    }

    public void updatePrioridade(String referencia, String prioridade) {
        Optional<Previsao> optional = previsaoRepository.findByReferencia(referencia);
        if (optional.isPresent()) {
            Previsao previsao = optional.get();
            previsao.setPrioridade(prioridade.equals("NA") ? null : prioridade);
            previsaoRepository.save(previsao);
        }
    }
}
