package com.cidadaoativo.enums;
public enum TipoUsuario {
    CIDADAO("Cidadão"), ATENDENTE("Atendente"), GESTOR("Gestor"), ADMIN("Admin");
    private final String descricao;
    TipoUsuario(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
}