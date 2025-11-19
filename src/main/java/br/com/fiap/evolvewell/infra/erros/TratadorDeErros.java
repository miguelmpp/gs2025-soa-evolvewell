package br.com.fiap.evolvewell.infra.erros;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroValidacao>> tratarErroValidacao(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors()
                .stream()
                .map(ErroValidacao::new)
                .toList();

        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroGeral> tratarResponseStatus(ResponseStatusException ex) {
        var body = new ErroGeral(ex.getReason() != null ? ex.getReason() : "Erro na requisição");
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroGeral> tratarErroGenerico(Exception ex) {
        // aqui você mostra uma mensagem genérica e evita expor stack trace de banco/API
        var body = new ErroGeral("Ocorreu um erro inesperado. Se persistir, contate o suporte.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
