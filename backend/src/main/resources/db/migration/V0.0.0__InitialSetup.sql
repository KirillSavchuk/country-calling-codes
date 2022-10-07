CREATE TABLE country
(
    id       VARCHAR(36) NOT NULL,
    name     VARCHAR(255),
    flag_url VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE country_calling_code
(
    id         VARCHAR(36) NOT NULL,
    code       INT         NOT NULL,
    country_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_country_calling_code_code ON country_calling_code (code);

ALTER TABLE country_calling_code
    ADD FOREIGN KEY (country_id) REFERENCES country (id);