package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.dto.AnaliseResponse;
import br.com.seaway.SOC_Web.dto.Cor01Response;
import br.com.seaway.SOC_Web.model.Analise;
import br.com.seaway.SOC_Web.model.Cor01;
import br.com.seaway.SOC_Web.repository.AnaliseRepository;
import br.com.seaway.SOC_Web.utils.AnaliseUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnaliseService {

    private final AnaliseRepository analiseRepository;

    private final AnaliseUtil analiseUtil;

    public List<AnaliseResponse> findAll() {
        List<Analise> analises = analiseRepository.findAll();
        return analises.stream().map(analiseUtil::createResponse)
                .toList();
    }

    public AnaliseResponse findByReferencia(String referencia) {
        return analiseRepository.findByReferencia(referencia)
                .map(analiseUtil::createResponse)
                .orElseThrow(() -> new EntityNotFoundException("Análise não encontrada para a referência: " + referencia));
    }
}
