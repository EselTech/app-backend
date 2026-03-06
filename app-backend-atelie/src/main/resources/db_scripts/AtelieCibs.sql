create database ateliecibs;

use ateliecibs;

CREATE TABLE `imposto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome_imposto` varchar(100) NOT NULL,
  `codigo_sgs` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

create table `historico_imposto` (
  `id` int not null auto_increment,
  `imposto_id` int not null,
  `valor` decimal(10, 4) not null,
  `data_registro` date not null,
  primary key (`id`),
  unique key `uk_imposto_data` (`imposto_id`,
`data_registro`),
  constraint `fk_historico_imposto_indicador` foreign key (`imposto_id`) references `imposto` (`id`) on
delete
	restrict on
	update
		cascade
) engine = InnoDB auto_increment = 66 default CHARSET = utf8mb4 collate = utf8mb4_0900_ai_ci;

INSERT INTO imposto
(id, nome_imposto, codigo_sgs)
VALUES(1, 'IPCA - Mensal', 433);
INSERT INTO imposto
(id, nome_imposto, codigo_sgs)
VALUES(2, 'IGP-M - Mensal', 189);
INSERT INTO imposto
(id, nome_imposto, codigo_sgs)
VALUES(3, 'Taxa Selic - Diária', 11);
INSERT INTO imposto
(id, nome_imposto, codigo_sgs)
VALUES(4, 'Dólar Comercial - Venda', 1);
INSERT INTO imposto
(id, nome_imposto, codigo_sgs)
VALUES(5, 'Euro Comercial - Venda', 21619);
