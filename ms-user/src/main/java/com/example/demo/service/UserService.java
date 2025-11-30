package com.example.demo.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.controller.dto.NewUserDTO;
import com.example.demo.integration.TicketClient;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Profile;
import com.example.demo.repository.entity.Role;
import com.example.demo.repository.entity.User;
import com.example.demo.service.stereotype.Business;
import com.example.demo.controller.dto.NewTicketDTO;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Business
@Service
@Validated
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder = 
        new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Set<String> defaultRoles;
    private final TicketClient ticketClient;

    public UserService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        @Value("${app.user.default.roles}") Set<String> defaultRoles,
        TicketClient ticketClient
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.defaultRoles = defaultRoles;
        this.ticketClient = ticketClient;
    }
    
    @Transactional
    public void cadastrarUsuario(@Valid NewUserDTO newUser) {
        if (!newUser.password().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres e conter pelo menos uma letra e um número");
        }
        
        userRepository.findByEmail(newUser.email())
            .ifPresent(user -> {
                throw new IllegalArgumentException("Usuário com o email " + newUser.email() + " já existe");
            });

        userRepository.findByHandle(newUser.handle())
            .ifPresent(user -> {
                throw new IllegalArgumentException("Usuário com o nome " + newUser.handle() + " já existe");
            });

        User user = new User();
        
        user.setEmail(newUser.email());
        user.setHandle(newUser.handle() != null ? newUser.handle() : generateHandle(newUser.email()));
        user.setPassword(passwordEncoder.encode(newUser.password()));
        
        Set<Role> roles = new HashSet<>();
        
        roles.addAll(roleRepository.findByNameIn(defaultRoles));

        Set<Role> additionalRoles = roleRepository.findByNameIn(newUser.roles());
        if (additionalRoles.size() != newUser.roles().size()) {
            throw new IllegalArgumentException("Alguns papéis não existem");
        }

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("O usuário deve ter pelo menos um papel");
        }

        user.setRoles(roles);

        Profile profile = new Profile();
        
        profile.setName(newUser.name());
        profile.setCompany(newUser.company());
        profile.setType(newUser.type() != null ? newUser.type() : Profile.AccountType.FREE);

        profile.setUser(user);
        user.setProfile(profile);

        userRepository.save(user); 


        User savedUser = userRepository.save(user);

        createOnboardingTickets(savedUser);
    }

    private void createOnboardingTickets(User user) {
        String systemEmail = "system@company.com";
        String userEmail = user.getEmail();
        String locality = "Escritório Central";

        NewTicketDTO onboardTicket = new NewTicketDTO(
            systemEmail,           // Creator
            userEmail,             // Assigne (o novo usuario)
            Collections.emptySet(),// Observers
            "Onboarding",          // Object
            "Realizar Treinamento Inicial", // Action
            "Completar trilha de boas vindas", // Details
            locality               // Locality
        );

        NewTicketDTO workstationTicket = new NewTicketDTO(
            systemEmail,
            userEmail,
            Collections.emptySet(),
            "Estação de Trabalho",
            "Alocar Equipamento",
            "Notebook e Periféricos",
            locality
        );

        try {
            ticketClient.createTicket(onboardTicket);
            ticketClient.createTicket(workstationTicket);
        } catch (Exception e) {
            System.err.println("Erro ao criar tickets automáticos: " + e.getMessage());
        }
    }

    private String generateHandle(String email) {
        String[] parts = email.split("@");
        String handle = parts[0];
        int i = 1;
        while (userRepository.existsByHandle(handle)) {
            handle = parts[0] + i++;
        }
        return handle;
    }
}
