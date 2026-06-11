package com.cidadaoativo.entity;
import com.cidadaoativo.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
@Entity
@Table(name = "historico_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;
    @Enumerated(EnumType.STRING)
    private Status statusAnterior;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status statusNovo;
    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;
    @Column(length = 500)
    private String motivo;
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataTransicao = LocalDateTime.now();
}