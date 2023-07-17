package com.example.ordersystem.repository;

import com.example.ordersystem.dto.AddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class AddressRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AddressDto> findByUserId(String userId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM address WHERE m_id = ?");
        try {
            return jdbcTemplate.query(query.toString(), new Object[]{userId}, (rs, rowNum) ->
                    AddressDto.builder()
                            .id(Long.valueOf(rs.getString("a_id")))
                            .name(rs.getString("a_name"))
                            .memberId(rs.getString("m_id"))
                            .address(rs.getString("a_addr"))
                            .phone(rs.getString("a_phone"))
                            .isActive(rs.getInt("a_active"))
                            .build()
            ).stream().filter(a -> a.getIsActive() == 1).collect(Collectors.toList());
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public Optional<AddressDto> findById(Long addrId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM address WHERE a_id = ?");
        try {
            AddressDto address = jdbcTemplate.queryForObject(query.toString(), new Object[]{addrId}, (rs, rowNum) ->
                    AddressDto.builder()
                            .id(Long.valueOf(rs.getString("a_id")))
                            .name(rs.getString("a_name"))
                            .memberId(rs.getString("m_id"))
                            .address(rs.getString("a_addr"))
                            .phone(rs.getString("a_phone"))
                            .build()
            );
            return Optional.ofNullable(address);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public String save(String name, String memberId, String address, String phone) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("AddAddress")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("aName", Types.VARCHAR),
                        new SqlParameter("aAddr", Types.VARCHAR),
                        new SqlParameter("aPhone", Types.VARCHAR),
                        new SqlOutParameter("errorMsg", Types.VARCHAR)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", memberId);
        inParams.put("aName", name);
        inParams.put("aAddr", address);
        inParams.put("aPhone", phone);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }

    public String delete(String id, Long addressId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("DeleteAddress")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("aId", Types.NUMERIC)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", id);
        inParams.put("aId", addressId);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }
}
