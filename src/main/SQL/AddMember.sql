CREATE OR REPLACE PROCEDURE AddMember(mId IN VARCHAR2, mPwd IN VARCHAR2, mEmail IN VARCHAR2, errorMsg OUT VARCHAR2)
IS
  mCount NUMBER;
BEGIN
  SELECT COUNT(*)
  INTO mCount
  FROM members
  WHERE m_id = mId;

  IF mCount > 0 THEN
    errorMsg := '이미 사용 중인 아이디입니다.';
  ELSE
    SELECT COUNT(*)
    INTO mCount
    FROM members
    WHERE m_email = mEmail;

    IF mCount > 0 THEN
      errorMsg := '이미 사용 중인 이메일입니다.';
    ELSE
      IF LENGTH(mPwd) <= 3 THEN
        errorMsg := '비밀번호를 4자리 이상으로 설정해주세요.';
      ELSE
        INSERT INTO members (m_id, m_pwd, m_email) VALUES (mId, mPwd, mEmail);
      END IF;
    END IF;
  END IF;
  
  COMMIT;
  
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    errorMsg := '오류가 발생하였습니다. 다시 시도해주세요.';
END;