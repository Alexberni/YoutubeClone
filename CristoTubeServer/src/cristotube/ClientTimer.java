/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cristotube;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex
 */
public class ClientTimer extends Thread{
    private ServerThread client;
    private long startTime = System.currentTimeMillis();
    public ClientTimer(ServerThread client){
    
        this.client = client;
    
    }
    
    @Override
    public void run() {
       while(true){
            long elapsedTime = System.currentTimeMillis() - startTime; 
            long elapsedSeconds = elapsedTime / 1000;            
            this.client.onlineTime = elapsedSeconds;
       }
    }
    
    
}
