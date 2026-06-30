package com.engenharia.construcao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de uma Aresta (Parede) para uso nas requisições REST.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArestaRequestDTO {

    private String identificador;

    @Positive(message = "Comprimento deve ser positivo")
    @NotNull(message = "Comprimento é obrigatório")
    private Double comprimento;

    @Positive(message = "Espessura deve ser positiva")
    @NotNull(message = "Espessura é obrigatória")
    private Double espessura;

    @Positive(message = "Altura deve ser positiva")
    @NotNull(message = "Altura é obrigatória")
    private Double altura;

    private Boolean possuiPorta = false;
    private Double alturaPorta;
    private Double larguraPorta;

    private Boolean possuiJanela = false;
    private Double alturaJanela;
    private Double larguraJanela;

    /**
     * Área líquida da parede descontando abertura de porta e janela (m²).
     */
    public double getAreaLiquida() {
        double areaTotal = comprimento * altura;
        double areaPorta = Boolean.TRUE.equals(possuiPorta) && alturaPorta != null && larguraPorta != null
                ? alturaPorta * larguraPorta : 0.0;
        double areaJanela = Boolean.TRUE.equals(possuiJanela) && alturaJanela != null && larguraJanela != null
                ? alturaJanela * larguraJanela : 0.0;
        return Math.max(0, areaTotal - areaPorta - areaJanela);
    }
}
