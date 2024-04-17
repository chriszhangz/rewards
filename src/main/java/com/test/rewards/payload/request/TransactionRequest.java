package com.test.rewards.payload.request;

import com.test.rewards.model.Transaction;
import java.util.List;

public class TransactionRequest {
  private List<Transaction> transactions;

  public List<Transaction> getTransactions() {
    return transactions;
  }

  public void setTransactions(List<Transaction> transactions) {
    this.transactions = transactions;
  }
}
