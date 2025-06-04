CREATE TABLE supported_currency (code VARCHAR(32) PRIMARY KEY);

CREATE TABLE conversion_history (
                                    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    source_currency_code VARCHAR(3) NOT NULL,
                                    target_currency_code VARCHAR(3) NOT NULL,
                                    source_amount NUMERIC(24, 6) NOT NULL,
                                    exchange_rate NUMERIC(18, 10) NOT NULL,
                                    transaction_date TIMESTAMP WITH TIME ZONE NOT NULL
);
