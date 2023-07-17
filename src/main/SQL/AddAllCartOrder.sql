create or replace PROCEDURE AddAllCartOrder(mId IN VARCHAR2, aId IN NUMBER, errorMsg OUT VARCHAR2)
IS
    aActive address.a_active%TYPE;
    aMemberId address.m_id%TYPE;
BEGIN    
    SELECT a_active, m_id INTO aActive, aMemberId FROM address WHERE a_id = aId;
    
    IF aActive = 0 THEN
        errorMsg := '삭제된 배송지입니다.';
        RETURN;
    ELSIF aMemberId <> mId THEN
        errorMsg := '올바르지 않은 배송지입니다.';
        RETURN;
    END IF;
    
    INSERT INTO orders (m_id, p_id, p_number, a_id, o_order_date)
    SELECT m_id, p_id, p_number, aId, SYSDATE
    FROM cart
    WHERE m_id = mId;

    DELETE FROM cart WHERE m_id = mId;

    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        errorMsg := '오류가 발생했습니다: ' || SQLERRM;
        ROLLBACK;
END;