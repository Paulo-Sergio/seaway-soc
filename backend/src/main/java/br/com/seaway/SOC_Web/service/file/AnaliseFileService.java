package br.com.seaway.SOC_Web.service.file;

import br.com.seaway.SOC_Web.exception.ArquivosNaoEncontradoException;
import br.com.seaway.SOC_Web.model.Analise;
import br.com.seaway.SOC_Web.repository.AnaliseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnaliseFileService {

    private final AnaliseRepository analiseRepository;

    @Value("${analise-txt.file.fixed-path}")
    private String fixedPath;

    @Value("${analise-txt.file.filename}")
    private String filename;

    /**
     * Processa o arquivo ANALISE.TXT do caminho fixo
     */
    public void processFixedPathFile() throws IOException {
        Path filePath = Paths.get(fixedPath, filename);

        if (!Files.exists(filePath)) {
            log.error("Arquivo não encontrado: {}", filePath);
            throw new ArquivosNaoEncontradoException("Arquivo não encontrado: " + filePath);
        }

        log.info("Processando arquivo: {}", filePath);
        processFile(filePath);
        Files.delete(filePath);
    }

    /**
     * Processa o conteúdo do arquivo
     */
    public void processFile(Path filePath) throws IOException {
        List<Analise> analises = new ArrayList<>();
        int nLinha = 0;

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.ISO_8859_1)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                System.out.println(nLinha);

                try {
                    Analise analise = parseLine(line);
                    System.out.println(analise);
                    analises.add(analise);
                } catch (Exception e) {
                    log.error("Erro ao processar linha: {}", line, e);
                }
            }
        }

        // Limpa a tabela antes de inserir novos registros
        analiseRepository.truncateTable();

        // Salva todos os registros processados no banco de dados
        analiseRepository.saveAll(analises);
        log.info("Processados e salvos {} registros de analises", analises.size());
    }

    /**
     * Analisa uma linha do arquivo e converte para um objeto Analise
     * Todos os campos são mantidos como String para simplificar o processamento
     */
    private Analise parseLine(String line) {
        if (line.length() < 192) { // Garante que a linha tenha o tamanho mínimo
            throw new IllegalArgumentException("Linha muito curta para processar: " + line);
        }

        Analise analise = new Analise();

        // Parse dos campos de acordo com o formato de largura fixa
        analise.setGenero(extractField(line, 0, 1));
        analise.setTipo(extractField(line, 1, 2));
        analise.setClassificacao(extractField(line, 2, 7));
        analise.setDescricaoGrupo(extractField(line, 7, 60));
        analise.setReferencia(extractField(line, 60, 64));
        analise.setDescricaoProduto(extractField(line, 64, 104));

        // Parse dos campos numéricos (mantidos como String)
        int currentPos = 104;

        analise.setVenda(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        // Parse do índice (mantido como String)
        analise.setIndice(extractField(line, currentPos, currentPos + 6)); // 4 inteiros + ponto + 1 decimal
        currentPos += 6;

        analise.setEstoque(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        analise.setVendasDezDias(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        analise.setCalculadoLoja(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        analise.setInformadoLoja(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        analise.setInformadoAtacado(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        analise.setCalculadoAtacado(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        analise.setInformadoTotal(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        analise.setSugestaoOc(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        // Parse do campo de data (mantido como String)
        analise.setDataSugestao(extractField(line, currentPos, currentPos + 10));
        currentPos += 10;

        analise.setSugestaoAnterior(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        analise.setOcEmAberto(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        analise.setPedidoAtender(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        analise.setTipoProduto(extractField(line, currentPos, currentPos + 1));
        currentPos += 1;

        analise.setTemPromocao(extractField(line, currentPos, currentPos + 1));
        currentPos += 1;

        analise.setAgrupa(extractField(line, currentPos, currentPos + 6));
        currentPos += 6;

        analise.setGiro(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        analise.setBloqueado(extractField(line, currentPos, currentPos + 1));
        currentPos += 1;

        analise.setEstoqueFabrica(extractField(line, currentPos, currentPos + 4));

        return analise;
    }

    /**
     * Extrai um campo da linha como String, verificando limites e fazendo trim
     */
    private String extractField(String line, int startPos, int endPos) {
        if (startPos >= line.length()) {
            return "";
        }

        if (endPos > line.length()) {
            endPos = line.length();
        }

        return line.substring(startPos, endPos).trim();
    }
}