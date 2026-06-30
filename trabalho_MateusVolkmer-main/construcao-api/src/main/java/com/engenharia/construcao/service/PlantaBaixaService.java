package com.engenharia.construcao.service;

import com.engenharia.construcao.model.PlantaBaixa;
import com.engenharia.construcao.repository.PlantaBaixaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de gerenciamento da Planta Baixa (Grafo G=(V,A)).
 */
@Service
@RequiredArgsConstructor
public class PlantaBaixaService {

    private final PlantaBaixaRepository plantaBaixaRepository;

    @Transactional
    public PlantaBaixa salvar(PlantaBaixa plantaBaixa) {
        return plantaBaixaRepository.save(plantaBaixa);
    }

    @Transactional(readOnly = true)
    public List<PlantaBaixa> listarTodas() {
        return plantaBaixaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<PlantaBaixa> buscarPorId(Long id) {
        return plantaBaixaRepository.findById(id);
    }

    @Transactional
    public void deletar(Long id) {
        plantaBaixaRepository.deleteById(id);
    }
}
