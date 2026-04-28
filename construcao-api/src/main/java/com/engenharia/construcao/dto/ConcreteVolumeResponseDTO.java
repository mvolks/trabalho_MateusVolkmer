package com.engenharia.construcao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Resposta do cálculo do volume de concreto para as vigas baldrame.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcreteVolumeResponseDTO {

    /** Volume total de concreto em m³ */
    private Double volumeTotalM3;

    /** Quantidade de arestas (paredes) processadas */
    private Integer quantidadeArestas;

    /** Soma total dos comprimentos das paredes em metros */
    private Double comprimentoTotalParedes;

    /** Altura da viga informada em metros */
    private Double alturaViga;

    /** Largura da viga informada em metros */
    private Double larguraViga;

    /** Fórmula utilizada no cálculo */
    private String formula;

    /** Detalhamento por aresta */
    private List<ConcreteDetalheArestaDTO> detalhes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConcreteDetalheArestaDTO {
        private String identificador;
        private Double comprimento;
        private Double larguraViga;
        private Double alturaViga;
        private Double volumeM3;
    }
}
