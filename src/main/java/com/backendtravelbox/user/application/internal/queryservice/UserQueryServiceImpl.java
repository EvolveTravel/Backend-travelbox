package com.backendtravelbox.user.application.internal.queryservice;

import com.backendtravelbox.user.domain.model.aggregates.User;
import com.backendtravelbox.user.domain.model.queries.GetAllUserQuery;
import com.backendtravelbox.user.domain.model.queries.GetUserByIdQuery;
import com.backendtravelbox.user.domain.service.UserQueryService;
import com.backendtravelbox.user.infraestructure.persistance.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    // Constructor para inyectar la dependencia de UserRepository
    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        // Manejar la consulta para obtener un usuario por ID
        return userRepository.findById(query.id());
    }

    @Override
    public List<User> handle(GetAllUserQuery query) {
        // Manejar la consulta para obtener todos los usuarios
        return userRepository.findAll();
    }
}