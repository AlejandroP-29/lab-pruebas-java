package com.lab.tienda;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository repositorio;
    private final CalculadoraDescuentos calculadora = new CalculadoraDescuentos();

    public ProductoController(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProductoRequest datos) {
        if (datos.nombre() == null || datos.nombre().isBlank()
            || datos.precio() == null || datos.precio() < 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Datos inválidos"));
        }
        Producto guardado = repositorio.save(new Producto(datos.nombre(), datos.precio()));
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping("/{id}/precio-final")
    public ResponseEntity<?> precioFinal(@PathVariable Long id,
        @RequestParam(defaultValue = "0") double descuento) {
        Optional<Producto> producto = repositorio.findById(id);
        if (producto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No encontrado"));
        }
        try {
            double precio = calculadora.calcularPrecioFinal(producto.get().getPrecio(), descuento);
            return ResponseEntity.ok(Map.of("precioFinal", precio));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}