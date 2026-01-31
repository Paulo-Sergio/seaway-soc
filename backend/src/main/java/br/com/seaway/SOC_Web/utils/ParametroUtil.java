package br.com.seaway.SOC_Web.utils;

import br.com.seaway.SOC_Web.dto.ParametroResponse;
import br.com.seaway.SOC_Web.model.Parametro;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class ParametroUtil {

    public ParametroResponse createResponse(Parametro parametro) {
        // Definindo o formato da hora
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");

        return ParametroResponse.builder()
                .id(parametro.getId())
                .dataCalculo(SeawayUtil.parseData(parametro.getDataCalculo()))
                .horaCalculo(LocalTime.parse(parametro.getHoraCalculo(), formatterTime))
                .codColecao(parametro.getCodColecao())
                .nomeColecao(parametro.getNomeColecao())
                .build();
    }
}
