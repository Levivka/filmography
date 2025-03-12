-- Создание таблицы films (если она еще не существует)
CREATE TABLE IF NOT EXISTS films (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    director VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    rating DOUBLE PRECISION NOT NULL,
    synopsis TEXT
);