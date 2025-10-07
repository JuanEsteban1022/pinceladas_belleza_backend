package com.management.backend_pinceladas_belleza.proveedores.repository;

import com.management.backend_pinceladas_belleza.proveedores.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedoresRepository extends JpaRepository<Proveedor, Long> {
}
