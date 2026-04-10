package com.cidadaoativo.repository;

import com.cidadaoativo.domain.Usuario;
import com.cidadaoativo.domain.TipoUsuario;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioUsuarios {
    private final Map<String, Usuario> usuarios = new HashMap<>();

    public void salvar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        usuarios.put(usuario.getId(), usuario);
    }

    public Usuario buscarPorId(String id) {
        if (id == null) {
            return null;
        }
        return usuarios.get(id);
    }

    public List<Usuario> buscarPorTipo(TipoUsuario tipo) {
        return usuarios.values().stream()
                .filter(u -> u.getTipoUsuario() == tipo)
                .collect(Collectors.toList());
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    public int contar() {
        return usuarios.size();
    }
}