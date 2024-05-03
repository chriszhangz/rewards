package com.test.rewards.controller;

import com.test.rewards.model.MonthlyRewardPoint;
import com.test.rewards.model.Transaction;
import com.test.rewards.payload.reponse.RewardPointsResponse;
import com.test.rewards.payload.request.TransactionRequest;
import com.test.rewards.util.RewardPointsUtil;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rewards")
public class RewardPointsController {

  @PostMapping("/calculateRewardPoints")
  public ResponseEntity<List<RewardPointsResponse>> calculateRewardPoints(
      @RequestBody TransactionRequest transactionRequest) {

    // store monthly points for each customer
    Map<String, Map<String, Integer>> monthlyPoints = new HashMap<>();

    // loop all transactions to calculate points for each customer
    for (Transaction transaction : transactionRequest.getTransactions()) {
      // skip bad data
      if (transaction == null
          || transaction.getTransactionAmount() <= 0
          || Strings.isEmpty(transaction.getCustomerName())) {
        continue;
      }

      // calculate reward points
      int points = RewardPointsUtil.calculatePoints(transaction.getTransactionAmount());


      // calculate month
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
      String month = transaction.getTransactionDate().format(formatter);

      // sum the points for each month
      monthlyPoints
          .computeIfAbsent(transaction.getCustomerName(), k -> new HashMap<>())
          .merge(month, points, Integer::sum);
    }

    // create response objects
    List<RewardPointsResponse> responses = new ArrayList<>();

    // loop monthly points to build response objects
    monthlyPoints.forEach(
        (k, v) -> {
          RewardPointsResponse rewardPointsResponse = new RewardPointsResponse();
          responses.add(rewardPointsResponse);
          rewardPointsResponse.setCustomerName(k);
          rewardPointsResponse.setMonthlyRewardPoints(
              v.entrySet().stream()
                  .sorted(Map.Entry.comparingByKey()) // sort by month
                  .map(x -> new MonthlyRewardPoint(x.getKey(), x.getValue()))
                  .collect(Collectors.toList()));
          rewardPointsResponse.setTotalRewardPoints(v.values().stream().mapToInt(x -> x).sum());
        });

    return ResponseEntity.ok(responses);
  }
}
