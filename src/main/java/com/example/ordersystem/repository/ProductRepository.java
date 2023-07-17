package com.example.ordersystem.repository;

import com.example.ordersystem.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ProductDto> findAll() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM product");
        return jdbcTemplate.query(query.toString(), (rs, rowNum) ->
                ProductDto.builder()
                        .id(Long.parseLong(rs.getString("p_id")))
                        .name(rs.getString("p_name"))
                        .detail(rs.getString("p_detail"))
                        .price(Long.parseLong(rs.getString("p_price")))
                        .build()
        );
    }

    public Optional<ProductDto> findById(Long productId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM product WHERE p_id = ?");
        try {
             ProductDto product = jdbcTemplate.queryForObject(query.toString(), new Object[]{productId}, (rs, rowNum) ->
                    ProductDto.builder()
                            .id(rs.getLong("p_id"))
                            .name(rs.getString("p_name"))
                            .detail(rs.getString("p_detail"))
                            .price(rs.getLong("p_price"))
                            .build()
            );
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public float calculateRating(Long productId) {
        String query = "{ ? = call CalculateRating(?) }";
        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
             CallableStatement stmt = conn.prepareCall(query)) {
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.setLong(2, productId);
            stmt.execute();
            float avgRating = stmt.getFloat(1);
            return avgRating;
        } catch (SQLException e) {
            return 0.0f;
        }
    }
}
