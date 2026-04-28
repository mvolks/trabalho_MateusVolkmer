package com.engenharia.construcao.repository;

import com.engenharia.construcao.model.Aresta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArestaRepository extends JpaRepository<Aresta, Long> {
    List<Aresta> findByNomeCoomodo(String nomeCoomodo);
}
