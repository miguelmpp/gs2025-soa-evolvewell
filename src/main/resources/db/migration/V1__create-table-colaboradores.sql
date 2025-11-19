CREATE TABLE colaboradores (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               ativo TINYINT(1) NOT NULL,
                               nome VARCHAR(100) NOT NULL,
                               email VARCHAR(150) NOT NULL UNIQUE,
                               telefone VARCHAR(20) NOT NULL,
                               cargo VARCHAR(100) NOT NULL,
                               departamento VARCHAR(100) NOT NULL,
                               modo_trabalho VARCHAR(20) NOT NULL,
                               horas_sono INT,
                               minutos_exercicio_semana INT,
                               pausas_turno INT,
                               nivel_estresse_auto INT,
                               PRIMARY KEY (id)
);
