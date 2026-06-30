package com.engenharia.construcao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Vértice do Grafo G=(V,A) — representa o encontro de paredes,
 * onde são posicionados os pilares estruturais.
 */
@Entity
@Table(name = "vertices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vertice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador do pilar (ex: P1, P2, ...) */
    @NotBlank(message = "Identificador do vértice é obrigatório")
    @Column(nullable = false)
    private String identificador;

    /** Coordenada X na planta baixa (metros) */
    @Column(nullable = false)
    private Double coordenadaX;

    /** Coordenada Y na planta baixa (metros) */
    @Column(nullable = false)
    private Double coordenadaY;

    /** Descrição ou observação do pilar */
    private String descricao;
}
