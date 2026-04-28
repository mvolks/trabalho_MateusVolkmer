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
 * Requisição para cálculo do volume de concreto da fundação (viga baldrame).
 *
 * Fórmula: V = L x A x C  (para cada viga, onde L e C derivam da parede)
 * - L = largura da viga (fornecida pelo usuário)
 * - A = altura da viga (fornecida pelo usuário)
 * - C = comprimento da parede (vinda da aresta)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcreteVolumeRequestDTO {

    @NotEmpty(message = "Lista de arestas não pode ser vazia")
    @Valid
    private List<ArestaRequestDTO> arestas;

    /** Altura da viga baldrame em metros */
    @Positive(message = "Altura da viga deve ser positiva")
    @NotNull(message = "Altura da viga é obrigatória")
    private Double alturaViga;

    /** Largura da viga baldrame em metros */
    @Positive(message = "Largura da viga deve ser positiva")
    @NotNull(message = "Largura da viga é obrigatória")
    private Double larguraViga;
}
