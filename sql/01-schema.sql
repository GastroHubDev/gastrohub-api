-- ============================================================================
-- GastroHub API - Schema (espelha o DDL gerado pelo Hibernate)
-- ----------------------------------------------------------------------------
-- Executado automaticamente pelo container MySQL na primeira inicializacao
-- (via /docker-entrypoint-initdb.d), ANTES da API subir. Usa CREATE TABLE
-- IF NOT EXISTS para conviver com o ddl-auto=update do Hibernate sem conflito.
-- ============================================================================

CREATE TABLE IF NOT EXISTS user_type (
  id            BINARY(16)   NOT NULL,
  base_category ENUM('CLIENT','OWNER') NOT NULL,
  name          VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_type_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS tb_users (
  id           BINARY(16)   NOT NULL,
  email        VARCHAR(255) NOT NULL,
  name         VARCHAR(255) NOT NULL,
  password     VARCHAR(255) NOT NULL,
  user_type_id BINARY(16)   NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_tb_users_email (email),
  KEY fk_tb_users_user_type (user_type_id),
  CONSTRAINT fk_tb_users_user_type FOREIGN KEY (user_type_id) REFERENCES user_type (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS restaurant (
  id                  BINARY(16)   NOT NULL,
  address             VARCHAR(255) NOT NULL,
  kitchen_type        ENUM('BRAZILIAN','INDIAN','ITALIAN','JAPANESE','MEXICAN') NOT NULL,
  name                VARCHAR(255) NOT NULL,
  opening_hours       VARCHAR(255) NOT NULL,
  restaurant_owner_id BINARY(16)   NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS menu_items (
  id                 BINARY(16)     NOT NULL,
  description        TEXT           NOT NULL,
  name               VARCHAR(255)   NOT NULL,
  only_in_restaurant BIT(1)         NOT NULL,
  photo_path         VARCHAR(255)   DEFAULT NULL,
  price              DECIMAL(10,2)  NOT NULL,
  restaurant_id      BINARY(16)     NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
