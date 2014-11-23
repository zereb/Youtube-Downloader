/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithread_downloader;

import com.alee.global.StyleConstants;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author qqqq
 */
public class DownloadMission extends WebPanel implements Runnable{
    
    Download download;
    WebLabel wl;
    WebProgressBar progressBar1;
    Thread t;
    
    public DownloadMission(Download download){
        this.download=download;
        setUndecorated(false);
        setLayout(new GridLayout(1, 4));
        setPaintSides ( false, false, true, false );
        setPreferredSize(new Dimension(600, 70));
        setMargin(2);
        
        progressBar1 = new WebProgressBar ( 0, 100 );
        progressBar1.setValue ( 0 );
        progressBar1.setIndeterminate ( false );
        progressBar1.setStringPainted ( true );
        
        WebPanel w1=new WebPanel();
        WebPanel w2=new WebPanel();
        w2.setLayout(new GridLayout(2,1));
        WebPanel progresss=new WebPanel();
        WebPanel underProgresss=new WebPanel();
        WebPanel w4=new WebPanel();
        
        w1.setUndecorated(false);
        w2.setUndecorated(false);
        progresss.setUndecorated(false);
        underProgresss.setUndecorated(false);
        w4.setUndecorated(false);
        
       
        
//        wl = new WebLabel(Download.STATUSES[download.getStatus()]);
        WebLabel name=new WebLabel(download.)
        
        
        add(w4);
       
        add(w2);
        add(w1);
        w4.add(wl);
         w2.add(progresss);
        w2.add(underProgresss);
        progresss.add(progressBar1);
        
        
        
        
        t = new Thread(this);
        t.start();
        
         
      
    }

    public void run() {
        while (true) {            
            wl.setText(Download.STATUSES[download.getStatus()]);
            progressBar1.setValue((int) download.getProgress());  
            try {
                t.sleep(250);
            } catch (InterruptedException ex) {
            }
           
        }
        
        
    }


}
