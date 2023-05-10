# Customer Rewards Calculator
## Overview
A retailer offers a rewards program to its customers awarding points based on each recorded purchase as follows:

For every dollar spent over $50 on the transaction, the customer receives one point.
In addition, for every dollar spent over $100, the customer receives another point.
Ex: for a $120 purchase, the customer receives
(120 - 50) x 1 + (120 - 100) x 1 = 90 points

This service computes and returns reward points collected by customer for a given month / all months from an already initialized dataset. Please follow the below instructions for using the service.
Dillinger is a cloud-enabled, mobile-ready, offline-storage compatible,

## Example Dataset

There are two tables created as input dataset for this service.
1. Customers : Contains list of customers with uniwue Id and name.
2. Transactions : Contains list of all transactions for all users. Each record has a unique transaction Id, transaction Date, amount and customer Id.

Below images represent example tables.

![customers table](/images/customers.png "Customers")
![transactions table](/images/transactions.png "Transactions")

To insert additional data, add the following SQL commands to `/resources/data.sql` in the source code.
```
INSERT INTO customers (name) VALUES ('John');
INSERT INTO transactions (amount,transaction_date, customer_id) VALUES (50,'2023-05-07', 1);
```
Ensure SQL is installed on your computer.

## API usage

The API can be involed from your local host endpoint. It has two optional query parameters, `customerId` and `month`.
Note - Both query parameter names are case sensitive and API will ignore the parameter if it is misspelt.

Valid scenarios

1. Get rewards earned by all customers for all months.
   Request : ` curl -s http://localhost:8080/api/rewards`
   Expected response :
    ```
    {
      "rewardsPerCustomer": {
        "1": {
          "May": 0,
          "March": 150,
          "April": 50
        },
        "2": {
          "May": 200,
          "March": 288
        },
        "3": {
          "May": 0,
          "January": 25,
          "February": 26
        },
        "4": {
          "May": 52,
          "April": 25
        }
      },
      "totalRewardsPerCustomer": {
        "1": 200,
        "2": 488,
        "3": 51,
        "4": 77
      },
      "rewardsPerMonth": {
        "May": 252,
        "March": 438,
            "January": 25,
            "February": 26,
            "April": 75
          }
    }
    ```
2. Get rewards earned bu customer with Id`1`
   Request : `curl -s "http://localhost:8080/api/rewards?customerId=1"`
   Expected response :
    ```
    {
      "rewardsPerCustomer": {
        "1": {
          "May": 0,
          "March": 150,
          "April": 50
        }
      },
      "totalRewardsPerCustomer": {
        "1": 200
      },
      "rewardsPerMonth": {
        "May": 0,
        "March": 150,
        "April": 50
      }
    }
    ```
3. Get rewards earned by all customers for `March`
   Request : `curl -s "http://localhost:8080/api/rewards?month=March"`
   Expected response :
    ```
    {
      "rewardsPerCustomer": {
        "1": {
          "March": 150
        },
        "2": {
          "March": 288
        }
      },
      "totalRewardsPerCustomer": {
        "1": 150,
        "2": 288
      },
      "rewardsPerMonth": {
        "March": 438
      }
    }
    ```
4. Get rewards earned by customer with Id `1` for `March`
   Request : `curl -s "http://localhost:8080/api/rewards?customerId=1&month=March"`
   Expected response :
    ```
    {
      "rewardsPerCustomer": {
        "1": {
          "March": 150
        }
      },
      "totalRewardsPerCustomer": {
        "1": 150
      },
      "rewardsPerMonth": {
        "March": 150
      }
    }
    ```
5. Get rewards for valid customer and month where transactions for the combination do not exist in database.
   Request : `curl -s "http://localhost:8080/api/rewards?customerId=2&month=December"`
   Expected Response :
    ```
    {
      "rewardsPerCustomer": {},
      "totalRewardsPerCustomer": {},
      "rewardsPerMonth": {}
    }
    ```

Error scenarios

5. Get rewards earned for a customer not present in the database
   Request : `curl -s "http://localhost:8080/api/rewards?customerId=5"`
   Expected response :
    ```
    {
      "errorMessage": "Customer with ID 5 not found",
      "exceptionType": "CUSTOMER_NOT_FOUND"
    }
    ```
6. Incorrect Month specified.
   Request : `curl -s "http://localhost:8080/api/rewards?month=Januery"`
   Expected response :
    ```
    {
      "errorMessage": "Invalid month: Januery",
      "exceptionType": "INVALID_MONTH"
    }
    ```
7. Incorrect Customer Id format.
   Request : `curl -s "http://localhost:8080/api/rewards?customerId=-5"`
   Expected response :
    ```
    {
      "errorMessage": "Invalid Customer Id: -5",
      "exceptionType": "INVALID_CUSTOMER_ID"
    }
    ```
Health check endpoint

There is also a health check endpoint that can be used to check if service is up / down.

    Request : `http://localhost:8080/api/rewards/health` on your browser
    Response : a message appears stating 'service is running' or 'service is down'

![health check example dmeo from browser](/images/healthcheck.png "Health Check Demo")

## Installing and running service on your local machine

1. Clone the repository on your local machine
2. Ensure all necessary tools to run the application are installed. i.e. [mvn](https://maven.apache.org/download.cgi) , [mysql](https://dev.mysql.com/downloads/workbench/)
3. In `application.properties` , change the username and password for mysql to your root account credentials and save it.
4. Go to root directory of the spring project and run `mvn spring-boot:run
   ` . This shoulw spin up required DB instance and start spring project. Look for console logs like this
    ```
    2023-05-09T19:35:32.689-07:00  INFO 51524 --- [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint(s) beneath base path '/actuator'
    2023-05-09T19:35:32.729-07:00  INFO 51524 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
    2023-05-09T19:35:32.737-07:00  INFO 51524 --- [           main] c.r.c.CustomerRewardsServiceApplication  : Started CustomerRewardsServiceApplication in 2.024 seconds (process running for 2.158)
    ```
5. That's it ! Your application is running and go get those reward points !

## Run tests for the service.
To run unit tests associated with the services, go to root directory of spring boot project and run `mvn test`.





