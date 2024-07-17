CREATE TABLE posting (
     id BIGSERIAL PRIMARY KEY,
     posting_description VARCHAR(255) NOT NULL,
     due_date DATE NOT NULL,
     payment_date DATE,
     posting_value DECIMAL(10, 2) NOT NULL,
     note VARCHAR(100),
     posting_type VARCHAR(20) NOT NULL,
     category_id BIGSERIAL NOT NULL,
     person_id BIGSERIAL NOT NULL,
     FOREIGN KEY (category_id) REFERENCES category(id),
     FOREIGN KEY (person_id) REFERENCES person(id)
);

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Salário mensal',
        '2017-06-10',
        null,
        6500.00,
        'Distribuição de lucros',
        'RECEITA',
        1,
        1
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Supermercado',
        '2017-02-10',
        '2017-02-10',
        100.32,
        null,
        'DESPESA',
        2,
        2
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Academia',
        '2017-06-10',
        null,
        120,
        null,
        'DESPESA',
        3,
        3
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Conta de luz',
        '2017-02-10',
        '2017-02-10',
        110.44,
        null,
        'DESPESA',
        3,
        4
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Conta de água',
        '2017-06-10',
        null,
        200.30,
        null,
        'DESPESA',
        3,
        5
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Restaurante',
        '2017-03-10',
        '2017-03-10',
        1010.32,
        null,
        'DESPESA',
        4,
        6
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Venda vídeo game',
        '2017-06-10',
        null,
        500,
        null,
        'RECEITA',
        1,
        7
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Clube',
        '2017-03-10',
        '2017-03-10',
        400.32,
        null,
        'DESPESA',
        4,
        8
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Impostos',
        '2017-06-10',
        null,
        123.64,
        'Multas',
        'DESPESA',
        3,
        9
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Multa',
        '2017-04-10',
        '2017-04-10',
        665.33,
        null,
        'DESPESA',
        5,
        10
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Padaria',
        '2017-06-10',
        null,
        8.32,
        null,
        'DESPESA',
        1,
        5
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Papelaria',
        '2017-04-10',
        '2017-04-10',
        2100.32,
        null,
        'DESPESA',
        5,
        4
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Almoço',
        '2017-06-10',
        null,
        1040.32,
        null,
        'DESPESA',
        4,
        3
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Café',
        '2017-04-10',
        '2017-04-10',
        4.32,
        null,
        'DESPESA',
        4,
        2
    );

INSERT INTO
    posting (
    posting_description,
    due_date,
    payment_date,
    posting_value,
    note,
    posting_type,
    category_id,
    person_id
)
VALUES
    (
        'Lanche',
        '2017-06-10',
        null,
        10.20,
        null,
        'DESPESA',
        4,
        1
    );