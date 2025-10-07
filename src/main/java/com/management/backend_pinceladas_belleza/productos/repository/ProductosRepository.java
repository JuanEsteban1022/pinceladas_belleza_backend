package com.management.backend_pinceladas_belleza.productos.repository;

import com.management.backend_pinceladas_belleza.productos.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductosRepository extends JpaRepository<Productos, Long> {
}
