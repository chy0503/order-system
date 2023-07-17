DROP TABLE members CASCADE CONSTRAINTS;  
DROP TABLE address CASCADE CONSTRAINTS;  
DROP TABLE product CASCADE CONSTRAINTS;  
DROP TABLE cart CASCADE CONSTRAINTS;  
DROP TABLE orders CASCADE CONSTRAINTS;  
DROP TABLE review CASCADE CONSTRAINTS;  

CREATE TABLE members
(
    m_id        VARCHAR2(30),
    m_pwd       VARCHAR2(10) not null,
    m_email     VARCHAR2(30) not null,
    CONSTRAINT m_pk PRIMARY KEY (m_id)
);

INSERT INTO members (m_id, m_pwd, m_email) VALUES ('user', 'password', 'email@google.com');

CREATE TABLE address
(
    a_id        NUMBER(5) generated always as IDENTITY,
    a_name      VARCHAR2(30) not null,
    m_id        VARCHAR2(30) not null,
    a_addr      VARCHAR2(100) not null,
    a_phone     VARCHAR2(20) not null,
    a_active    NUMBER(1) not null,
    CONSTRAINT a_pk PRIMARY KEY (a_id),
    CONSTRAINT a_m_id_fk FOREIGN KEY (m_id) REFERENCES members (m_id)
);

INSERT INTO address (a_name, m_id, a_addr, a_phone, a_active) VALUES ('우리집', 'user', '수원시 장안구 화서동', '010-0000-0000', 1);

CREATE TABLE product
(
    p_id        NUMBER(5) generated always as IDENTITY,
    p_name      VARCHAR2(100) not null,
    p_detail    VARCHAR2(100) not null,
    p_price     NUMBER(10) not null,
    CONSTRAINT p_pk PRIMARY KEY (p_id)
);

INSERT INTO product (p_name, p_detail, p_price) VALUES ('딸기잼 800g', '달콤해요', 9000);
INSERT INTO product (p_name, p_detail, p_price) VALUES ('라이스 페이퍼 400g', '쌀가루 40%', 6000);
INSERT INTO product (p_name, p_detail, p_price) VALUES ('풀무원샘물 500ml X 20EA', '물을 많이 먹어야지 건강해진다', 7600);
INSERT INTO product (p_name, p_detail, p_price) VALUES ('원목 라운드 스탠드 파티션 1200', '인테리어 최고', 49900);
INSERT INTO product (p_name, p_detail, p_price) VALUES ('소프트 암막커튼', '1+1 입니다', 16900);
INSERT INTO product (p_name, p_detail, p_price) VALUES ('블랙스틸 프라이팬', '28cm가 제일 실용성 있어요', 70000);
INSERT INTO product (p_name, p_detail, p_price) VALUES ('마리우스 스툴', '이케아에서 제일 많이 팔린 상품', 7000);
INSERT INTO product (p_name, p_detail, p_price) VALUES ('만다린 800g', '새콤해요', 7800);
INSERT INTO product (p_name, p_detail, p_price) VALUES ('바이오코어 건강한 생유산균', '장에 좋다', 87100);
INSERT INTO product (p_name, p_detail, p_price) VALUES ('포켓몬스터 파이리 봉제 인형 45cm', 'cute', 33000);

CREATE TABLE cart
(
    c_id        NUMBER(5) generated always as IDENTITY,
    m_id        VARCHAR2(30),
    p_id        NUMBER(5),
    p_number    NUMBER(2),
    CONSTRAINT c_pk PRIMARY KEY (c_id),
    CONSTRAINT c_m_id_fk FOREIGN KEY (m_id) REFERENCES members (m_id), 
    CONSTRAINT c_p_id_fk FOREIGN KEY (p_id) REFERENCES product (p_id)
);

CREATE TABLE orders
(
    o_id        NUMBER(5) generated always as IDENTITY,
    m_id        VARCHAR2(30),
    p_id        NUMBER(5),
    p_number    NUMBER(2),
    a_id        NUMBER(5),
    o_order_date DATE not null,
    o_confirm_date DATE,
    CONSTRAINT o_pk PRIMARY KEY (o_id),
    CONSTRAINT o_m_id_fk FOREIGN KEY (m_id) REFERENCES members (m_id), 
    CONSTRAINT o_p_id_fk FOREIGN KEY (p_id) REFERENCES product (p_id)
);

CREATE TABLE review
(
    r_id        NUMBER(5) generated always as IDENTITY,
    m_id        VARCHAR2(30),
    o_id        NUMBER(5),
    r_content   VARCHAR2(100),
    r_rating    NUMBER(1) not null,
    r_write_date DATE not null,
    CONSTRAINT r_pk PRIMARY KEY (r_id),
    CONSTRAINT r_m_id_fk FOREIGN KEY (m_id) REFERENCES members (m_id), 
    CONSTRAINT r_o_id_fk FOREIGN KEY (o_id) REFERENCES orders (o_id)
);

commit;