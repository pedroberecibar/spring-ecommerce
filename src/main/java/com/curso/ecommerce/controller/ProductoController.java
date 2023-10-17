package com.curso.ecommerce.controller;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private  final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping("")
    public String show(){
        return "productos/show";
    }

    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto){
        Usuario u = new Usuario(
                1L,
                "Juan",
                "juanperez21",
                "jperez@gmail.com",
                "huelva 1433",
                "3518462894",
                "ADMIN",
                "pepito1234",
                new ArrayList<>(), // Lista de productos (puede estar vacía si es necesario)
                new ArrayList<>()  // Lista de ordenes (puede estar vacía si es necesario)
        );

        producto.setUsuario(u);
        LOGGER.info("Este es el objeto Usuario{}", u);
        LOGGER.info("Este es el objeto Producto{}", producto);
        productoService.save(producto);
        return "redirect:/productos";
    }

}
