package com.test.rewards.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RewardPointsUtilTest {
  @Test
  @DisplayName("Ensure correct handling of zero")
  void test_calculatePoints_zero() {
    assertEquals(0, RewardPointsUtil.calculatePoints(0.00));
  }

  @Test
  @DisplayName("Transaction below $50 should return 0 point")
  void test_calculatePoints_belowFifty() {
    assertEquals(0, RewardPointsUtil.calculatePoints(50.00));
    assertEquals(0, RewardPointsUtil.calculatePoints(50.99));
  }

  @Test
  @DisplayName("1 point for every dollar spent over $50")
  void test_calculatePoints_betweenFiftyAndOneHundred() {
    assertEquals(1, RewardPointsUtil.calculatePoints(51.00));
    assertEquals(50, RewardPointsUtil.calculatePoints(100.99));
  }

  @Test
  @DisplayName("2 points for every dollar spent over $100")
  void test_calculatePoints_overOneHundred() {
    assertEquals(50 + 2, RewardPointsUtil.calculatePoints(101.00));
    assertEquals(50 + 256 * 2, RewardPointsUtil.calculatePoints(356.99));
  }
}
