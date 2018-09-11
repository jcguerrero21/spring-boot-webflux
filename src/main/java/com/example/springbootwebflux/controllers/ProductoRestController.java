package com.example.springbootwebflux.controllers;

import com.example.springbootwebflux.models.dao.ProductoDao;
import com.example.springbootwebflux.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    @Autowired
    private ProductoDao productoDao;

    private static final Logger log = LoggerFactory.getLogger(ProductoRestController.class);

    @GetMapping()
    public Flux<Producto> index(){
        Flux<Producto> productos = productoDao.findAll().map(producto -> {

            producto.setNombre(producto.getNombre().toUpperCase());
            return producto;
        }).doOnNext(producto -> log.info(producto.getNombre()));

        return productos;
    }

    @GetMapping("/{id}")
    public Mono<Producto> show(@PathVariable String id) {

        //Mono<Producto> producto = productoDao.findById(id);   //Esta forma es más rápida
        Flux<Producto> productos = productoDao.findAll();

        Mono<Producto> producto = productos.filter(p -> p.getId().equals(id)).next(); //pasamos somo el primer item del listado flux ccn el .next

        return producto;

    }
}
