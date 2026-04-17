CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    username VARCHAR(255),
    role VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    content TEXT,
    url TEXT NOT NULL,
    url_hash VARCHAR(32) NOT NULL UNIQUE,
    source VARCHAR(255),
    sector VARCHAR(255),
    impact_level VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    published_at TIMESTAMP,
    fetched_at TIMESTAMP NOT NULL
);

CREATE TABLE keyword_rules (
    id BIGSERIAL PRIMARY KEY,
    sector VARCHAR(255) NOT NULL,
    keyword VARCHAR(100) NOT NULL,
    impact_score INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE fetch_logs (
    id BIGSERIAL PRIMARY KEY,
    fetched_at TIMESTAMP NOT NULL,
    articles_fetched INTEGER,
    articles_saved INTEGER,
    status VARCHAR(255),
    error_message TEXT
);

CREATE TABLE user_subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    sector VARCHAR(255) NOT NULL,
    min_impact_level VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_user_subscriptions_user_sector UNIQUE (user_id, sector),
    CONSTRAINT fk_user_subscriptions_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    article_id BIGINT NOT NULL,
    is_read BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_notifications_article FOREIGN KEY (article_id) REFERENCES articles(id)
);

CREATE TABLE sectors (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE user_sectors (
    user_id BIGINT NOT NULL,
    sector_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, sector_id),
    CONSTRAINT fk_user_sectors_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_sectors_sector FOREIGN KEY (sector_id) REFERENCES sectors(id)
);
