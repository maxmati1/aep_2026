package com.cidadaoativo.controller;
import com.cidadaoativo.dto.RegistroDTO;
import com.cidadaoativo.dto.LoginDTO;
import com.cidadaoativo.dto.UsuarioResponseDTO;
import com.cidadaoativo.entity.Usuario;
import com.cidadaoativo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioService usuarioService;
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroDTO registro) {
        try {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setEmail(registro.getEmail());
            novoUsuario.setSenha(registro.getSenha());
            novoUsuario.setNome(registro.getNome());
            novoUsuario.setCpf(registro.getCpf());
            novoUsuario.setTipo(registro.getTipo());
            Usuario usuarioCriado = usuarioService.registrar(novoUsuario);
            return ResponseEntity.ok(toDTO(usuarioCriado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO login) {
        try {
            Usuario usuario = usuarioService.autenticar(login.getEmail(), login.getSenha());
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("usuario", toDTO(usuario));
            resposta.put("token", "jwt-token-placeholder");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> obterPerfil(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(u -> ResponseEntity.ok(toDTO(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    private UsuarioResponseDTO toDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setEmail(usuario.getEmail());
        dto.setNome(usuario.getNome());
        dto.setCpf(usuario.getCpf());
        dto.setTipo(usuario.getTipo());
        return dto;
    }
}