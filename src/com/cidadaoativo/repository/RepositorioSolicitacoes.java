package com.cidadaoativo.repository;

import com.cidadaoativo.domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioSolicitacoes {
    private final Map<String, Solicitacao> solicitacoes = new HashMap<>();

    public void salvar(Solicitacao solicitacao) {
        if (solicitacao == null) {
            throw new IllegalArgumentException("Solicitação não pode ser nula");
        }
        solicitacoes.put(solicitacao.getProtocolo(), solicitacao);
    }

    public Solicitacao buscarPorProtocolo(String protocolo) {
        if (protocolo == null) {
            return null;
        }
        return solicitacoes.get(protocolo);
    }

    public List<Solicitacao> buscarPorCidadao(String idCidadao) {
        return solicitacoes.values().stream()
                .filter(s -> s.getIdCidadao().equals(idCidadao))
                .collect(Collectors.toList());
    }

    public List<Solicitacao> buscarPorStatus(StatusSolicitacao status) {
        return solicitacoes.values().stream()
                .filter(s -> s.getStatus() == status)
                .sorted(Comparator.comparing(Solicitacao::getDataCriacao).reversed())
                .collect(Collectors.toList());
    }

    public List<Solicitacao> buscarPorCategoria(Categoria categoria) {
        return solicitacoes.values().stream()
                .filter(s -> s.getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    public List<Solicitacao> buscarPorBairro(String bairro) {
        return solicitacoes.values().stream()
                .filter(s -> s.getLocalizacao().equalsIgnoreCase(bairro))
                .collect(Collectors.toList());
    }

    public List<Solicitacao> buscarPorPrioridade(NivelPrioridade prioridade) {
        return solicitacoes.values().stream()
                .filter(s -> s.getPrioridade() == prioridade)
                .collect(Collectors.toList());
    }

    public void atualizar(Solicitacao solicitacao) {
        if (solicitacao == null) {
            throw new IllegalArgumentException("Solicitação não pode ser nula");
        }
        solicitacoes.put(solicitacao.getProtocolo(), solicitacao);
    }

    public List<Solicitacao> listarTodas() {
        return new ArrayList<>(solicitacoes.values());
    }

    public int contar() {
        return solicitacoes.size();
    }

    public void limpar() {
        solicitacoes.clear();
    }
}