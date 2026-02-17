package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.dto.Cor01Response;
import br.com.seaway.SOC_Web.dto.Cor03Response;
import br.com.seaway.SOC_Web.model.Cor01;
import br.com.seaway.SOC_Web.model.Cor03;
import br.com.seaway.SOC_Web.repository.Cor03Repository;
import br.com.seaway.SOC_Web.utils.Cor01Util;
import br.com.seaway.SOC_Web.utils.Cor03Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Cor03Service {

    private final Cor03Repository cor03Repository;

    private final Cor03Util cor03Util;

    public List<Cor03Response> findByReferenciaAndCor(String referencia, String codCor) {
        List<Cor03> cores = cor03Repository.findByReferencia(referencia, codCor);
        return cores.stream().map(cor03Util::createResponse)
                .toList();
    }
}
