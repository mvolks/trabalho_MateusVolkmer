package com.engenharia.construcao;

import com.engenharia.construcao.dto.ArestaRequestDTO;
import com.engenharia.construcao.dto.ConcreteVolumeRequestDTO;
import com.engenharia.construcao.dto.ConcreteVolumeResponseDTO;
import com.engenharia.construcao.dto.TijoloRequestDTO;
import com.engenharia.construcao.dto.TijoloResponseDTO;
import com.engenharia.construcao.service.ConcreteCalculoService;
import com.engenharia.construcao.service.TijoloCalculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para os serviços de cálculo de engenharia civil.
 *
 * Cenário: Casa residencial de exemplo com 4 ambientes
 * - Sala:     5.0 × 4.0 m  → parede de 5.0 m + parede de 4.0 m
 * - Quarto 1: 3.0 × 3.0 m  → parede de 3.0 m + parede de 3.0 m
 * - Cozinha:  3.0 × 2.5 m  → parede de 3.0 m + parede de 2.5 m
 * - Banheiro: 2.0 × 1.5 m  → parede de 2.0 m + parede de 1.5 m
 */
class ConstrucaoServiceTest {

    private ConcreteCalculoService concreteService;
    private TijoloCalculoService tijoloService;

    private List<ArestaRequestDTO> arestas;

    @BeforeEach
    void setUp() {
        concreteService = new ConcreteCalculoService();
        tijoloService   = new TijoloCalculoService();

        // Monta as arestas da planta baixa de exemplo
        arestas = List.of(
            // Sala
            ArestaRequestDTO.builder()
                .identificador("A1-Sala-Frente")
                .comprimento(5.0).espessura(0.14).altura(2.80)
                .possuiPorta(true).alturaPorta(2.10).larguraPorta(0.90)
                .possuiJanela(false)
                .build(),
            ArestaRequestDTO.builder()
                .identificador("A2-Sala-Lateral")
                .comprimento(4.0).espessura(0.14).altura(2.80)
                .possuiPorta(false)
                .possuiJanela(true).alturaJanela(1.20).larguraJanela(1.50)
                .build(),
            // Quarto 1
            ArestaRequestDTO.builder()
                .identificador("A3-Quarto1-Frente")
                .comprimento(3.0).espessura(0.14).altura(2.80)
                .possuiPorta(true).alturaPorta(2.10).larguraPorta(0.80)
                .possuiJanela(false)
                .build(),
            ArestaRequestDTO.builder()
                .identificador("A4-Quarto1-Lateral")
                .comprimento(3.0).espessura(0.14).altura(2.80)
                .possuiPorta(false)
                .possuiJanela(true).alturaJanela(1.00).larguraJanela(1.20)
                .build(),
            // Cozinha
            ArestaRequestDTO.builder()
                .identificador("A5-Cozinha-Frente")
                .comprimento(3.0).espessura(0.14).altura(2.80)
                .possuiPorta(true).alturaPorta(2.10).larguraPorta(0.80)
                .possuiJanela(true).alturaJanela(0.80).larguraJanela(1.00)
                .build(),
            ArestaRequestDTO.builder()
                .identificador("A6-Cozinha-Lateral")
                .comprimento(2.5).espessura(0.14).altura(2.80)
                .possuiPorta(false).possuiJanela(false)
                .build(),
            // Banheiro
            ArestaRequestDTO.builder()
                .identificador("A7-Banheiro-Frente")
                .comprimento(2.0).espessura(0.14).altura(2.80)
                .possuiPorta(true).alturaPorta(2.10).larguraPorta(0.70)
                .possuiJanela(false)
                .build(),
            ArestaRequestDTO.builder()
                .identificador("A8-Banheiro-Lateral")
                .comprimento(1.5).espessura(0.14).altura(2.80)
                .possuiPorta(false)
                .possuiJanela(true).alturaJanela(0.60).larguraJanela(0.60)
                .build()
        );
    }

    // ======================================================
    // ETAPA 2 — Volume de Concreto (Viga Baldrame)
    // ======================================================

    @Test
    @DisplayName("ET2-T1: Deve calcular volume de concreto com parâmetros válidos")
    void deveCalcularVolumeConcreto() {
        ConcreteVolumeRequestDTO request = ConcreteVolumeRequestDTO.builder()
                .arestas(arestas)
                .alturaViga(0.50)   // 50 cm
                .larguraViga(0.20)  // 20 cm
                .build();

        ConcreteVolumeResponseDTO response = concreteService.calcularVolumeConcreto(request);

        assertNotNull(response);
        assertEquals(8, response.getQuantidadeArestas());
        assertTrue(response.getVolumeTotalM3() > 0, "Volume total deve ser positivo");

        // Comprimento total = 5+4+3+3+3+2.5+2+1.5 = 24.0 m
        assertEquals(24.0, response.getComprimentoTotalParedes(), 0.01);

        // V = L x A x C_total = 0.20 x 0.50 x 24.0 = 2.40 m³
        assertEquals(2.40, response.getVolumeTotalM3(), 0.01);

        assertNotNull(response.getFormula());
        assertEquals(8, response.getDetalhes().size());

        System.out.println("=== ETAPA 2: Volume de Concreto (Viga Baldrame) ===");
        System.out.printf("Comprimento total das paredes : %.2f m%n", response.getComprimentoTotalParedes());
        System.out.printf("Largura da viga               : %.2f m%n", response.getLarguraViga());
        System.out.printf("Altura da viga                : %.2f m%n", response.getAlturaViga());
        System.out.printf("VOLUME TOTAL DE CONCRETO      : %.4f m³%n", response.getVolumeTotalM3());
        System.out.println("--- Detalhamento por aresta ---");
        response.getDetalhes().forEach(d ->
            System.out.printf("  %-30s | C=%.2fm | V=%.4f m³%n",
                d.getIdentificador(), d.getComprimento(), d.getVolumeM3())
        );
    }

    @Test
    @DisplayName("ET2-T2: Deve retornar zero quando lista de arestas é vazia")
    void deveRetornarZeroParaListaVazia() {
        ConcreteVolumeRequestDTO request = ConcreteVolumeRequestDTO.builder()
                .arestas(List.of())
                .alturaViga(0.50)
                .larguraViga(0.20)
                .build();

        ConcreteVolumeResponseDTO response = concreteService.calcularVolumeConcreto(request);

        assertEquals(0.0, response.getVolumeTotalM3());
        assertEquals(0, response.getQuantidadeArestas());
    }

    @Test
    @DisplayName("ET2-T3: Deve calcular corretamente viga única")
    void deveCalcularVigaUnica() {
        ArestaRequestDTO arestaUnica = ArestaRequestDTO.builder()
                .identificador("P1")
                .comprimento(10.0).espessura(0.14).altura(2.80)
                .possuiPorta(false).possuiJanela(false)
                .build();

        ConcreteVolumeRequestDTO request = ConcreteVolumeRequestDTO.builder()
                .arestas(List.of(arestaUnica))
                .alturaViga(0.60)
                .larguraViga(0.25)
                .build();

        ConcreteVolumeResponseDTO response = concreteService.calcularVolumeConcreto(request);

        // V = 0.25 x 0.60 x 10.0 = 1.50 m³
        assertEquals(1.50, response.getVolumeTotalM3(), 0.001);
    }

    // ======================================================
    // ETAPA 3 — Quantidade de Tijolos
    // ======================================================

    @Test
    @DisplayName("ET3-T1: Deve calcular quantidade de tijolos com parâmetros válidos")
    void deveCalcularQuantidadeTijolos() {
        TijoloRequestDTO request = TijoloRequestDTO.builder()
                .arestas(arestas)
                .larguraTijolo(0.09)        // 9 cm
                .alturaTijolo(0.19)         // 19 cm
                .comprimentoTijolo(0.29)    // 29 cm
                .espessuraJunta(0.01)       // 1 cm
                .percentualPerda(0.10)      // 10%
                .build();

        TijoloResponseDTO response = tijoloService.calcularQuantidadeTijolos(request);

        assertNotNull(response);
        assertTrue(response.getQuantidadeTotalSemPerda() > 0);
        assertTrue(response.getQuantidadeTotalComPerda() >= response.getQuantidadeTotalSemPerda());
        assertTrue(response.getAreaTotalLiquidaM2() > 0);

        // Área de face do tijolo com junta = (0.29+0.01) x (0.19+0.01) = 0.30 x 0.20 = 0.06 m²
        assertEquals(0.06, response.getAreaFaceTijoloComJunta(), 0.0001);

        System.out.println("\n=== ETAPA 3: Quantidade de Tijolos (9x19x29 cm) ===");
        System.out.printf("Área total líquida das paredes: %.2f m²%n", response.getAreaTotalLiquidaM2());
        System.out.printf("Área de face do tijolo c/junta: %.4f m²%n", response.getAreaFaceTijoloComJunta());
        System.out.printf("Dimensões do tijolo           : %s%n", response.getDimensoesTijolo());
        System.out.printf("Tijolos sem perda             : %d un%n", response.getQuantidadeTotalSemPerda());
        System.out.printf("Percentual de perda aplicado  : %.0f%%%n", response.getPercentualPerdaAplicado() * 100);
        System.out.printf("TIJOLOS TOTAL COM PERDA       : %d un%n", response.getQuantidadeTotalComPerda());
        System.out.println("--- Detalhamento por aresta ---");
        response.getDetalhes().forEach(d ->
            System.out.printf("  %-30s | Área=%.2fm² | Qtd=%d | c/perda=%d%n",
                d.getIdentificador(), d.getAreaLiquidaM2(),
                d.getQuantidadeTijolosSemPerda(), d.getQuantidadeTijolosComPerda())
        );
    }

    @Test
    @DisplayName("ET3-T2: Parede sem abertura deve usar área bruta")
    void paredesSemAberturaDeveUsarAreaBruta() {
        ArestaRequestDTO parede = ArestaRequestDTO.builder()
                .identificador("P-Solida")
                .comprimento(4.0).espessura(0.14).altura(2.80)
                .possuiPorta(false).possuiJanela(false)
                .build();

        // Área esperada = 4.0 x 2.80 = 11.20 m²
        assertEquals(11.20, parede.getAreaLiquida(), 0.001);

        TijoloRequestDTO request = TijoloRequestDTO.builder()
                .arestas(List.of(parede))
                .larguraTijolo(0.09).alturaTijolo(0.19).comprimentoTijolo(0.29)
                .espessuraJunta(0.01).percentualPerda(0.0)
                .build();

        TijoloResponseDTO response = tijoloService.calcularQuantidadeTijolos(request);

        // Área do tijolo = 0.06 m², tijolos = TETO(11.20 / 0.06) = TETO(186,67) = 187
        assertEquals(187, response.getQuantidadeTotalSemPerda());
    }

    @Test
    @DisplayName("ET3-T3: Deve descontar abertura de porta e janela na mesma parede")
    void deveDescontarAberturaPortaEJanela() {
        ArestaRequestDTO parede = ArestaRequestDTO.builder()
                .identificador("P-Com-Abertura")
                .comprimento(3.0).espessura(0.14).altura(2.80)
                .possuiPorta(true).alturaPorta(2.10).larguraPorta(0.80)
                .possuiJanela(true).alturaJanela(0.80).larguraJanela(1.00)
                .build();

        // Área bruta = 3.0 x 2.80 = 8.40 m²
        // Área porta  = 2.10 x 0.80 = 1.68 m²
        // Área janela = 0.80 x 1.00 = 0.80 m²
        // Área líquida = 8.40 - 1.68 - 0.80 = 5.92 m²
        assertEquals(5.92, parede.getAreaLiquida(), 0.001);
    }
}
