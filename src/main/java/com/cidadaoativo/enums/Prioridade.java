package com.cidadaoativo.enums;
public enum Prioridade {
    BAIXA("Baixa", 15), MEDIA("Média", 10), ALTA("Alta", 5), URGENTE("Urgente", 1);
    private final String descricao;
    private final int diasSLA;
    Prioridade(String descricao, int diasSLA) { this.descricao = descricao; this.diasSLA = diasSLA; }
    public String getDescricao() { return descricao; }
    public int getDiasSLA() { return diasSLA; }
}