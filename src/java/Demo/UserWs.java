/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Demo;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import MID_BO.UserImpl;
import org.json.simple.JSONObject;
/**
 *
 * @author hamza
 */
@WebService(serviceName = "UserWs")
public class UserWs {
    UserImpl userImpl = new UserImpl();
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "SignUp")
    public String SignUp(@WebParam(name = "name") String name,@WebParam(name = "email") String email,@WebParam(name = "password") String password) {
        userImpl.user.connect();
        JSONObject jsonObject = userImpl.userSignUpImpl(email, password, name);
        userImpl.user.session.close();
        return jsonObject.toString();
    }
    
    @WebMethod(operationName = "Login")
    public String Login(@WebParam(name = "email") String email,@WebParam(name = "password") String password) {
        userImpl.user.connect();
        JSONObject jsonObject = userImpl.userLoginImpl(email, password);
        userImpl.user.session.close();
        return jsonObject.toString();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addFriend")
    public String addFriend(@WebParam(name = "useremail") String useremail, @WebParam(name = "friendemail") String friendemail) {
        userImpl.user.connect();
        JSONObject jsonObject = userImpl.addFriend(useremail, friendemail);
        userImpl.user.session.close();
        return jsonObject.toString();
    }
}
