```mermaid

flowchart TB
    
    subgraph Platform["PayGuard Platform"]
        subgraph Services["Microservices"]
            User["User Service"]
            Payment["Payment Service"]
            Fraud-Engine["Fraud Service"]
            Notification["Notification Service"]
            Reconciliation["Reconciliation Service"]
        
```