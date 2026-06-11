package com.cidadaoativo.dto;
import lombok.Data;
@Data
public class AtualizarStatusDTO {
    private String status;
    private Long responsavelId;
    private String motivo;
}