package com.example.ordersystem.repository;

import com.example.ordersystem.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<MemberDto> findByUserId(String userId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM members WHERE m_id = ?");
        try {
            MemberDto member = jdbcTemplate.queryForObject(query.toString(), new Object[]{userId}, (rs, rowNum) ->
                    MemberDto.builder()
                            .id(rs.getString("m_id"))
                            .pwd(rs.getString("m_pwd"))
                            .email(rs.getString("m_email"))
                            .build()
            );
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public String save(String id, String password, String email) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("AddMember")
                .declareParameters(
                        new SqlParameter("mId", Types.VARCHAR),
                        new SqlParameter("mPwd", Types.VARCHAR),
                        new SqlParameter("mEmail", Types.VARCHAR),
                        new SqlOutParameter("errorMsg", Types.VARCHAR)
                );

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("mId", id);
        inParams.put("mPwd", password);
        inParams.put("mEmail", email);

        Map<String, Object> outParams = jdbcCall.execute(inParams);

        String errorMsg = (String) outParams.get("errorMsg");
        if (errorMsg != null) {
            return errorMsg;
        } else {
            return "success";
        }
    }
}
