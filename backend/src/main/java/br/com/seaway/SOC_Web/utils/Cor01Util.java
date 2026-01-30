package br.com.seaway.SOC_Web.utils;

import br.com.seaway.SOC_Web.dto.Cor01Response;
import br.com.seaway.SOC_Web.model.Cor01;
import org.springframework.stereotype.Component;

@Component
public class Cor01Util {

    public Cor01Response createResponse(Cor01 cor01) {
        return Cor01Response.builder()
                .id(cor01.getId())
                .referencia(cor01.getReferencia())
                .codigoCor(Integer.parseInt(cor01.getCodigoCor()))
                .nomeCor(cor01.getNomeCor())
                .venda(Integer.parseInt(cor01.getVenda()))
                .venda10Dias(Integer.parseInt(cor01.getVenda10Dias()))
                .estoque(Integer.parseInt(cor01.getEstoque()))
                .indice(Double.parseDouble(cor01.getIndice()))
                .iop(Double.parseDouble(cor01.getIop()))
                .classe(cor01.getClasse())
                .build();
    }
}
