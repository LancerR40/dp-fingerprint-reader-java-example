/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package dpfingerprintreader;

/**
 *
 * @author TTruc
*/

import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    /**
     * @param args the command line arguments
    */
    
    public static void main(String[] args) throws JSONException {
        // TODO code application logic here
        
        Main main = new Main();
        System.out.print(main.enrollment());
    }
    
    public String enrollment() throws JSONException {
        Enrollment enrollment = new Enrollment();
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
