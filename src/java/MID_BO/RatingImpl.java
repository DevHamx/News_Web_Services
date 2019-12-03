/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MID_BO;

import DAO.RatingDAO;
import java.nio.ByteBuffer;
import org.apache.commons.codec.DecoderException;
import org.json.simple.JSONObject;

/**
 *
 * @author hamza
 */
public class RatingImpl {
    public RatingDAO ratingDAO;
    
    public RatingImpl() {
        ratingDAO = new RatingDAO();
    }
    public JSONObject addCommentImpl(String url,int rating,String userEmail,String authorEmail,String newsDate) throws DecoderException{
        return ratingDAO.rateNews(url, rating, userEmail, authorEmail, newsDate);
    }
}
