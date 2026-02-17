package br.com.seaway.SOC_Web.exception;

import br.com.seaway.SOC_Web.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SemOrdemCorteDataHojeException.class)
    public ResponseEntity<ErrorResponse> semOrdemCorteException(SemOrdemCorteDataHojeException e) {
        log.error("Erro: {}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Sem Ordem de Corte"
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ArquivosNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> arquivosNaoEncontradoException(ArquivosNaoEncontradoException e) {
        log.error("Erro: {}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "Arquivos de atualização não encontrado"
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException exc) {
        log.error("Erro de IO ocorreu", exc);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao processar arquivo: " + exc.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception exc) {
        log.error("Erro inesperado ocorreu", exc);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocorreu um erro inesperado: " + exc.getMessage());
    }
}
