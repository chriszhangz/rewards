package com.test.rewards.payload.reponse;

import com.test.rewards.model.MonthlyRewardPoint;
import java.util.List;

public class RewardPointsResponse {
  private String customerName;
  private List<MonthlyRewardPoint> monthlyRewardPoints;
  private int totalRewardPoints;

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public List<MonthlyRewardPoint> getMonthlyRewardPoints() {
    return monthlyRewardPoints;
  }

  public void setMonthlyRewardPoints(List<MonthlyRewardPoint> monthlyRewardPoints) {
    this.monthlyRewardPoints = monthlyRewardPoints;
  }

  public int getTotalRewardPoints() {
    return totalRewardPoints;
  }

  public void setTotalRewardPoints(int totalRewardPoints) {
    this.totalRewardPoints = totalRewardPoints;
  }
}
