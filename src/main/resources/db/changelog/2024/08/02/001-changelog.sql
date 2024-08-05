-- liquibase formatted sql

-- changeset hari:1722619162032-1
CREATE SEQUENCE IF NOT EXISTS book_seq START WITH 1 INCREMENT BY 1;

-- changeset hari:1722619162032-2
CREATE SEQUENCE IF NOT EXISTS book_transaction_seq START WITH 1 INCREMENT BY 1;

-- changeset hari:1722619162032-3
CREATE SEQUENCE IF NOT EXISTS member_seq START WITH 1 INCREMENT BY 1;

-- changeset hari:1722619162032-4
CREATE SEQUENCE IF NOT EXISTS revinfo_seq START WITH 1 INCREMENT BY 50;

-- changeset hari:1722619162032-5
CREATE TABLE book
(
    id               BIGINT                      NOT NULL,
    version          BIGINT,
    created_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by       VARCHAR(255)                NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_by       VARCHAR(255)                NOT NULL,
    deleted          BOOLEAN DEFAULT FALSE       NOT NULL,
    title            VARCHAR(255)                NOT NULL,
    author           VARCHAR(255)                NOT NULL,
    isbn             VARCHAR(255)                NOT NULL,
    genre            VARCHAR(255),
    publication_date date,
    copies_available INTEGER                     NOT NULL,
    CONSTRAINT pk_book PRIMARY KEY (id)
);

-- changeset hari:1722619162032-6
CREATE TABLE book_aud
(
    rev              BIGINT NOT NULL,
    revtype          SMALLINT,
    id               BIGINT NOT NULL,
    title            VARCHAR(255),
    author           VARCHAR(255),
    isbn             VARCHAR(255),
    genre            VARCHAR(255),
    publication_date date,
    copies_available INTEGER,
    CONSTRAINT pk_book_aud PRIMARY KEY (rev, id)
);

-- changeset hari:1722619162032-7
CREATE TABLE book_transaction
(
    id           BIGINT                      NOT NULL,
    version      BIGINT,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by   VARCHAR(255)                NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_by   VARCHAR(255)                NOT NULL,
    deleted      BOOLEAN DEFAULT FALSE       NOT NULL,
    book_fk      BIGINT,
    member_fk    BIGINT,
    request_date date                        NOT NULL,
    status       VARCHAR(255)                NOT NULL,
    issue_date   date,
    due_date     date,
    return_date  date,
    CONSTRAINT pk_book_transaction PRIMARY KEY (id)
);

-- changeset hari:1722619162032-8
CREATE TABLE book_transaction_aud
(
    rev          BIGINT NOT NULL,
    revtype      SMALLINT,
    id           BIGINT NOT NULL,
    book_fk      BIGINT,
    member_fk    BIGINT,
    request_date date,
    status       VARCHAR(255),
    issue_date   date,
    due_date     date,
    return_date  date,
    CONSTRAINT pk_book_transaction_aud PRIMARY KEY (rev, id)
);

-- changeset hari:1722619162032-9
CREATE TABLE member
(
    id         BIGINT                      NOT NULL,
    version    BIGINT,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_by VARCHAR(255)                NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_by VARCHAR(255)                NOT NULL,
    deleted    BOOLEAN DEFAULT FALSE       NOT NULL,
    name       VARCHAR(255)                NOT NULL,
    email      VARCHAR(255)                NOT NULL,
    password   VARCHAR(255)                NOT NULL,
    phone      VARCHAR(255),
    role       VARCHAR(255),
    CONSTRAINT pk_member PRIMARY KEY (id)
);

-- changeset hari:1722619162032-10
CREATE TABLE member_aud
(
    rev      BIGINT NOT NULL,
    revtype  SMALLINT,
    id       BIGINT NOT NULL,
    name     VARCHAR(255),
    email    VARCHAR(255),
    password VARCHAR(255),
    phone    VARCHAR(255),
    role     VARCHAR(255),
    CONSTRAINT pk_member_aud PRIMARY KEY (rev, id)
);

-- changeset hari:1722619162032-11
CREATE TABLE revinfo
(
    rev       BIGINT       NOT NULL,
    revtstmp  BIGINT,
    username  VARCHAR(255) NOT NULL,
    user_type VARCHAR(255) NOT NULL,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

-- changeset hari:1722619162032-12
ALTER TABLE book
    ADD CONSTRAINT uc_book_isbn UNIQUE (isbn);

-- changeset hari:1722619162032-13
ALTER TABLE member
    ADD CONSTRAINT uc_member_email UNIQUE (email);

-- changeset hari:1722619162032-14
ALTER TABLE member
    ADD CONSTRAINT uc_member_phone UNIQUE (phone);

-- changeset hari:1722619162032-15
ALTER TABLE book_aud
    ADD CONSTRAINT FK_BOOK_AUD_ON_REV FOREIGN KEY (rev) REFERENCES revinfo (rev);

-- changeset hari:1722619162032-16
ALTER TABLE book_transaction_aud
    ADD CONSTRAINT FK_BOOK_TRANSACTION_AUD_ON_REV FOREIGN KEY (rev) REFERENCES revinfo (rev);

-- changeset hari:1722619162032-17
ALTER TABLE book_transaction
    ADD CONSTRAINT FK_BOOK_TRANSACTION_ON_BOOK FOREIGN KEY (book_fk) REFERENCES book (id);

-- changeset hari:1722619162032-18
ALTER TABLE book_transaction
    ADD CONSTRAINT FK_BOOK_TRANSACTION_ON_MEMBER FOREIGN KEY (member_fk) REFERENCES member (id);

-- changeset hari:1722619162032-19
ALTER TABLE member_aud
    ADD CONSTRAINT FK_MEMBER_AUD_ON_REV FOREIGN KEY (rev) REFERENCES revinfo (rev);

