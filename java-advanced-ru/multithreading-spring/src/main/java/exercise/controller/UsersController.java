package exercise.controller;

import exercise.model.User;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import exercise.service.UserService;


@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UserService service;

    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public Flux<User> getUsers() {
        return userService.findAll();
    }

    // BEGIN

    @GetMapping(path = "/{id}")
    public Mono<User> get(@PathVariable int id) {
        return service.get(id);
    }

    @PatchMapping(path = "/{id}")
    public Mono<User> update(@PathVariable int id, @RequestBody User user) {
        return service.update(id, user);
    }

    @PostMapping
    public Mono<User> save(@RequestBody User user) {
        return service.create(user);
    }

    @DeleteMapping(path = "/{id}")
    public Mono<Void> delete(@PathVariable int id) {
        return service.delete(id);
    }
    // END
}
