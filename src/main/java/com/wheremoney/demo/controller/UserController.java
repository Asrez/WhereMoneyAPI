package com.wheremoney.demo.controller;

import com.wheremoney.demo.dto.SignUpDto;
import com.wheremoney.demo.entity.User;
import com.wheremoney.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService service;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<SignUpDto> addUser(@Valid @RequestBody SignUpDto signUpDto) {
        User user = modelMapper.map(signUpDto, User.class);
        SignUpDto signUpDtoResponse = modelMapper.map(service.addUser(user), SignUpDto.class);
        return new ResponseEntity<SignUpDto>(signUpDtoResponse, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<SignUpDto> getUser(@PathVariable("id") Long id) {
        SignUpDto signUpDto = modelMapper.map(service.getUser(id), SignUpDto.class);
        return new ResponseEntity<SignUpDto>(signUpDto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        return new ResponseEntity<Object>(service.deleteUser(id), HttpStatus.OK);
    }

    @GetMapping
    public List<SignUpDto> getAll() {
        return service.getAll().stream().map(user -> modelMapper.map(user, SignUpDto.class))
                .collect(Collectors.toList());
    }

    @PutMapping("{id}")
    public ResponseEntity<SignUpDto> updateUser(@PathVariable("id") Long id, @RequestBody SignUpDto signUpDto) {
        User user = modelMapper.map(signUpDto, User.class);
        SignUpDto signUpDtoResponse = modelMapper.map(service.updateUser(id, user), SignUpDto.class);
        return new ResponseEntity<SignUpDto>(signUpDtoResponse, HttpStatus.OK);
    }

}
