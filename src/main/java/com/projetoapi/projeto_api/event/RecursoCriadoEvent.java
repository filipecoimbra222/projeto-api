package com.projetoapi.projeto_api.event;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEvent;
import java.io.Serial;
import java.io.Serializable;

public class RecursoCriadoEvent extends ApplicationEvent implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    private HttpServletResponse response;
    private Long codigo;

    public RecursoCriadoEvent(Object source,  HttpServletResponse response, Long codigo) {
        super(source);
        this.response = response;
        this.codigo = codigo;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Long getCodigo() {
        return codigo;
    }
}
