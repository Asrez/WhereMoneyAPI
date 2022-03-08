package com.asrez.wheremoney.api.controller;

import com.asrez.wheremoney.api.entity.Type;
import com.asrez.wheremoney.api.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/type")
public class TypeController {

    private final TypeService typeService;

    @Autowired
    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @PostMapping
    public ResponseEntity<Type> addType(@RequestBody @Valid Type type) {
        return ResponseEntity.ok(typeService.addType(type));
    }

    @GetMapping
    public List<Type> getAllTypes() {
        return typeService.getAllTypes();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteType(@PathVariable("id") Long id) {
        return ResponseEntity.ok(typeService.deleteType(id));
    }
}
