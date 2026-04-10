package com.cidadaoativo.service;

import com.cidadaoativo.domain.Usuario;
import com.cidadaoativo.domain.TipoUsuario;
import com.cidadaoativo.repository.RepositorioUsuarios;
import java.util.List;
import java.util.UUID;

public class ServicoUsuarios {
    private final RepositorioUsuarios repositorio;

    public ServicoUsuarios(RepositorioUsuarios repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("Repositório não pode ser nulo");
        }
        this.repositorio = repositorio;
    }

    public Usuario criarUsuario(String nome, String email, String telefone,
                                String endereco, String bairro, TipoUsuario tipoUsuario) {
        validarCriacaoUsuario(nome, email, tipoUsuario);

        String id = UUID.randomUUID().toString();
        Usuario usuario = new Usuario(id, nome, email, telefone, endereco, bairro, tipoUsuario);
        repositorio.salvar(usuario);
        return usuario;
    }

    private void validarCriacaoUsuario(String nome, String email, TipoUsuario tipoUsuario) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (email == null || email.trim().isEmpty() || !validarEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        if (tipoUsuario == null) {
            throw new IllegalArgumentException("Tipo de usuário é obrigatório");
        }
    }

    private boolean validarEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    public Usuario buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        return repositorio.buscarPorId(id);
    }

    public List<Usuario> listarPorTipo(TipoUsuario tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo não pode ser nulo");
        }
        return repositorio.buscarPorTipo(tipo);
    }

    public List<Usuario> listarTodos() {
        return repositorio.listarTodos();
    }

    public int contarAtendentes() {
        return (int) repositorio.listarTodos().stream()
                .filter(u -> u.getTipoUsuario() == TipoUsuario.ATENDENTE)
                .count();
    }
}