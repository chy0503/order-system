package com.example.ordersystem.repository;

import com.example.ordersystem.dto.CartDto;
import com.example.ordersystem.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.*;

@Repository
public class CartRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CartDto> findByUserId(String userId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM cart WHERE m_id = ?");
        try {
            return jdbcTemplate.query(query.toString(), new Object[]{userId}, (rs, rowNum) ->
                    CartDto.builder()
                            .id(rs.getLong("c_id"))
                            .memberId(rs.getString("m_id"))
                            .productId(rs.getLong("p_id"))
                            .num(rs.getInt("p_number"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public void save(String memberId, Long productId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("AddCart")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("pId", Types.NUMERIC)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", memberId);
        inParams.put("pId", productId);
        jdbcCall.execute(inParams);
    }

    public String delete(String memberId, Long cartId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DeleteCart")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("cId", Types.NUMERIC)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", memberId);
        inParams.put("cId", cartId);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }

    public String deleteAll(String id) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DeleteAllCart")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", id);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }

    public Optional<CartDto> findById(Long cartId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM cart WHERE c_id = ?");
        try {
            CartDto cart = jdbcTemplate.queryForObject(query.toString(), new Object[]{cartId}, (rs, rowNum) ->
                    CartDto.builder()
                            .id(rs.getLong("c_id"))
                            .memberId(rs.getString("m_id"))
                            .productId(rs.getLong("p_id"))
                            .num(rs.getInt("p_number"))
                            .build()
            );
            return Optional.ofNullable(cart);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
