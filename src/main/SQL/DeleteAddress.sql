CREATE OR REPLACE PROCEDURE DeleteAddress(mId IN VARCHAR2, aId IN NUMBER, errorMsg OUT VARCHAR2)
IS
BEGIN
    UPDATE address SET a_active = 0 WHERE a_id = aId;
    EXCEPTION
        WHEN OTHERS THEN
            errorMsg := '오류가 발생했습니다: ' || SQLERRM;
END;