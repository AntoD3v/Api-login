package com.dathvader.data.sql;

import com.dathvader.data.DataSet;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventHeaderV4;
import com.github.shyiko.mysql.binlog.event.QueryEventData;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.regex.Pattern;

public class QueryEvent implements BinaryLogClient.EventListener {

    private final DataSet dataSet;

    public QueryEvent(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public void onEvent(Event event) {

        dataSet.updateLogPosition(((EventHeaderV4)event.getHeader()).getPosition());
        if(!(event.getData() instanceof QueryEventData))
            return;

        String sql = null;
        try {
            sql = ((QueryEventData)event.getData()).getSql().toLowerCase();
            if(sql.startsWith("insert") || sql.startsWith("update")){

                Statement statement = CCJSqlParserUtil.parse(sql);

                if(sql.startsWith("insert")){
                    Insert insert = (Insert) statement;
                    String user = null, pass = null;
                    for (int i = 0; i < insert.getColumns().size(); i++) {
                        if(insert.getColumns().get(i).getColumnName().equals("pseudonyme"))
                            user = parseString(insert.getItemsList(), i);
                        if(insert.getColumns().get(i).getColumnName().equals("motdepasse"))
                            pass = parseString(insert.getItemsList(), i);
                    }
                    if(pass != null && user != null)
                        dataSet.addClient(user, pass);

                }else if(sql.startsWith("update")){

                }

            }else if(sql.startsWith("truncate")){
                dataSet.getClients().clear();
            }
        } catch (JSQLParserException e) {
            System.out.println("\nError sql: "+e.getMessage()+"" +
                    "\nRequest: "+sql);
        }catch (Exception e){

            System.out.println("\nError sql: "+e.getMessage()+"" +
                    "\nRequest: "+sql);
        }

    }

    public String parseString(ItemsList itemsList, int i) {
        Expression expression = ((ExpressionList)itemsList).getExpressions().get(i);
        if (expression instanceof net.sf.jsqlparser.schema.Column)
            return ((net.sf.jsqlparser.schema.Column) expression).getColumnName();
        if (expression instanceof StringValue)
            return ((StringValue)expression).getValue();
        return null;
    }
}
