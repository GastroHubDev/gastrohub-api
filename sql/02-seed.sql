-- ============================================================================
-- GastroHub API - Seed de dados para testes
-- ----------------------------------------------------------------------------
-- Popula todas as entidades: user_type, tb_users, restaurant, menu_items.
-- Os IDs sao UUIDs fixos (armazenados como BINARY(16)) para manter as
-- relacoes consistentes e permitir re-execucao (idempotente).
--
-- Carregado automaticamente pelo container MySQL na primeira inicializacao
-- (via /docker-entrypoint-initdb.d), logo apos o 01-schema.sql.
--
-- Para rodar manualmente:
--   docker exec -i gastrohub-mysql mysql -ugastrohub -pgastrohub gastrohub < sql/02-seed.sql
-- ============================================================================

USE gastrohub;

-- Limpeza (respeitando a ordem de dependencia) para tornar o script re-executavel
DELETE FROM menu_items;
DELETE FROM restaurant;
DELETE FROM tb_users;
DELETE FROM user_type;

-- ---------------------------------------------------------------------------
-- USER TYPES
-- ---------------------------------------------------------------------------
INSERT INTO user_type (id, name, base_category) VALUES
  (UNHEX(REPLACE('11111111-1111-1111-1111-111111111111','-','')), 'Cliente',       'CLIENT'),
  (UNHEX(REPLACE('22222222-2222-2222-2222-222222222222','-','')), 'Proprietario',  'OWNER'),
  (UNHEX(REPLACE('33333333-3333-3333-3333-333333333333','-','')), 'Gerente',       'OWNER');

-- ---------------------------------------------------------------------------
-- USERS
--   Proprietarios: owners que possuem restaurantes
--   Clientes: usuarios finais
-- ---------------------------------------------------------------------------
-- Proprietarios (user_type Proprietario)
INSERT INTO tb_users (id, name, email, password, user_type_id) VALUES
  (UNHEX(REPLACE('a1000000-0000-0000-0000-000000000001','-','')), 'Marco Rossi',      'marco.rossi@gastrohub.com',    'password123', UNHEX(REPLACE('22222222-2222-2222-2222-222222222222','-',''))),
  (UNHEX(REPLACE('a1000000-0000-0000-0000-000000000002','-','')), 'Yuki Tanaka',      'yuki.tanaka@gastrohub.com',    'password123', UNHEX(REPLACE('22222222-2222-2222-2222-222222222222','-',''))),
  (UNHEX(REPLACE('a1000000-0000-0000-0000-000000000003','-','')), 'Joana Silva',      'joana.silva@gastrohub.com',    'password123', UNHEX(REPLACE('22222222-2222-2222-2222-222222222222','-',''))),
  (UNHEX(REPLACE('a1000000-0000-0000-0000-000000000004','-','')), 'Diego Fernandez',  'diego.fernandez@gastrohub.com','password123', UNHEX(REPLACE('22222222-2222-2222-2222-222222222222','-','')));

-- Gerente (user_type Gerente)
INSERT INTO tb_users (id, name, email, password, user_type_id) VALUES
  (UNHEX(REPLACE('a2000000-0000-0000-0000-000000000001','-','')), 'Priya Sharma',     'priya.sharma@gastrohub.com',   'password123', UNHEX(REPLACE('33333333-3333-3333-3333-333333333333','-','')));

-- Clientes (user_type Cliente)
INSERT INTO tb_users (id, name, email, password, user_type_id) VALUES
  (UNHEX(REPLACE('c1000000-0000-0000-0000-000000000001','-','')), 'Ana Costa',        'ana.costa@example.com',        'password123', UNHEX(REPLACE('11111111-1111-1111-1111-111111111111','-',''))),
  (UNHEX(REPLACE('c1000000-0000-0000-0000-000000000002','-','')), 'Bruno Almeida',    'bruno.almeida@example.com',    'password123', UNHEX(REPLACE('11111111-1111-1111-1111-111111111111','-',''))),
  (UNHEX(REPLACE('c1000000-0000-0000-0000-000000000003','-','')), 'Carla Mendes',     'carla.mendes@example.com',     'password123', UNHEX(REPLACE('11111111-1111-1111-1111-111111111111','-',''))),
  (UNHEX(REPLACE('c1000000-0000-0000-0000-000000000004','-','')), 'Daniel Souza',     'daniel.souza@example.com',     'password123', UNHEX(REPLACE('11111111-1111-1111-1111-111111111111','-',''))),
  (UNHEX(REPLACE('c1000000-0000-0000-0000-000000000005','-','')), 'Elena Rodrigues',  'elena.rodrigues@example.com',  'password123', UNHEX(REPLACE('11111111-1111-1111-1111-111111111111','-','')));

-- ---------------------------------------------------------------------------
-- RESTAURANTS
--   restaurant_owner_id referencia um usuario proprietario
-- ---------------------------------------------------------------------------
INSERT INTO restaurant (id, name, address, kitchen_type, opening_hours, restaurant_owner_id) VALUES
  (UNHEX(REPLACE('e1000000-0000-0000-0000-000000000001','-','')), 'Cantina do Marco',     'Rua Augusta, 100 - Sao Paulo',      'ITALIAN',   'Seg-Dom 11:00-23:00', UNHEX(REPLACE('a1000000-0000-0000-0000-000000000001','-',''))),
  (UNHEX(REPLACE('e1000000-0000-0000-0000-000000000002','-','')), 'Sushi Tanaka',         'Av. Paulista, 2000 - Sao Paulo',    'JAPANESE',  'Ter-Dom 18:00-23:30', UNHEX(REPLACE('a1000000-0000-0000-0000-000000000002','-',''))),
  (UNHEX(REPLACE('e1000000-0000-0000-0000-000000000003','-','')), 'Tempero da Joana',     'Rua das Flores, 45 - Rio de Janeiro','BRAZILIAN', 'Seg-Sab 11:00-15:00', UNHEX(REPLACE('a1000000-0000-0000-0000-000000000003','-',''))),
  (UNHEX(REPLACE('e1000000-0000-0000-0000-000000000004','-','')), 'Taqueria El Diego',    'Rua Mexico, 300 - Belo Horizonte',  'MEXICAN',   'Qua-Dom 17:00-00:00', UNHEX(REPLACE('a1000000-0000-0000-0000-000000000004','-',''))),
  (UNHEX(REPLACE('e1000000-0000-0000-0000-000000000005','-','')), 'Curry House Priya',    'Av. India, 88 - Curitiba',          'INDIAN',    'Seg-Dom 12:00-22:00', UNHEX(REPLACE('a2000000-0000-0000-0000-000000000001','-','')));

-- ---------------------------------------------------------------------------
-- MENU ITEMS
--   restaurant_id referencia um restaurante
--   only_in_restaurant: bit(1) -> 1 (somente no local) / 0 (delivery tambem)
-- ---------------------------------------------------------------------------
-- Cantina do Marco (ITALIAN)
INSERT INTO menu_items (id, name, description, price, only_in_restaurant, photo_path, restaurant_id) VALUES
  (UNHEX(REPLACE('f1000000-0000-0000-0000-000000000001','-','')), 'Spaghetti Carbonara', 'Massa fresca com ovos, guanciale, pecorino e pimenta preta.', 48.90, 0, '/images/carbonara.jpg', UNHEX(REPLACE('e1000000-0000-0000-0000-000000000001','-',''))),
  (UNHEX(REPLACE('f1000000-0000-0000-0000-000000000002','-','')), 'Lasagna Bolognese',   'Lasanha ao molho bolonhesa artesanal com bechamel.',          54.00, 0, '/images/lasagna.jpg',   UNHEX(REPLACE('e1000000-0000-0000-0000-000000000001','-',''))),
  (UNHEX(REPLACE('f1000000-0000-0000-0000-000000000003','-','')), 'Tiramisu',            'Sobremesa classica italiana com cafe e mascarpone.',          26.50, 0, NULL,                    UNHEX(REPLACE('e1000000-0000-0000-0000-000000000001','-',''))),
  (UNHEX(REPLACE('f1000000-0000-0000-0000-000000000004','-','')), 'Chianti da Casa',     'Taca de vinho tinto Chianti - servido apenas no salao.',      32.00, 1, NULL,                    UNHEX(REPLACE('e1000000-0000-0000-0000-000000000001','-','')));

-- Sushi Tanaka (JAPANESE)
INSERT INTO menu_items (id, name, description, price, only_in_restaurant, photo_path, restaurant_id) VALUES
  (UNHEX(REPLACE('f2000000-0000-0000-0000-000000000001','-','')), 'Combo Sushi 20 pecas','Selecao do chef com salmao, atum e peixe branco.',            89.90, 0, '/images/combo20.jpg',   UNHEX(REPLACE('e1000000-0000-0000-0000-000000000002','-',''))),
  (UNHEX(REPLACE('f2000000-0000-0000-0000-000000000002','-','')), 'Temaki Salmao',       'Cone de alga recheado com salmao fresco e cream cheese.',     28.00, 0, '/images/temaki.jpg',    UNHEX(REPLACE('e1000000-0000-0000-0000-000000000002','-',''))),
  (UNHEX(REPLACE('f2000000-0000-0000-0000-000000000003','-','')), 'Missoshiru',          'Sopa tradicional de misso com tofu e cebolinha.',             12.00, 0, NULL,                    UNHEX(REPLACE('e1000000-0000-0000-0000-000000000002','-',''))),
  (UNHEX(REPLACE('f2000000-0000-0000-0000-000000000004','-','')), 'Omakase do Chef',     'Experiencia degustacao exclusiva no balcao.',                180.00, 1, NULL,                    UNHEX(REPLACE('e1000000-0000-0000-0000-000000000002','-','')));

-- Tempero da Joana (BRAZILIAN)
INSERT INTO menu_items (id, name, description, price, only_in_restaurant, photo_path, restaurant_id) VALUES
  (UNHEX(REPLACE('f3000000-0000-0000-0000-000000000001','-','')), 'Feijoada Completa',   'Feijoada com acompanhamentos, arroz, couve e farofa.',        45.00, 0, '/images/feijoada.jpg',  UNHEX(REPLACE('e1000000-0000-0000-0000-000000000003','-',''))),
  (UNHEX(REPLACE('f3000000-0000-0000-0000-000000000002','-','')), 'Moqueca de Peixe',    'Moqueca capixaba com dende e leite de coco.',                 62.00, 0, '/images/moqueca.jpg',   UNHEX(REPLACE('e1000000-0000-0000-0000-000000000003','-',''))),
  (UNHEX(REPLACE('f3000000-0000-0000-0000-000000000003','-','')), 'Pudim de Leite',      'Pudim de leite condensado com calda de caramelo.',            18.00, 0, NULL,                    UNHEX(REPLACE('e1000000-0000-0000-0000-000000000003','-','')));

-- Taqueria El Diego (MEXICAN)
INSERT INTO menu_items (id, name, description, price, only_in_restaurant, photo_path, restaurant_id) VALUES
  (UNHEX(REPLACE('f4000000-0000-0000-0000-000000000001','-','')), 'Tacos al Pastor',     'Tres tacos de carne suina marinada com abacaxi.',             34.00, 0, '/images/tacos.jpg',     UNHEX(REPLACE('e1000000-0000-0000-0000-000000000004','-',''))),
  (UNHEX(REPLACE('f4000000-0000-0000-0000-000000000002','-','')), 'Guacamole',           'Pasta de abacate com tomate, cebola e coentro.',              24.00, 0, '/images/guacamole.jpg', UNHEX(REPLACE('e1000000-0000-0000-0000-000000000004','-',''))),
  (UNHEX(REPLACE('f4000000-0000-0000-0000-000000000003','-','')), 'Margarita',           'Coquetel de tequila, limao e triple sec - somente no local.', 29.00, 1, NULL,                    UNHEX(REPLACE('e1000000-0000-0000-0000-000000000004','-','')));

-- Curry House Priya (INDIAN)
INSERT INTO menu_items (id, name, description, price, only_in_restaurant, photo_path, restaurant_id) VALUES
  (UNHEX(REPLACE('f5000000-0000-0000-0000-000000000001','-','')), 'Chicken Tikka Masala','Frango em molho cremoso de tomate e especiarias.',            49.00, 0, '/images/tikka.jpg',     UNHEX(REPLACE('e1000000-0000-0000-0000-000000000005','-',''))),
  (UNHEX(REPLACE('f5000000-0000-0000-0000-000000000002','-','')), 'Naan de Alho',        'Pao indiano assado no forno tandoor com alho.',               14.00, 0, '/images/naan.jpg',      UNHEX(REPLACE('e1000000-0000-0000-0000-000000000005','-',''))),
  (UNHEX(REPLACE('f5000000-0000-0000-0000-000000000003','-','')), 'Biryani de Cordeiro', 'Arroz basmati com cordeiro e especiarias aromaticas.',        58.00, 0, NULL,                    UNHEX(REPLACE('e1000000-0000-0000-0000-000000000005','-',''))),
  (UNHEX(REPLACE('f5000000-0000-0000-0000-000000000004','-','')), 'Mango Lassi',         'Bebida gelada de iogurte com manga.',                         16.00, 0, NULL,                    UNHEX(REPLACE('e1000000-0000-0000-0000-000000000005','-','')));
