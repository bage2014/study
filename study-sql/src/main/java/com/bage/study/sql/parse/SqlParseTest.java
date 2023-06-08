package com.bage.study.sql.parse;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SqlParseTest {
    public static void main(String[] args) throws JSQLParserException {
        String sql = "update tb_user set a = 1,b = \"a2d\" where id = 123";
//        sql = sql.replaceAll("(\\s+)\"([^\\d].*?)\"", "$1[$2]");
        System.out.println(sql);
        Update stmt = (Update) CCJSqlParserUtil.parse(sql
                , parser -> parser
                        .withSquareBracketQuotation(true)
//                        .withSquareBracketQuotation(false)
        );

        ArrayList<UpdateSet> updateSets = stmt.getUpdateSets();
        for (UpdateSet updateSet : updateSets) {
            System.out.println(updateSet.getExpressions().get(0).toString());
        }
    }
    public static void select() throws JSQLParserException {
        // 根据sql创建select
        Select stmt = (Select) CCJSqlParserUtil.parse("SELECT col1 AS a, col2 AS b, col3 AS c FROM table T WHERE col1 = 10 AND col2 = 20 AND col3 = 30");

        Map<String, Expression> map = new HashMap<>();
        Map<String, String> mapTable = new HashMap<>();

        ((PlainSelect) stmt.getSelectBody()).getFromItem().accept(new FromItemVisitorAdapter() {
            @Override
            public void visit(Table table) {
                // 获取别名 => 表名
                mapTable.put(table.getAlias().getName(), table.getName());
            }
        });

        ((PlainSelect) stmt.getSelectBody()).getWhere().accept(new ExpressionVisitorAdapter() {
            @Override
            public void visit(AndExpression expr) {
                // 获取where表达式
                System.out.println(expr);
            }
        });

        for (SelectItem selectItem : ((PlainSelect)stmt.getSelectBody()).getSelectItems()) {
            selectItem.accept(new SelectItemVisitorAdapter() {
                @Override
                public void visit(SelectExpressionItem item) {
                    // 获取字段别名 => 字段名
                    map.put(item.getAlias().getName(), item.getExpression());
                }
            });
        }

        System.out.println("map " + map);
        System.out.println("mapTables" + mapTable);
    }
}
