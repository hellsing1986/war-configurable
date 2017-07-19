package vn.vnptnet.archetype.warconfigurable.mapper;

import org.json.JSONObject;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultColumnMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by User on 5/30/2017.
 */
public class UniversalJsonMapper implements ResultColumnMapper{

    @Override
    public Object mapColumn(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
        //System.out.println("columnNumber "+columnNumber);
        JSONObject p = new JSONObject();
        for (int i=1; i <= r.getMetaData().getColumnCount(); i++){
            String column = r.getMetaData().getColumnName(i);
            p.put(column, r.getString(column));
        }
        return p;
    }

    @Override
    public Object mapColumn(ResultSet r, String columnLabel, StatementContext ctx) throws SQLException {
        //System.out.println("columnLabel "+columnLabel);
        JSONObject p = new JSONObject();
        for (int i=1; i <= r.getMetaData().getColumnCount(); i++){
            String column = r.getMetaData().getColumnName(i);
            p.put(column, r.getString(column));
        }
        return p;
    }
}
/*public class UniversalJsonMapper extends TypedMapper<JSONObject> {

    @Override
    protected JSONObject extractByName(ResultSet r, String name) throws SQLException {
        JSONObject p = new JSONObject();
        for (int i=0; i < r.getMetaData().getColumnCount(); i++){
            String column = r.getMetaData().getColumnName(i);
            p.put(column, r.getString(column));
        }
        return p;
    }

    @Override
    protected JSONObject extractByIndex(ResultSet r, int index) throws SQLException {
        JSONObject p = new JSONObject();
        for (int i=1; i <= r.getMetaData().getColumnCount(); i++){
            String column = r.getMetaData().getColumnName(i);
            p.put(column, r.getString(column));
        }
        return p;
    }
}*/
