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
import com.digitalpersona.uareu.*;

public class Enrollment {
    
    /* class to build a enrollment object */
    public class EnrollmentData {
        public String     encodedFMDEnrollment = null;
        public Fmd.Format FMDFormat            = null;
        public String     errorMessage         = null;
            
        EnrollmentData(String encoded, Fmd.Format format, String errorMsg) {
            encodedFMDEnrollment = encoded;
            FMDFormat            = format;
            errorMessage         = errorMsg;
        }
    }
    
    public Reader         reader     = null;
    public EnrollmentData enrollment = null;
    
    public class EnrollmentProcess implements Engine.EnrollmentCallback {
        public Reader         reader     = null;
        public EnrollmentData enrollment = null;
        
        
        EnrollmentProcess() {}
        
        
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
                    continue;
                }
                
                else if (event.captureResult.quality == Reader.CaptureQuality.CANCELED) {
                    continue;
                }
                
                else if (event.captureResult.image == null && event.captureResult.quality != Reader.CaptureQuality.GOOD) {
                    continue;
                }
                
                if (event.captureResult != null && event.captureResult.quality == Reader.CaptureQuality.GOOD) {
                    Engine engine = UareUGlobal.GetEngine();
                
                    try {
                        // extract features
                        Fmd fmd = engine.CreateFmd(event.captureResult.image, Fmd.Format.ANSI_378_2004);

                        // return prefmd 
                        prefmd = new Engine.PreEnrollmentFmd();
                        prefmd.fmd = fmd;
                        prefmd.view_index = 0;

                    } catch(UareUException e) {
                        prefmd = null;
                    }
                }
            }
            
            return prefmd;
        }
        
        public EnrollmentData getEnrollment() {
            return enrollment;
        }
        
        public void enrollmentListener(Fmd fmd, String errorMsg) {
            boolean isClose = closeReader();
            
            if (isClose == true) {
                if (errorMsg != "") {
                    EnrollmentData data = new EnrollmentData(null, null, errorMsg);
                    enrollment = data;
                } else {
                    String encoded = Base64.getEncoder().encodeToString(fmd.getData());
                    EnrollmentData data = new EnrollmentData(encoded, fmd.getFormat(), "");
                    enrollment = data;
                }
            } else {
                EnrollmentData data = new EnrollmentData(null, null, "Ocurrió un error al liberal los recursos del lector de huellas");
                enrollment = data;
            }
        }
        
        private void getReader() {
            try {
                ReaderCollection readers = UareUGlobal.GetReaderCollection();
                readers.GetReaders();

                reader = readers.get(0);
            } catch(UareUException e) {
                System.out.print("");
            }
        }
        
        public boolean openReader() {
            try {
                reader.Open(Reader.Priority.EXCLUSIVE);
                return true;
            } catch(UareUException e) {
                return false;
            }
        }
        
        public boolean closeReader() {
            try {
                reader.Close();
                return true;
            } catch (UareUException e) {
                return false;
            }
        }
        
        public void run() {
            getReader();
            boolean isOpen = openReader();
            
            if (isOpen == true) {
                Engine engine = UareUGlobal.GetEngine();
                try {
                    boolean cancel = false;

                    while(!cancel) {
                        /* run fmd enrollment */
                        Fmd fmd = engine.CreateEnrollmentFmd(Fmd.Format.ANSI_378_2004, this);

                        if (fmd == null) {
                            enrollmentListener(null, "El FMD template no pudo ser creado");
                        } else {
                            enrollmentListener(fmd, "");
                        }

                        break;
                    }

                } catch(UareUException e) {
                    System.out.print(e);
                }
            } else {
                EnrollmentData data = new EnrollmentData(null, null, "Ocurrió un error al activar el lector de huellas");
                enrollment = data;
            }
        }
    }
    
    public EnrollmentData getEnrollment() {
        return enrollment;
    }
    
    public void run() {
        EnrollmentProcess enrollmentProcess = new EnrollmentProcess();
        enrollmentProcess.run();
        
        /* set enrollment object */
        enrollment = enrollmentProcess.getEnrollment();
    }
    
    
    Enrollment() {}
    
}

/*
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
*/

// System.out.print(fmd);
            
            // JSONObject json = new JSONObject();
            
            // json.put("encodedFMDTemplate", encoded);
            // json.put("format", fmd.getFormat());
                
            // enrollment = json.toString();
                
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

/*
    public void sendToListener(Fmd fmd, Reader.CaptureResult cr, Reader.Status rs, UareUException ex, String errorMsg) {
            
            
            String encoded = Base64.getEncoder().encodeToString(fmd.getData());
            
            try {
              
                reader.Close();
                
               
                EnrollmentData data = new EnrollmentData(encoded, fmd.getFormat(), "");
                enrollment = data;
            } catch(UareUException e) {
                EnrollmentData data = new EnrollmentData(null, null, "Ocurrió un error al liberar los procesos del lector de huella");
                enrollment = data;
            }
            
        }
*/
