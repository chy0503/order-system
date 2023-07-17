package com.example.ordersystem.repository;

import com.example.ordersystem.controller.form.ReviewForm;
import com.example.ordersystem.dto.CartDto;
import com.example.ordersystem.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReviewRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String save(ReviewForm reviewForm) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("AddReview")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("oId", Types.NUMERIC),
                        new SqlParameter("rContent", Types.VARCHAR),
                        new SqlParameter("rRating", Types.NUMERIC),
                        new SqlOutParameter("errorMsg", Types.VARCHAR)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", reviewForm.getMemberId());
        inParams.put("oId", reviewForm.getOrderId());
        inParams.put("rContent", reviewForm.getContent());
        inParams.put("rRating", reviewForm.getRating());

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }

    public List<ReviewDto> findByUserId(String userId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM review WHERE m_id = ?");
        try {
            return jdbcTemplate.query(query.toString(), new Object[]{userId}, (rs, rowNum) ->
                    ReviewDto.builder()
                            .id(rs.getLong("r_id"))
                            .memberId(rs.getString("m_id"))
                            .orderId(rs.getLong("o_id"))
                            .content(rs.getString("r_content"))
                            .rating(rs.getInt("r_rating"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<ReviewDto> findByProductId(Long productId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM review r INNER JOIN orders o ON r.o_id = o.o_id WHERE o.p_id = ?");
        try {
            return jdbcTemplate.query(query.toString(), new Object[]{productId}, (rs, rowNum) ->
                    ReviewDto.builder()
                            .id(rs.getLong("r_id"))
                            .memberId(rs.getString("m_id"))
                            .orderId(rs.getLong("o_id"))
                            .content(rs.getString("r_content"))
                            .rating(rs.getInt("r_rating"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public boolean isReviewedOrder(Long orderId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(*) FROM review WHERE o_id = ?");

        try {
            Integer count = jdbcTemplate.queryForObject(query.toString(), new Object[]{orderId}, Integer.class);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public String delete(String memberId, Long reviewId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DeleteReview")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("rId", Types.NUMERIC)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", memberId);
        inParams.put("rId", reviewId);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }

    public String deleteAll(String memberId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DeleteAllReview")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", memberId);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }
}
