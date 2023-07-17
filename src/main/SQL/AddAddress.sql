CREATE OR REPLACE PROCEDURE AddAddress(mId IN VARCHAR2, aName IN VARCHAR2, aAddr IN VARCHAR2, aPhone IN VARCHAR2, errorMsg OUT VARCHAR2)
IS
  active    NUMBER;
BEGIN
  SELECT COUNT(*)
  INTO active
  FROM address
  WHERE a_name = aName
    AND a_active = 1
    AND m_id = mId;
    
  IF active > 0 THEN
    errorMsg := '이미 저장된 배송지명입니다.';
  ELSE
    INSERT INTO address (m_id, a_name, a_addr, a_phone, a_active) VALUES (mId, aName, aAddr, aPhone, 1);
  END IF;
END;