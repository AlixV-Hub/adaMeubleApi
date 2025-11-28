package com.meubles.Service;

import com.meubles.DTO.MaterialDTO;
import com.meubles.Repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public List<MaterialDTO> getAllMaterials() {
        return materialRepository.findAll().stream()
                .map(entity -> new MaterialDTO(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }
}