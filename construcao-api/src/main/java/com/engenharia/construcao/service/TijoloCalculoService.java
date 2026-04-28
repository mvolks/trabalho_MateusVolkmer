package com.engenharia.construcao.service;

import com.engenharia.construcao.dto.ArestaRequestDTO;
import com.engenharia.construcao.dto.TijoloRequestDTO;
import com.engenharia.construcao.dto.TijoloResponseDTO;
import com.engenharia.construcao.dto.TijoloResponseDTO.TijoloDetalheArestaDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pelo cálculo da quantidade de tijolos nas paredes.
 *
 * Fórmula aplicada:
 *  1. Área líquida da parede:
 *       Área_líquida = (Comprimento × Altura) - Área_porta - Área_janela
 *
 *  2. Área de face do tijolo com junta:
 *       Área_tijolo = (Comprimento_tijolo + espessura_junta) × (Altura_tijolo + espessura_junta)
 *
 *  3. Quantidade de tijolos (sem perda):
 *       Qtd = TETO(Área_líquida / Área_tijolo)
 *
 *  4. Quantidade com acréscimo de perda:
 *       Qtd_total = TETO(Qtd × (1 + percentualPerda))
 */
@Service
public class TijoloCalculoService {

    /**
     * Calcula a quantidade total de tijolos para as paredes informadas.
     *
     * @param request DTO com lista de arestas e especificações do tijolo
     * @return DTO com quantidade total e detalhamento por aresta
     */
    public TijoloResponseDTO calcularQuantidadeTijolos(TijoloRequestDTO request) {
        // Dimensões com junta
        double modHorizontal = request.getComprimentoTijolo() + request.getEspessuraJunta();
        double modVertical   = request.getAlturaTijolo()      + request.getEspessuraJunta();
        double areaFaceTijolo = modHorizontal * modVertical;

        List<TijoloDetalheArestaDTO> detalhes = new ArrayList<>();
        double areaTotalLiquida = 0.0;
        int totalSemPerda = 0;
        int totalComPerda = 0;

        for (ArestaRequestDTO aresta : request.getArestas()) {
            double areaLiquida = aresta.getAreaLiquida();
            int qtdSemPerda = (int) Math.ceil(areaLiquida / areaFaceTijolo);
            int qtdComPerda = (int) Math.ceil(qtdSemPerda * (1.0 + request.getPercentualPerda()));

            areaTotalLiquida += areaLiquida;
            totalSemPerda    += qtdSemPerda;
            totalComPerda    += qtdComPerda;

            detalhes.add(TijoloDetalheArestaDTO.builder()
                    .identificador(aresta.getIdentificador() != null
                            ? aresta.getIdentificador() : "Aresta " + (detalhes.size() + 1))
                    .comprimento(aresta.getComprimento())
                    .altura(aresta.getAltura())
                    .possuiPorta(Boolean.TRUE.equals(aresta.getPossuiPorta()))
                    .possuiJanela(Boolean.TRUE.equals(aresta.getPossuiJanela()))
                    .areaLiquidaM2(Math.round(areaLiquida * 100.0) / 100.0)
                    .quantidadeTijolosSemPerda(qtdSemPerda)
                    .quantidadeTijolosComPerda(qtdComPerda)
                    .build());
        }

        String dimensoes = String.format("%.0fx%.0fx%.0f cm",
                request.getLarguraTijolo() * 100,
                request.getAlturaTijolo() * 100,
                request.getComprimentoTijolo() * 100);

        return TijoloResponseDTO.builder()
                .quantidadeTotalComPerda(totalComPerda)
                .quantidadeTotalSemPerda(totalSemPerda)
                .areaTotalLiquidaM2(Math.round(areaTotalLiquida * 100.0) / 100.0)
                .percentualPerdaAplicado(request.getPercentualPerda())
                .formula("Qtd = TETO(Área_líquida / ((C_tijolo + junta) × (A_tijolo + junta))) × (1 + %perda)")
                .dimensoesTijolo(dimensoes)
                .areaFaceTijoloComJunta(Math.round(areaFaceTijolo * 100000.0) / 100000.0)
                .detalhes(detalhes)
                .build();
    }
}
