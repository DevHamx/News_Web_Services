/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Demo;

import MID_BO.NewsImpl;
import MID_BO.CommentImpl;
import MID_BO.RatingImpl;
import java.io.UnsupportedEncodingException;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.commons.codec.DecoderException;
import org.json.simple.JSONObject;

/**
 *
 * @author hamza
 */
@WebService(serviceName = "NewsWs")
public class NewsWs {
    NewsImpl newsImpl = new NewsImpl();
    CommentImpl commentImpl = new CommentImpl();
    RatingImpl RatingImpl = new RatingImpl();
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "create")
    public String create(@WebParam(name = "url") String url,@WebParam(name = "titre") String titre,@WebParam(name = "userEmail") String userEmail) throws DecoderException {
        newsImpl.news.connect();
        JSONObject jsonObject = newsImpl.newsCreateImpl(url, titre, userEmail);
        newsImpl.news.session.close();
        return jsonObject.toString();
    }
    
    @WebMethod(operationName = "addComment")
    public String addComment(@WebParam(name = "url") String url,@WebParam(name = "comment") String comment,@WebParam(name = "userEmail") String userEmail) throws DecoderException {
        commentImpl.commentObject.connect();
        JSONObject jsonObject = commentImpl.addCommentImpl(url, comment, userEmail);
        commentImpl.commentObject.session.close();
        return jsonObject.toString();
    }
    @WebMethod(operationName = "rateNews")
    public String rateNews(@WebParam(name = "url") String url,@WebParam(name = "rating") int rating,@WebParam(name = "userEmail") String userEmail,@WebParam(name = "authorEmail") String authorEmail,@WebParam(name = "newsDate") String newsDate) throws DecoderException {
        RatingImpl.ratingDAO.connect();
        JSONObject jsonObject = RatingImpl.ratingDAO.rateNews(url, rating, userEmail, authorEmail, newsDate);
        RatingImpl.ratingDAO.session.close();
        return jsonObject.toString();
    }
    
    @WebMethod(operationName = "titleOfNewsLikedByUser")
    public String titleOfNewsLikedByUser(@WebParam(name = "userEmail") String userEmail) throws DecoderException {
        newsImpl.news.connect();
        JSONObject jsonObject = newsImpl.titleOfNewsLikedByUser(userEmail);
        newsImpl.news.session.close();
        return jsonObject.toString();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllNewsSortedFromNewOnes")
    public String getAllNewsSortedFromNewOnes() throws DecoderException, UnsupportedEncodingException {
        newsImpl.news.connect();
        JSONObject jsonObject = newsImpl.getAllNewsSortedFromNewOnes();
        newsImpl.news.session.close();
        return jsonObject.toString();
    }
    
    @WebMethod(operationName = "getUserNewsSortedFromNewOnes")
    public String getUserNewsSortedFromNewOnes(@WebParam(name = "userEmail") String userEmail) throws DecoderException, UnsupportedEncodingException {
        newsImpl.news.connect();
        JSONObject jsonObject = newsImpl.getUserNewsSortedFromNewOnes(userEmail);
        newsImpl.news.session.close();
        return jsonObject.toString();
    }
}
