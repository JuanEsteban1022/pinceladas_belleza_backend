package com.management.backend_pinceladas_belleza.proveedores.services;

import com.management.backend_pinceladas_belleza.exception.BadRequestException;
import com.management.backend_pinceladas_belleza.exception.ResourceNotFoundException;
import com.management.backend_pinceladas_belleza.proveedores.dto.ProveedorDto;
import com.management.backend_pinceladas_belleza.proveedores.entity.Proveedor;
import com.management.backend_pinceladas_belleza.proveedores.interfaces.IProveedores;
import com.management.backend_pinceladas_belleza.proveedores.repository.ProveedoresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorService implements IProveedores {

    private final ProveedoresRepository proveedoresRepository;

    @Override
    public List<Proveedor> getProveedor() {
        return proveedoresRepository.findAll();
    }

    @Override
    public Proveedor getProveedorById(Long id) {
        return proveedoresRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", "id", id));
    }

    @Override
    public Proveedor createProveedor(ProveedorDto proveedor) {
        if (proveedor.getNombre() == null || proveedor.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre del proveedor es requerido");
        }
        
        Proveedor proveedorEntity = new Proveedor();
        proveedorEntity.setNombre(proveedor.getNombre());
        proveedorEntity.setContacto(proveedor.getContacto());
        proveedorEntity.setFechaCreated(LocalDate.now());
        return proveedoresRepository.save(proveedorEntity);
    }

    @Override
    public Proveedor updateProveedor(Proveedor proveedor) {
        if (proveedor.getId() == null) {
            throw new BadRequestException("El ID del proveedor es requerido para actualizar");
        }
        
        Proveedor proveedorEntity = getProveedorById(proveedor.getId());
        proveedorEntity.setNombre(proveedor.getNombre());
        proveedorEntity.setContacto(proveedor.getContacto());
        return proveedoresRepository.save(proveedorEntity);
    }

    @Override
    public String deleteProveedor(Long id) {
        Proveedor proveedorEntity = getProveedorById(id);
        proveedoresRepository.deleteById(id);
        return "Proveedor eliminado exitosamente";
    }
}
