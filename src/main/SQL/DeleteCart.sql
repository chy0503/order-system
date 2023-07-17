CREATE OR REPLACE PROCEDURE DeleteCart(mId IN VARCHAR2, cId IN VARCHAR2, errorMsg OUT VARCHAR2)
IS
    cart_mId cart.m_id%TYPE;
    MEMBER_MISMATCH EXCEPTION;
BEGIN
    SELECT m_id
    INTO cart_mId
    FROM cart
    WHERE c_id = cId;
    
    IF cart_mId != mId THEN
        RAISE MEMBER_MISMATCH;
    END IF;    
    
    DELETE FROM cart WHERE c_id = cId;
    
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            errorMsg := '해당 상품의 장바구니가 존재하지 않습니다.';
        WHEN MEMBER_MISMATCH THEN
            errorMsg := '본인의 장바구니 상품이 아닙니다.';
        WHEN OTHERS THEN
            errorMsg := '오류가 발생했습니다: ' || SQLERRM;
END;