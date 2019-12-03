/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import java.net.InetSocketAddress;
/**
 *
 * @author hamza
 */
public class ConnectionSession {
    public CqlSession session;
    //adresseand port of cassandra's host
    private String ipAdresse = "192.168.56.105";
    private int port = 9042;
    //keySpace name A.K.A Database name
    private String keySpace = "News123";
     
    public void connect() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress(ipAdresse, port))
                .withKeyspace(CqlIdentifier.fromCql(keySpace))
                .withLocalDatacenter("datacenter1")
                .build();
    }

    public String getIpAdresse() {
        return ipAdresse;
    }

    public void setIpAdresse(String ipAdresse) {
        this.ipAdresse = ipAdresse;
    }

    public String getKeySpace() {
        return keySpace;
    }

    public void setKeySpace(String keySpace) {
        this.keySpace = keySpace;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    
}
