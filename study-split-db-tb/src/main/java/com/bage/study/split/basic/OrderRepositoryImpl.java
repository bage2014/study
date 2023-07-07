/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.bage.study.split.basic;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public final class OrderRepositoryImpl implements OrderRepository {

    private final DataSource dataSource;

    public OrderRepositoryImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS tb_order (" +
                "id BIGINT NOT NULL AUTO_INCREMENT, " +
                "order_id BIGINT NOT NULL, " +
                "user_id INT NOT NULL, " +
                "status VARCHAR(16), " +
                "create_time timestamp(3), " +
                "channel VARCHAR(16), " +
                "supplier VARCHAR(16), " +
                "PRIMARY KEY (id))";
        System.out.println(sql);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (final SQLException ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void dropTable() {
        String sql = "DROP TABLE tb_order";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (final SQLException ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void deleteTable() {
        String sql = "DELETE FROM tb_order";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (final SQLException ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public int insert(Order order) {
        String sql = "INSERT INTO tb_order (order_id,user_id, status,create_time,channel,supplier) VALUES (?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            buildParam(order, preparedStatement);
            int res = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    order.setId(resultSet.getLong(1));
                }
            }
            return res;
        } catch (final SQLException ignored) {
            ignored.printStackTrace();
        }
        return 0;
    }

    private void buildParam(Order order, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, order.getOrderId());
        preparedStatement.setLong(2, order.getUserId());
        preparedStatement.setString(3, order.getStatus());
        java.util.Date from = Date.from(order.getCreateTime().atZone(ZoneId.systemDefault()).toInstant());
        preparedStatement.setDate(4, new Date(from.getTime()));
        preparedStatement.setString(5, order.getChannel());
        preparedStatement.setString(6, order.getChannel());
    }

    @Override
    public int delete(Long orderId) {
        String sql = "DELETE FROM tb_order WHERE order_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            return preparedStatement.executeUpdate();
        } catch (final SQLException ignored) {
            ignored.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(Order order) {
        String sql = "update tb_order set order_id=?,user_id=?, status=?,create_time=?,channel=?,supplier=?) WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            buildParam(order, preparedStatement);
            preparedStatement.setLong(7, order.getId());
            int res = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    order.setId(resultSet.getLong(1));
                }
            }
            return res;
        } catch (final SQLException ignored) {
            ignored.printStackTrace();
        }
        return 0;
    }

    @Override
    public Order query(Long orderId) {
        String sql = "SELECT * FROM tb_order WHERE order_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return getOrder(resultSet);
            }
        } catch (final SQLException ignored) {
            ignored.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> queryByPage(String condition, int targetPage, int pageSize) {
        List<Order> result = new ArrayList<>();

        int startIndex = (targetPage - 1) * pageSize;
        startIndex = startIndex < 0 ? 0 : startIndex;

        String sql = "SELECT * FROM tb_order limit ?,?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, startIndex);
            preparedStatement.setLong(2, pageSize);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(getOrder(resultSet));
            }
        } catch (final SQLException ignored) {
            ignored.printStackTrace();
        }
        return result;
    }


    private Order getOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setOrderId(resultSet.getLong("order_id"));
        order.setUserId(resultSet.getLong("user_id"));
        order.setStatus(resultSet.getString("status"));
        LocalDateTime ldt = Instant.ofEpochMilli(resultSet.getDate("create_time").getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        order.setCreateTime(ldt);
        order.setChannel(resultSet.getString("channel"));
        order.setSupplier(resultSet.getString("supplier"));
        return order;
    }

}
