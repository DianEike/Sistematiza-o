package com.diandev.sistematizacao.controller;

import com.diandev.sistematizacao.models.Profissional;
import com.diandev.sistematizacao.service.ProfissionalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    private final ProfissionalService service;

    public ProfissionalController(ProfissionalService service) {
        this.service = service;
    }

    @GetMapping
    public List<Profissional> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String especialidade
    ) {
        return service.buscar(nome, especialidade);
    }
}