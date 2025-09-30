package com.projetoapi.projeto_api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Classe responsável por tratar exceções da API e retornar respostas padronizadas.
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Trata erros de mensagem HTTP não legível (JSON inválido, por exemplo).
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {
        
        String mensagemUsuario = messageSource.getMessage(
            "mensagem.invalida", 
            null, 
            "Mensagem inválida", 
            LocaleContextHolder.getLocale()
        );
        
        String mensagemDesenvolvedor = ex.getMostSpecificCause().toString();
            
        List<Error.ErrorData> erros = List.of(
            new Error.ErrorData(mensagemUsuario, mensagemDesenvolvedor)
        );
        
        return handleExceptionInternal(
            ex,
            new Error(
                status.value(),
                "Corpo da requisição inválido. Verifique o erro de sintaxe.",
                OffsetDateTime.now(),
                erros
            ), 
            headers, 
            status, 
            request
        );
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,WebRequest request) {

        String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.toString();
        List<Error.ErrorData> erros = List.of(new Error.ErrorData(mensagemUsuario, mensagemDesenvolvedor));
        Error error = new Error(
            HttpStatus.NOT_FOUND.value(),
            mensagemUsuario,
            OffsetDateTime.now(),
            erros
        );
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }

    /**
     * Trata erros de validação de campos (Bean Validation).
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request) {
            
        List<Error.ErrorData> erros = new ArrayList<>();
        
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String nome = error instanceof FieldError 
                ? ((FieldError) error).getField() 
                : error.getObjectName();
                
            String mensagem = messageSource.getMessage(
                error, 
                LocaleContextHolder.getLocale()
            );
            
            erros.add(new Error.ErrorData(
                mensagem,
                String.format("Campo '%s': %s", nome, error.getDefaultMessage())
            ));
        }
        
        return handleExceptionInternal(
            ex, 
            new Error(
                status.value(),
                "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
                OffsetDateTime.now(),
                erros
            ), 
            headers, 
            status, 
            request
        );
    }

    /**
     * Classe que representa a estrutura padrão de erros da API.
     */
    @Getter
    public static class Error {
        private final Integer status;
        private final String titulo;
        private final OffsetDateTime dataHora;
        private final List<ErrorData> erros;

        public Error(Integer status, String titulo, OffsetDateTime dataHora, List<ErrorData> erros) {
            this.status = status;
            this.titulo = titulo;
            this.dataHora = dataHora;
            this.erros = new ArrayList<>(erros); // Defensive copy
        }

        // Custom getter for defensive copy of the list
        public List<ErrorData> getErros() {
            return new ArrayList<>(erros); // Defensive copy
        }

        /**
         * Classe que representa os detalhes de um erro específico.
         */
        @Getter
        public static class ErrorData {
            private final String mensagemUsuario;
            private final String mensagemDesenvolvedor;

            public ErrorData(String mensagemUsuario, String mensagemDesenvolvedor) {
                this.mensagemUsuario = mensagemUsuario;
                this.mensagemDesenvolvedor = mensagemDesenvolvedor;
            }

            // Getters are now provided by @Lombok
        }
        
    }
}
