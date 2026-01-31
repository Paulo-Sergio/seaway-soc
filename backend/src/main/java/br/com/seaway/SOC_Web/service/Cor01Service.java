package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.dto.Cor01Response;
import br.com.seaway.SOC_Web.model.Cor01;
import br.com.seaway.SOC_Web.repository.Cor01Repository;
import br.com.seaway.SOC_Web.utils.Cor01Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Cor01Service {

    private final Cor01Repository cor01Repository;

    private final Cor01Util cor01Util;

    public List<Cor01Response> findAll() {
        List<Cor01> cores = cor01Repository.findAll();
        return cores.stream().map(cor01Util::createResponse)
                .toList();
    }

    public List<Cor01Response> findByReferencia(String referencia) {
        List<Cor01> cores = cor01Repository.findByReferencia(referencia);
        return cores.stream().map(cor01Util::createResponse)
                .toList();
    }
}
