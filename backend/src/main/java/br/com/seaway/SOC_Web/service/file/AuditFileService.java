package br.com.seaway.SOC_Web.service.file;

import br.com.seaway.SOC_Web.model.Audit;
import br.com.seaway.SOC_Web.model.AuditSummary;
import br.com.seaway.SOC_Web.repository.AuditRepository;
import br.com.seaway.SOC_Web.repository.AuditSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço responsável por processar o arquivo AUDIT.TXT e persistir
 * os registros nas tabelas audit e audit_summary.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditFileService {

    private final AuditRepository auditRepository;

    private final AuditSummaryRepository auditSummaryRepository;

    @Value("${audit-txt.file.fixed-path}")
    private String fixedPath;

    @Value("${audit-txt.file.filename}")
    private String filename;

    // Constantes para o tamanho dos campos
    private static final int TAMANHO_REFERENCIA = 4;
    private static final int TAMANHO_CODIGO_LOJA = 3;
    private static final int TAMANHO_NOME_LOJA = 15;
    private static final int TAMANHO_GRADE_MINIMA = 3;
    private static final int TAMANHO_ESTOQUE_LOJA = 4;
    private static final int TAMANHO_VENDAS_10_DIAS = 4;
    private static final int TAMANHO_VENDA_ACUMULADA = 4;
    private static final int TAMANHO_REGISTRO_LOJA = TAMANHO_CODIGO_LOJA + TAMANHO_NOME_LOJA +
            TAMANHO_GRADE_MINIMA + TAMANHO_ESTOQUE_LOJA +
            TAMANHO_VENDAS_10_DIAS + TAMANHO_VENDA_ACUMULADA;
    private static final int TAMANHO_CAMPOS_FINAIS = 24; // 6 campos de 4 dígitos

    /**
     * Processa o arquivo AUDIT.TXT do caminho fixo
     */
    public void processFixedPathFile() throws IOException {
        Path filePath = Paths.get(fixedPath, filename);
        if (!Files.exists(filePath)) {
            log.error("Arquivo AUDIT não encontrado: {}", filePath);
            throw new IOException("Arquivo AUDIT não encontrado: " + filePath);
        }
        log.info("Processando arquivo AUDIT: {}", filePath);
        processFile(filePath);
    }

    /**
     * Processa o conteúdo do arquivo
     * Usa transação para garantir consistência entre as duas tabelas
     */
    @Transactional
    public void processFile(Path filePath) throws IOException {
        List<Audit> audits = new ArrayList<>();
        Map<String, AuditSummary> summaries = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.ISO_8859_1)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    ProcessedLine result = parseLineMultipleRecords(line);
                    audits.addAll(result.getAudits());
                    if (result.getAuditSummary() != null) {
                        summaries.put(result.getAuditSummary().getReferencia(), result.getAuditSummary());
                    }
                } catch (Exception e) {
                    log.error("Erro ao processar linha AUDIT: {}", line, e);
                }
            }
        }

        // Limpa as tabelas antes de inserir novos registros
        auditRepository.deleteAll();
        auditSummaryRepository.deleteAll();

        // Salva todos os registros processados no banco de dados
        auditRepository.saveAll(audits);
        auditSummaryRepository.saveAll(summaries.values());

        log.info("Processados e salvos {} registros de auditoria e {} resumos",
                audits.size(), summaries.size());
    }

    /**
     * Classe auxiliar para retornar os resultados do processamento de uma linha
     */
    private static class ProcessedLine {
        private final List<Audit> audits;
        private final AuditSummary auditSummary;

        public ProcessedLine(List<Audit> audits, AuditSummary auditSummary) {
            this.audits = audits;
            this.auditSummary = auditSummary;
        }

        public List<Audit> getAudits() {
            return audits;
        }

        public AuditSummary getAuditSummary() {
            return auditSummary;
        }
    }

    /**
     * Analisa uma linha do arquivo e extrai múltiplos registros de auditoria
     * e um resumo com os campos finais
     */
    private ProcessedLine parseLineMultipleRecords(String line) {
        List<Audit> audits = new ArrayList<>();
        AuditSummary summary = null;

        if (line.length() < TAMANHO_REFERENCIA) {
            throw new IllegalArgumentException("Linha muito curta para processar: " + line);
        }

        // Extrai a referência (primeiros 4 dígitos)
        String referencia = extractField(line, 0, TAMANHO_REFERENCIA);

        // Calcula quantos registros de loja existem nesta linha
        int posicaoInicial = TAMANHO_REFERENCIA;
        int maxLojas = 15; // Máximo de 15 lojas por linha
        int lojaAtual = 0;

        while (lojaAtual < maxLojas &&
                posicaoInicial + TAMANHO_REGISTRO_LOJA <= line.length()) {

            // Extrai os dados de uma loja
            int pos = posicaoInicial;

            // Código da Loja (3 dígitos)
            String codigoLoja = extractField(line, pos, pos + TAMANHO_CODIGO_LOJA);
            pos += TAMANHO_CODIGO_LOJA;

            // Se o código da loja estiver vazio, podemos pular para o próximo registro
            if (codigoLoja.trim().isEmpty()) {
                posicaoInicial += TAMANHO_REGISTRO_LOJA;
                lojaAtual++;
                continue;
            }

            // Nome da Loja (15 caracteres)
            String nomeLoja = extractField(line, pos, pos + TAMANHO_NOME_LOJA);
            pos += TAMANHO_NOME_LOJA;

            // Grade Mínima (3 dígitos)
            String gradeMinima = extractField(line, pos, pos + TAMANHO_GRADE_MINIMA);
            pos += TAMANHO_GRADE_MINIMA;

            // Estoque da Loja (4 dígitos)
            String estoqueLoja = extractField(line, pos, pos + TAMANHO_ESTOQUE_LOJA);
            pos += TAMANHO_ESTOQUE_LOJA;

            // Vendas dos 10 dias (4 dígitos)
            String vendas10Dias = extractField(line, pos, pos + TAMANHO_VENDAS_10_DIAS);
            pos += TAMANHO_VENDAS_10_DIAS;

            // Venda acumulada (4 dígitos)
            String vendaAcumulada = extractField(line, pos, pos + TAMANHO_VENDA_ACUMULADA);

            // Cria o objeto Audit para esta loja
            Audit audit = new Audit();
            audit.setReferencia(referencia);
            audit.setCodigoLoja(codigoLoja);
            audit.setNomeLoja(nomeLoja);
            audit.setGradeMinima(gradeMinima);
            audit.setEstoqueLoja(estoqueLoja);
            audit.setVendas10Dias(vendas10Dias);
            audit.setVendaAcumulada(vendaAcumulada);

            audits.add(audit);

            // Avança para o próximo registro de loja
            posicaoInicial += TAMANHO_REGISTRO_LOJA;
            lojaAtual++;
        }

        // Verifica se há campos finais para processar
        int posicaoCamposFinais = posicaoInicial;
        if (posicaoCamposFinais + TAMANHO_CAMPOS_FINAIS <= line.length()) {
            // Extrai os campos finais
            int pos = posicaoCamposFinais;

            // Previsão Total (4 dígitos)
            String previsaoTotal = extractField(line, pos, pos + 4);
            pos += 4;

            // OC em aberto (4 dígitos)
            String ocEmAberto = extractField(line, pos, pos + 4);
            pos += 4;

            // Estoque Fábrica (4 dígitos)
            String estoqueFabrica = extractField(line, pos, pos + 4);
            pos += 4;

            // Estoque Loja (4 dígitos)
            String estoqueLojaFinal = extractField(line, pos, pos + 4);
            pos += 4;

            // Pedidos Expedido/Faturado/Despachado (4 dígitos)
            String pedidosExpFacDesp = extractField(line, pos, pos + 4);
            pos += 4;

            // Saldo de Pedido Anterior (4 dígitos)
            String saldoPedidoAnterior = extractField(line, pos, pos + 4);

            // Cria um objeto AuditSummary para os campos finais
            summary = new AuditSummary();
            summary.setReferencia(referencia);
            summary.setPrevisaoTotal(previsaoTotal);
            summary.setOcEmAberto(ocEmAberto);
            summary.setEstoqueFabrica(estoqueFabrica);
            summary.setEstoqueLojaFinal(estoqueLojaFinal);
            summary.setPedidosExpFacDesp(pedidosExpFacDesp);
            summary.setSaldoPedidoAnterior(saldoPedidoAnterior);
        }

        return new ProcessedLine(audits, summary);
    }

    /**
     * Extrai um campo da linha como String, verificando limites
     */
    private String extractField(String line, int startPos, int endPos) {
        if (startPos >= line.length()) {
            return "";
        }
        if (endPos > line.length()) {
            endPos = line.length();
        }
        return line.substring(startPos, endPos);
    }
}
