
package multithread_downloader;

import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.*;


// This class downloads a file from a URL.
class Download extends Observable implements Runnable, Constants {
  // Max size of download buffer.
  private static final int MAX_BUFFER_SIZE = 1024;

  private int renamer=1;
  public int id=0;
  // These are the status names.
  
  

  
  public int quality;

  // These are the status codes.
  public static final int DOWNLOADING = 0;
  public static final int PAUSED = 1;
  public static final int COMPLETE = 2;
  public static final int CANCELLED = 3;
  public static final int ERROR = 4;
  public static final int QUEUE = 5;

  public String errorText=null;
  private URL url; // download URL
  private String filePath;
  private String fileName;
  private int size; // size of download in bytes
  private int downloaded; // number of bytes downloaded
  private int status; // current status of download
  private int speed;
  public File pubFile;
  
  // Constructor for Download.
  public Download(URL url, String filePath, String fileName, int quality, int id) {
    this.quality=quality;
    this.url = url;
    this.fileName=fileName;
    this.filePath = filePath+"/";
    this.id=id;
    size = -1;
    downloaded = 0;
    status = QUEUE;

    // Begin the download.
      if (status!=QUEUE) {
          download();
      }
  }
  
  
  
  public String formatFileSize(long fsize) {
    String hrSize = null;

    double b = fsize;
    double k = fsize/1024.0;
    double m = ((fsize/1024.0)/1024.0);
    double g = (((fsize/1024.0)/1024.0)/1024.0);
    double t = ((((fsize/1024.0)/1024.0)/1024.0)/1024.0);

    DecimalFormat dec = new DecimalFormat("0.00");

    if ( t>1 ) {
        hrSize = dec.format(t).concat(" TB");
    } else if ( g>1 ) {
        hrSize = dec.format(g).concat(" GB");
    } else if ( m>1 ) {
        hrSize = dec.format(m).concat(" MB");
    } else if ( k>1 ) {
        hrSize = dec.format(k).concat(" KB");
    } else {
        hrSize = dec.format(b).concat(" Bytes");
    }

    return hrSize;
}
    public String formatFileSize(int fsize) {
       return formatFileSize((long) fsize);
    }


  // Get this download's URL.
  public String getUrl() {
    return url.toString();
  }

  // Get this download's size.
  public int getSize() {
    return size;
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
      if(status!=CANCELLED){  
        status = PAUSED;
        stateChanged();
    }
    
  }

  // Resume this download.
  public void resume() {
    if(status!=CANCELLED){  
        status = DOWNLOADING;
        stateChanged();
        download();
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
       return speed;
   }
   public int getDownloaded(){
       return  downloaded;
   }
   
   private void check(){
        if (new File(filePath+fileName+".mp4").isFile()) {
            fileName=fileName+"("+renamer+")";
            renamer++;
           Log.putDebug("Fiele already exist. New file name");
            check();
        }   
   }

  // Download file.
  public void run() {
    if(status!=QUEUE){
        RandomAccessFile file = null;
        InputStream stream = null;

        try {
           Log.putInfo(" Open connection to URL quality: "+quality);
          HttpURLConnection connection =
            (HttpURLConnection) url.openConnection();

         Log.putInfo("// Specify what portion of file to download.");
          connection.setRequestProperty("Range",
            "bytes=" + downloaded + "-");

         Log.putInfo("/ Connect to server.");
          connection.connect();

         Log.putInfo(" Make sure response code is in the 200 range.");
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
           Log.putInfo("trying to create file: "+filePath+fileName+".mp4");

            check();
            file = new RandomAccessFile(filePath+fileName+".mp4", "rw");
            pubFile=new File(filePath+fileName+".mp4");
          file.seek(downloaded);
           Log.putInfo("Created");

          stream = connection.getInputStream();
          long lastTime=System.currentTimeMillis();
         int currenD=downloaded;
         while (status == QUEUE){
             Log.putDebug("queue");
         }
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
               Log.putInfo(e.toString());
            }
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
