package com.cidadaoativo.dto;
import com.cidadaoativo.enums.TipoUsuario;
import lombok.Data;
@Data
public class RegistroDTO {
    private String email;
    private String senha;
    private String nome;
    private String cpf;
    private TipoUsuario tipo;
}