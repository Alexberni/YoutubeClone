/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import javax.swing.JOptionPane;
public class InstantInputDialog {
    public String data;
    public String legend;
    
  public InstantInputDialog(String mode){
    if(mode.equals("titulo")){
        this.legend = "Titulo del Video: ";
    }
    else
        this.legend = "Descripcion del Video: ";
}
  
  public void main() {
    String input = JOptionPane.showInputDialog(legend);
    data = input;
  }
}
