package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.dto.GruposMaisVendidosResponse;
import br.com.seaway.SOC_Web.dto.PrevisaoResponse;
import br.com.seaway.SOC_Web.model.Previsao;
import br.com.seaway.SOC_Web.repository.PrevisaoRepository;
import br.com.seaway.SOC_Web.utils.PrevisaoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrevisaoService {

    private final PrevisaoRepository previsaoRepository;

    private final PrevisaoUtil previsaoUtil;

    public List<String> findAllGrupos() {
        List<String> grupos = previsaoRepository.findAllDistinctDescricaoGrupo();
        return grupos;
    }

    public List<PrevisaoResponse> findByDescricaoGrupo(String descricaoGrupo) {
        List<Previsao> previsoes = previsaoRepository.findByDescricaoGrupo(descricaoGrupo);
        return previsoes.stream().map(previsaoUtil::createResponse)
                .toList();
    }

    public List<GruposMaisVendidosResponse> obterDescricaoGruposEVendasSum() {
        return previsaoRepository.findDescricaoGrupoAndVendasSum();
    }
}
