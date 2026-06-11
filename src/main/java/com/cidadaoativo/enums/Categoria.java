package com.cidadaoativo.enums;
public enum Categoria {
    ILUMINACAO("Iluminação"), BURACO("Buraco"), LIMPEZA("Limpeza"),
    SAUDE("Saúde"), EDUCACAO("Educação"), SEGURANCA("Segurança"),
    TRANSPORTE("Transporte"), HABITACAO("Habitação"), OUTRO("Outro");
    private final String descricao;
    Categoria(String descricao) { this.descricao = descricao; }
    public String getDescricao() { return descricao; }
}