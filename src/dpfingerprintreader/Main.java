/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dpfingerprintreader;

/**
 *
 * @author TTruc
 */

import com.digitalpersona.uareu.*;
import org.json.*;

public class Main {

    /**
     * @param args the command line arguments
    */
    
    Reader reader             = null;
    String getReaderException = null;
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        // Main main = new Main();
        // System.out.print(main.enrollment());
    }
    
    private void getReader() {
        try {
            /* get readers collection */
            ReaderCollection readers = UareUGlobal.GetReaderCollection();
            readers.GetReaders();
            
            /* set reader */
            reader = readers.get(0);
            
            /* open reader */
            reader.Open(Reader.Priority.EXCLUSIVE);
            
        } catch(UareUException e) {
            getReaderException = "Ocurrió un error al obtener el lector de huella";
        }
    }
    
    public JSONObject enrollment() {
        getReader();
        
        Enrollment enrollment = new Enrollment(reader);
        return enrollment.run();
    }

}
