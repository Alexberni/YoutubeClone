package NormalClasses;
    
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
      }
      
      @Override
      public void close(){ }
  
      @Override
      public void write(int b) throws IOException {
          write(new byte[] {(byte) b}, 0, 1);
      }
   
      @Override
      public void write(byte b[], int off, int len) throws IOException {  
          textArea.append(new String(b, off, len));  
      }    

      public static PrintStream getTextPrintStream(JTextArea textArea) {
              return new PrintStream(new TextAreaOutPutStream(textArea)); //,true,"ISO-8859-1");
      }
      
  }
 
 
