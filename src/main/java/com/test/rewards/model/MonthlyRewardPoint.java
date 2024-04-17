package com.test.rewards.model;

public class MonthlyRewardPoint {
  private String month;
  private int points;

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public MonthlyRewardPoint(String month, int points) {
    this.month = month;
    this.points = points;
  }
}
