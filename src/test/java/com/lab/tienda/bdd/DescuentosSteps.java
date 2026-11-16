package com.lab.tienda.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.lab.tienda.CalculadoraDescuentos;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

public class DescuentosSteps {
    private final CalculadoraDescuentos calculadora = new CalculadoraDescuentos();
    private double precio;
    private Double resultado;
    private Exception error;

    @Dado("un producto que cuesta {double}")
    public void unProductoQueCuesta(double precio) {
        this.precio = precio;
    }

    @Cuando("aplico un descuento de {double}")
    public void aplicoUnDescuentoDe(double descuento) {
        try {
            resultado = calculadora.calcularPrecioFinal(precio, descuento);
        } catch (IllegalArgumentException e) {
            error = e;
        }
    }

    @Entonces("el precio final es {double}")
    public void elPrecioFinalEs(double esperado) {
        assertEquals(esperado, resultado, 0.001);
    }

    @Entonces("el sistema rechaza el descuento")
    public void elSistemaRechazaElDescuento() {
        assertNotNull(error);
    }
}