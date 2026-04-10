package com.cidadaoativo.domain;

public enum Categoria {
    ILUMINACAO("Iluminação Pública"),
    BURACO_VIA("Buraco na Via"),
    LIMPEZA("Limpeza de Rua"),
    SAUDE("Saúde"),
    SEGURANCA_ESCOLAR("Segurança Escolar"),
    ZELADORIA("Zeladoria"),
    DRENAGEM("Drenagem"),
    ASFALTO("Asfaltamento"),
    ARVORE_PODA("Poda de Árvore"),
    OUTRO("Outro");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}