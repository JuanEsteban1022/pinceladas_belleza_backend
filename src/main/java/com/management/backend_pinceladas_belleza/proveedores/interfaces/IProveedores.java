package com.management.backend_pinceladas_belleza.proveedores.interfaces;

import com.management.backend_pinceladas_belleza.proveedores.dto.ProveedorDto;
import com.management.backend_pinceladas_belleza.proveedores.entity.Proveedor;

import java.util.List;

public interface IProveedores {
    List<Proveedor> getProveedor();

    Proveedor getProveedorById(Long id);

    Proveedor createProveedor(ProveedorDto proveedor);

    Proveedor updateProveedor(Proveedor proveedor);

    String deleteProveedor(Long id);
}
