package com.engenharia.construcao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Cômodo da planta baixa.
 * Formado por paredes (arestas) que podem ser compartilhadas.
 */
@Entity
@Table(name = "comodos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome do cômodo (ex: Sala, Quarto 1, Cozinha) */
    @NotBlank(message = "Nome do cômodo é obrigatório")
    @Column(nullable = false)
    private String nome;

    /** Largura do cômodo em metros */
    @Positive(message = "Largura deve ser positiva")
    @Column(nullable = false)
    private Double largura;

    /** Comprimento do cômodo em metros */
    @Positive(message = "Comprimento deve ser positivo")
    @Column(nullable = false)
    private Double comprimento;

    /** Altura do cômodo em metros */
    @Positive(message = "Altura deve ser positiva")
    @Column(nullable = false)
    private Double altura;

    /** Paredes (arestas) que formam o cômodo */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "comodo_arestas",
        joinColumns = @JoinColumn(name = "comodo_id"),
        inverseJoinColumns = @JoinColumn(name = "aresta_id")
    )
    @Builder.Default
    private List<Aresta> paredes = new ArrayList<>();

    /**
     * @return Área do piso do cômodo em m²
     */
    public double getArea() {
        return largura * comprimento;
    }

    /**
     * @return Perímetro do cômodo em metros
     */
    public double getPerimetro() {
        return 2 * (largura + comprimento);
    }
}
