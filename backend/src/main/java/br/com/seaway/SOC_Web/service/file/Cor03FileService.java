package br.com.seaway.SOC_Web.service.file;

import br.com.seaway.SOC_Web.exception.ArquivosNaoEncontradoException;
import br.com.seaway.SOC_Web.model.Cor03;
import br.com.seaway.SOC_Web.repository.Cor03Repository;
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
public class Cor03FileService {

    private final Cor03Repository cor03Repository;

    @Value("${cor03-txt.file.fixed-path}")
    private String fixedPath;

    @Value("${cor03-txt.file.filename}")
    private String filename;

    /**
     * Processa o arquivo COR03.TXT do caminho fixo
     */
    public void processFixedPathFile() throws IOException {
        Path filePath = Paths.get(fixedPath, filename);
        if (!Files.exists(filePath)) {
            log.error("Arquivo não encontrado: {}", filePath);
            throw new ArquivosNaoEncontradoException("Arquivo não encontrado: " + filePath);
        }
        log.info("Processando arquivo COR03: {}", filePath);
        processFile(filePath);
        Files.delete(filePath);
    }

    /**
     * Processa o conteúdo do arquivo
     */
    public void processFile(Path filePath) throws IOException {
        List<Cor03> cores = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.ISO_8859_1)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    Cor03 cor03 = parseLine(line);
                    cores.add(cor03);
                } catch (Exception e) {
                    log.error("Erro ao processar linha COR03: {}", line, e);
                }
            }
        }
        // Limpa a tabela antes de inserir novos registros
        cor03Repository.truncateTable();
        // Salva todos os registros processados no banco de dados
        cor03Repository.saveAll(cores);
        log.info("Processados e salvos {} registros de cores03", cores.size());
    }

    /**
     * Analisa uma linha do arquivo e converte para um objeto Cor03
     * Formato: Referencia(4) + CodigoCor(2) + NomeCor(15) + CodigoLoja(3) + NomeLoja(15) + Tamanho(2) + Estoque(3)
     */
    private Cor03 parseLine(String line) {
        if (line.length() < 44) { // Verificamos o tamanho mínimo necessário (4+2+15+3+15+2+3 = 44)
            throw new IllegalArgumentException("Linha muito curta para processar: " + line);
        }

        Cor03 cor03 = new Cor03();

        // Parse dos campos de acordo com o formato de largura fixa
        int currentPos = 0;

        // Referencia (4 digitos)
        cor03.setReferencia(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        // Código da cor (2 digitos)
        cor03.setCodigoCor(extractField(line, currentPos, currentPos + 2));
        currentPos += 2;

        // Nome da Cor (15 caracteres)
        cor03.setNomeCor(extractField(line, currentPos, currentPos + 15));
        currentPos += 15;

        // Código da Loja (3 digitos)
        cor03.setCodigoLoja(extractField(line, currentPos, currentPos + 3));
        currentPos += 3;

        // Nome da Loja (15 caracteres)
        cor03.setNomeLoja(extractField(line, currentPos, currentPos + 15));
        currentPos += 15;

        // Tamanho (2 digitos)
        cor03.setTamanho(extractField(line, currentPos, currentPos + 2));
        currentPos += 2;

        // Estoque (3 digitos)
        cor03.setEstoque(extractField(line, currentPos, currentPos + 3));

        // Ignoramos o resto da linha conforme especificado

        return cor03;
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