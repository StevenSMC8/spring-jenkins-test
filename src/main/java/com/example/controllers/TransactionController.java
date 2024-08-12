package com.example.controllers;

import com.example.entities.transactions.Transaction;
import com.example.services.TransactionService;
import com.example.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping("/transactions")
    public String getUserTransactions(Model model, HttpServletRequest request) {
        List<Transaction> transactions = transactionService.getTransactionsForLoggedInUser(request);
        String loggedInUsername = userService.getLoggedInUser(request).getUsername() + "'s";

        double totalBalance = transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        model.addAttribute("username", loggedInUsername);
        model.addAttribute("transactions", transactions);
        model.addAttribute("totalBalance", totalBalance);

        return "transactions";
    }
}
