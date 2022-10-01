/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dpfingerprintreader;

/**
 *
 * @author TTruc
 */

import java.util.concurrent.TimeUnit;
import com.digitalpersona.uareu.*;

public class Capture {
    
    // class to build capture data
    public class CaptureEvent {
        public Reader.CaptureResult captureResult = null;
        public Reader.Status        readerStatus  = null;
        public UareUException       exception     = null;
        public InterruptedException interrupted   = null;
        
        CaptureEvent(Reader.CaptureResult cr, Reader.Status rs, UareUException ex, InterruptedException in) {
            captureResult = cr;
            readerStatus  = rs;
            exception     = ex;
            interrupted   = in;
        }
    }
    
    private Reader           reader      = null;
    private CaptureEvent     lastCapture = null;
    
    public Capture(Reader r) {
        reader = r;
    }
    
    public void capture() {
        try {
            boolean isReady = false;
            
            while(!isReady) {
                Reader.Status rs = reader.GetStatus();
                
                if (rs.status == Reader.ReaderStatus.BUSY) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch(InterruptedException e) {
                        notifyListener(null, rs, null, e);
                        break; 
                    }
                } else if (rs.status == Reader.ReaderStatus.READY || rs.status == Reader.ReaderStatus.NEED_CALIBRATION) {
                    isReady = true;
                    break;
                } else {
                    notifyListener(null, rs, null, null);
                    break;
                }
                
            }
            
            if (isReady == true) {
                Reader.CaptureResult captureResult = reader.Capture(Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_DEFAULT, reader.GetCapabilities().resolutions[0], -1);
                notifyListener(captureResult, null, null, null);
            }
            
        } catch(UareUException e) {
            notifyListener(null, null, e, null);
        }
    }
    
    public CaptureEvent getLastCapture() {
        return lastCapture;
    }
    
    public void notifyListener(Reader.CaptureResult cr, Reader.Status rs, UareUException ex, InterruptedException in) {
        final CaptureEvent event = new CaptureEvent(cr, rs, ex, in);
        lastCapture = event;
    }
    
}
