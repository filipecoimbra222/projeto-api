CREATE TABLE lancamento (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(50) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(10,2) NOT NULL,
    observacao VARCHAR(100),
    tipo VARCHAR(20) NOT NULL,
    codigo_categoria BIGINT(20) NOT NULL,
    codigo_pessoa BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_categoria) REFERENCES categorias(codigo),
    FOREIGN KEY (codigo_pessoa) REFERENCES pessoa(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Inserir alguns registros de exemplo
INSERT INTO lancamento (descricao, data_vencimento, data_pagamento, valor, observacao, tipo, codigo_categoria, codigo_pessoa) VALUES
    ('Salário mensal', '2025-10-05', '2025-10-05', 6000.00, 'Distribuição de lucros', 'RECEITA', 1, 1),
    ('Supermercado', '2025-10-10', '2025-10-10', 350.00, 'Compras do mês', 'DESPESA', 3, 1),
    ('Conta de luz', '2025-10-15', NULL, 150.00, NULL, 'DESPESA', 5, 2),
    ('Restaurante', '2025-10-20', '2025-10-19', 85.00, 'Jantar em família', 'DESPESA', 2, 2),
    ('Freelance', '2025-10-25', NULL, 1500.00, 'Projeto web', 'RECEITA', 1, 3);
