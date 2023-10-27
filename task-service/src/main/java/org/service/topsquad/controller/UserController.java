package org.service.topsquad.controller;

import java.util.List;
import org.service.topsquad.exceptionhandler.ResourceNotFoundException;
import org.service.topsquad.model.UserEntity;
import org.service.topsquad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/admin/users")
    public List<UserEntity> findAll() {
        return service.findAll();
    }

      @GetMapping({"/admin/{userName}", "/users/{userName}"})
      public ResponseEntity<UserEntity> getById(@PathVariable final String userName) {
        try {
            UserEntity task = service.findByUserName(userName);
            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
