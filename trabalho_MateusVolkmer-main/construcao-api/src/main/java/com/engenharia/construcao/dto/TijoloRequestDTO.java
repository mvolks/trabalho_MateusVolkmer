package com.engenharia.construcao.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Requisição para cálculo da quantidade de tijolos nas paredes.
 *
 * Fórmula:
 * 1. Área líquida da parede = (Comprimento x Altura) - Área de abertura
 * 2. Área do tijolo com junta = (Comprimento + junta) x (Altura + junta)
 * 3. Quantidade = Área líquida / Área do tijolo (arredondado para cima)
 * 4. Com perda = Quantidade x (1 + percentualPerda)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TijoloRequestDTO {

    @NotEmpty(message = "Lista de arestas não pode ser vazia")
    @Valid
    private List<ArestaRequestDTO> arestas;

    /** Largura do tijolo em metros (ex: 0.09 para tijolo 9cm) */
    @Positive(message = "Largura do tijolo deve ser positiva")
    @NotNull(message = "Largura do tijolo é obrigatória")
    private Double larguraTijolo;

    /** Altura do tijolo em metros (ex: 0.19 para tijolo 19cm) */
    @Positive(message = "Altura do tijolo deve ser positiva")
    @NotNull(message = "Altura do tijolo é obrigatória")
    private Double alturaTijolo;

    /** Comprimento do tijolo em metros (ex: 0.29 para tijolo 29cm) */
    @Positive(message = "Comprimento do tijolo deve ser positivo")
    @NotNull(message = "Comprimento do tijolo é obrigatório")
    private Double comprimentoTijolo;

    /** Espessura da junta de argamassa em metros (padrão: 0.01 = 1cm) */
    @Builder.Default
    private Double espessuraJunta = 0.01;

    /** Percentual de perda para reposição (ex: 0.10 = 10%) */
    @Builder.Default
    private Double percentualPerda = 0.10;
}
