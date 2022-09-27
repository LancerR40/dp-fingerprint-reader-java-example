/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dpfingerprintreader;

/**
 *
 * @author TTruc
 */

import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.digitalpersona.uareu.*;
import org.json.*;

public class Enrollment {
    public Reader reader     = null;
    public String enrollment = null;
    
    public class EnrollmentProcess implements Engine.EnrollmentCallback {
        public Reader reader     = null;
        public String enrollment = null;

        public class EnrollmentEvent {
            public Reader.CaptureResult readerCapture = null;
            public Reader.Status        readerStatus  = null;
            public UareUException       exception     = null;
            public Fmd                  enrollmentFmd = null;
            
            EnrollmentEvent(Fmd fmd, Reader.CaptureResult cr, Reader.Status rs, UareUException ex) {
                enrollmentFmd = fmd;
                readerCapture = cr;
                readerStatus  = rs;
                exception     = ex;
            }
        }
        
        EnrollmentProcess(Reader r) {
            reader = r;
        }
        
        @Override
        public Engine.PreEnrollmentFmd GetFmd(Fmd.Format format) {
            Engine.PreEnrollmentFmd prefmd = null;
            
            while(prefmd == null) {
                // capture instance
                Capture capture = new Capture(reader);
                
                // start capture
                capture.capture();
                
                // get last capture data
                Capture.CaptureEvent event = capture.getLastCapture();
                
                if (event.captureResult == null) {
                    System.out.print("Hello 1");
                    break;
                }
                
                else if (event.captureResult.quality == Reader.CaptureQuality.CANCELED) {
                    System.out.print("Hello 2");
                    break;
                }
                
                else if (event.captureResult.image == null && event.captureResult.quality != Reader.CaptureQuality.GOOD) {
                    System.out.print("Hello 3");
                    break;
                }
                
                Engine engine = UareUGlobal.GetEngine();
                
                try {
                    // extract features
                    Fmd fmd = engine.CreateFmd(event.captureResult.image, Fmd.Format.ANSI_378_2004);
                    
                    // return prefmd 
                    prefmd = new Engine.PreEnrollmentFmd();
                    prefmd.fmd = fmd;
                    prefmd.view_index = 0;
                    
                   
                } catch(UareUException e) {
                    System.out.print("Aquí es el error 1");
                }
            }
            
            return prefmd;
        }
        
        public String getEnrollment() {
            return enrollment;
        }
        
        public void sendToListener(Fmd fmd, Reader.CaptureResult cr, Reader.Status rs, UareUException ex) {
            // System.out.print(fmd);
            
            JSONObject json = new JSONObject();
            
            try {
                String encoded = Base64.getEncoder().encodeToString(fmd.getData());
                
                json.put("encodedFMDTemplate", encoded);
                json.put("format", fmd.getFormat());
                
                enrollment = json.toString();
                
                // System.out.print(fmd);
                
                // String encoded = Base64.getEncoder().encodeToString(fmd.getData());
                // System.out.print(encoded);
                
                // System.out.print("\n");
                
                // byte[] decode = Base64.getDecoder().decode(encoded);
                // System.out.print(decode);
                
                // System.out.print("\n");
                
                // Fmd a = UareUGlobal.GetImporter().ImportFmd(decode, Fmd.Format.ANSI_378_2004, Fmd.Format.ANSI_378_2004);
                // System.out.print(a);
                
                // System.out.print("\n");
                
                // Fmd b = engine.CreateFmd(fmd.getData(), fmd.getWidth(), fmd.getHeight(), fmd.getResolution(), 0, fmd.getCbeffId(), Fmd.Format.ANSI_378_2004);
                // System.out.print(fmd.getViews()[0].getFingerPosition());
                
            } catch (JSONException ex1) {
                Logger.getLogger(Enrollment.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        public void run() {
            Engine engine = UareUGlobal.GetEngine();
            
            try {
                boolean cancel = false;
                
                while(!cancel) {
                    /* run fmd enrollment */
                    Fmd fmd = engine.CreateEnrollmentFmd(Fmd.Format.ANSI_378_2004, this);
                    
                    if (fmd == null) {
                        sendToListener(null, null, null, null);
                    } else {
                        sendToListener(fmd, null, null, null);
                    }
                    
                    break;
                }
                
            } catch(UareUException e) {
                System.out.print(e);
            }
        }
    }
    
    public String getEnrollment() {
        return enrollment;
    }
    
    public void run() {
        EnrollmentProcess enrollmentProcess = new EnrollmentProcess(reader);
        enrollmentProcess.run();
        
        /* set enrollment json */
        enrollment = enrollmentProcess.getEnrollment();
    }
    
    Enrollment(Reader r) {
        reader = r;
    }
    
}
