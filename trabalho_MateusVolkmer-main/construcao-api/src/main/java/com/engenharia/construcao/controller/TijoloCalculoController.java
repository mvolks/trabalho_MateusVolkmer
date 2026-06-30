package com.engenharia.construcao.controller;

import com.engenharia.construcao.dto.TijoloRequestDTO;
import com.engenharia.construcao.dto.TijoloResponseDTO;
import com.engenharia.construcao.service.TijoloCalculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para cálculo da quantidade de tijolos nas paredes.
 *
 * Etapa 3 — Cálculo da Quantidade de Tijolos de Paredes
 */
@RestController
@RequestMapping("/api/v1/tijolos")
@RequiredArgsConstructor
@Tag(name = "Tijolos", description = "Cálculo de quantidade de tijolos para paredes")
public class TijoloCalculoController {

    private final TijoloCalculoService tijoloCalculoService;

    /**
     * POST /api/v1/tijolos/quantidade-paredes
     *
     * Recebe uma lista de arestas e as dimensões do tijolo,
     * calcula a quantidade necessária de tijolos descontando aberturas.
     *
     * @param request DTO com arestas e dimensões do tijolo
     * @return Quantidade total e detalhamento por aresta
     */
    @PostMapping("/quantidade-paredes")
    @Operation(
        summary = "Calcular quantidade de tijolos para paredes",
        description = """
            Calcula a quantidade de tijolos necessários para as paredes.
            
            **Fórmula:**
            1. Área líquida = (Comprimento × Altura) - Área_porta - Área_janela
            2. Área do tijolo com junta = (C_tijolo + junta) × (A_tijolo + junta)
            3. Qtd sem perda = TETO(Área_líquida / Área_tijolo)
            4. Qtd total = TETO(Qtd × (1 + %perda))
            
            **Padrão ABNT:** tijolo 9×19×29 cm com junta de 1 cm
            - Módulo horizontal: 30 cm | Módulo vertical: 20 cm
            - Área de face com junta: 0.06 m²  → ~16,67 tijolos/m²
            """
    )
    public ResponseEntity<TijoloResponseDTO> calcularQuantidadeTijolos(
            @Valid @RequestBody TijoloRequestDTO request) {

        TijoloResponseDTO response = tijoloCalculoService.calcularQuantidadeTijolos(request);
        return ResponseEntity.ok(response);
    }
}
