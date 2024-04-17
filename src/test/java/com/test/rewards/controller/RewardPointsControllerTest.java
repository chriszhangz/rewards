package com.test.rewards.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = RewardPointsController.class)
class RewardPointsControllerTest {

  @Autowired private MockMvc mvc;

  @BeforeEach
  void setUp() {}

  @Test
  @DisplayName("Test normal cases")
  void test_calculateRewardPoints_normal() throws Exception {
    // Create a mock request body
    String requestBody =
        "{\n"
            + "  \"transactions\": [\n"
            + "    {\n"
            + "      \"customerName\": \"Jack\",\n"
            + "      \"transactionAmount\": 10.05,\n"
            + "      \"transactionDate\": \"2024-04-05T10:00:00\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"customerName\": \"Chris\",\n"
            + "      \"transactionAmount\": 89.99,\n"
            + "      \"transactionDate\": \"2024-03-20T17:30:00\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"customerName\": \"Jack\",\n"
            + "      \"transactionAmount\": 120.00,\n"
            + "      \"transactionDate\": \"2024-03-25T18:45:00\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"customerName\": \"Jack\",\n"
            + "      \"transactionAmount\": 73.99,\n"
            + "      \"transactionDate\": \"2024-03-05T09:45:00\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    // Perform the POST request
    mvc.perform(
            MockMvcRequestBuilders.post("/rewards/calculateRewardPoints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(2)))
        .andExpect(jsonPath("$[*].customerName", containsInAnyOrder("Jack", "Chris")))
        .andExpect(jsonPath("$.[?(@.customerName == \"Chris\")].monthlyRewardPoints.*", hasSize(1)))
        .andExpect(
            jsonPath(
                    "$[*].[?(@.customerName == \"Chris\" && @.monthlyRewardPoints[0].month == \"2024-03\" "
                        + "&& @.monthlyRewardPoints[0].points == 39 && @.totalRewardPoints == 39)]")
                .exists())
        .andExpect(jsonPath("$.[?(@.customerName == \"Jack\")].monthlyRewardPoints.*", hasSize(2)))
        .andExpect(
            jsonPath(
                    "$[*].[?(@.customerName == \"Jack\" && @.monthlyRewardPoints[0].month == \"2024-03\" "
                        + "&& @.monthlyRewardPoints[0].points == 113 && @.monthlyRewardPoints[1].month == \"2024-04\" "
                        + "&& @.monthlyRewardPoints[1].points == 0 && @.totalRewardPoints == 113)]")
                .exists());
  }

  @Test
  @DisplayName("Test edge cases")
  void test_calculateRewardPoints_edge() throws Exception {
    // Create a mock request body
    String requestBody =
        "{\n"
            + "  \"transactions\": [\n"
            + "    {\n"
            + "      \"customerName\": \"Jack\",\n"
            + "      \"transactionAmount\": 100.00,\n"
            + "      \"transactionDate\": \"2024-04-05T10:00:00\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"customerName\": \"Chris\",\n"
            + "      \"transactionAmount\": 0.01,\n"
            + "      \"transactionDate\": \"2024-03-31T23:59:59\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"customerName\": \"Jack\",\n"
            + "      \"transactionAmount\": 101.00,\n"
            + "      \"transactionDate\": \"2024-03-25T18:45:00\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"customerName\": \"Chris\",\n"
            + "      \"transactionAmount\": 50.99,\n"
            + "      \"transactionDate\": \"2024-02-01T00:00:00\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"customerName\": \"Jack\",\n"
            + "      \"transactionAmount\": 50.00,\n"
            + "      \"transactionDate\": \"2024-03-05T09:45:00\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    // Perform the POST request
    mvc.perform(
            MockMvcRequestBuilders.post("/rewards/calculateRewardPoints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(2)))
        .andExpect(jsonPath("$[*].customerName", containsInAnyOrder("Jack", "Chris")))
        .andExpect(jsonPath("$.[?(@.customerName == \"Chris\")].monthlyRewardPoints.*", hasSize(2)))
        .andExpect(
            jsonPath(
                    "$[*].[?(@.customerName == \"Chris\" && @.monthlyRewardPoints[0].month == \"2024-02\" "
                        + "&& @.monthlyRewardPoints[0].points == 0 && @.monthlyRewardPoints[1].month == \"2024-03\" "
                        + "&& @.monthlyRewardPoints[1].points == 0 && @.totalRewardPoints == 0)]")
                .exists())
        .andExpect(jsonPath("$.[?(@.customerName == \"Jack\")].monthlyRewardPoints.*", hasSize(2)))
        .andExpect(
            jsonPath(
                    "$[*].[?(@.customerName == \"Jack\" && @.monthlyRewardPoints[0].month == \"2024-03\" "
                        + "&& @.monthlyRewardPoints[0].points == 52 && @.monthlyRewardPoints[1].month == \"2024-04\" "
                        + "&& @.monthlyRewardPoints[1].points == 50 && @.totalRewardPoints == 102)]")
                .exists());
  }

  @Test
  @DisplayName("Bad data will be skipped")
  void test_calculateRewardPoints_badData() throws Exception {
    // Create a mock request body
    String requestBody =
        "{\n"
            + "  \"transactions\": [\n"
            + "    {\n"
            + "      \"customerName\": \"\",\n"
            + "      \"transactionAmount\": 100.00,\n"
            + "      \"transactionDate\": \"2024-04-05T10:00:00\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"customerName\": \"Chris\",\n"
            + "      \"transactionAmount\": -10.01,\n"
            + "      \"transactionDate\": \"2024-03-31T23:59:59\"\n"
            + "    },\n"
            + "    {\n"
            + "      \"customerName\": \"Chris\",\n"
            + "      \"transactionAmount\": 0.00,\n"
            + "      \"transactionDate\": \"2024-02-01T00:00:00\"\n"
            + "    }\n"
            + "  ]\n"
            + "}";

    // Perform the POST request
    mvc.perform(
            MockMvcRequestBuilders.post("/rewards/calculateRewardPoints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(0)));
  }
}
