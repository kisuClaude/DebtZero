package com.chubeo.DebtZero.config;

import com.chubeo.DebtZero.entity.Role;
import com.chubeo.DebtZero.entity.User;
import com.chubeo.DebtZero.exception.AppException;
import com.chubeo.DebtZero.exception.ErrorCode;
import com.chubeo.DebtZero.repository.RoleRepository;
import com.chubeo.DebtZero.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitializer implements ApplicationRunner {
    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${admin.default-password}")
    String adminDefaultPassword;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (roleRepository.findByName("ROLE_USER").isEmpty()){
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setDescription("Regular user");
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()){
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("Admin");
            roleRepository.save(adminRole);
        }
        if (userRepository.findByUsername("admin").isEmpty()){
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("chuhonganh0701@gmail.com");
            admin.setFirstName("Admin");
            admin.setLastName("System");
            admin.setPassword(passwordEncoder.encode(adminDefaultPassword));
            admin.setRoles(new HashSet<>(Set.of(adminRole)));

            userRepository.save(admin);
        }
    }
}
