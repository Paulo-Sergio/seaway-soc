package br.com.seaway.SOC_Web.service.file;

import br.com.seaway.SOC_Web.model.Cor01;
import br.com.seaway.SOC_Web.model.Previsao;
import br.com.seaway.SOC_Web.repository.Cor01Repository;
import br.com.seaway.SOC_Web.repository.PrevisaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutputFilesService {

    @Value("${export-txts}")
    private String fixedPath;

    private final PrevisaoRepository previsaoRepository;

    private final Cor01Repository cor01Repository;

    @Transactional
    public void exportRemanejados() {
        String fileName = fileNameRemanejados();
        Path filePath = Paths.get(fixedPath, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            List<Previsao> previsoes = previsaoRepository.findNotNullRemanejar();

            for (Previsao prev : previsoes) {
                String line = prev.getReferencia() + prev.getRemanejar();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Erro ao escrever o arquivo remanejados: {}", e.getMessage());
        }
    }

    @Transactional
    public void exportClasses() {
        Path filePath = Paths.get(fixedPath, "CLASSE.TXT");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            List<Cor01> cores = cor01Repository.findNotNullClasse();

            for (Cor01 cor : cores) {
                String line = cor.getReferencia() + cor.getCodigoCor() + cor.getClasse();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Erro ao escrever o arquivo CLASSE.TXT: {}", e.getMessage());
        }
    }

    @Transactional
    public void exportSOC() {
        Path filePath = Paths.get(fixedPath, "SOC.TXT");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            List<Previsao> previsoes = previsaoRepository.findValidDatesSugestao();

            for (Previsao prev : previsoes) {
                String line = prev.getCodigoColecao() + prev.getReferencia() +  prev.getSugestaoOc() + prev.getDataSugestao();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Erro ao escrever o arquivo CLASSE.TXT: {}", e.getMessage());
        }
    }

    private String fileNameRemanejados() {
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("ddMMyy")); // Formato: ddMMyy
        return "R" + datePart + ".TXT";
    }
}
