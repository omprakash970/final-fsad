package com.klef.loanflowbackend.service;

import com.klef.loanflowbackend.dto.PaymentDTO;
import com.klef.loanflowbackend.entity.Lender;
import com.klef.loanflowbackend.entity.Payment;
import com.klef.loanflowbackend.repository.LenderRepository;
import com.klef.loanflowbackend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LenderRepository lenderRepository;

    public List<PaymentDTO> getPaymentsForLenderUser(Long lenderUserId) {
        Lender lender = lenderRepository.findByUserId(lenderUserId).orElse(null);
        if (lender == null) {
            return List.of();
        }

        // Simple approach: filter in-memory by lenderId through Loan relation.
        return paymentRepository.findAll().stream()
                .filter(p -> p.getLoan() != null && p.getLoan().getLender() != null && lender.getId().equals(p.getLoan().getLender().getId()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private PaymentDTO toDTO(Payment p) {
        return PaymentDTO.builder()
                .id(p.getId())
                .paymentId(p.getPaymentId())
                .loanId(p.getLoan() != null ? p.getLoan().getId() : null)
                .loanCode(p.getLoan() != null ? p.getLoan().getLoanId() : null)
                .borrowerName(p.getLoan() != null && p.getLoan().getBorrower() != null ? p.getLoan().getBorrower().getUser().getFullName() : null)
                .lenderName(p.getLoan() != null && p.getLoan().getLender() != null ? p.getLoan().getLender().getUser().getFullName() : null)
                .amount(p.getAmount())
                .paymentDate(p.getPaymentDate())
                .method(p.getMethod() != null ? p.getMethod().toString() : null)
                .status(p.getStatus() != null ? p.getStatus().toString() : null)
                .build();
    }
}

