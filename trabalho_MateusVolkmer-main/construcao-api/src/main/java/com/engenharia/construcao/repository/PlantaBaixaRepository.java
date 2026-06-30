package com.engenharia.construcao.repository;

import com.engenharia.construcao.model.PlantaBaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantaBaixaRepository extends JpaRepository<PlantaBaixa, Long> {
}
