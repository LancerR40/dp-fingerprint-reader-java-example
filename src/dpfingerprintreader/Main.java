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
    
    public static void main(String[] args) throws JSONException, UareUException {
        // TODO code application logic here
        
        //Main main = new Main();
        //System.out.print(main.enrollment());
        
        // Verification verification = new Verification();
        // verification.decodeFMDs();
        
        Identification identification = new Identification();
        identification.start();
    }
    
    public String verification(String encodedFMDs) throws JSONException, UareUException {
        Verification verification = new Verification(encodedFMDs);
        verification.start();
        
        Verification.VerificationEvent data = verification.getVerificationEvent();
        JSONObject json = new JSONObject();
        
        try {
            json.put("verificationStatus", data.verificationStatus);
            json.put("errorMessage", data.errorMessage);
        } catch(JSONException e) {
            json.put("errorMessage", "Ocurrió un error al obtener los datos del enrolamiento");
        }
  
        return json.toString();
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
