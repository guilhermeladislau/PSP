-- Catálogo de alimentos. Valores nutricionais sempre por 100 g.
-- unaccent é extensão "trusted" desde o PostgreSQL 13: o dono do banco pode criá-la.
CREATE EXTENSION IF NOT EXISTS unaccent;

CREATE TABLE alimento (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome                  VARCHAR(200)  NOT NULL,
    porcao_padrao_gramas  NUMERIC(7, 2) NOT NULL CHECK (porcao_padrao_gramas > 0),
    calorias_kcal         NUMERIC(7, 2) NOT NULL CHECK (calorias_kcal >= 0),
    proteinas_g           NUMERIC(6, 2) NOT NULL CHECK (proteinas_g >= 0),
    carboidratos_g        NUMERIC(6, 2) NOT NULL CHECK (carboidratos_g >= 0),
    gorduras_g            NUMERIC(6, 2) NOT NULL CHECK (gorduras_g >= 0)
);
