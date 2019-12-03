/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MID_BO;
import DAO.UserDAO;
import java.util.HashMap;
import org.json.simple.JSONObject;
/**
 *
 * @author hamza
 */
public class UserImpl {
    public UserDAO user;
    
    public UserImpl() {
        user = new UserDAO();
    }
    public JSONObject userSignUpImpl(String email,String password,String name){
        return user.signUp(email, password, name);
    }
    
    public JSONObject userLoginImpl(String email,String password){
        return user.login(email, password);
    }
    public JSONObject addFriend(String useremail,String firendemail){
        return user.addFriend(useremail, firendemail);
    }
}
