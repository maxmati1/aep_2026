package com.cidadaoativo.entity;
import com.cidadaoativo.enums.Categoria;
import com.cidadaoativo.enums.Prioridade;
import com.cidadaoativo.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "solicitacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String protocolo;
    @ManyToOne
    @JoinColumn(name = "cidadao_id")
    private Usuario cidadao;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;
    @Column(nullable = false, length = 500)
    private String descricao;
    @Column(nullable = false)
    private String localizacao;
    @Column(nullable = false)
    private String bairro;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ABERTO;
    @Column(nullable = false)
    private Boolean anonimo = false;
    @Column
    private String nomeAnonimo;
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();
    @Column
    private LocalDateTime dataResolucao;
    @Column
    private LocalDateTime prazoSLA;
    @ManyToOne
    @JoinColumn(name = "atendente_id")
    private Usuario atendente;
    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL)
    private List<HistoricoStatus> historico = new ArrayList<>();
    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();
    @PrePersist
    public void gerarProtocolo() {
        if (this.protocolo == null) {
            this.protocolo = "PROTO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }
}