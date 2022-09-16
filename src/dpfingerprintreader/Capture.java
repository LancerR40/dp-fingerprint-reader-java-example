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
    
    private ReaderCollection readers = null;
    private Reader           reader  = null;
    
    public Capture() {
    
    }
    
    public void startCapture() {
        getReader();
        capture();
    }
    
    public void getReader() {
        try {
            // get readers collection
            readers = UareUGlobal.GetReaderCollection();
            readers.GetReaders();
            
            // set reader
            reader = readers.get(0);
            
            // open reader in exclusive mode
            reader.Open(Reader.Priority.EXCLUSIVE);
            
        } catch(UareUException e) {
            System.out.print(e);
        }
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
                        System.out.print(e);
                        break; 
                    }
                }
                
                else if (rs.status == Reader.ReaderStatus.READY || rs.status == Reader.ReaderStatus.NEED_CALIBRATION) {
                    // ready for capture
                    isReady = true;
                    break;
                }
                
                else {
                    // reader notify error
                    break;
                }
                
            }
            
            if (isReady == true) {
                Reader.CaptureResult captureResult = reader.Capture(Fid.Format.ANSI_381_2004, Reader.ImageProcessing.IMG_PROC_PIV, reader.GetCapabilities().resolutions[0], -1);
                notifyListener(captureResult, null, null);
            }
            
            
            
        } catch(UareUException e) {
            System.out.print(e);
        }
    }
    
    public void notifyListener(Reader.CaptureResult cr, Reader.Status rs, UareUException exception) {
        System.out.print(cr);
    }
    
}
