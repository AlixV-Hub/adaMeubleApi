package com.meubles.Controller;


import com.meubles.DTO.ColorDTO;
import com.meubles.Service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @GetMapping
    public ResponseEntity<List<ColorDTO>> getAllColors() {
        return ResponseEntity.ok(colorService.getAllColors());
    }
}