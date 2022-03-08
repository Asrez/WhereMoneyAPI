package com.asrez.wheremoney.api.service;

import com.asrez.wheremoney.api.entity.Type;
import com.asrez.wheremoney.api.exception.ApiRequestException;
import com.asrez.wheremoney.api.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TypeService {
    private final TypeRepository typeRepository;

    @Autowired
    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public Type getType(Long id) {
        Optional<Type> typeOptional = typeRepository.findById(id);
        if (typeOptional.isEmpty())
            throw new ApiRequestException("Type not found!", " TYPE_NOT_FOUND");

        return typeOptional.get();
    }
}
