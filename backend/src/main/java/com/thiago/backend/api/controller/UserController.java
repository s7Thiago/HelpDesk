package com.thiago.backend.api.controller;

import javax.servlet.http.HttpServletRequest;

import com.thiago.backend.api.entity.User;
import com.thiago.backend.api.response.Response;
import com.thiago.backend.api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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
}
