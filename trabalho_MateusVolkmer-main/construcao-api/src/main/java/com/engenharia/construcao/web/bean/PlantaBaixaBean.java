package com.engenharia.construcao.web.bean;

import com.engenharia.construcao.model.Aresta;
import com.engenharia.construcao.model.Comodo;
import com.engenharia.construcao.model.PlantaBaixa;
import com.engenharia.construcao.model.Vertice;
import com.engenharia.construcao.service.PlantaBaixaService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Managed Bean da tela de submissão de orçamento (Etapa 1).
 *
 * Permite ao usuário informar a planta da casa de forma estruturada,
 * cadastrando o grafo G=(V,A): vértices (pilares) e arestas (paredes),
 * além dos cômodos formados por essas paredes.
 *
 * Ciclo de uso esperado pelo usuário:
 *   1. Informar dados gerais da planta (nome/descrição)
 *   2. Adicionar vértices (pilares) com suas coordenadas
 *   3. Adicionar arestas (paredes) ligando dois vértices já cadastrados
 *   4. Adicionar cômodos (informando nome e dimensões)
 *   5. Enviar a solicitação de orçamento (persiste a planta baixa)
 */
@Named
@ViewScoped
public class PlantaBaixaBean implements Serializable {

    @Inject
    private transient PlantaBaixaService plantaBaixaService;

    // Dados gerais da planta
    private String nomePlanta;
    private String descricaoPlanta;

    // Listas que compõem o grafo da planta
    private List<Vertice> vertices = new ArrayList<>();
    private List<Aresta> arestas = new ArrayList<>();
    private List<Comodo> comodos = new ArrayList<>();

    // Campos auxiliares para o formulário de novo vértice
    private String novoVerticeIdentificador;
    private Double novoVerticeX;
    private Double novoVerticeY;
    private String novoVerticeDescricao;

    // Campos auxiliares para o formulário de nova aresta (parede)
    private String novaArestaIdentificador;
    private Vertice novaArestaOrigem;
    private Vertice novaArestaDestino;
    private Double novaArestaComprimento;
    private Double novaArestaEspessura = 0.14;
    private Double novaArestaAltura = 2.80;
    private Boolean novaArestaPossuiPorta = false;
    private Double novaArestaAlturaPorta;
    private Double novaArestaLarguraPorta;
    private Boolean novaArestaPossuiJanela = false;
    private Double novaArestaAlturaJanela;
    private Double novaArestaLarguraJanela;
    private String novaArestaNomeComodo;

    // Campos auxiliares para o formulário de novo cômodo
    private String novoComodoNome;
    private Double novoComodoLargura;
    private Double novoComodoComprimento;
    private Double novoComodoAltura = 2.80;

    // Identificação do orçamento gerado após o envio
    private Long idOrcamentoGerado;

    @PostConstruct
    public void init() {
        // Tela inicia vazia, pronta para o usuário compor o grafo da planta
    }

    // ---------- Vértices ----------

    public void adicionarVertice() {
        if (novoVerticeIdentificador == null || novoVerticeIdentificador.isBlank()
                || novoVerticeX == null || novoVerticeY == null) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN,
                    "Informe identificador e coordenadas X/Y do pilar.");
            return;
        }

        Vertice vertice = Vertice.builder()
                .identificador(novoVerticeIdentificador)
                .coordenadaX(novoVerticeX)
                .coordenadaY(novoVerticeY)
                .descricao(novoVerticeDescricao)
                .build();

        vertices.add(vertice);

        novoVerticeIdentificador = null;
        novoVerticeX = null;
        novoVerticeY = null;
        novoVerticeDescricao = null;

        adicionarMensagem(FacesMessage.SEVERITY_INFO, "Pilar adicionado com sucesso.");
    }

    public void removerVertice(Vertice vertice) {
        vertices.remove(vertice);
        adicionarMensagem(FacesMessage.SEVERITY_INFO, "Pilar removido.");
    }

    // ---------- Arestas (paredes) ----------

    public void adicionarAresta() {
        if (novaArestaIdentificador == null || novaArestaIdentificador.isBlank()
                || novaArestaOrigem == null || novaArestaDestino == null
                || novaArestaComprimento == null || novaArestaEspessura == null
                || novaArestaAltura == null) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN,
                    "Preencha identificador, pilares de origem/destino, comprimento, espessura e altura da parede.");
            return;
        }

        if (novaArestaOrigem.equals(novaArestaDestino)) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN,
                    "O pilar de origem deve ser diferente do pilar de destino.");
            return;
        }

        Aresta aresta = Aresta.builder()
                .identificador(novaArestaIdentificador)
                .verticeOrigem(novaArestaOrigem)
                .verticeDestino(novaArestaDestino)
                .comprimento(novaArestaComprimento)
                .espessura(novaArestaEspessura)
                .altura(novaArestaAltura)
                .possuiPorta(Boolean.TRUE.equals(novaArestaPossuiPorta))
                .alturaPorta(novaArestaAlturaPorta)
                .larguraPorta(novaArestaLarguraPorta)
                .possuiJanela(Boolean.TRUE.equals(novaArestaPossuiJanela))
                .alturaJanela(novaArestaAlturaJanela)
                .larguraJanela(novaArestaLarguraJanela)
                .nomeCoomodo(novaArestaNomeComodo)
                .build();

        arestas.add(aresta);

        novaArestaIdentificador = null;
        novaArestaOrigem = null;
        novaArestaDestino = null;
        novaArestaComprimento = null;
        novaArestaPossuiPorta = false;
        novaArestaAlturaPorta = null;
        novaArestaLarguraPorta = null;
        novaArestaPossuiJanela = false;
        novaArestaAlturaJanela = null;
        novaArestaLarguraJanela = null;
        novaArestaNomeComodo = null;

        adicionarMensagem(FacesMessage.SEVERITY_INFO, "Parede adicionada com sucesso.");
    }

    public void removerAresta(Aresta aresta) {
        arestas.remove(aresta);
        adicionarMensagem(FacesMessage.SEVERITY_INFO, "Parede removida.");
    }

    // ---------- Cômodos ----------

    public void adicionarComodo() {
        if (novoComodoNome == null || novoComodoNome.isBlank()
                || novoComodoLargura == null || novoComodoComprimento == null
                || novoComodoAltura == null) {
            adicionarMensagem(FacesMessage.SEVERITY_WARN,
                    "Informe nome, largura, comprimento e altura do cômodo.");
            return;
        }

        Comodo comodo = Comodo.builder()
                .nome(novoComodoNome)
                .largura(novoComodoLargura)
                .comprimento(novoComodoComprimento)
                .altura(novoComodoAltura)
                .build();

        comodos.add(comodo);

        novoComodoNome = null;
        novoComodoLargura = null;
        novoComodoComprimento = null;

        adicionarMensagem(FacesMessage.SEVERITY_INFO, "Cômodo adicionado com sucesso.");
    }

    public void removerComodo(Comodo comodo) {
        comodos.remove(comodo);
        adicionarMensagem(FacesMessage.SEVERITY_INFO, "Cômodo removido.");
    }

    // ---------- Submissão do orçamento ----------

    public String enviarSolicitacao() {
        if (nomePlanta == null || nomePlanta.isBlank()) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Informe o nome do projeto/planta.");
            return null;
        }
        if (vertices.isEmpty()) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Cadastre ao menos um pilar (vértice).");
            return null;
        }
        if (arestas.isEmpty()) {
            adicionarMensagem(FacesMessage.SEVERITY_ERROR, "Cadastre ao menos uma parede (aresta).");
            return null;
        }

        PlantaBaixa plantaBaixa = PlantaBaixa.builder()
                .nome(nomePlanta)
                .descricao(descricaoPlanta)
                .vertices(new ArrayList<>(vertices))
                .arestas(new ArrayList<>(arestas))
                .comodos(new ArrayList<>(comodos))
                .build();

        PlantaBaixa salva = plantaBaixaService.salvar(plantaBaixa);
        this.idOrcamentoGerado = salva.getId();

        adicionarMensagem(FacesMessage.SEVERITY_INFO,
                "Solicitação de orçamento enviada com sucesso! Número do orçamento: " + salva.getId());

        return null; // mantém na mesma view para exibir o número gerado
    }

    public void novaSolicitacao() {
        nomePlanta = null;
        descricaoPlanta = null;
        vertices = new ArrayList<>();
        arestas = new ArrayList<>();
        comodos = new ArrayList<>();
        idOrcamentoGerado = null;
    }

    private void adicionarMensagem(FacesMessage.Severity severidade, String texto) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severidade, texto, null));
    }

    // ---------- Getters/Setters ----------

    public String getNomePlanta() {
        return nomePlanta;
    }

    public void setNomePlanta(String nomePlanta) {
        this.nomePlanta = nomePlanta;
    }

    public String getDescricaoPlanta() {
        return descricaoPlanta;
    }

    public void setDescricaoPlanta(String descricaoPlanta) {
        this.descricaoPlanta = descricaoPlanta;
    }

    public List<Vertice> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertice> vertices) {
        this.vertices = vertices;
    }

    public List<Aresta> getArestas() {
        return arestas;
    }

    public void setArestas(List<Aresta> arestas) {
        this.arestas = arestas;
    }

    public List<Comodo> getComodos() {
        return comodos;
    }

    public void setComodos(List<Comodo> comodos) {
        this.comodos = comodos;
    }

    public String getNovoVerticeIdentificador() {
        return novoVerticeIdentificador;
    }

    public void setNovoVerticeIdentificador(String novoVerticeIdentificador) {
        this.novoVerticeIdentificador = novoVerticeIdentificador;
    }

    public Double getNovoVerticeX() {
        return novoVerticeX;
    }

    public void setNovoVerticeX(Double novoVerticeX) {
        this.novoVerticeX = novoVerticeX;
    }

    public Double getNovoVerticeY() {
        return novoVerticeY;
    }

    public void setNovoVerticeY(Double novoVerticeY) {
        this.novoVerticeY = novoVerticeY;
    }

    public String getNovoVerticeDescricao() {
        return novoVerticeDescricao;
    }

    public void setNovoVerticeDescricao(String novoVerticeDescricao) {
        this.novoVerticeDescricao = novoVerticeDescricao;
    }

    public String getNovaArestaIdentificador() {
        return novaArestaIdentificador;
    }

    public void setNovaArestaIdentificador(String novaArestaIdentificador) {
        this.novaArestaIdentificador = novaArestaIdentificador;
    }

    public Vertice getNovaArestaOrigem() {
        return novaArestaOrigem;
    }

    public void setNovaArestaOrigem(Vertice novaArestaOrigem) {
        this.novaArestaOrigem = novaArestaOrigem;
    }

    public Vertice getNovaArestaDestino() {
        return novaArestaDestino;
    }

    public void setNovaArestaDestino(Vertice novaArestaDestino) {
        this.novaArestaDestino = novaArestaDestino;
    }

    public Double getNovaArestaComprimento() {
        return novaArestaComprimento;
    }

    public void setNovaArestaComprimento(Double novaArestaComprimento) {
        this.novaArestaComprimento = novaArestaComprimento;
    }

    public Double getNovaArestaEspessura() {
        return novaArestaEspessura;
    }

    public void setNovaArestaEspessura(Double novaArestaEspessura) {
        this.novaArestaEspessura = novaArestaEspessura;
    }

    public Double getNovaArestaAltura() {
        return novaArestaAltura;
    }

    public void setNovaArestaAltura(Double novaArestaAltura) {
        this.novaArestaAltura = novaArestaAltura;
    }

    public Boolean getNovaArestaPossuiPorta() {
        return novaArestaPossuiPorta;
    }

    public void setNovaArestaPossuiPorta(Boolean novaArestaPossuiPorta) {
        this.novaArestaPossuiPorta = novaArestaPossuiPorta;
    }

    public Double getNovaArestaAlturaPorta() {
        return novaArestaAlturaPorta;
    }

    public void setNovaArestaAlturaPorta(Double novaArestaAlturaPorta) {
        this.novaArestaAlturaPorta = novaArestaAlturaPorta;
    }

    public Double getNovaArestaLarguraPorta() {
        return novaArestaLarguraPorta;
    }

    public void setNovaArestaLarguraPorta(Double novaArestaLarguraPorta) {
        this.novaArestaLarguraPorta = novaArestaLarguraPorta;
    }

    public Boolean getNovaArestaPossuiJanela() {
        return novaArestaPossuiJanela;
    }

    public void setNovaArestaPossuiJanela(Boolean novaArestaPossuiJanela) {
        this.novaArestaPossuiJanela = novaArestaPossuiJanela;
    }

    public Double getNovaArestaAlturaJanela() {
        return novaArestaAlturaJanela;
    }

    public void setNovaArestaAlturaJanela(Double novaArestaAlturaJanela) {
        this.novaArestaAlturaJanela = novaArestaAlturaJanela;
    }

    public Double getNovaArestaLarguraJanela() {
        return novaArestaLarguraJanela;
    }

    public void setNovaArestaLarguraJanela(Double novaArestaLarguraJanela) {
        this.novaArestaLarguraJanela = novaArestaLarguraJanela;
    }

    public String getNovaArestaNomeComodo() {
        return novaArestaNomeComodo;
    }

    public void setNovaArestaNomeComodo(String novaArestaNomeComodo) {
        this.novaArestaNomeComodo = novaArestaNomeComodo;
    }

    public String getNovoComodoNome() {
        return novoComodoNome;
    }

    public void setNovoComodoNome(String novoComodoNome) {
        this.novoComodoNome = novoComodoNome;
    }

    public Double getNovoComodoLargura() {
        return novoComodoLargura;
    }

    public void setNovoComodoLargura(Double novoComodoLargura) {
        this.novoComodoLargura = novoComodoLargura;
    }

    public Double getNovoComodoComprimento() {
        return novoComodoComprimento;
    }

    public void setNovoComodoComprimento(Double novoComodoComprimento) {
        this.novoComodoComprimento = novoComodoComprimento;
    }

    public Double getNovoComodoAltura() {
        return novoComodoAltura;
    }

    public void setNovoComodoAltura(Double novoComodoAltura) {
        this.novoComodoAltura = novoComodoAltura;
    }

    public Long getIdOrcamentoGerado() {
        return idOrcamentoGerado;
    }
}
