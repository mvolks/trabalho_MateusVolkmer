package com.engenharia.construcao.web.bean;

import com.engenharia.construcao.model.Vertice;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Named;

import java.util.List;

/**
 * Converte o identificador textual do vértice (usado no value do <option>)
 * de volta para a instância de Vertice presente na lista atual do bean,
 * permitindo usar h:selectOneMenu com objetos Vertice antes de persistir
 * (quando ainda não existe um ID gerado pelo banco).
 */
@Named("verticeConverter")
@FacesConverter(value = "verticeConverter", managed = true)
public class VerticeConverter implements Converter<Vertice> {

    private final PlantaBaixaBean plantaBaixaBean;

    public VerticeConverter() {
        FacesContext context = FacesContext.getCurrentInstance();
        this.plantaBaixaBean = context.getApplication()
                .evaluateExpressionGet(context, "#{plantaBaixaBean}", PlantaBaixaBean.class);
    }

    @Override
    public Vertice getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        List<Vertice> vertices = plantaBaixaBean.getVertices();
        return vertices.stream()
                .filter(v -> v.getIdentificador().equals(value))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Vertice value) {
        if (value == null) {
            return "";
        }
        return value.getIdentificador();
    }
}
