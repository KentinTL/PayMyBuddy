-- Creation de la table "users"
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
);

-- Creation de la table "user_friends"
CREATE TABLE user_friends (
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id)
);

-- Creation de la table "transactions"
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(38,2) NOT NULL,
    date DATETIME(6) NOT NULL,
    description VARCHAR(255),
    fee DECIMAL(38,2) NOT NULL,
    transaction_type TINYINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    FOREIGN KEY (receiver_id) REFERENCES users(id),
    FOREIGN KEY (sender_id) REFERENCES users(id)
);