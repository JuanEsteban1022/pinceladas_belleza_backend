package com.management.backend_pinceladas_belleza.categorias.repository;

import com.management.backend_pinceladas_belleza.categorias.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
