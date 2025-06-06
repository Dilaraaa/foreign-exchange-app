# 💱 Currency Converter API

A RESTful API for currency conversion, exchange rate lookup, historical data retrieval, and bulk conversions via CSV upload.

> **Base URL:** `http://localhost:8080`

---

## 🚀 Endpoints

### 🔹 GET `/currency/exchange-rate`

Retrieve exchange rate between two currencies.

#### Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| `from` | string | ✅ | Source currency code (e.g., USD) |
| `to`   | string | ✅ | Target currency code (e.g., TRY) |

#### Response

```
32.45
```

#### Errors

- `400 BAD REQUEST` – Invalid currency code, missing parameters  
- `500 INTERNAL SERVER ERROR` – Unexpected error

---

### 🔹 GET `/currency/conversion`

Convert an amount from one currency to another.

#### Parameters

| Name   | Type   | Required | Description |
|--------|--------|----------|-------------|
| `from` | string | ✅ | Source currency code |
| `to`   | string | ✅ | Target currency code |
| `amount` | number | ✅ | Amount to convert     |

#### Response

```json
{
  "amountInTargetCurrency": 6789.45,
  "transactionId": 123456
}
```

#### Errors

- `400 BAD REQUEST` – Invalid currency code, missing parameters, misformatted parameters  
- `500 INTERNAL SERVER ERROR` – Unexpected error

---

### 🔹 GET `/currency/conversion-history`

Retrieve past conversions by transaction ID or date.

> ⚠️ Either `transactionId` or `date` must be provided.

#### Parameters

| Name | Type | Required | Description |
|------|------|----------|-------------|
| `transactionId` | string | ❌ | Filter by transaction ID |
| `date` | string (dd.MM.yyyy) | ❌ | Filter by conversion date |
| `page` | int | ❌ | Page number (default: 0) |
| `size` | int | ❌ | Page size (default: 10, max: 100) |

#### Response

```json
{
  "content": [
    {
      "transactionId": 1,
      "sourceCurrencyCode": "USD",
      "targetCurrencyCode": "TRY",
      "sourceAmount": 210,
      "exchangeRate": 39.2742,
      "transactionDate": "2025-06-06T02:48:20.347793+03:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "unpaged": false,
    "paged": true
  },
  "last": true,
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "numberOfElements": 1,
  "first": true,
  "empty": false
}
```

#### Errors

- `400 BAD REQUEST` – Missing parameters, misformatted parameters  
- `500 INTERNAL SERVER ERROR` – Unexpected error

---

### 🔹 POST `/currency/bulk-conversion`

Upload a CSV file for bulk conversion.

#### Request

- Content-Type: `multipart/form-data`
- Parameter: `file` — CSV file (max: 10MB)

#### CSV Format

```
sourceCurrencyCode,targetCurrencyCode,amount
USD,TRY,100
EUR,GBP,50
```

Each row maps to:

```json
{
  "sourceCurrencyCode": "USD",
  "targetCurrencyCode": "TRY",
  "amount": 100.00
}
```

#### Response

`200 OK` — Downloadable `conversion-results.csv` file

#### Errors

- `400 BAD REQUEST` – Missing or invalid file  
- `413 PAYLOAD TOO LARGE` – File exceeds 10MB  
- `500 INTERNAL SERVER ERROR` – Unexpected error

---

## ❗ Error Responses

All error responses follow this format:

```json
{
  "error": "Description of the problem"
}
```

---

## 📘 Common Error Codes

| Code | Description |
|------|-------------|
| `400` | Validation error, missing or invalid parameter |
| `404` | No resource found |
| `413` | File too large |
| `500` | Unexpected server error |


## Swagger URL

/swagger-ui/index.html

## Setup

![mvn-clean-package](https://github.com/user-attachments/assets/e241b3bb-6ca1-4947-a1be-8d9c6e773ffe)

![docker-build](https://github.com/user-attachments/assets/af7b2d54-0244-4295-85ae-17dfd1258512)

![docker-run](https://github.com/user-attachments/assets/1539e349-b6f6-492e-8aea-c047204cd8d3)

- `mvn clean package -DskipTests`
- `docker build -t currency-converter .`
- `docker run -p 8080:8080 currency-converter`




