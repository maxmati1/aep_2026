package com.cidadaoativo.service;

import com.cidadaoativo.domain.*;
import com.cidadaoativo.repository.RepositorioSolicitacoes;
import java.util.*;
import java.util.stream.Collectors;

public class ServicoSolicitacoes {
    private final RepositorioSolicitacoes repositorio;

    public ServicoSolicitacoes(RepositorioSolicitacoes repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("Repositório não pode ser nulo");
        }
        this.repositorio = repositorio;
    }

    public Solicitacao criarSolicitacao(String idCidadao, Categoria categoria,
                                        String descricao, String localizacao,
                                        boolean anonimo, NivelPrioridade prioridade) {
        validarCriacaoSolicitacao(idCidadao, categoria, descricao, localizacao, prioridade);

        Solicitacao solicitacao = new Solicitacao(idCidadao, categoria, descricao,
                localizacao, anonimo, prioridade);
        repositorio.salvar(solicitacao);
        return solicitacao;
    }

    private void validarCriacaoSolicitacao(String idCidadao, Categoria categoria,
                                           String descricao, String localizacao,
                                           NivelPrioridade prioridade) {
        if (idCidadao == null || idCidadao.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do cidadão é obrigatório");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        if (descricao == null || descricao.trim().isEmpty() || descricao.length() < 10) {
            throw new IllegalArgumentException("Descrição deve ter no mínimo 10 caracteres");
        }
        if (localizacao == null || localizacao.trim().isEmpty()) {
            throw new IllegalArgumentException("Localização é obrigatória");
        }
        if (prioridade == null) {
            throw new IllegalArgumentException("Prioridade é obrigatória");
        }
    }

    public Solicitacao buscarPorProtocolo(String protocolo) {
        if (protocolo == null || protocolo.trim().isEmpty()) {
            throw new IllegalArgumentException("Protocolo não pode ser nulo ou vazio");
        }
        return repositorio.buscarPorProtocolo(protocolo);
    }

    public List<Solicitacao> listarSolicitacoesCidadao(String idCidadao) {
        if (idCidadao == null || idCidadao.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do cidadão é obrigatório");
        }
        return repositorio.buscarPorCidadao(idCidadao);
    }

    public List<Solicitacao> listarPorStatus(StatusSolicitacao status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo");
        }
        return repositorio.buscarPorStatus(status);
    }

    public List<Solicitacao> listarPorCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }
        return repositorio.buscarPorCategoria(categoria);
    }

    public List<Solicitacao> listarPorBairro(String bairro) {
        if (bairro == null || bairro.trim().isEmpty()) {
            throw new IllegalArgumentException("Bairro não pode ser nulo ou vazio");
        }
        return repositorio.buscarPorBairro(bairro);
    }

    public List<Solicitacao> listarPorPrioridade(NivelPrioridade prioridade) {
        if (prioridade == null) {
            throw new IllegalArgumentException("Prioridade não pode ser nula");
        }
        return repositorio.buscarPorPrioridade(prioridade);
    }

    public List<Solicitacao> listarAtrasadas() {
        List<Solicitacao> todas = repositorio.listarTodas();
        return todas.stream()
                .filter(Solicitacao::estaAtrasada)
                .sorted(Comparator.comparing(Solicitacao::getDataPrazo))
                .collect(Collectors.toList());
    }

    public void atualizarStatus(String protocolo, StatusSolicitacao novoStatus,
                                String responsavel, String comentario) {
        Solicitacao solicitacao = buscarPorProtocolo(protocolo);
        if (solicitacao == null) {
            throw new IllegalArgumentException("Solicitação não encontrada: " + protocolo);
        }

        solicitacao.atualizarStatus(novoStatus, responsavel, comentario);
        repositorio.atualizar(solicitacao);
    }

    public List<Solicitacao> listarTodas() {
        return repositorio.listarTodas();
    }

    public List<Solicitacao> listarFilaTriagem() {
        return listarPorStatus(StatusSolicitacao.TRIAGEM);
    }

    public List<Solicitacao> listarEmExecucao() {
        return listarPorStatus(StatusSolicitacao.EM_EXECUCAO);
    }

    public int contarSolicitacoesAbertas() {
        return (int) repositorio.listarTodas().stream()
                .filter(s -> s.getStatus() == StatusSolicitacao.ABERTO)
                .count();
    }

    public int contarSolicitacoesResolvidas() {
        return (int) repositorio.listarTodas().stream()
                .filter(s -> s.getStatus() == StatusSolicitacao.RESOLVIDO ||
                        s.getStatus() == StatusSolicitacao.ENCERRADO)
                .count();
    }
}