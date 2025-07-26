package com.springboot.backend.optica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.optica.modelo.TipoTarjeta;
import com.springboot.backend.optica.service.ITipoTarjetaService;

@RestController
@RequestMapping("/api/tipos-tarjeta")
public class TipoTarjetaController {

    @Autowired
    private ITipoTarjetaService tipoTarjetaService;

    @GetMapping
    public List<TipoTarjeta> listar() {
        return tipoTarjetaService.listarTodos();
    }
}