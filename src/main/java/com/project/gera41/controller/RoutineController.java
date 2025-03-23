package com.project.gera41.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/routines/")
public class RoutineController {

    @GetMapping
    public List<String> listRoutines() {
        return Arrays.asList(
                "NFe",
                "NFSe",
                "Aprovação financeira",
                "Aprovação de estoque"
        );
    }
}
