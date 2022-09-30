/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dpfingerprintreader;

/**
 *
 * @author TTruc
*/

import com.digitalpersona.uareu.*;
import java.util.Base64;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Verification {
    
    private VerificationEvent verificationEvent = null;
    private Fmd               fmd               = null;
    private Fmd[]             fmds              = null;
    private String            encodedFMDs       = null;
    
    Verification(String encoded) {
        encodedFMDs = encoded;
    }
    
    public class VerificationEvent {
        boolean verificationStatus = false;
        String  errorMessage       = null;
        
        VerificationEvent(boolean status, String errorMsg) {
            verificationStatus = status;
            errorMessage       = errorMsg;
        }
    }
    
    public void decodeFMDs() throws JSONException, UareUException {
        JSONObject json = new JSONObject(encodedFMDs);
        
        JSONArray  jsonArr = new JSONArray(json.getJSONArray("FMDs").toString());
        JSONObject jsonObj = new JSONObject();
        
        fmds = new Fmd[jsonArr.length()];
        
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonObj = jsonArr.getJSONObject(i);
            
            byte[] decode = Base64.getDecoder().decode(jsonObj.getString("data"));
            String format = jsonObj.getString("format"); 
            
            Fmd someFMD = UareUGlobal.GetImporter().ImportFmd(decode, Fmd.Format.valueOf(format), Fmd.Format.ANSI_378_2004);
            fmds[i] = someFMD;
          
        }
        
        JSONObject currentFMDObject = new JSONObject(json.getJSONObject("currentFMD").toString());
        
        byte[] decode = Base64.getDecoder().decode(currentFMDObject.getString("data"));
        String format = jsonObj.getString("format"); 
        
        Fmd currentFMD = UareUGlobal.GetImporter().ImportFmd(decode, Fmd.Format.valueOf(format), Fmd.Format.ANSI_378_2004);  
        fmd = currentFMD;
    }
    
    private void eventListener(boolean status, String errorMsg) {
        VerificationEvent event = new VerificationEvent(status, errorMsg);
        verificationEvent = event;
    }
    
    public void verification() throws JSONException, UareUException {
        Engine engine = UareUGlobal.GetEngine();
        boolean isMatched = false;
        
        for (int i = 0; i < fmds.length; i++) {
            try {
                int falsematch_rate = engine.Compare(fmd, 0, fmds[i], 0);
                int target_falsematch_rate = Engine.PROBABILITY_ONE / 100000;

                if (falsematch_rate < target_falsematch_rate){
                    isMatched = true;
                    break;
                } else {
                    isMatched = false;
                }
            } catch(UareUException e) {
                System.out.print(e);
            }
        }
        
        if (isMatched) {
            eventListener(isMatched, "La huella se encuentra registrada");
        } else {
            eventListener(isMatched, "");
        }
    }
    
    public VerificationEvent getVerificationEvent() {
        return verificationEvent;
    }
    
    public void start() throws JSONException, UareUException {
        /* decode all FMDs coming from Python */
        decodeFMDs();
        
        /* execute verification process */
        verification();
    }
    
}
