package br.com.fiap.abctechapi.controller;

import br.com.fiap.abctechapi.entity.Assistance;
import br.com.fiap.abctechapi.service.AssistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/assists")
@RestController
public class AssistanceController {

    private final AssistanceService assistanceService;

    @Autowired
    public AssistanceController(AssistanceService assistanceService){
        this.assistanceService = assistanceService;
    }

    @GetMapping
    public ResponseEntity<List<Assistance>> getAssists(){
        return ResponseEntity.ok(assistanceService.getAssists());
    }

}
