package com.bage.study.mybatis.springboot.typehandler;

import com.bage.study.mybatis.springboot.org.domain.Sex;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.VARCHAR)
public class SexTypeHandler extends BaseTypeHandler<Sex> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Sex parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public Sex getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String orignValue = rs.getString(columnName);
        return getSexValue(orignValue);
    }

    @Override
    public Sex getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String orignValue = rs.getString(columnIndex);
        return getSexValue(orignValue);
    }

    @Override
    public Sex getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String orignValue = cs.getString(columnIndex);
        return getSexValue(orignValue);
    }

    private Sex getSexValue(String code) {
        return Sex.of(code);
    }

}
