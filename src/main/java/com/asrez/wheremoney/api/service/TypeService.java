package com.asrez.wheremoney.api.service;

import com.asrez.wheremoney.api.entity.Type;
import com.asrez.wheremoney.api.exception.ApiRequestException;
import com.asrez.wheremoney.api.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TypeService {
    private final TypeRepository typeRepository;

    @Autowired
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public Type addType(Type type) {
        return typeRepository.save(type);
    }

    public Type getType(Long id) {
        Optional<Type> typeOptional = typeRepository.findById(id);
        if (typeOptional.isEmpty())
            throw new ApiRequestException("Type not found!", " TYPE_NOT_FOUND");

        return typeOptional.get();
    }

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }


    public Map<Object, Object> deleteType(Long id) {
        Type type = getType(id);
        typeRepository.deleteById(type.getId());
        Map<Object, Object> model = new HashMap<>();
        model.put("id", id);
        model.put("success", true);
        return model;
    }
}
