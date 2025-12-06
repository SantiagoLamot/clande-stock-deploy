CREATE DATABASE IF NOT EXISTS clandestock_db;

USE clandestock_db;

CREATE TABLE IF NOT EXISTS Local_tb (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre_local VARCHAR(255) NOT NULL
);

INSERT INTO Local_tb (nombre_local) VALUES
('tenedor_libre'),
('termas'),
('heladeria');
