package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.model.Previsao;
import br.com.seaway.SOC_Web.repository.PrevisaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrevisaoService {

    private final PrevisaoRepository previsaoRepository;

    public List<Previsao> findAll() {
        return previsaoRepository.findAll();
    }
}
