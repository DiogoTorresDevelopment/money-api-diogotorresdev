CREATE TABLE category (
  id BIGSERIAL PRIMARY KEY,
  category_name VARCHAR(50) NOT NULL
);

INSERT INTO
    category (category_name)
VALUES
    ('Lazer'),
    ('Alimentação'),
    ('Supermercado'),
    ('Farmácia'),
    ('Outros');