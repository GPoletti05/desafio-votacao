package com.sicredi.desafio_votacao.exception;

import com.sicredi.desafio_votacao.dto.ResponseDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PautaException.class)
    public ResponseEntity<ResponseDTO<Object>> handlePautaException(PautaException ex) {
        ResponseDTO<Object> response = ResponseDTO.builder()
                .mensagem(ex.getMessage())
                .objeto(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AssociadoException.class)
    public ResponseEntity<ResponseDTO<Void>> handleAssociadoException(AssociadoException ex) {
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .mensagem(ex.getMessage())
                .objeto(null)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Object>> handleGenericException(Exception ex) {
        ResponseDTO<Object> response = ResponseDTO.builder()
                .mensagem("Ocorreu um erro: " + ex.getMessage())
                .objeto(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // pega só a primeira mensagem de erro
        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("Dados inválidos!");

        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .mensagem(mensagem)
                .objeto(null)
                .build();

        return ResponseEntity.badRequest().body(response);
    }


}
