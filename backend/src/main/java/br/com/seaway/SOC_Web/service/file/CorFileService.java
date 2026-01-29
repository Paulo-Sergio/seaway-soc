package br.com.seaway.SOC_Web.service.file;

import br.com.seaway.SOC_Web.model.Analise;
import br.com.seaway.SOC_Web.model.Cor;
import br.com.seaway.SOC_Web.repository.AnaliseRepository;
import br.com.seaway.SOC_Web.repository.CorRepository;
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
public class CorFileService {

    private final CorRepository corRepository;

    @Value("${cor-txt.file.fixed-path}")
    private String fixedPath;

    @Value("${cor-txt.file.filename}")
    private String filename;

    /**
     * Processa o arquivo COR01.TXT do caminho fixo
     */
    public void processFixedPathFile() throws IOException {
        Path filePath = Paths.get(fixedPath, filename);
        if (!Files.exists(filePath)) {
            log.error("Arquivo não encontrado: {}", filePath);
            throw new IOException("Arquivo não encontrado: " + filePath);
        }
        log.info("Processando arquivo COR: {}", filePath);
        processFile(filePath);
    }

    /**
     * Processa o conteúdo do arquivo
     */
    public void processFile(Path filePath) throws IOException {
        List<Cor> cores = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.ISO_8859_1)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    Cor cor = parseLine(line);
                    cores.add(cor);
                } catch (Exception e) {
                    log.error("Erro ao processar linha COR: {}", line, e);
                }
            }
        }
        // Limpa a tabela antes de inserir novos registros
        corRepository.deleteAll();
        // Salva todos os registros processados no banco de dados
        corRepository.saveAll(cores);
        log.info("Processados e salvos {} registros de cores", cores.size());
    }

    /**
     * Analisa uma linha do arquivo e converte para um objeto Cor
     * Todos os campos são mantidos como String para simplificar o processamento
     */
    private Cor parseLine(String line) {
        if (line.length() < 45) {
            throw new IllegalArgumentException("Linha muito curta para processar: " + line);
        }

        Cor cor = new Cor();

        // Parse dos campos de acordo com o formato de largura fixa
        int currentPos = 0;

        // Referencia (4 digitos)
        cor.setReferencia(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        // Código da cor (2 digitos)
        cor.setCodigoCor(extractField(line, currentPos, currentPos + 2));
        currentPos += 2;

        // Nome da Cor (15 digitos)
        cor.setNomeCor(extractField(line, currentPos, currentPos + 15));
        currentPos += 15;

        // Venda (4 digitos)
        cor.setVenda(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        // Venda 10 dias (4 digitos)
        cor.setVenda10Dias(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        // Estoque (4 digitos)
        cor.setEstoque(extractField(line, currentPos, currentPos + 4));
        currentPos += 4;

        cor.setIndice(extractField(line, currentPos, currentPos + 6)); // 4 inteiros + ponto + 1 decimal
        currentPos += 6;

        cor.setIop(extractField(line, currentPos, currentPos + 6)); // 4 inteiros + ponto + 1 decimal
        currentPos += 6;

        // Aqui você pega o restante da string para setClasse
        cor.setClasse(line.substring(currentPos));

        return cor;
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