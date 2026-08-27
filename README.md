- POST /agents -> add an agent as latitude, longitude
- POST /orders -> add an order as latitude longitude
- GET /agents, /ordes -> see the current state

- POST /dispatch/run -> run the algorithm
- GET /dispatch/candidate/{orderId} -> show the candidate pool

```
export DB_URL=jdbc:postgresql://localhost:5432/dispatch
export DB_USERNAME=dispatch_user
export DB_PASSWORD=dispatch_pass
```