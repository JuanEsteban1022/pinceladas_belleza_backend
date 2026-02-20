package com.management.backend_pinceladas_belleza.productos.controller;

import com.management.backend_pinceladas_belleza.productos.interfaces.IProductos;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class ProductosControllerTestConfig {

    @Bean
    @Primary
    public IProductos productos() {
        return mock(IProductos.class);
    }
}
