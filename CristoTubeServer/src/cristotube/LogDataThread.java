package cristotube;


import Models.Video;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex
 */

public class LogDataThread extends Thread {
    private JTable table;
    private ArrayList<ServerThread> clients = new ArrayList<>();
    private long startTime = System.currentTimeMillis();
    
    public LogDataThread(JTable table, ArrayList<ServerThread> clients) {
        this.table = table;
        this.clients = clients;
    }
    
    @Override
    public void run() {
        long elapsedTime;
        while(true){
            elapsedTime = System.currentTimeMillis();
            if(elapsedTime - startTime > 1000){
                updateTable(this.clients);      
                startTime = elapsedTime;
            }
        }
    }
    
   synchronized void updateTable(ArrayList<ServerThread> clients) { 
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        int rows = model.getRowCount(); 
        for(int i = rows - 1; i >=0; i--)
        {
           model.removeRow(i); 
        }
        for(ServerThread client : clients){
            String data1 = client.currentUser.getLogin();
            String data2 = Long.toString(client.onlineTime);
            String data3 = "working on";
            Object[] row = { data1, data2, data3};
            model.addRow(row);         
        }
}

    
    
    
}
