/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Utils.Functions;
import static Utils.Functions.stringToHex;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.servererrors.QueryValidationException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.DecoderException;
import org.json.simple.JSONObject;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.*;
/**
 *
 * @author hamza
 */
public class RatingDAO extends ConnectionSession{
    private ResultSet resultSet=null;
    private String url;
    private int rating;
    private String authorEmail,userEmail;
    private final Map<String,Integer> ratingMap=new HashMap<>();
    
    public JSONObject rateNews(String url,int rating,String userEmail,String authorEmail,String newsDate) throws DecoderException{
        JSONObject responseJsonObject = new JSONObject();
        this.url= url;
        this.userEmail = userEmail;
        this.rating = rating;
        this.authorEmail = authorEmail;
        ratingMap.put(this.userEmail, this.rating);
        PreparedStatement preparedStatement1 = session.prepare("update examen set rating = rating + ? where url = ?");
        PreparedStatement preparedStatement2 = session.prepare("update news_by_user set rating =rating + ? where useremail = ? and date_ajouter = ? and url = ?");
        PreparedStatement preparedStatement3 = session.prepare("update news_sorted set rating =rating + ? where url = ? and date_ajouter = ?");
        // les j’aime/je d ́déteste sont stockés dans une collection différente de celle des news sous la forme
        PreparedStatement preparedStatement4 = session.prepare("insert into ratings_by_news (url,useremail,rating) VALUES(:url,:useremail,:rating);");
        PreparedStatement preparedStatement5 = session.prepare("insert into ratings_by_user (useremail,url,rating) VALUES(:useremail,:url,:rating);");
         
        try{
        session.execute(preparedStatement1.bind(ratingMap,ByteBuffer.wrap(stringToHex(url))));
        session.execute(preparedStatement2.bind(ratingMap,this.authorEmail,newsDate,ByteBuffer.wrap(stringToHex(url))));
        session.execute(preparedStatement3.bind(ratingMap,ByteBuffer.wrap(stringToHex(url)),newsDate));
        session.execute(preparedStatement4.bind(ByteBuffer.wrap(stringToHex(url)),this.userEmail, this.rating));
        session.execute(preparedStatement5.bind(this.userEmail,ByteBuffer.wrap(stringToHex(url)), this.rating));
        
        }
        catch(QueryValidationException e){
            responseJsonObject.put("error", e.getMessage());
            return responseJsonObject;
        }
       
        return responseJsonObject;
    }
    
    public JSONObject newsScoreTotal() throws IOException
    {
        JSONObject responseJsonObject = new JSONObject();
        Configuration conf = new Configuration();
        Job job = new Job(conf, "news score");
        job.setJarByClass(RatingDAO.class);
        // mapper configuration.
    job.setMapperClass(RatingMapper.class);
    job.setMapOutputKeyClass(ByteBuffer.class);
    job.setMapOutputValueClass(IntWritable.class);
    //job.setInputFormatClass(RatingDAO.class);
    // Reducer configuration
    job.setReducerClass(RatingAggregator.class);
    job.setOutputKeyClass(ByteBuffer.class);
    job.setOutputValueClass(Integer.class);
    //job.setOutputFormatClass(HashMap.class);

        return responseJsonObject;
    }
}
