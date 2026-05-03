package com.internship.tool.config;

import com.internship.tool.entity.Role;
import com.internship.tool.entity.User;
import com.internship.tool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Seed Admin
            User admin = User.builder()
                    .fullName("Admin User")
                    .email("admin@example.com")
                    .passwordHash("$2a$10$xyz") // Placeholder hash
                    .role(Role.ADMIN)
                    .department("IT")
                    .isActive(true)
                    .build();
            userRepository.save(admin);

            // Seed Manager
            User manager = User.builder()
                    .fullName("Audit Manager")
                    .email("manager@example.com")
                    .passwordHash("$2a$10$xyz")
                    .role(Role.MANAGER)
                    .department("Compliance")
                    .isActive(true)
                    .build();
            userRepository.save(manager);

            // Seed Auditor
            User auditor = User.builder()
                    .fullName("Senior Auditor")
                    .email("auditor@example.com")
                    .passwordHash("$2a$10$xyz")
                    .role(Role.AUDITOR)
                    .department("Internal Audit")
                    .isActive(true)
                    .build();
            userRepository.save(auditor);
            
            System.out.println("Database seeded successfully with initial users.");
            
            // TODO: In a real project, inject AuditProgramRepository and AuditTaskRepository to seed dummy programs and tasks for Demo Day
        }
    }
}
