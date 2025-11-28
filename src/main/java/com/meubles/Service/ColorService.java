package com.meubles.Service;

import com.meubles.DTO.ColorDTO;
import com.meubles.Repository.ColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    public List<ColorDTO> getAllColors() {
        return colorRepository.findAll().stream()
                .map(entity -> new ColorDTO(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }
}