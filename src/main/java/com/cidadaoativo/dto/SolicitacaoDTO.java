package com.cidadaoativo.dto;
import com.cidadaoativo.enums.Categoria;
import com.cidadaoativo.enums.Prioridade;
import com.cidadaoativo.enums.Status;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class SolicitacaoDTO {
    private Long id;
    private String protocolo;
    private Categoria categoria;
    private String descricao;
    private String localizacao;
    private String bairro;
    private Prioridade prioridade;
    private Status status;
    private Boolean anonimo;
    private String nomeAnonimo;
    private Long cidadaoId;
    private LocalDateTime dataCriacao;
    private LocalDateTime prazoSLA;
}