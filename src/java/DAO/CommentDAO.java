/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import static Utils.Functions.dateFromTimeUUID;
import static Utils.Functions.stringToHex;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.servererrors.QueryValidationException;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.DecoderException;
import org.json.simple.JSONObject;
import java.util.UUID;

/**
 *
 * @author hamza
 */
public class CommentDAO extends ConnectionSession{
    private ResultSet resultSet=null;
    private String url;
    private UUID commentId;
    private String comment,userEmail;
    
    public JSONObject addComment(String url,String comment,String userEmail) throws DecoderException{
        JSONObject responseJsonObject = new JSONObject();
        this.url = url;
        this.commentId = Uuids.timeBased();
        this.comment = comment ;
        this.userEmail = userEmail;
        PreparedStatement preparedStatement1 = session.prepare("insert into comments_by_news (url,commentid,comment,useremail) VALUES(:url, :commentid, :comment,:useremail);");
        try{
        resultSet=session.execute(preparedStatement1.bind(ByteBuffer.wrap(stringToHex(url)),this.commentId, this.comment,this.userEmail));
        }
        catch(QueryValidationException e){
            responseJsonObject.put("error", e.getMessage());
            return responseJsonObject;
        }
        return responseJsonObject;
    }
    public List<Map<String,String>> getComment(String url,CqlSession session) {
        List<Map<String,String>> comments = new ArrayList<>();
        resultSet= session.execute("select commentid,comment,useremail from comments_by_news where url =textAsBlob('"+url+"');");
        Iterator<Row> commentsIterator = resultSet.iterator();
            while (commentsIterator.hasNext()) {
                Row nextComment = commentsIterator.next();
                Map<String,String> commentsInfo = new HashMap<>();
                commentsInfo.put("userEmail",nextComment.getString("useremail"));
                commentsInfo.put("comment",nextComment.getString("comment"));
                commentsInfo.put("date",dateFromTimeUUID(nextComment.getUuid("commentid")));
                comments.add(commentsInfo);
        }
        return comments;
    }
}
