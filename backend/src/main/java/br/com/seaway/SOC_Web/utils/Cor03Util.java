package br.com.seaway.SOC_Web.utils;

import br.com.seaway.SOC_Web.dto.Cor03Response;
import br.com.seaway.SOC_Web.model.Cor03;
import org.springframework.stereotype.Component;

@Component
public class Cor03Util {

    public Cor03Response createResponse(Cor03 cor03) {
        return Cor03Response.builder()
                .referencia(cor03.getReferencia())
                .codigoCor(cor03.getCodigoCor())
                .nomeCor(cor03.getNomeCor())
                .codigoLoja(cor03.getCodigoLoja())
                .nomeLoja(cor03.getNomeLoja())
                .tamanho(cor03.getTamanho())
                .estoque(Integer.parseInt(cor03.getEstoque()))
                .build();
    }
}
