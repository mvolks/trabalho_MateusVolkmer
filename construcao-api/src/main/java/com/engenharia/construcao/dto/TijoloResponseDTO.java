package com.engenharia.construcao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Resposta do cálculo da quantidade de tijolos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TijoloResponseDTO {

    /** Quantidade total de tijolos (com acréscimo de perda) */
    private Integer quantidadeTotalComPerda;

    /** Quantidade de tijolos sem acréscimo de perda */
    private Integer quantidadeTotalSemPerda;

    /** Área total líquida de todas as paredes em m² */
    private Double areaTotalLiquidaM2;

    /** Percentual de perda aplicado */
    private Double percentualPerdaAplicado;

    /** Fórmula utilizada */
    private String formula;

    /** Dimensões do tijolo utilizado */
    private String dimensoesTijolo;

    /** Área de face do tijolo com junta em m² */
    private Double areaFaceTijoloComJunta;

    /** Detalhamento por aresta */
    private List<TijoloDetalheArestaDTO> detalhes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TijoloDetalheArestaDTO {
        private String identificador;
        private Double comprimento;
        private Double altura;
        private Boolean possuiPorta;
        private Boolean possuiJanela;
        private Double areaLiquidaM2;
        private Integer quantidadeTijolosSemPerda;
        private Integer quantidadeTijolosComPerda;
    }
}
