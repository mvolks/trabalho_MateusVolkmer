package com.engenharia.construcao.service;

import com.engenharia.construcao.dto.ArestaRequestDTO;
import com.engenharia.construcao.dto.ConcreteVolumeRequestDTO;
import com.engenharia.construcao.dto.ConcreteVolumeResponseDTO;
import com.engenharia.construcao.dto.ConcreteVolumeResponseDTO.ConcreteDetalheArestaDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pelo cálculo do volume de concreto
 * para as vigas baldrame da fundação.
 *
 * Fórmula aplicada por aresta:
 *   V = L x A x C
 *   Onde:
 *     L = Largura da viga baldrame (fornecida pelo usuário)
 *     A = Altura da viga baldrame (fornecida pelo usuário)
 *     C = Comprimento da parede (obtida da aresta)
 *
 * Volume total = Somatório de V para todas as arestas
 */
@Service
public class ConcreteCalculoService {

    /**
     * Calcula o volume total de concreto para as vigas baldrame.
     *
     * @param request DTO com lista de arestas e dimensões da viga
     * @return DTO com volume total e detalhamento por aresta
     */
    public ConcreteVolumeResponseDTO calcularVolumeConcreto(ConcreteVolumeRequestDTO request) {
        List<ConcreteDetalheArestaDTO> detalhes = new ArrayList<>();
        double volumeTotal = 0.0;
        double comprimentoTotal = 0.0;

        for (ArestaRequestDTO aresta : request.getArestas()) {
            // V = L x A x C  (Largura x Altura x Comprimento)
            double volume = request.getLarguraViga()
                    * request.getAlturaViga()
                    * aresta.getComprimento();

            volumeTotal += volume;
            comprimentoTotal += aresta.getComprimento();

            detalhes.add(ConcreteDetalheArestaDTO.builder()
                    .identificador(aresta.getIdentificador() != null
                            ? aresta.getIdentificador() : "Aresta " + (detalhes.size() + 1))
                    .comprimento(aresta.getComprimento())
                    .larguraViga(request.getLarguraViga())
                    .alturaViga(request.getAlturaViga())
                    .volumeM3(Math.round(volume * 10000.0) / 10000.0)
                    .build());
        }

        return ConcreteVolumeResponseDTO.builder()
                .volumeTotalM3(Math.round(volumeTotal * 10000.0) / 10000.0)
                .quantidadeArestas(request.getArestas().size())
                .comprimentoTotalParedes(Math.round(comprimentoTotal * 100.0) / 100.0)
                .alturaViga(request.getAlturaViga())
                .larguraViga(request.getLarguraViga())
                .formula("V = L × A × C (por aresta), onde L=Largura da viga, A=Altura da viga, C=Comprimento da parede")
                .detalhes(detalhes)
                .build();
    }
}
