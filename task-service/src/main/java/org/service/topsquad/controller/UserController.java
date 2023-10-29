package org.service.topsquad.controller;

import java.util.List;

import org.service.topsquad.exceptionhandler.ResourceNotFoundException;
import org.service.topsquad.exceptionhandler.ResourceValidateException;
import org.service.topsquad.model.Account;
import org.service.topsquad.model.Response;
import org.service.topsquad.model.UserEntity;
import org.service.topsquad.model.UserModel;
import org.service.topsquad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/admin/users")
    public List<UserEntity> findAll() {
        return service.findAllUser();
    }

    @PatchMapping("/admin/deactivate")
    public ResponseEntity<Object> deactivateUser(@RequestBody final UserModel model) {
        try {
            return service.updateUserStatus(model.getUserName(), model.getStatus())
                    ? ResponseEntity.status(HttpStatus.OK).body("User status updated.")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot update user status.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }

    @GetMapping("/users/{userName}")
    public ResponseEntity<UserEntity> getByUsername(@PathVariable final String userName) {
        try {
            UserEntity task = service.findByUserName(userName);
            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users/register")
    public ResponseEntity<Object> register(@RequestBody final UserModel model) {
        try {
            UserEntity task = service.create(model);
            return ResponseEntity.status(HttpStatus.OK).body(task);
        } catch (ResourceValidateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }

    @PatchMapping("/users/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody final Account model) {
        try {
            return service.changePassword(model)
                    ? ResponseEntity.status(HttpStatus.OK).body("Password updated.")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot update password.");
        } catch (ResourceNotFoundException | ResourceValidateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response(e.getMessage()));
        }
    }
}
