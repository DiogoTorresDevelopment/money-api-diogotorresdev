CREATE TABLE user_account (
    id BIGINT PRIMARY KEY,
    user_account_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    user_account_password VARCHAR(255) NOT NULL
);

CREATE TABLE permission (
    id BIGINT PRIMARY KEY,
    permission_description VARCHAR(255) NOT NULL
);

CREATE TABLE user_account_permission (
    user_account_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (user_account_id, permission_id),
    FOREIGN KEY (user_account_id) REFERENCES user_account(id),
    FOREIGN KEY (permission_id) REFERENCES permission(id)
);

INSERT INTO
    user_account (
        id,
        user_account_name,
        email,
        user_account_password
    )
VALUES
    (
        1,
        'admin',
        'admin@moneyapi.com',
        '$2a$10$R.c7H8vN.ts.4oS9sYZ3ROYoAi6KzLy3xDkODTrnJVJwdzVcLWnje'
    ),
    (
        2,
        'hally',
        'hally@moneyapi.com',
        '$2a$10$Zc3w6HyuPOPXamaMhh.PQOXvDnEsadztbfi6/RyZWJDzimE8WQjaq'
    );

INSERT INTO
    permission (id, permission_description)
VALUES
    (1, 'ROLE_REGISTER_CATEGORY'),
    (2, 'ROLE_VIEW_CATEGORY'),
    (3, 'ROLE_UPDATE_CATEGORY'),
    (4, 'ROLE_DELETE_CATEGORY'),
    (5, 'ROLE_REGISTER_PERSON'),
    (6, 'ROLE_VIEW_PERSON'),
    (7, 'ROLE_UPDATE_PERSON'),
    (8, 'ROLE_DELETE_PERSON'),
    (9, 'ROLE_REGISTER_POSTING'),
    (10, 'ROLE_VIEW_POSTING'),
    (11, 'ROLE_UPDATE_POSTING'),
    (12, 'ROLE_DELETE_POSTING');

INSERT INTO
    user_account_permission (user_account_id, permission_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7),
    (1, 8),
    (1, 9),
    (1, 10),
    (1, 11),
    (1, 12);

INSERT INTO
    user_account_permission (user_account_id, permission_id)
VALUES
    (2, 2),
    (2, 6),
    (2, 10);