package com.example.springbootwebflux;

import com.example.springbootwebflux.models.dao.ProductoDao;
import com.example.springbootwebflux.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

    @Autowired
    private ProductoDao productoDao;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        mongoTemplate.dropCollection("productos").subscribe();

        Flux.just(new Producto("TV Panasonic Pantalla LCD", 456.89),
                new Producto("Sony Camara HD Digital", 177.89),
                new Producto("Apple iPod", 46.89),
                new Producto("Sony Notebook", 846.89),
                new Producto("Hewlett Packard Multifuncional", 200.89),
                new Producto("Bianchi Bicileta", 70.89),
                new Producto("HP Notebook Omen 17", 2500.89),
                new Producto("Mica Cómoda 5 Cajones", 150.89),
                new Producto("TV Sony Bravia OLED 4k Ultra HD", 2255.89)
        )
                .flatMap(producto -> {
                    producto.setCreateAt(new Date());
                    return productoDao.save(producto);
                })
                .subscribe(producto -> log.info("INSERT: " + producto.getId() + " " + producto.getNombre()));
    }
}
