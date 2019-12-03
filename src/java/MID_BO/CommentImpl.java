/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MID_BO;

import DAO.CommentDAO;
import com.datastax.oss.driver.api.core.CqlSession;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.DecoderException;
import org.json.simple.JSONObject;

/**
 *
 * @author hamza
 */
public class CommentImpl {
    public CommentDAO commentObject;
    
    public CommentImpl() {
        commentObject = new CommentDAO();
    }
    public JSONObject addCommentImpl(String url,String comment,String userEmail) throws DecoderException{
        return commentObject.addComment(url, comment, userEmail);
    }
    public List<Map<String,String>> getComment(String url,CqlSession session){
        return commentObject.getComment(url,session);
    }
}
