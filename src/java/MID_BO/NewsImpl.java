/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MID_BO;

import DAO.NewsDAO;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.DecoderException;
import org.json.simple.JSONObject;

/**
 *
 * @author hamza
 */
public class NewsImpl {
    public NewsDAO news;
    
    public NewsImpl() {
        news = new NewsDAO();
    }
    public JSONObject newsCreateImpl(String url,String titre,String userEmail) throws DecoderException{
        return news.createNews(url, titre, userEmail);
    }
    
    public JSONObject titleOfNewsLikedByUser(String userEmail){
        return news.titleOfNewsLikedByUser(userEmail);
    }
    public JSONObject getAllNewsSortedFromNewOnes() throws DecoderException, UnsupportedEncodingException{
        return news.getAllNewsSortedFromNewOnes();
    }
    public JSONObject getUserNewsSortedFromNewOnes(String userEmail) throws DecoderException, UnsupportedEncodingException{
        return news.getUserNewsSortedFromNewOnes(userEmail);
    }
}
