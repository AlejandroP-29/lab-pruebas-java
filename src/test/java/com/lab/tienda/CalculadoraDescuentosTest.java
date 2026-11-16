package com.lab.tienda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class CalculadoraDescuentosTest {
    private final CalculadoraDescuentos calculadora = new CalculadoraDescuentos();

    @ParameterizedTest
    @CsvSource({"100,0,100", "200,25,150", "50,100,0", "0,30,0", "80,50,40"})
    void calculaPrecioFinal(double precio, double descuento, double esperado) {
        assertEquals(esperado, calculadora.calcularPrecioFinal(precio, descuento), 0.001);
    }

    @Test
    void rechazaPrecioNegativo() {
        assertThrows(IllegalArgumentException.class,
            () -> calculadora.calcularPrecioFinal(-1, 10));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-5, 101})
    void rechazaDescuentoFueraDeRango(double descuento) {
        assertThrows(IllegalArgumentException.class,
            () -> calculadora.calcularPrecioFinal(100, descuento));
    }
}