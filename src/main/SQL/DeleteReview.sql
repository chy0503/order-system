CREATE OR REPLACE PROCEDURE DeleteReview(mId IN VARCHAR2, rId IN NUMBER, errorMsg OUT VARCHAR2)
IS
    review_m_id review.m_id%TYPE;
    MEMBER_MISMATCH EXCEPTION;
BEGIN
    SELECT m_id
    INTO review_m_id
    FROM review
    WHERE r_id = rId;
    
    IF review_m_id != mId THEN
        RAISE MEMBER_MISMATCH;
    END IF;    
    
    DELETE FROM review WHERE r_id = rId;
    
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            errorMsg := '해당 리뷰가 존재하지 않습니다.';
        WHEN MEMBER_MISMATCH THEN
            errorMsg := '본인이 작성한 리뷰가 아닙니다.';
        WHEN OTHERS THEN
            errorMsg := '오류가 발생했습니다: ' || SQLERRM;
END;