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
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    /**
     * @param args the command line arguments
    */
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        //Main main = new Main();
        //System.out.print(main.enrollment());
        
        // Verification verification = new Verification();
        // verification.decodeFMDs();
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
        
        /*
        JSONObject json = new JSONObject(encodedFMDs);
        
        JSONObject currentFMD = new JSONObject(json.getJSONObject("currentFMD").toString());
        
        JSONArray  jsonArr = new JSONArray(json.getJSONArray("FMDs").toString());
        JSONObject jsonObj = new JSONObject();
        
        JSONArray jsonArrTest = new JSONArray();
        
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonObj = jsonArr.getJSONObject(i);
            
            byte[] decode = Base64.getDecoder().decode(jsonObj.getString("data"));
            String format = jsonObj.getString("format"); 
            
            Fmd fmd = UareUGlobal.GetImporter().ImportFmd(decode, Fmd.Format.valueOf(format), Fmd.Format.ANSI_378_2004);
            
            jsonArrTest.put(Base64.getEncoder().encodeToString(fmd.getData()));
        }
        
        return currentFMD.toString();
        */
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
