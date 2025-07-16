package com.example.userservice.service;

import com.example.userservice.dto.DailySummaryDTO;
import com.example.userservice.dto.TransactionTimeDTO;
import com.example.userservice.entity.PerformanceMetrics;
import com.example.userservice.entity.Transaction;
import com.example.userservice.entity.User;
import com.example.userservice.repository.PerformanceMetricsRepository;
import com.example.userservice.repository.TransactionRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TechnicalService {
    private final PerformanceMetricsRepository performanceRepo;
    private final TransactionRepository transactionRepo;
    private final UserRepository userRepo;

    public TechnicalService(PerformanceMetricsRepository performanceRepo,
                            TransactionRepository transactionRepo,
                            UserRepository userRepo) {
        this.performanceRepo = performanceRepo;
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
    }

    public List<PerformanceMetrics> getFailuresByDateAndLocation(String date, Integer locationId) {
        if (date != null && locationId != null) {
            return performanceRepo.findByDateAndLocationId(LocalDate.parse(date), locationId);
        } else if (date != null) {
            return performanceRepo.findByDate(LocalDate.parse(date));
        } else if (locationId != null) {
            return performanceRepo.findByLocationId(locationId);
        } else {
            return performanceRepo.findAll();
        }
    }

    public List<TransactionTimeDTO> getExecutionTimes(String date, Integer locationId) {
        List<Transaction> transactions = transactionRepo.findAll();

        return transactions.stream()
                .filter(tx -> {
                    if (date != null && !tx.getTimestamp().toLocalDate().toString().equals(date)) return false;
                    if (locationId != null) {
                        User user = userRepo.findById(tx.getTargetId()).orElse(null);
                        return user != null && user.getLocationId().equals(locationId);
                    }
                    return true;
                })
                .map(tx -> new TransactionTimeDTO(tx.getId(), tx.getTimestamp(), tx.getTimestamp().plusSeconds(2)))
                .collect(Collectors.toList());
    }

    public List<DailySummaryDTO> getDailySummaries() {
        List<Transaction> allTx = transactionRepo.findAll();

        return allTx.stream()
                .collect(Collectors.groupingBy(tx -> tx.getTimestamp().toLocalDate()))
                .entrySet()
                .stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Transaction> txList = entry.getValue();

                    int total = txList.size();
                    int failed = (int) txList.stream().filter(tx -> "failed".equalsIgnoreCase(tx.getStatus())).count();

                    double avgTime = txList.stream()
                            .mapToDouble(tx -> 2.0) // replace with real value if tracked
                            .average().orElse(0);

                    return new DailySummaryDTO(date.toString(), total, failed, avgTime);
                })
                .sorted(Comparator.comparing(DailySummaryDTO::getDate))
                .collect(Collectors.toList());
    }
}