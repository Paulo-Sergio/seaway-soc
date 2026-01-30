package br.com.seaway.SOC_Web.service.file;

import br.com.seaway.SOC_Web.model.Parametro;
import br.com.seaway.SOC_Web.repository.ParametroRepository;
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
public class ParametroFileService {

    private final ParametroRepository parametroRepository;

    @Value("${parametro-txt.file.fixed-path}")
    private String fixedPath;

    @Value("${parametro-txt.file.filename}")
    private String filename;

    /**
     * Processa o arquivo PARAMETR.TXT do caminho fixo
     */
    public void processFixedPathFile() throws IOException {
        Path filePath = Paths.get(fixedPath, filename);
        if (!Files.exists(filePath)) {
            log.error("Arquivo não encontrado: {}", filePath);
            throw new IOException("Arquivo não encontrado: " + filePath);
        }
        log.info("Processando arquivo PARAMETR: {}", filePath);
        processFile(filePath);
    }

    /**
     * Processa o conteúdo do arquivo
     */
    public void processFile(Path filePath) throws IOException {
        List<Parametro> parametros = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.ISO_8859_1)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    Parametro parametro = parseLine(line);
                    parametros.add(parametro);
                } catch (Exception e) {
                    log.error("Erro ao processar linha PARAMETR: {}", line, e);
                }
            }
        }
        // Limpa a tabela antes de inserir novos registros
        parametroRepository.deleteAll();
        // Salva todos os registros processados no banco de dados
        parametroRepository.saveAll(parametros);
        log.info("Processados e salvos {} registros de parametros", parametros.size());
    }

    /**
     * Analisa uma linha do arquivo e converte para um objeto Parametro
     * Todos os campos são mantidos como String para simplificar o processamento
     */
    private Parametro parseLine(String line) {
        Parametro parametro = new Parametro();

        // Parse dos campos de acordo com o formato de largura fixa
        int currentPos = 0;

        // dataCalculo (10 digitos)
        parametro.setDataCalculo(extractField(line, currentPos, currentPos + 10));
        currentPos += 10;

        // horaCalculo (8 digitos)
        parametro.setHoraCalculo(extractField(line, currentPos, currentPos + 8));
        currentPos += 8;

        // codColecao (5 digitos)
        parametro.setCodColecao(extractField(line, currentPos, currentPos + 5));
        currentPos += 5;

        // nomeColecao (15 digitos)
        parametro.setNomeColecao(extractField(line, currentPos, currentPos + 15));

        return parametro;
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
        return line.substring(startPos, endPos);
    }
}