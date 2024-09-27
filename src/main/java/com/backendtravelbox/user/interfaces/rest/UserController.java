package com.backendtravelbox.user.interfaces.rest;

import com.backendtravelbox.user.domain.model.commands.DeleteUserCommand;
import com.backendtravelbox.user.domain.model.queries.GetAllUserQuery;
import com.backendtravelbox.user.domain.model.queries.GetUserByIdQuery;
import com.backendtravelbox.user.domain.service.UserCommandService;
import com.backendtravelbox.user.domain.service.UserQueryService;
import com.backendtravelbox.user.interfaces.rest.resource.CreateUserResource;
import com.backendtravelbox.user.interfaces.rest.resource.UpdateUserResource;
import com.backendtravelbox.user.interfaces.rest.resource.UserResource;
import com.backendtravelbox.user.interfaces.rest.transform.CreateUserCommandFromResourceAssembler;
import com.backendtravelbox.user.interfaces.rest.transform.UpdateUserCommandFromResourceAssembler;
import com.backendtravelbox.user.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "User", description = "User Management Endpoints")
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    // Constructor para inyectar las dependencias de UserCommandService y UserQueryService
    public UserController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @PostMapping
    public ResponseEntity<UserResource> createUser(@RequestBody CreateUserResource createUserResource) {
        // Convertir el recurso de creación de usuario en un comando
        var createUserCommand = CreateUserCommandFromResourceAssembler.toCommandFromResource(createUserResource);
        // Manejar el comando de creación de usuario
        var id = userCommandService.handle(createUserCommand);
        if (id == 0L) {
            return ResponseEntity.badRequest().build();
        }

        // Consultar el usuario recién creado por ID
        var getUserByIdQuery = new GetUserByIdQuery(id);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Convertir la entidad de usuario en un recurso de usuario
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers() {
        // Manejar la consulta para obtener todos los usuarios
        var getAllUserQuery = new GetAllUserQuery();
        var user = userQueryService.handle(getAllUserQuery);
        var userResource = user.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long id) {
        // Manejar la consulta para obtener un usuario por ID
        var getUserByIdQuery = new GetUserByIdQuery(id);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Convertir la entidad de usuario en un recurso de usuario
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResource> updateUser(@PathVariable Long id, @RequestBody UpdateUserResource updateUserResource) {
        // Convertir el recurso de actualización de usuario en un comando
        var updateUserCommand = UpdateUserCommandFromResourceAssembler.toCommandFromResource(id, updateUserResource);
        // Manejar el comando de actualización de usuario
        var updateUser = userCommandService.handle(updateUserCommand);
        if (updateUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Convertir la entidad de usuario actualizada en un recurso de usuario
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(updateUser.get());
        return ResponseEntity.ok(userResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Manejar el comando de eliminación de usuario
        var deleteUserCommand = new DeleteUserCommand(id);
        userCommandService.handle(deleteUserCommand);
        return ResponseEntity.ok("User with given id successfully deleted");
    }
}