/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dpfingerprintreader;

import com.digitalpersona.uareu.*;
import java.util.Base64;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author TTruc
*/

public class Identification {
    private Reader reader      = null;
    private Fmd[]  fmds        = null;
    private String encodedFMDs = null;
    
    /*
    Identification(String encoded) {
        encodedFMDs = encoded;
    }
    */
    
    /*
    private void decodeFMDs() throws JSONException, UareUException {
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
    }
    */
    
    private void eventListener(boolean status, String errorMsg) {}
    
    private boolean getReader() {
        try {
            ReaderCollection readers = UareUGlobal.GetReaderCollection();
            readers.GetReaders();
            reader = readers.get(0);
                
            return true;
        } catch(UareUException e) {
            return false;
        }
    }
        
    private boolean openReader() {
        try {
            reader.Open(Reader.Priority.EXCLUSIVE);
            return true;
        } catch(UareUException e) {
            return false;
        }
    }
        
    private boolean closeReader() {
        try {
            reader.Close();
            return true;
        } catch (UareUException e) {
            return false;
        }
    }
    
    private void identification() throws UareUException {
        getReader();
        openReader();
        
        Capture capture = new Capture(reader);
        
        Engine engine = UareUGlobal.GetEngine();
        fmds = new Fmd[2];
        
        for(int x = 0; x <= 1; x++) {
            capture.capture();
            Capture.CaptureEvent event1 = capture.getLastCapture();
            
            Fmd a = engine.CreateFmd(event1.captureResult.image, Fmd.Format.ANSI_378_2004);
            fmds[x] = a;
            
            System.out.print("Foto tomada \n");
        }
        
        capture.capture();
        Capture.CaptureEvent event2 = capture.getLastCapture();
        
        System.out.print("Foto tomada \n");
        
        Fmd fmd = engine.CreateFmd(event2.captureResult.image, Fmd.Format.ANSI_378_2004);
        
        int falsepositive_rate = Engine.PROBABILITY_ONE / 100000; 
        
        Engine.Candidate[] candidates = engine.Identify(fmd, 0, fmds, falsepositive_rate, fmds.length);
        // System.out.print(candidates.length);
        
        if (candidates.length != 0) {
            int falsematch_rate = engine.Compare(fmd, 0, fmds[candidates[0].fmd_index], candidates[0].view_index);
            // double target_falsematch_rate = falsematch_rate / Engine.PROBABILITY_ONE ;
            int target_falsematch_rate = Engine.PROBABILITY_ONE / 100000;
            
            System.out.println(falsematch_rate < target_falsematch_rate);
        } else {
            System.out.print("No match");
        }
        
        closeReader();
    }
    
    public void start() throws JSONException, UareUException {
        // decodeFMDs();
        identification();
    }
}
