package com.engenharia.construcao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Aresta do Grafo G=(V,A) — representa uma parede.
 * Conecta dois vértices (pilares) e pertence a um ou mais cômodos.
 * As paredes podem ser compartilhadas entre cômodos adjacentes.
 */
@Entity
@Table(name = "arestas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aresta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador único da parede (ex: A1, P1-P2) */
    @NotBlank(message = "Identificador da aresta é obrigatório")
    @Column(nullable = false)
    private String identificador;

    /** Vértice de origem (pilar de início da parede) */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vertice_origem_id", nullable = false)
    private Vertice verticeOrigem;

    /** Vértice de destino (pilar de fim da parede) */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vertice_destino_id", nullable = false)
    private Vertice verticeDestino;

    /** Comprimento da parede em metros */
    @Positive(message = "Comprimento deve ser positivo")
    @Column(nullable = false)
    private Double comprimento;

    /** Espessura da parede em metros (ex: 0.14 para tijolo padrão) */
    @Positive(message = "Espessura deve ser positiva")
    @Column(nullable = false)
    private Double espessura;

    /** Altura da parede em metros */
    @Positive(message = "Altura deve ser positiva")
    @Column(nullable = false)
    private Double altura;

    /** Indica se a parede possui porta */
    @Column(nullable = false)
    private Boolean possuiPorta = false;

    /** Altura da porta em metros (quando possuiPorta = true) */
    private Double alturaPorta;

    /** Largura da porta em metros (quando possuiPorta = true) */
    private Double larguraPorta;

    /** Indica se a parede possui janela */
    @Column(nullable = false)
    private Boolean possuiJanela = false;

    /** Altura da janela em metros (quando possuiJanela = true) */
    private Double alturaJanela;

    /** Largura da janela em metros (quando possuiJanela = true) */
    private Double larguraJanela;

    /** Indica se é uma parede compartilhada entre dois cômodos */
    @Column(nullable = false)
    private Boolean paredeCompartilhada = false;

    /** Nome do cômodo ao qual a parede pertence */
    private String nomeCoomodo;

    /**
     * Calcula a área líquida da parede (descontando aberturas de portas e janelas)
     * Utilizada para calcular a quantidade de tijolos
     *
     * @return área líquida em m²
     */
    public double getAreaLiquida() {
        double areaTotal = comprimento * altura;
        double areaPorta = 0.0;
        double areaJanela = 0.0;

        if (Boolean.TRUE.equals(possuiPorta) && alturaPorta != null && larguraPorta != null) {
            areaPorta = alturaPorta * larguraPorta;
        }
        if (Boolean.TRUE.equals(possuiJanela) && alturaJanela != null && larguraJanela != null) {
            areaJanela = alturaJanela * larguraJanela;
        }

        return Math.max(0, areaTotal - areaPorta - areaJanela);
    }
}
