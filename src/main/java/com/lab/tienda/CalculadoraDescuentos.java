package com.lab.tienda;

public class CalculadoraDescuentos {
    public double calcularPrecioFinal(double precio, double descuento) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (descuento < 0 || descuento > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
        }
        double resultado = precio - (precio * (descuento / 100));
        return Math.round(resultado * 100.0) / 100.0;
    }
}