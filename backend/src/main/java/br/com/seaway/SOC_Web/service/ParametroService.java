package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.dto.ParametroResponse;
import br.com.seaway.SOC_Web.model.Cor01;
import br.com.seaway.SOC_Web.model.Parametro;
import br.com.seaway.SOC_Web.repository.ParametroRepository;
import br.com.seaway.SOC_Web.utils.ParametroUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParametroService {

    private final ParametroRepository parametroRepository;

    private final ParametroUtil parametroUtil;

    public ParametroResponse findAll() {
        List<Parametro> parametros = parametroRepository.findAll();
        return parametros.stream().map(parametroUtil::createResponse)
                .findFirst().orElse(null);
    }
}
