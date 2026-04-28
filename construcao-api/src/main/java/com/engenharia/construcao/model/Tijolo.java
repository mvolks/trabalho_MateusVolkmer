package com.engenharia.construcao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tijolo — especificações dimensionais do bloco cerâmico utilizado.
 * As medidas padrão ABNT são: 9x19x29 cm (largura x altura x comprimento)
 * Com junta de argamassa de ~1 cm em cada direção:
 *   módulo horizontal = comprimento + 1 = 30 cm
 *   módulo vertical   = altura + 1     = 20 cm
 */
@Entity
@Table(name = "tijolos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tijolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Largura do tijolo em metros (ex: 0.09) */
    @Positive(message = "Largura deve ser positiva")
    @Column(nullable = false)
    private Double largura;

    /** Altura do tijolo em metros (ex: 0.19) */
    @Positive(message = "Altura deve ser positiva")
    @Column(nullable = false)
    private Double altura;

    /** Comprimento do tijolo em metros (ex: 0.29) */
    @Positive(message = "Comprimento deve ser positivo")
    @Column(nullable = false)
    private Double comprimento;

    /** Espessura da junta de argamassa em metros (padrão: 0.01) */
    @Builder.Default
    private Double espessuraJunta = 0.01;

    /**
     * Módulo horizontal = comprimento + junta (m)
     */
    public double getModuloHorizontal() {
        return comprimento + espessuraJunta;
    }

    /**
     * Módulo vertical = altura + junta (m)
     */
    public double getModuloVertical() {
        return altura + espessuraJunta;
    }

    /**
     * Área de face de um tijolo com junta (m²)
     */
    public double getAreaFaceComJunta() {
        return getModuloHorizontal() * getModuloVertical();
    }
}
