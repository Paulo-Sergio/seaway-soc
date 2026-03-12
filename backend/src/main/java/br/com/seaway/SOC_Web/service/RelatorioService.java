package br.com.seaway.SOC_Web.service;

import br.com.seaway.SOC_Web.dto.RelatorioOCResponse;
import br.com.seaway.SOC_Web.exception.SemOrdemCorteDataHojeException;
import br.com.seaway.SOC_Web.model.Previsao;
import br.com.seaway.SOC_Web.repository.PrevisaoRepository;
import br.com.seaway.SOC_Web.utils.PrevisaoUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Buscar dados normais da data específica (excluindo remanejados)
        List<Previsao> previsoes = previsaoRepository.findByData(data.format(formatter));
        List<RelatorioOCResponse> registrosNormais = previsoes.stream()
                .map(previsaoUtil::createResponseRelatorioOC)
                .toList();

        // Buscar remanejados (sem filtro de data)
        List<Previsao> remanejados = previsaoRepository.findRemanejados();
        List<RelatorioOCResponse> registrosRemanejadosL = remanejados.stream()
                .filter(r -> "L".equalsIgnoreCase(r.getRemanejar()))
                .map(previsaoUtil::createResponseRelatorioOC)
                .toList();

        List<RelatorioOCResponse> registrosRemanejadosLA = remanejados.stream()
                .filter(r -> "LA".equalsIgnoreCase(r.getRemanejar()))
                .map(previsaoUtil::createResponseRelatorioOC)
                .toList();

        if (registrosNormais.isEmpty() && registrosRemanejadosL.isEmpty() && registrosRemanejadosLA.isEmpty()) {
            throw new SemOrdemCorteDataHojeException("Nenhuma ordem de corte encontrada para a data de Hoje");
        }

        // Compilar o subrelatório de remanejados
        Resource subreportResource = resourceLoader.getResource("classpath:reports/ordem_corte_remanejados_sub.jrxml");
        InputStream subreportInputStream = subreportResource.getInputStream();
        JasperReport subreportJasperReport = JasperCompileManager.compileReport(subreportInputStream);
        subreportInputStream.close();

        // Carregar o template do relatório principal
        Resource resource = resourceLoader.getResource("classpath:reports/ordem_corte.jrxml");
        InputStream inputStream = resource.getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        inputStream.close();

        // Preparar parâmetros
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("LOGO_PATH", resourceLoader.getResource("classpath:images/logo-seaway.png").getURL().toString());
        parameters.put("DATA_RELATORIO", Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        parameters.put("USUARIO", usuario);

        // Passar os datasources dos remanejados como parâmetros
        parameters.put("datasourceRemanejadosL", new JRBeanCollectionDataSource(registrosRemanejadosL));
        parameters.put("datasourceRemanejadosLA", new JRBeanCollectionDataSource(registrosRemanejadosLA));

        // Passar o subrelatório compilado como parâmetro
        parameters.put("subreportJasper", subreportJasperReport);

        // Usar apenas os registros normais como datasource principal
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(registrosNormais);

        // Preencher e exportar
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
