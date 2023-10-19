INSERT INTO categorias (nombre) VALUES ('Gafas de Sol');
INSERT INTO categorias (nombre) VALUES ('Receta');
INSERT INTO categorias (nombre) VALUES ('Accesorios');

INSERT INTO marcas (nombre) VALUES ('Belong');
INSERT INTO marcas (nombre) VALUES ('Blitz');
INSERT INTO marcas (nombre) VALUES ('Carolina Emanuel');
INSERT INTO marcas (nombre) VALUES ('Mariana Arias');
INSERT INTO marcas (nombre) VALUES ('Mito');
INSERT INTO marcas (nombre) VALUES ('Mua');
INSERT INTO marcas (nombre) VALUES ('Pierri Cardin');
INSERT INTO marcas (nombre) VALUES ('Rusty');
INSERT INTO marcas (nombre) VALUES ('Tascanj');
INSERT INTO marcas (nombre) VALUES ('Vulk');
INSERT INTO marcas (nombre) VALUES ('Westbury');


INSERT INTO `users` (username,password,enabled) VALUES ('admin','$2a$10$i2vc/OoYSWsVDbbVm//QMu32SkVTqS9sIU.gkqVGH29.0hp9wj8Iq',1); 

INSERT INTO `roles` (name) VALUES ('ROLE_ADMIN'); 

INSERT INTO `users_roles` (user_id,role_id) VALUES (1,1); 
