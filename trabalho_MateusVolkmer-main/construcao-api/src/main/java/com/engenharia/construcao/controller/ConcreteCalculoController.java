package com.engenharia.construcao.controller;

import com.engenharia.construcao.dto.ConcreteVolumeRequestDTO;
import com.engenharia.construcao.dto.ConcreteVolumeResponseDTO;
import com.engenharia.construcao.service.ConcreteCalculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para cálculo do volume de concreto da fundação (viga baldrame).
 *
 * Etapa 2 — Volume de Concreto na Fundação
 */
@RestController
@RequestMapping("/api/v1/concreto")
@RequiredArgsConstructor
@Tag(name = "Concreto", description = "Cálculo de volume de concreto para viga baldrame")
public class ConcreteCalculoController {

    private final ConcreteCalculoService concreteCalculoService;

    /**
     * POST /api/v1/concreto/volume-fundacao
     *
     * Recebe uma lista de arestas (paredes) com largura e altura da viga baldrame
     * e calcula o volume total de concreto necessário para a fundação.
     *
     * @param request DTO com lista de arestas, altura e largura da viga
     * @return Volume total e detalhamento por aresta
     */
    @PostMapping("/volume-fundacao")
    @Operation(
        summary = "Calcular volume de concreto para fundação",
        description = """
            Calcula o volume de concreto para as vigas baldrame da fundação.
            
            **Fórmula:** V = L × A × C (por aresta)
            - L = Largura da viga baldrame (informada pelo usuário)
            - A = Altura da viga baldrame (informada pelo usuário)  
            - C = Comprimento da parede (obtida de cada aresta)
            
            **Volume total** = Σ V para todas as arestas
            """
    )
    public ResponseEntity<ConcreteVolumeResponseDTO> calcularVolumeFundacao(
            @Valid @RequestBody ConcreteVolumeRequestDTO request) {

        ConcreteVolumeResponseDTO response = concreteCalculoService.calcularVolumeConcreto(request);
        return ResponseEntity.ok(response);
    }
}
