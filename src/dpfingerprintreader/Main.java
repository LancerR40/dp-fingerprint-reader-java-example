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
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    /**
     * @param args the command line arguments
    */
    
    Reader reader = null;
    
    public static void main(String[] args) throws JSONException {
        // TODO code application logic here
        
        Main main = new Main();
        System.out.print(main.enrollment());
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
            System.out.print("");
        }
    }
    
    public String enrollment() throws JSONException {
        getReader();
        
        Enrollment enrollment = new Enrollment(reader);
        enrollment.run();
        
        Enrollment.EnrollmentData data = enrollment.getEnrollment();
        JSONObject json = new JSONObject();
        
        try {
            json.put("encodedFMDTemplate", data.encodedFMDEnrollment);
            json.put("format", data.FMDFormat);
            json.put("errorMessage", "");
        } catch(JSONException e) {
            json.put("errorMessage", "Ocurrió un error al obtener los datos del enrolamiento");
        }
  
        return json.toString();
    }

}
