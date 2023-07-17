CREATE OR REPLACE PROCEDURE ConfirmOrder(mId IN VARCHAR2, oId IN NUMBER, errorMsg OUT VARCHAR2)
IS
    order_m_id VARCHAR2(30);
BEGIN
    SELECT m_id
    INTO order_m_id
    FROM orders
    WHERE o_id = oId;
    
    IF order_m_id <> mId THEN
        errorMsg := '주문자와 일치하지 않습니다.';
    ELSE
        UPDATE orders
        SET o_confirm_date = SYSDATE
        WHERE o_id = oId;
    END IF;
END;