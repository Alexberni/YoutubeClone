package NormalClasses;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
 
import javax.swing.JTextArea;

public final class TextAreaOutPutStream extends OutputStream {
    private final JTextArea textArea;
  
      public TextAreaOutPutStream(final JTextArea textArea) {
          this.textArea = textArea;
      }
  
      @Override
      public void flush(){ 
          //textArea.append(sb.toString());
          //sb.setLength(0);
      }
      
      @Override
      public void close(){ }
  
      @Override
      public void write(int b) throws IOException {
          /*
          if (b == '\r')
              return;
          
          if (b == '\n') {
              textArea.append(sb.toString()+"\n");
              sb.setLength(0);
              return;
          }
          
          sb.append((char)b);
          */
          //textArea.append(Character.toString((char)b));
          write(new byte[] {(byte) b}, 0, 1);
      }
   
      @Override
      public void write(byte b[], int off, int len) throws IOException {  
          textArea.append(new String(b, off, len));  
      }    
      
      
      /**
       * Factory method for creating a PrintStream to print to selected TextArea
       * @param textArea area where to print
       * @return created PrintStream ready to print to TextArea
       * @example
       * <pre name="test">
       * #import java.io.*;
       * #import javax.swing.*;
       *   JTextArea text = new JTextArea();
       *   PrintStream tw = TextAreaOutputStream.getTextPrintStream(text);
       *   tw.print("Hyvää"); // skandit toimi
       *   tw.print(" ");
       *   tw.print("päivää!");
       *   text.getText() === "Hyvää päivää!";
       *   text.setText("");
       *   tw.print("ä");
       *   text.getText() === "ä"; 
       * </pre>
       */
      public static PrintStream getTextPrintStream(JTextArea textArea) {
              return new PrintStream(new TextAreaOutPutStream(textArea)); //,true,"ISO-8859-1");
      }
      
  }
 
 