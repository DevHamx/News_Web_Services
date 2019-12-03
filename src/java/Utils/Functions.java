/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author hamza
 */
public class Functions {
    
    public static byte[] stringToHex(String string) throws DecoderException {
        String hex = String.format("%032x", new BigInteger(1, string.getBytes()));
    return Hex.decodeHex(hex);
}   
    public static String hexToString(byte[] hex) throws UnsupportedEncodingException{
        return new String(hex, "UTF-8");
    }
    public static String dateFromTimeUUID(UUID id){
        final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
        long timeStamp = (id.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000;
        Date date = new Date(timeStamp);
        return date.toString();
    }
}
