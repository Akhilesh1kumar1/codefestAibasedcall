package com.sr.capital.config.db;

import com.sr.capital.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@Component
public class CommonJdbcUtill {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

   LoggerUtil logger =LoggerUtil.getLogger(CommonJdbcUtill.class);

    public List<Map<String, Object>> buildAndExecuteQuery(String[] tableNames, List<String> selectFields,
                                                          HashMap<String, Object> whereClauseValues
            , Map<String, Object> mpJoinConditions
            , String[] arrSorkeysOrder
            , Integer offset
            , Integer limit, boolean groupBy
    ) throws ParseException {

        HashMap<String,Object> executableQuery =getExecutableQuery(tableNames,  selectFields,
                whereClauseValues
                ,  mpJoinConditions
                ,  arrSorkeysOrder
                ,  offset
                ,  limit,groupBy);
        logger.debug("executable query "+executableQuery);
        return this.namedParameterJdbcTemplate.queryForList((String) executableQuery.get("query"), (ConcurrentHashMap<String, Object>)executableQuery.get("tempWhereClauseValues"));
    }

    public List<Map<String, Object>> buildAndExecuteQueryCustomDateFormat(String[] tableNames, List<String> selectFields,
                                                                          HashMap<String, Object> whereClauseValues
            , Map<String, Object> mpJoinConditions
            , String[] arrSorkeysOrder
            , Integer offset
            , Integer limit,boolean groupBy
    ) throws ParseException {

        HashMap<String,Object> executableQuery =getExecutableQuery(tableNames,  selectFields,
                whereClauseValues
                ,  mpJoinConditions
                ,  arrSorkeysOrder
                ,  offset
                ,  limit,groupBy);
        return this.namedParameterJdbcTemplate.query((String) executableQuery.get("query"), (ConcurrentHashMap<String, Object>)executableQuery.get("tempWhereClauseValues"),new RowMapper<Map<String,Object>>() {

            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                //The map per row
                Map<String,Object> map = new HashMap<String,Object>();
                //The target date format
                DateFormat nice = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Iterating the columns.
                ResultSetMetaData meta = rs.getMetaData();
                for(int i = 1; i <= meta.getColumnCount(); ++i) {
                    Object o = rs.getObject(i);
                    //If a date use formatted String instead
                    if(o instanceof Date) {
                        o = nice.format(o);
                    }
                    //put into map.
                    map.put(meta.getColumnLabel(i), o);
                }
                return map;
            }

        });
        //System.out.println(sql.toString());
        //  return this.namedParameterJdbcTemplate.queryForList(sql.toString(), tempWhereClauseValues);
    }

    private HashMap<String,Object> getExecutableQuery(String[] tableNames, List<String> selectFields,
                                                      HashMap<String, Object> whereClauseValues
            , Map<String, Object> mpJoinConditions
            , String[] arrSorkeysOrder
            , Integer offset
            , Integer limit,boolean groupBy) throws ParseException {
        HashMap<String,Object> result =new HashMap<>();
        ConcurrentHashMap<String, Object> tempWhereClauseValues = new ConcurrentHashMap();
        // tempWhereClauseValues.putAll( whereClauseValues);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        StringBuilder sql = new StringBuilder("Select ");
        sql.append(StringUtils.join(selectFields, ","));
        sql.append(" from ").append(StringUtils.join(tableNames, ",")).append(" where ");

        if (mpJoinConditions != null && !mpJoinConditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : mpJoinConditions.entrySet()) {
                sql.append(entry.getKey());
                if (entry instanceof Map) {
                    Map<String, String> mpOuterJoin = (Map<String, String>) entry;
                    for (Map.Entry<String, String> entry1 : mpOuterJoin.entrySet()) {
                        sql.append(entry1.getValue()).append(entry1.getKey()).append("and");
                    }
                } else {
                    sql.append("=").append(entry.getValue()).append(" and ");
                }
            }
        }

        for (Map.Entry<String, Object> entry : whereClauseValues.entrySet()) {

            if(entry.getKey().contains( "-" )){
                String[] splitKeys = entry.getKey().split("-");
                sql.append(splitKeys[0]).append(" ").append(splitKeys[1]).append(":").append(splitKeys[0]).append(" and ");
                tempWhereClauseValues.put(splitKeys[0],entry.getValue());
                //  tempWhereClauseValues.remove( entry.getKey() );

            }else if(entry.getKey().contains( "^" )){
                String[] splitKeys = entry.getKey().split("\\^");
                sql.append(splitKeys[0]).append(" ").append(splitKeys[1]).append("(:").append(splitKeys[0]).append(") and ");
                tempWhereClauseValues.put(splitKeys[0],entry.getValue());
                //  tempWhereClauseValues.remove( entry.getKey() );

            }else if(entry.getKey().contains(">=")){
                sql.append("created_at>=").append(":").append("created_at").append(" and ");
                Date date = dateFormat.parse((String) entry.getValue());
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.MINUTE, -330);
                tempWhereClauseValues.put("created_at",dateFormat.format(c.getTime()));
            }else if(entry.getKey().contains("<=")){
                Date date = dateFormat.parse((String) entry.getValue());
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.MINUTE, -5);
                sql.append("la.created_at<=").append(":").append("created_at").append(" and ");
                tempWhereClauseValues.put("created_at",dateFormat.format(c.getTime()));
            }else{
                sql.append(entry.getKey()).append("=:").append(entry.getKey()).append(" and ");
                tempWhereClauseValues.put(entry.getKey(),entry.getValue());
            }

        }
        if(groupBy){
            //   sql.append("group by orders.id ");
        }
       sql.append(" la.is_enabled=1 ");
        if (arrSorkeysOrder != null && arrSorkeysOrder.length > 0) {
            sql.append("order by ").append(StringUtils.join(arrSorkeysOrder, ","));
        }

        if(limit!=null){
            sql.append("  limit ");
            if(offset!=null) sql.append(offset).append(",");
            sql.append(limit);
        }
        result.put("query",sql.toString());
        result.put("tempWhereClauseValues",tempWhereClauseValues);
        logger.info("executable query "+sql.toString()+" tempWhereClauseValues"+tempWhereClauseValues);

        return result;
    }
}
