CREATE OR REPLACE PROCEDURE DeleteAllReview(mId IN VARCHAR2, errorMsg OUT VARCHAR2)
IS
BEGIN
    DELETE FROM review WHERE m_id = mId;
    
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            errorMsg := '해당 리뷰가 존재하지 않습니다.';
        WHEN OTHERS THEN
            errorMsg := '오류가 발생했습니다: ' || SQLERRM;
END;