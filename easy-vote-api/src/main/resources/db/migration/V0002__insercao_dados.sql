INSERT INTO tb_roles (name)
VALUES ('ROLE_CONDOMINO'),
       ('ROLE_SINDICO');

INSERT INTO tb_users (name, email, password, status, apartment, role_id, password_default)
VALUES ('Administrador', 'admin@email.com', '$2a$10$CkvVHgUNoSEtgyrt9UB2RuRN0wWy5H/8FVbeL7v.bVrb6cuQoI9O2', 'ATIVO',
        101, 2, 0);