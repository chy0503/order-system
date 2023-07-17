CREATE OR REPLACE FUNCTION CalculateRating(pId IN NUMBER)
    RETURN NUMBER
IS
    total_count NUMBER;
    total_sum NUMBER;
    avg_rating NUMBER;
BEGIN
    SELECT COUNT(*), SUM(r_rating)
    INTO total_count, total_sum
    FROM review r
    INNER JOIN orders o ON r.o_id = o.o_id
    WHERE o.p_id = pId;
    
    IF total_count = 0 THEN
        avg_rating := 0;
    ELSE
        avg_rating := total_sum / total_count;
    END IF;
    
    RETURN avg_rating;
END;