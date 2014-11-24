/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithread_downloader;

import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.extended.panel.WebOverlay;
import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author qqqq
 */
public class DownloadMission extends WebPanel implements Runnable{
    String[] items = { "LD", "SD", "HD" };
    
    Download download;
    WebLabel wl;
    WebProgressBar progressBar1;
    Thread t;
      WebLabel status;WebLabel speed;WebLabel ammount;
    public DownloadMission(Download download){
        this.download=download;
        setUndecorated(false);
        setLayout(new BorderLayout());
        
        setPaintSides ( true, false, true, true );
        setPreferredSize(new Dimension(600, 60));
        
        
        progressBar1 = new WebProgressBar ( 0, 100 );
        progressBar1.setValue ( 0 );
        progressBar1.setIndeterminate ( false );
        progressBar1.setStringPainted ( true );
        
        JPanel w1=new JPanel();
        JPanel w2=new JPanel();
        w2.setLayout(new GridLayout(2,1));
        w2.setPreferredSize(new Dimension(300, 58));
        WebPanel progresss=new WebPanel();
        WebPanel underProgresss=new WebPanel();
        underProgresss.setLayout(new GridLayout(1,3));
        JPanel w4=new JPanel();
        w4.setLayout(new GridLayout(2,1));
        w4.setPreferredSize(new Dimension(200, 58));
        w1.setPreferredSize(new Dimension(130, 58));
        
            
//        w1.setUndecorated(false);
//        w2.setUndecorated(false);
//        progresss.setUndecorated(false);
//        underProgresss.setUndecorated(false);
//        w4.setUndecorated(false);
        
       
        
//        wl = new WebLabel(Download.STATUSES[download.getStatus()]);
        WebLabel name=new WebLabel(download.getTitle());
        WebLabel path=new WebLabel(download.getPath());
        TooltipManager.setTooltip ( name, download.getTitle(), TooltipWay.trailing, 0 );
        TooltipManager.setTooltip ( path, download.getPath(), TooltipWay.trailing, 0 );
      
        status = new WebLabel(Download.STATUSES[download.getStatus()]);
        speed = new WebLabel(download.getSpeed());
        ammount = new WebLabel(download.getSize()+"/"+download.getDownloaded());
        status.setDrawShade(true);
        status.setShadeColor(new Color(Download.COLORS[download.getStatus()]));
        status.setFontSize(10);
        TooltipManager.setTooltip ( status, download.errorText, TooltipWay.trailing, 0 );
        speed.setFontSize(10);
        ammount.setFontSize(10);
        
        
        
        WebButton stopB = new WebButton ("Stop");
        WebButton pauseB = new WebButton ("Pause");
        WebButton resumeB = new WebButton ("Start");
        WebButton openB = new WebButton ("Open");
        stopB.setFontSize(11);
        pauseB.setFontSize(11);
        resumeB.setFontSize(11);
        openB.setFontSize(11);
        stopB.setPreferredWidth(60);
        pauseB.setPreferredWidth(60);
        openB.setPreferredWidth(60);
        resumeB.setPreferredWidth(60);

        stopB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                download.cancel();
            }
        });
        pauseB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                download.pause();
            }
        });
        resumeB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                download.resume();
            }
        });
        stopB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                download.cancel();
            }
        });

        WebButtonGroup buttonGroup1 = new WebButtonGroup ( stopB, pauseB);
        WebButtonGroup buttonGroup2 = new WebButtonGroup ( resumeB, openB);
        WebButtonGroup buttonGroup = new WebButtonGroup ( WebButtonGroup.VERTICAL, true, buttonGroup1, buttonGroup2);
        
        WebOverlay quality = new WebOverlay ();
        quality.setComponent(w1);
        WebLabel overlay = new WebLabel(items[download.quality]);
        overlay.setDrawShade(true);
        overlay.setShadeColor(new Color(Download.COLORS[download.quality]));
        quality.addOverlay ( overlay, SwingConstants.TRAILING, SwingConstants.TOP );
        quality.setComponentMargin ( 0, 5, 0, -10 );
        
        
        add(w4, BorderLayout.WEST);
       
        add(w2, BorderLayout.CENTER);
        add(quality, BorderLayout.EAST);
        w4.add(name);
        w4.add(path);
         w2.add(progresss);
        w2.add(underProgresss);
        progresss.add(progressBar1);
        underProgresss.add(status);
        underProgresss.add(speed);
        underProgresss.add(ammount);
        w1.add(buttonGroup);
       
        
        t = new Thread(this);
        t.start();
        
         
      
    }

    public void run() {
        while (true) {            
            status.setText(Download.STATUSES[download.getStatus()]);
            status.setShadeColor(new Color(Download.COLORS[download.getStatus()]));
            speed.setText(download.getSpeed()+"K/bs");
            ammount.setText(download.getDownloaded()/1024+"/"+download.getSize()/1024+"Mb");
            progressBar1.setValue((int) download.getProgress());  
            try {
                t.sleep(250);
            } catch (InterruptedException ex) {
            }
           
        }
        
        
    }


}
