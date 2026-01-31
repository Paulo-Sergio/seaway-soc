package br.com.seaway.SOC_Web.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SeawayUtil {

    public static LocalDate parseData(String dataString) {
        // Remover espaços em branco
        if (dataString == null || dataString.trim().isEmpty()) {
            return null; // Retorna null se a string estiver vazia ou nula
        }

        // Definindo o formato da data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            // Fazendo o parse da string para LocalDate
            return LocalDate.parse(dataString.trim(), formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data inválido: " + e.getMessage());
            return null; // Retorna null em caso de erro de parse
        }
    }

    public static int parseInt(String agrupaString) {
        // Remover espaços em branco
        if (agrupaString == null || agrupaString.trim().isEmpty()) {
            return 0; // Retorna null se a string estiver vazia ou nula
        }

        // Remover espaços em branco adicionais
        agrupaString = agrupaString.trim();

        try {
            // Fazendo o parse da string para Integer
            return Integer.parseInt(agrupaString);
        } catch (NumberFormatException e) {
            System.out.println("Formato de número inválido: " + e.getMessage());
            return 0;
        }
    }
}
