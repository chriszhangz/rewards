# Rewards Service

Provide web service of reward program

## Dependencies

The following are needed to compile and run the project:

1. JDK 22

### Test request body for the endpoint: /rewards/calculateRewardPoints

```json
{
  "transactions": [
    {
      "customerName": "Jack",
      "transactionAmount": 100.05,
      "transactionDate": "2024-04-05T10:00:00"
    },
    {
      "customerName": "Chris",
      "transactionAmount": 50.99,
      "transactionDate": "2024-03-20T17:30:00"
    },
    {
      "customerName": "Jack",
      "transactionAmount": 120.00,
      "transactionDate": "2024-03-25T18:45:00"
    },
    {
      "customerName": "Jack",
      "transactionAmount": 73.99,
      "transactionDate": "2024-03-05T09:45:00"
    }
  ]
}
```