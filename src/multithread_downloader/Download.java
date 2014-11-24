
package multithread_downloader;

import java.io.*;
import java.net.*;
import java.util.*;


// This class downloads a file from a URL.
class Download extends Observable implements Runnable {
  // Max size of download buffer.
  private static final int MAX_BUFFER_SIZE = 1024;

  private int renamer=1;
  // These are the status names.
  public static final String STATUSES[] = {"Downloading",
    "Paused", "Complete", "Cancelled", "Error"};
  
  public static final int COLORS[] = {0x0B9424, 0x94880B, 0x0EB7C8, 0xC8720E, 0xC8170E};
  
  public int quality;

  // These are the status codes.
  public static final int DOWNLOADING = 0;
  public static final int PAUSED = 1;
  public static final int COMPLETE = 2;
  public static final int CANCELLED = 3;
  public static final int ERROR = 4;

  public String errorText=null;
  private URL url; // download URL
  private String filePath;
  private String fileName;
  private int size; // size of download in bytes
  private int downloaded; // number of bytes downloaded
  private int status; // current status of download
  private int speed;
  
  // Constructor for Download.
  public Download(URL url, String filePath, String fileName, int quality) {
    this.quality=quality;
    this.url = url;
    this.fileName=fileName;
    this.filePath = filePath+"/";
    size = -1;
    downloaded = 0;
    status = DOWNLOADING;

    // Begin the download.
    download();
  }

  // Get this download's URL.
  public String getUrl() {
    return url.toString();
  }

  // Get this download's size.
  public int getSize() {
    return size/1024;
  }
  

  // Get this download's progress.
  public float getProgress() {
    return ((float) downloaded / size) * 100;
  }

  // Get this download's status.
  public int getStatus() {
    return status;
  }

  // Pause this download.
  public void pause() {
    status = PAUSED;
    stateChanged();
  }

  // Resume this download.
  public void resume() {
    if(status!=CANCELLED){  
        status = DOWNLOADING;
        stateChanged();
       // download();
    }
  }

  // Cancel this download.
  public void cancel() {
    status = CANCELLED;
    stateChanged();
  }

  // Mark this download as having an error.
  private void error() {
    status = ERROR;
    stateChanged();
  }

  // Start or resume downloading.
  private void download() {
    Thread thread = new Thread(this);
    thread.start();
  }

  // Get file name portion of URL.
  private String getFileName(URL url) {
    String fileName = url.getFile();
    return fileName.substring(fileName.lastIndexOf('/') + 1);
  }
  
   public String getTitle(){
       return  fileName;
   }
   public String getPath(){
       return  filePath;
   }
   public int getSpeed(){
       //System.out.print("speed: "+speed+"speed/8: "+speed/8+"speed/(8*1024): "+speed/(8*1024)+"speed/1024: "+speed/1024);
       return speed/(1024);
   }
   public int getDownloaded(){
       return  downloaded/(1024);
   }
   
   private void check(){
        if (new File(filePath+fileName+".mp4").isFile()) {
            fileName=fileName+"("+renamer+")";
            renamer++;
            System.out.println("Fiele already exist. New file name");
            check();
        }   
   }

  // Download file.
  public void run() {
    RandomAccessFile file = null;
    InputStream stream = null;

    try {
        System.out.println(" Open connection to URL quakity: "+quality);
      HttpURLConnection connection =
        (HttpURLConnection) url.openConnection();

      System.out.println("// Specify what portion of file to download.");
      connection.setRequestProperty("Range",
        "bytes=" + downloaded + "-");

      System.out.println("/ Connect to server.");
      connection.connect();
      
      System.out.println(" Make sure response code is in the 200 range.");
      if (connection.getResponseCode() / 100 != 2) {
        error();
        
      }

      // Check for valid content length.
      int contentLength = connection.getContentLength();
      if (contentLength < 1) {
        error();
      }

      /* Set the size for this download if it
         hasn't been already set. */
      if (size == -1) {
        size = contentLength;
        stateChanged();
      }

      // Open file and seek to the end of it.
        System.out.println("trying to create file: "+filePath+fileName+".mp4");
       
        check();
        file = new RandomAccessFile(filePath+fileName+".mp4", "rw");
      file.seek(downloaded);
        System.out.println("Created");

      stream = connection.getInputStream();
      long lastTime=System.currentTimeMillis();
     int currenD=downloaded;
      while (status == DOWNLOADING) {
          if(System.currentTimeMillis()-lastTime>=1000){
              lastTime=System.currentTimeMillis();
              speed=downloaded-currenD;
              currenD=downloaded;
          }
          
        /* Size buffer according to how much of the
           file is left to download. */
        byte buffer[];
        if (size - downloaded > MAX_BUFFER_SIZE) {
          buffer = new byte[MAX_BUFFER_SIZE];
        } else {
          buffer = new byte[size - downloaded];
        }

        // Read from server into buffer.
        int read = stream.read(buffer);
        if (read == -1)
          break;

        // Write buffer to file.
        file.write(buffer, 0, read);
        downloaded += read;
        stateChanged();
      }

      /* Change status to complete if this point was
         reached because downloading has finished. */
      if (status == DOWNLOADING) {
        status = COMPLETE;
        stateChanged();
      }
    } catch (Exception e) {
      error();
      errorText=errorText+"\n"+e.toString();
    } finally {
      // Close file.
      if (file != null) {
        try {
          file.close();
        } catch (Exception e) {
            errorText=errorText+"\n"+e.toString();
        }
      }

      // Close connection to server.
      if (stream != null) {
        try {
          stream.close();
        } catch (Exception e) {
            errorText=errorText+"\n"+e.toString();
            System.out.println(e);
        }
      }
    }
  }

  // Notify observers that this download's status has changed.
  private void stateChanged() {
    setChanged();
    notifyObservers();
  }
}
