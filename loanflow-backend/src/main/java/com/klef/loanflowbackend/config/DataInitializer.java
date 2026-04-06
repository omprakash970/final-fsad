package com.klef.loanflowbackend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.klef.loanflowbackend.entity.Role;
import com.klef.loanflowbackend.entity.User;
import com.klef.loanflowbackend.repository.BorrowerRepository;
import com.klef.loanflowbackend.repository.EmiScheduleRepository;
import com.klef.loanflowbackend.repository.LenderRepository;
import com.klef.loanflowbackend.repository.LoanRepository;
import com.klef.loanflowbackend.repository.LoanRequestRepository;
import com.klef.loanflowbackend.repository.PaymentRepository;
import com.klef.loanflowbackend.repository.RiskReportRepository;
import com.klef.loanflowbackend.repository.SecurityLogRepository;
import com.klef.loanflowbackend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeData(
            UserRepository userRepository,
            BorrowerRepository borrowerRepository,
            LenderRepository lenderRepository,
            LoanRepository loanRepository,
            LoanRequestRepository loanRequestRepository,
            EmiScheduleRepository emiScheduleRepository,
            PaymentRepository paymentRepository,
            RiskReportRepository riskReportRepository,
            SecurityLogRepository securityLogRepository) {

        return args -> {
            // Clear all existing sample data
            System.out.println("Clearing existing data...");
            securityLogRepository.deleteAll();
            riskReportRepository.deleteAll();
            paymentRepository.deleteAll();
            emiScheduleRepository.deleteAll();
            loanRequestRepository.deleteAll();
            loanRepository.deleteAll();
            lenderRepository.deleteAll();
            borrowerRepository.deleteAll();
            userRepository.deleteAll();
            System.out.println("✓ All data cleared");

            // Create admin account
            User adminUser = User.builder()
                    .fullName("System Admin")
                    .email("admin@loanflow.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(adminUser);
            System.out.println("✓ Admin account created: admin@loanflow.com / admin123");
            System.out.println("✓ DataInitializer completed");
        };
    }
}
