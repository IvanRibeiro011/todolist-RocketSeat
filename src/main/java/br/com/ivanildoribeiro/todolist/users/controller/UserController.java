package br.com.ivanildoribeiro.todolist.users.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.ivanildoribeiro.todolist.users.model.UserModel;
import br.com.ivanildoribeiro.todolist.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping("/save")
    public ResponseEntity<UserModel> create(@RequestBody UserModel model) {
        if (repository.existsByUsername(model.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String encoded = BCrypt.withDefaults().hashToString(12, model.getPassword().toCharArray());
        model.setPassword(encoded);
        return new ResponseEntity<>(this.repository.save(model), HttpStatus.CREATED);
    }
}
