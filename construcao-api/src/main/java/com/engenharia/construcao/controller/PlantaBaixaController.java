package com.engenharia.construcao.controller;

import com.engenharia.construcao.model.PlantaBaixa;
import com.engenharia.construcao.service.PlantaBaixaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento da Planta Baixa (Grafo G=(V,A)).
 *
 * Etapa 1 — Modelar Planta Baixa
 */
@RestController
@RequestMapping("/api/v1/plantas")
@RequiredArgsConstructor
@Tag(name = "Planta Baixa", description = "Gerenciamento da planta baixa como Grafo G=(V,A)")
public class PlantaBaixaController {

    private final PlantaBaixaService plantaBaixaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar uma nova planta baixa com vértices, arestas e cômodos")
    public ResponseEntity<PlantaBaixa> criar(@Valid @RequestBody PlantaBaixa plantaBaixa) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(plantaBaixaService.salvar(plantaBaixa));
    }

    @GetMapping
    @Operation(summary = "Listar todas as plantas baixas cadastradas")
    public ResponseEntity<List<PlantaBaixa>> listar() {
        return ResponseEntity.ok(plantaBaixaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar planta baixa por ID")
    public ResponseEntity<PlantaBaixa> buscarPorId(@PathVariable Long id) {
        return plantaBaixaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover planta baixa")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        plantaBaixaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
