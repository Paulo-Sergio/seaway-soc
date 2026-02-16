package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.dto.RelatorioOCResponse;
import br.com.seaway.SOC_Web.model.Previsao;
import br.com.seaway.SOC_Web.repository.PrevisaoRepository;
import br.com.seaway.SOC_Web.utils.PrevisaoUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    private final PrevisaoRepository previsaoRepository;
    private final PrevisaoUtil previsaoUtil;
    private final ResourceLoader resourceLoader;

    public RelatorioService(PrevisaoRepository previsaoRepository, PrevisaoUtil previsaoUtil, ResourceLoader resourceLoader) {
        this.previsaoRepository = previsaoRepository;
        this.previsaoUtil = previsaoUtil;
        this.resourceLoader = resourceLoader;
    }

    public byte[] gerarRelatorioOrdemCorte(LocalDate data, String usuario) throws Exception {
        // Buscar dados do banco
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Previsao> previsoes = previsaoRepository.findByData(data.format(formatter));
        List<RelatorioOCResponse> registros = previsoes.stream()
                .map(previsaoUtil::createResponseRelatorioOC)
                .toList();

        if (registros.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma ordem de corte encontrada para a data: " + data);
        }

        // Carregar o template do relatório
        Resource resource = resourceLoader.getResource("classpath:reports/ordem_corte.jrxml");
        InputStream inputStream = resource.getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        // Preparar parâmetros
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("LOGO_PATH", resourceLoader.getResource("classpath:images/logo-seaway.png").getURL().toString());
        parameters.put("DATA_RELATORIO", Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        parameters.put("USUARIO", usuario);

        // Preparar a fonte de dados
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registros);

        // Preencher o relatório
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Exportar para PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
