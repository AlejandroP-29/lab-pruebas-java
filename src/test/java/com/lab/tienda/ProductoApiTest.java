package com.lab.tienda;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoApiTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private ResultActions crear(String nombre, double precio) throws Exception {
        String json = mapper.writeValueAsString(Map.of("nombre", nombre, "precio", precio));
        return mvc.perform(post("/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json));
    }

    private long crearYObtenerId() throws Exception {
        String respuesta = crear("Cuaderno", 20)
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        return mapper.readTree(respuesta).get("id").asLong();
    }

    @Test
    void creaProductoYCalculaPrecioFinal() throws Exception {
        long id = crearYObtenerId();
        mvc.perform(get("/productos/{id}/precio-final", id).param("descuento", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.precioFinal").value(18.0));
    }

    @Test
    void productoInexistenteDevuelve404() throws Exception {
        mvc.perform(get("/productos/999999/precio-final"))
            .andExpect(status().isNotFound());
    }

    @Test
    void precioNegativoSeRechaza() throws Exception {
        crear("Lápiz", -5).andExpect(status().isBadRequest());
    }

    @Test
    void descuentoInvalidoDevuelve400() throws Exception {
        long id = crearYObtenerId();
        mvc.perform(get("/productos/{id}/precio-final", id).param("descuento", "150"))
            .andExpect(status().isBadRequest());
    }
}