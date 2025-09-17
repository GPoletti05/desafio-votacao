/* =========================
   PAUTA
   ========================= */
CREATE SEQUENCE IF NOT EXISTS pauta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS pauta (
    id BIGINT NOT NULL DEFAULT nextval('pauta_id_seq'),
    titulo VARCHAR(255) NOT NULL,
    motivo TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT pauta_pk PRIMARY KEY (id)
    );

ALTER SEQUENCE pauta_id_seq OWNED BY pauta.id;

/* =========================
   ASSOCIADO
   ========================= */
CREATE SEQUENCE IF NOT EXISTS associado_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS associado (
    id BIGINT NOT NULL DEFAULT nextval('associado_id_seq'),
    nome VARCHAR(255) NOT NULL,
    cpf CHAR(11) NOT NULL,
    CONSTRAINT associado_pk PRIMARY KEY (id),
    CONSTRAINT uq_associado_cpf UNIQUE (cpf)
    );

ALTER SEQUENCE associado_id_seq OWNED BY associado.id;


/* =========================
   SESSÃO DE VOTAÇÃO
   ========================= */
CREATE SEQUENCE IF NOT EXISTS sessao_votacao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS sessao_votacao (
    id BIGINT NOT NULL DEFAULT nextval('sessao_votacao_id_seq'),
    pauta_id BIGINT NOT NULL UNIQUE REFERENCES pauta(id),
    data_abertura TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_fechamento TIMESTAMP NOT NULL,
    encerrada BOOLEAN,
    CONSTRAINT sessao_votacao_pk PRIMARY KEY (id),
    CONSTRAINT fk_sessao_pauta FOREIGN KEY (pauta_id) REFERENCES pauta(id)
    );

ALTER SEQUENCE sessao_votacao_id_seq OWNED BY sessao_votacao.id;


/* =========================
   VOTO
   ========================= */
CREATE SEQUENCE IF NOT EXISTS voto_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE IF NOT EXISTS voto (
    id BIGINT NOT NULL DEFAULT nextval('voto_id_seq'),
    associado_id BIGINT NOT NULL,
    sessao_votacao_id BIGINT NOT NULL,
    escolha VARCHAR(3) NOT NULL,
    data_voto TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT voto_pk PRIMARY KEY (id),
    CONSTRAINT fk_voto_associado FOREIGN KEY (associado_id) REFERENCES associado(id),
    CONSTRAINT fk_voto_sessao_votacao FOREIGN KEY (sessao_votacao_id) REFERENCES sessao_votacao(id),
    CONSTRAINT uq_voto UNIQUE (associado_id, sessao_votacao_id)
    );

ALTER SEQUENCE voto_id_seq OWNED BY voto.id;