CREATE OR REPLACE PROCEDURE AddCart(mId IN VARCHAR2, pId IN NUMBER)
IS
    countCart NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO countCart
    FROM cart
    WHERE m_id = mId AND p_id = pId;
    
    IF countCart > 0  THEN
        UPDATE cart SET p_number = p_number + 1 WHERE m_id = mId AND p_id = pId;
    ELSE
        INSERT INTO cart (m_id, p_id, p_number) VALUES (mId, pId, 1);
    END IF;
END;    