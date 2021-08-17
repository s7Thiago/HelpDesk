package com.thiago.backend.api.controller;

import javax.servlet.http.HttpServletRequest;

import com.thiago.backend.api.entity.User;
import com.thiago.backend.api.response.Response;
import com.thiago.backend.api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*") // Liberando o acesso para qualquer porta / servidor
public class UserController {

    @Autowired
    private UserService userService;

    // Usado para criptografar a senha
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Salva um usuário no banco de dados Todos os métodos controller em
     * UserController vai retornar um objeto desta classe. isso é feito com o
     * objetivo de padronizar a comunicação do frontend com o backend.
     * 
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')") // Autorização de acordo com o perfil
    public ResponseEntity<Response<User>> create(HttpServletRequest request, //
            @RequestBody User user, // Dados que serão enviados pela requisição
            BindingResult result // se houver algum erro, será passado por aqui nas validações
    ) {
        Response<User> response = new Response<User>();
        try {
            validateCreateUser(user, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }

            // Criptografando a senha do usuário
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User userPersisted = (User) userService.createOrUpdate(user);

            // Colocando o usuário criado na resposta
            response.setData(userPersisted);

        } catch (DuplicateKeyException dE) {
            response.getErrors().add("Email já registrado!");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    // Valida a entrada
    private void validateCreateUser(User user, BindingResult result) {
        if (user.getEmail() == null) {
            result.addError(new ObjectError("User email", "E-mail não pode ser vazio"));
        }
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')") // Somente o administrador do sistema pode usar este método
    public ResponseEntity<Response<User>> update(HttpServletRequest request, //
            @RequestBody User user, // Dados que serão enviados pela requisição
            BindingResult result // se houver algum erro, será passado por aqui nas validações
    ) {
        Response<User> response = new Response<User>();
        try {

            validateUpdateUser(user, result);
            if (result.hasErrors()) {
                result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User userPersisted = (User) userService.createOrUpdate(user);
            response.setData(userPersisted);

        } catch (Exception e) {
            response.getErrors().add(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    // Valida a atualização de uma entrada
    private void validateUpdateUser(User user, BindingResult result) {
        if (user.getId() == null) {
            result.addError(new ObjectError("User", "campo ID não possui informação"));
        }

        if (user.getEmail() == null) {
            result.addError(new ObjectError("User email", "E-mail não pode ser vazio"));
        }
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasAnyRole('ADMIN')") // Autorização de acordo com o perfil
    public ResponseEntity<Response<User>> findById(@PathVariable("id") String id) {
        Response<User> response = new Response<User>();
        User user = userService.findById(id);

        // Se o id pesquisado não retornar nenhum usuário associado, retorna um erro
        if (user == null) {
            response.getErrors().add("Usuário " + id + " não encontrado!");
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAnyRole('ADMIN')") // Autorização de acordo com o perfil
    public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
        Response<String> response = new Response<String>();
        User user = userService.findById(id);

        // Se o id pesquisado não retornar nenhum usuário associado, retorna um erro
        if (user == null) {
            response.getErrors().add("Usuário " + id + " não encontrado!");
            return ResponseEntity.badRequest().body(response);
        }
        userService.delete(id);
        response.setData("Usuário " + id + " deletado!");
        return ResponseEntity.ok(new Response<String>());
    }

    @GetMapping(value = "{page}/{count}")
    @PreAuthorize("hasAnyRole('ADMIN')") // Autorização de acordo com o perfil
    public ResponseEntity<Response<Page<User>>> findAll(@PathVariable int page, @PathVariable int count) {
        Response<Page<User>> response = new Response<Page<User>>();
        Page<User> users = userService.findAll(page, count);
        response.setData(users);
        return ResponseEntity.ok(response);
    }
}
