package br.com.seaway.SOC_Web.service.file;

import br.com.seaway.SOC_Web.model.Previsao;
import br.com.seaway.SOC_Web.repository.PrevisaoRepository;
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
public class PrevisaoFileService {

    private final PrevisaoRepository previsaoRepository;

    @Value("${previsao-txt.file.fixed-path}")
    private String fixedPath;

    @Value("${previsao-txt.file.filename}")
    private String filename;

    /**
     * Processa o arquivo PREVISAO.TXT do caminho fixo
     */
    public void processFixedPathFile() throws IOException {
        Path filePath = Paths.get(fixedPath, filename);

        if (!Files.exists(filePath)) {
            log.error("Arquivo não encontrado: {}", filePath);
            throw new IOException("Arquivo não encontrado: " + filePath);
        }

        log.info("Processando arquivo: {}", filePath);
        processFile(filePath);
    }

    /**
     * Processa o conteúdo do arquivo
     */
    public void processFile(Path filePath) throws IOException {
        List<Previsao> previsoes = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.ISO_8859_1)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    Previsao previsao = parseLine(line);
                    System.out.println(previsao);
                    previsoes.add(previsao);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Erro ao processar linha: {}", line, e);
                }
            }
        }

        // Limpa a tabela antes de inserir novos registros
        previsaoRepository.deleteAll();

        // Salva todos os registros processados no banco de dados
        previsaoRepository.saveAll(previsoes);
        log.info("Processados e salvos {} registros de previsão", previsoes.size());
    }

    /**
     * Analisa uma linha do arquivo e converte para um objeto Previsao
     * Todos os campos são mantidos como String para simplificar o processamento
     */
    private Previsao parseLine(String line) {
        if (line.length() < 200) { // Garante que a linha tenha o tamanho mínimo
            throw new IllegalArgumentException("Linha muito curta para processar: " + line);
        }

        Previsao previsao = new Previsao();

        // Parse dos campos de acordo com o formato de largura fixa
        previsao.setGenero(extractField(line, 0, 1));
        previsao.setTipo(extractField(line, 1, 2));
        previsao.setClassificacao(extractField(line, 2, 7));
        previsao.setDescricaoGrupo(extractField(line, 7, 60));
        previsao.setReferencia(extractField(line, 60, 64));
        previsao.setDescricaoProduto(extractField(line, 64, 104));

        // Parse dos campos numéricos (mantidos como String)
        int currentPos = 104;

        previsao.setVenda(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        // Parse do índice (mantido como String)
        previsao.setIndice(extractField(line, currentPos, currentPos + 6)); // 4 inteiros + ponto + 1 decimal
        currentPos += 6;

        previsao.setEstoque(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        previsao.setVendasDezDias(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        previsao.setCalculadoLoja(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        previsao.setInformadoLoja(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        previsao.setInformadoAtacado(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        previsao.setCalculadoAtacado(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        previsao.setInformadoTotal(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        previsao.setSugestaoOc(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        // Parse do campo de data (mantido como String)
        previsao.setDataSugestao(extractField(line, currentPos, currentPos + 10));
        currentPos += 10;

        previsao.setSugestaoAnterior(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        previsao.setOcEmAberto(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        previsao.setPedidoAtender(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        previsao.setTipoProduto(extractField(line, currentPos, currentPos + 1));
        currentPos += 1;

        previsao.setTemPromocao(extractField(line, currentPos, currentPos + 1));
        currentPos += 1;

        previsao.setAgrupa(extractField(line, currentPos, currentPos + 6));
        currentPos += 6;

        previsao.setGiro(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        previsao.setBloqueado(extractField(line, currentPos, currentPos + 1));
        currentPos += 1;

        previsao.setCodigoColecao(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        // Aqui você pega o restante da string para setNomeColecao
        previsao.setNomeColecao(line.substring(currentPos).trim());

        return previsao;
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