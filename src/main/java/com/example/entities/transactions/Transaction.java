package com.example.entities.transactions;

import java.util.Date;
import com.example.entities.User;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "account_name")
    private String accountName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    private Date date;

    @Column(name = "transaction_description")
    private String description;

    @Column(name = "transaction_amount")
    private double amount;

    // Constructors, getters, and setters
    // Constructor
    public Transaction() {}

    public Transaction(User user, String accountName, Date date, String description, double amount) {
        this.user = user;
        this.accountName = accountName;
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    // Getters and setters
    // Id
    public Long getId() {
        return id;
    }

    // User
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Account Name
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    // Date
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Amount
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
