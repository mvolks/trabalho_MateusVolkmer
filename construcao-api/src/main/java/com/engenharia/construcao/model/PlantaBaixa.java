package com.engenharia.construcao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Planta Baixa representada como Grafo G=(V,A).
 * V = Vértices (pilares/encontros de paredes)
 * A = Arestas (paredes)
 */
@Entity
@Table(name = "plantas_baixas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantaBaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome do projeto / identificação da planta */
    @NotBlank(message = "Nome da planta é obrigatório")
    @Column(nullable = false)
    private String nome;

    /** Descrição ou endereço do imóvel */
    private String descricao;

    /** Data de criação */
    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    /** Vértices (pilares) do grafo */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "planta_id")
    @Builder.Default
    private List<Vertice> vertices = new ArrayList<>();

    /** Arestas (paredes) do grafo */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "planta_id")
    @Builder.Default
    private List<Aresta> arestas = new ArrayList<>();

    /** Cômodos formados pelas paredes */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "planta_id")
    @Builder.Default
    private List<Comodo> comodos = new ArrayList<>();
}
