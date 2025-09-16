CREATE TABLE pessoa (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL,
    logradouro VARCHAR(100),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(60),
    cep VARCHAR(9),
    cidade VARCHAR(60),
    estado VARCHAR(2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Inserir alguns registros de exemplo
INSERT INTO pessoa (nome, ativo, logradouro, numero, bairro, cep, cidade, estado) VALUES 
    ('João da Silva', true, 'Rua das Flores', '123', 'Centro', '12345-678', 'São Paulo', 'SP'),
    ('Maria Oliveira', true, 'Avenida Brasil', '456', 'Jardim América', '54321-876', 'Rio de Janeiro', 'RJ'),
    ('Carlos Santos', false, 'Rua das Palmeiras', '789', 'Vila Nova', '98765-432', 'Belo Horizonte', 'MG');
