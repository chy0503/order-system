CREATE OR REPLACE PROCEDURE AddReview(
    mId IN VARCHAR2,
    oId IN NUMBER,
    rContent IN VARCHAR2,
    rRating IN NUMBER,
    errorMsg OUT VARCHAR2
)
IS
    order_m_id VARCHAR2(30);
    order_confirm_date DATE;
BEGIN
    SELECT m_id, o_confirm_date
    INTO order_m_id, order_confirm_date
    FROM orders
    WHERE o_id = oId;

    IF order_m_id <> mId THEN
        errorMsg := '리뷰 작성자가 일치하지 않습니다.';
    ELSIF order_confirm_date IS NULL THEN
        errorMsg := '구매가 확정된 후에 리뷰를 작성할 수 있습니다.';
    ELSIF rRating < 1 OR rRating > 5 THEN
        errorMsg := '별점은 1점에서 5점 사이어야 합니다.';
    ELSE
        INSERT INTO review (m_id, o_id, r_content, r_rating, r_write_date) VALUES (mId, oId, rContent, rRating, SYSDATE);
    END IF;
END;