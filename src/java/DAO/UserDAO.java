/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import static Utils.Functions.stringToHex;
import Utils.PasswordHashingDemo;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.servererrors.QueryValidationException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import org.json.simple.JSONObject;
/**
 *
 * @author hamza
 */
public class UserDAO extends ConnectionSession{
    private ResultSet resultSet=null;
    private PreparedStatement preparedStatement=null;
    private String name,email;
    private String password;
    
    public JSONObject signUp(String email,String password,String name){
        JSONObject responseJsonObject = new JSONObject();
        this.name=name;
        this.email=email;
        this.password= PasswordHashingDemo.getPasswordHash(password);
        
        try{
        preparedStatement = session.prepare("insert into users (email,name,password) VALUES(?, ?, ?) IF NOT EXISTS;");
        BoundStatement statement = preparedStatement.bind()
          .setString(0, this.email)
          .setString(1, this.name)
          .setString(2, this.password);
 
        resultSet=session.execute(statement);
        }
        catch(QueryValidationException e){
            responseJsonObject.put("error", e.getMessage());
            return responseJsonObject;
        }
        
        if (resultSet.iterator().next().getBoolean("[applied]")==false) {
            responseJsonObject.put("error","Email already exists");
        } else {
            responseJsonObject.put("key", session.hashCode()+"");
            responseJsonObject.put("name", name);
            responseJsonObject.put("email", email);
            responseJsonObject.put("password", password);
        }
        

        return responseJsonObject;
    }
    
    public JSONObject login(String email,String password){
        JSONObject responseJsonObject = new JSONObject();
        this.email=email;
        this.password= PasswordHashingDemo.getPasswordHash(password);
        try{
            resultSet = session.execute("select * from users where email='"+this.email+"' and password='"+this.password+"';");
        }catch(QueryValidationException e){
            responseJsonObject.put("error", e.getMessage());
            return responseJsonObject;
        }
        Iterator<Row> it = resultSet.iterator();
        if (it.hasNext()) {
            Row row = it.next();
            responseJsonObject.put("key", session.hashCode()+"");
            responseJsonObject.put("name", row.getString("name"));
            responseJsonObject.put("email", row.getString("email"));
            responseJsonObject.put("password", row.getString("password"));
        } else {
            responseJsonObject.put("error", "Login unknown");
        }
        return responseJsonObject;
    }
    
    public JSONObject addFriend(String useremail,String firendemail){
        JSONObject responseJsonObject = new JSONObject();
        PreparedStatement preparedStatement1 = session.prepare("update users set friends = friends + ? where useremail = ?");
        try{
        session.execute(preparedStatement1.bind(firendemail,useremail));
        }
        catch(QueryValidationException e){
            responseJsonObject.put("error", e.getMessage());
            return responseJsonObject;
        }
        return responseJsonObject;
    }
    
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }


    
    
    

}
