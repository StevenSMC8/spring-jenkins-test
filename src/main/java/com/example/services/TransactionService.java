package com.example.services;

import com.example.entities.User;
import com.example.entities.transactions.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.*;
import java.util.List;

@Service
public class TransactionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    public List<Transaction> getTransactionsForLoggedInUser(HttpServletRequest request) {
        User loggedInUser = userService.getLoggedInUser(request);

        TypedQuery<Transaction> query = entityManager.createQuery(
                "SELECT t FROM Transaction t WHERE t.user = :user", Transaction.class);
        query.setParameter("user", loggedInUser);

        return query.getResultList();
    }
}
