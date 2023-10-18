package com.curso.ecommerce.controller;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private  final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @Autowired
    private UploadFileService upload;

    @GetMapping("")
    public String show(Model model){
        model.addAttribute("productos", productoService.findAll());
        return "productos/show";
    }

    @GetMapping("/create")
    public String create() {
        return "productos/create";
    }

    @PostMapping("/save")
    public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
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

        // IMAGEN
        if (producto.getId()==null){ // Cuando se crea un producto
            String nombreImagen = upload.saveImage(file);
            producto.setImagen(nombreImagen);
        } else {
            if(file.isEmpty()){ // Cuando editamos el producto pero no cambiamos la imagen
                Producto p = new Producto();
                p=productoService.get(producto.getId()).get();
                producto.setImagen(p.getImagen());
            } else {
                String nombreImagen = upload.saveImage((file));
                producto.setImagen(nombreImagen);
            }
        }

        LOGGER.info("PROBANDO IMAGEN: {}", producto.getImagen());
        productoService.save(producto);

        return "redirect:/productos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Producto product = new Producto();
        Optional<Producto> optionalProducto = productoService.get(id);
        product = optionalProducto.get();
        LOGGER.info("producto buscado: {}", product);
        model.addAttribute("producto", product);

        return "productos/edit";
    }

    @PostMapping("/update")
    public String update(Producto producto){
        productoService.update(producto);
        return "redirect:/productos";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        productoService.delete(id);
        return"redirect:/productos";
    }

}
