package com.cidadaoativo.dto;
import com.cidadaoativo.enums.TipoUsuario;
import lombok.Data;
@Data
public class UsuarioResponseDTO {
    private Long id;
    private String email;
    private String nome;
    private String cpf;
    private TipoUsuario tipo;
}