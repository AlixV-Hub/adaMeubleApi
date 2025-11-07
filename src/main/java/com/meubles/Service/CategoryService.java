package com.meubles.Service;

import com.meubles.DTO.CategoryDTO;
import com.meubles.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(entity -> new CategoryDTO(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }
}