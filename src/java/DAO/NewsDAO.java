/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import MID_BO.CommentImpl;
import Utils.Functions;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.servererrors.QueryValidationException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.codec.DecoderException;
import org.json.simple.JSONObject;

/**
 *
 * @author hamza
 */
public class NewsDAO extends ConnectionSession{
    private ResultSet resultSet=null;
    private String url;
    private String date_ajouter,titre,userEmail;
    CommentImpl commentImpl;
    public JSONObject createNews(String url,String titre,String userEmail) throws DecoderException{
        JSONObject responseJsonObject = new JSONObject();
        this.url = url;
        this.date_ajouter = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        this.titre =titre ;
        this.userEmail = userEmail;
        PreparedStatement preparedStatement1 = session.prepare("insert into examen (url,date_ajouter,titre,useremail) VALUES(:url, :date_ajouter, :titre,:useremail) IF NOT EXISTS;");
        PreparedStatement preparedStatement2 = session.prepare("insert into news_by_user (userEmail,date_ajouter,url,titre) VALUES(:useremail,:date_ajouter,:url, :titre);");
        PreparedStatement preparedStatement3 = session.prepare("insert into news_sorted (date_ajouter,url,titre,useremail) VALUES(:date_ajouter,:url, :titre,:useremail);");
        
        try{
        resultSet=session.execute(preparedStatement1.bind(ByteBuffer.wrap(Functions.stringToHex(url)), this.date_ajouter, this.titre,this.userEmail));
        }
        catch(QueryValidationException e){
            responseJsonObject.put("error", e.getMessage());
            return responseJsonObject;
        }
        
        if (resultSet.iterator().next().getBoolean("[applied]")==false) {
            responseJsonObject.put("error","News already exists");
        } 
        else{
            session.execute(preparedStatement2.bind(this.userEmail,this.date_ajouter,ByteBuffer.wrap(Functions.stringToHex(url)), this.titre));
            session.execute(preparedStatement3.bind(this.date_ajouter,ByteBuffer.wrap(Functions.stringToHex(url)), this.titre,this.userEmail));
        }
        return responseJsonObject;
    }
    
    public JSONObject getAllNewsSortedFromNewOnes() throws DecoderException, UnsupportedEncodingException{
        JSONObject responseJsonObject = new JSONObject();
        resultSet = session.execute("select url,useremail,date_ajouter,titre,rating from news_sorted");
        Iterator<Row> newsIterator = resultSet.iterator();
        List<Map<String,Object>> examen = new ArrayList<Map<String,Object>>();
        commentImpl = new CommentImpl();
        while (newsIterator.hasNext()) {
            Row nextNews = newsIterator.next();
            Map<String,Object> news = new HashMap();
            Map<String,Object> newsInfo = new HashMap<>();
            newsInfo.put("userEmail",nextNews.getString("useremail"));
            newsInfo.put("titre",nextNews.getString("titre"));
            newsInfo.put("date_ajouter",nextNews.getString("date_ajouter"));
            newsInfo.put("rating",nextNews.getMap("rating", String.class, Integer.class));
            List<Map<String,String>> comments = commentImpl.getComment(Functions.bytesToString(nextNews.getByteBuffer("url").array()),session);
            newsInfo.put("comments",comments);
            news.put("url",Functions.bytesToString(nextNews.getByteBuffer("url").array()));
            news.put("info",newsInfo);
            examen.add(news);
        }
        responseJsonObject.put("news", examen);
        return responseJsonObject;
    }
    
    public JSONObject titleOfNewsLikedByUser(String userEmail){
        JSONObject responseJsonObject = new JSONObject();
            resultSet = session.execute("select titre from examen where rating contains key '"+userEmail+"';");
            Iterator<Row> it = resultSet.iterator();
            List<String> titres = new ArrayList<String>();
            while (it.hasNext()) {
                titres.add(it.next().getString("titre"));
        }
            responseJsonObject.put("result",titres );
        return responseJsonObject;
    }
    
    public JSONObject getUserNewsSortedFromNewOnes(String userEmail) throws DecoderException, UnsupportedEncodingException{
        JSONObject responseJsonObject = new JSONObject();
        resultSet = session.execute("select url,date_ajouter,titre,rating from news_by_user where useremail = '"+userEmail+"'");
        Iterator<Row> newsIterator = resultSet.iterator();
        List<Map<String,Object>> examen = new ArrayList<Map<String,Object>>();
        commentImpl = new CommentImpl();
        while (newsIterator.hasNext()) {
            Row nextNews = newsIterator.next();
            Map<String,Object> news = new HashMap();
            Map<String,Object> newsInfo = new HashMap<>();
            newsInfo.put("titre",nextNews.getString("titre"));
            newsInfo.put("date_ajouter",nextNews.getString("date_ajouter"));
            newsInfo.put("rating",nextNews.getMap("rating", String.class, Integer.class));
            List<Map<String,String>> comments = commentImpl.getComment(Functions.bytesToString(nextNews.getByteBuffer("url").array()),session);
            newsInfo.put("comments",comments);
            news.put("url",Functions.bytesToString(nextNews.getByteBuffer("url").array()));
            news.put("info",newsInfo);
            examen.add(news);
        }
        responseJsonObject.put("news", examen);
        return responseJsonObject;
    }
}
