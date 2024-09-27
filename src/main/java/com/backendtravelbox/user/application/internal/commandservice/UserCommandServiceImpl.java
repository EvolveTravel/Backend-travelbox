package com.backendtravelbox.user.application.internal.commandservice;

import com.backendtravelbox.user.domain.model.aggregates.User;
import com.backendtravelbox.user.domain.model.commands.CreateUserCommand;
import com.backendtravelbox.user.domain.model.commands.DeleteUserCommand;
import com.backendtravelbox.user.domain.model.commands.UpdateUserCommand;
import com.backendtravelbox.user.domain.service.UserCommandService;
import com.backendtravelbox.user.infraestructure.persistance.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;

    // Constructor para inyectar la dependencia de UserRepository
    public UserCommandServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Long handle(CreateUserCommand command) {
        // Verificar si un usuario con el correo dado ya existe
        if (userRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        // Crear una nueva entidad User a partir del comando
        User user = new User(command);
        try {
            // Guardar el nuevo usuario en el repositorio
            userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al guardar el usuario: " + e.getMessage());
        }
        // Devolver el ID del usuario recién creado
        return user.getId();
    }

    @Override
    public Optional<User> handle(UpdateUserCommand command) {
        // Verificar si otro usuario con el mismo correo ya existe
        if (userRepository.existsByEmailAndIdIsNot(command.email(), command.id())) {
            throw new IllegalArgumentException("Ya existe un usuario con el mismo correo");
        }

        // Buscar el usuario por ID
        var result = userRepository.findById(command.id());
        if (result.isEmpty()) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        var userToUpdate = result.get();
        try {
            // Actualizar los detalles del usuario y guardar el usuario actualizado en el repositorio
            var updatedUser = userRepository.save(userToUpdate.updateUser(
                    command.firstName(),
                    command.lastName(),
                    command.email(),
                    command.userName(),
                    command.password(),
                    command.phoneNumber()));
            return Optional.of(updatedUser);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    @Override
    public void handle(DeleteUserCommand command) {
        // Verificar si el usuario existe por ID
        if (!userRepository.existsById(command.id())) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        try {
            // Eliminar el usuario por ID
            userRepository.deleteById(command.id());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al eliminar el usuario: " + e.getMessage());
        }
    }
}