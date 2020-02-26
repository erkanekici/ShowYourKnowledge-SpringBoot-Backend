package com.repository;

import com.config.DatabaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class DBUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    private static String schema;
//    @Value("${database.schema}")
//    public void setSchema(String schema) {
//        DBUtil.schema = schema;
//    }

    public String findUserNameByEmailAndPassword(String email, String password){
        try {
            String sql = "SELECT USER_NAME FROM USERINFO WHERE EMAIL = '"+email+"' AND USER_PASSWORD = '"+password+"'";
            return jdbcTemplate.queryForObject(sql, String.class);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String findUserIdByEmail(String email){
        try {
            String sql = "SELECT ID FROM USERINFO WHERE EMAIL = '"+email+"'";
            return jdbcTemplate.queryForObject(sql, String.class);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String findUserEmailById(String id){
        try {
            String sql = "SELECT EMAIL FROM USERINFO WHERE ID = '"+id+"'";
            return jdbcTemplate.queryForObject(sql, String.class);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<User> getUserByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM USERINFO WHERE EMAIL = '"+email+"' AND USER_PASSWORD = '"+password+"'";
        return jdbcTemplate.query(
                sql,(rs, rowNum) ->
                        new User(
                                rs.getString("ID"),
                                rs.getString("EMAIL"),
                                rs.getString("USER_PASSWORD"),
                                rs.getString("USER_NAME"),
                                rs.getInt("ACTIVE"),
                                rs.getInt("WRONG_ENTRY"),
                                rs.getString("REGISTER_TIME")
                        )
        );
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USERINFO";
        return jdbcTemplate.query(
            sql,(rs, rowNum) ->
                new User(
                    rs.getString("ID"),
                    rs.getString("EMAIL"),
                    rs.getString("USER_PASSWORD"),
                    rs.getString("USER_NAME"),
                    rs.getInt("ACTIVE"),
                    rs.getInt("WRONG_ENTRY"),
                    rs.getString("REGISTER_TIME")
                )
        );
    }

    public List<Topic> getTopicById(String topicId) {
        String sql = "SELECT * FROM TOPIC WHERE ID = '"+topicId+"'";
        return jdbcTemplate.query(
                sql,(rs, rowNum) ->
                        new Topic(
                                rs.getString("ID"),
                                rs.getString("USERID1"),
                                rs.getString("USERID2")
                        )
        );
    }

    public List<Topic> getSuitableTopics() {
        String sql = "SELECT * FROM TOPIC WHERE USERID2 IS NULL";
        return jdbcTemplate.query(
                sql,(rs, rowNum) ->
                        new Topic(
                                rs.getString("ID"),
                                rs.getString("USERID1"),
                                rs.getString("USERID2")
                        )
        );
    }

    @Transactional()
    public void updateUserById(String Id, int active, String date){
        String sql="UPDATE USERINFO SET ACTIVE=?, REGISTER_TIME=? WHERE ID=?";
        jdbcTemplate.update(con->{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,active);
            ps.setString(2,date);
            ps.setString(3,Id);
            return ps;
        });
    }

    @Transactional()
    public void insertUser(String Id, String email, String password, String username, int active, int wrongEntry, String date) {
        String sql="INSERT INTO USERINFO (ID, EMAIL, USER_PASSWORD, USER_NAME, ACTIVE, WRONG_ENTRY, REGISTER_TIME) VALUES(?,?,?,?,?,?,?)";

            jdbcTemplate.update(sql, ps -> {
                ps.setString(1, Id);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, username);
                ps.setInt(5, 0);
                ps.setInt(6, 0);
                ps.setString(7, date);
            });

    }

    @Transactional()
    public void insertTopic(String topicId, String userId1) {
        String sql="INSERT INTO TOPIC (ID, USERID1) VALUES(?,?)";

        jdbcTemplate.update(sql, ps -> {
            ps.setString(1, topicId);
            ps.setString(2, userId1);
            //ps.setString(3, null);
        });
    }

    @Transactional()
    public void updateTopicById(String topicId, String userId2){
        String sql="UPDATE TOPIC SET USERID2=? WHERE ID=?";
        jdbcTemplate.update(con->{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,userId2);
            ps.setString(2,topicId);

            return ps;
        });
    }

    @Transactional()
    public long insertRequestLog(String requestJsonData, String serviceName, String methodName, String startDate){

        String sql="INSERT INTO erkantablo (PersonId,LastName,FirstName,Time) VALUES(?,?,?,?)";
        KeyHolder keyHolder=new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {

            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,123);
            ps.setString(2,requestJsonData);
            ps.setString(3,methodName);
            ps.setString(4,startDate);
            return ps;
        },keyHolder);

        //Number key=keyHolder.getKey();
        //long generatedOid = key.longValue();
        long generatedOid = 1;
        return generatedOid;
    }

    @Transactional()
    public void insertResponseLog(String responseJsonData,long oid,long elapsedTime, String lastUpdated) {
        String sql="UPDATE erkantablo SET MW_RESPONSE_DATA=?,MW_EXECUTION_TIME_MILLIS=?,MW_LASTUPDATED=? WHERE OID=?";
        jdbcTemplate.update(con->{
            Clob clobResponseJsonData=con.createClob();
            clobResponseJsonData.setString(1,responseJsonData);
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setClob(1,clobResponseJsonData);
            ps.setLong(2,elapsedTime);
            ps.setString(3,lastUpdated);
            ps.setLong(4,oid);
            return ps;
        });
    }
}