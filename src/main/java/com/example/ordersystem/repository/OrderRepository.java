package com.example.ordersystem.repository;

import com.example.ordersystem.controller.form.OrderForm;
import com.example.ordersystem.dto.OrderDto;
import com.example.ordersystem.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.*;

@Repository
public class OrderRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<OrderDto> findAllOrder(String userId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM orders WHERE m_id = ?");
        try {
            return jdbcTemplate.query(query.toString(), new Object[]{userId}, (rs, rowNum) ->
                    OrderDto.builder()
                            .id(rs.getLong("o_id"))
                            .memberId(rs.getString("m_id"))
                            .productId(rs.getLong("p_id"))
                            .productNum(rs.getInt("p_number"))
                            .addressId(rs.getLong("a_id"))
                            .orderDate(rs.getDate("o_order_date"))
                            .confirmDate(rs.getDate("o_confirm_date"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Optional<OrderDto> findById(Long orderId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM orders WHERE o_id = ?");
        try {
            OrderDto order = jdbcTemplate.queryForObject(query.toString(), new Object[]{orderId}, (rs, rowNum) ->
                    OrderDto.builder()
                            .id(rs.getLong("o_id"))
                            .memberId(rs.getString("m_id"))
                            .productId(rs.getLong("p_id"))
                            .productNum(rs.getInt("p_number"))
                            .addressId(rs.getLong("a_id"))
                            .orderDate(rs.getDate("o_order_date"))
                            .confirmDate(rs.getDate("o_confirm_date"))
                            .build()
            );
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public String addOrder(OrderForm orderForm) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("AddOrder")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("pId", Types.NUMERIC),
                        new SqlParameter("pNumber", Types.NUMERIC),
                        new SqlParameter("aId", Types.NUMERIC),
                        new SqlParameter("inCart", Types.NUMERIC),
                        new SqlOutParameter("errorMsg", Types.VARCHAR)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", orderForm.getMemberId());
        inParams.put("pId", orderForm.getProductId());
        inParams.put("pNumber", orderForm.getProductNum());
        inParams.put("aId", orderForm.getAddressId());
        inParams.put("inCart", orderForm.getInCart());

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }

    public String confirmOrder(String memberId, Long orderId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("ConfirmOrder")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("oId", Types.NUMERIC),
                        new SqlOutParameter("errorMsg", Types.VARCHAR)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", memberId);
        inParams.put("oId", orderId);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }

    public String addAllCartOrder(String memberId, Long addrId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("AddAllCartOrder")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("aId", Types.NUMERIC),
                        new SqlOutParameter("errorMsg", Types.VARCHAR)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", memberId);
        inParams.put("aId", addrId);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }
}
