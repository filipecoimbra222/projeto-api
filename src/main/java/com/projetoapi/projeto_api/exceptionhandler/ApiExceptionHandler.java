package com.projetoapi.projeto_api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, "Mensagem inválida", LocaleContextHolder.getLocale());
        String mensagemDesenvolvedor = ex.getMostSpecificCause().toString();
        List<Error.ErrorData> erros = List.of(new Error.ErrorData(mensagemUsuario, mensagemDesenvolvedor));
        
        return handleExceptionInternal(ex, new Error(
            status.value(),
            "Corpo da requisição inválido. Verifique o erro de sintaxe.",
            OffsetDateTime.now(),
            erros
        ), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<Error.ErrorData> erros = new ArrayList<>();
        
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String nome = ((FieldError) error).getField();
            String mensagem = error.getDefaultMessage();
            erros.add(new Error.ErrorData(
                messageSource.getMessage(error, LocaleContextHolder.getLocale()),
                String.format("Campo '%s' %s", nome, mensagem)
            ));
        }
        
        return handleExceptionInternal(ex, new Error(
            status.value(),
            "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.",
            OffsetDateTime.now(),
            erros
        ), headers, status, request);
    }

    public static class Error {
        private Integer status;
        private String titulo;
        private OffsetDateTime dataHora;
        private List<ErrorData> erros;

        public Error(Integer status, String titulo, OffsetDateTime dataHora, List<ErrorData> erros) {
            this.status = status;
            this.titulo = titulo;
            this.dataHora = dataHora;
            this.erros = erros;
        }

        // Getters
        public Integer getStatus() {
            return status;
        }

        public String getTitulo() {
            return titulo;
        }

        public OffsetDateTime getDataHora() {
            return dataHora;
        }

        public List<ErrorData> getErros() {
            return erros;
        }

        public static class ErrorData {
            private String mensagemUsuario;
            private String mensagemDesenvolvedor;

            public ErrorData(String mensagemUsuario, String mensagemDesenvolvedor) {
                this.mensagemUsuario = mensagemUsuario;
                this.mensagemDesenvolvedor = mensagemDesenvolvedor;
            }

            // Getters
            public String getMensagemUsuario() {
                return mensagemUsuario;
            }

            public String getMensagemDesenvolvedor() {
                return mensagemDesenvolvedor;
            }
        }
    }
}
