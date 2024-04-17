package com.test.rewards.util;

public class RewardPointsUtil {

  // calculate points based on the transaction amount
  public static int calculatePoints(double transactionAmount) {

    int points = 0;

    // Assuming we can ignore the fractional part
    int amount = (int) Math.floor(transactionAmount);

    // 2 points for every dollar spent over $100
    if (amount > 100) {
      points += (amount - 100) * 2;
      amount = 100;
    }

    // 1 point for every dollar spent over $50
    if (amount > 50) {
      points += (amount - 50);
    }

    return points;
  }
}
