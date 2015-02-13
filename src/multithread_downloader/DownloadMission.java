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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import zereb.utils.download.Download;

/**
 *
 * @author qqqq
 */
public class DownloadMission extends WebPanel implements Runnable, Constants{
    
    Download download;
    WebLabel wl;
    WebProgressBar progressBar1;
    Thread t;
    WebLabel status;WebLabel speed;WebLabel ammount;
    public DownloadMission(Download download){
        this.download=download;
        setUndecorated(false);
        setPreferredHeight(52);
        setLayout(new BorderLayout());
        setPaintSides ( true, false, true, false);
        setMargin(0, 4, 0, 4);
        
        progressBar1 = new WebProgressBar (0, 100);
        progressBar1.setValue(0);
        progressBar1.setIndeterminate(false);
        progressBar1.setStringPainted (true);
        
        JPanel w1=new JPanel();
        JPanel w2=new JPanel();
        w2.setLayout(new GridLayout(2,1));
        w2.setPreferredSize(new Dimension(300, 38));
        WebPanel progresss=new WebPanel();
        WebPanel underProgresss=new WebPanel();
        underProgresss.setLayout(new GridLayout(1,3));
        JPanel w4=new JPanel();
        w4.setLayout(new GridLayout(2,1));
        w4.setPreferredSize(new Dimension(200, 38));
        w1.setPreferredSize(new Dimension(130, 38));
        
        WebLabel name=new WebLabel(download.getTitle());
        name.setBoldFont();
        name.setFontSize(10);
        WebLabel path=new WebLabel(download.getPath());
        path.setFontSize(10);
        TooltipManager.setTooltip ( name, download.getTitle(), TooltipWay.trailing, 0 );
        TooltipManager.setTooltip ( path, download.getPath(), TooltipWay.trailing, 0 );
      
        status = new WebLabel(Download.STATUSES[download.getStatus()]);
        speed = new WebLabel("");
        ammount = new WebLabel("");
        status.setDrawShade(true);
        status.setShadeColor(new Color(COLORS[download.getStatus()]));
        status.setFontSize(10);
        TooltipManager.setTooltip ( status, download.errorText, TooltipWay.trailing, 0 );
        speed.setFontSize(10);
        ammount.setFontSize(10);
        
        
        
        WebButton stopB = new WebButton ("Stop");
        WebButton pauseB = new WebButton ("Pause");
        WebButton resumeB = new WebButton ("Start");
        WebButton openB = new WebButton ("Open");
        stopB.setFontSize(10);
        pauseB.setFontSize(10);
        resumeB.setFontSize(10);
        openB.setFontSize(10);
        stopB.setPreferredWidth(55);
        pauseB.setPreferredWidth(55);
        openB.setPreferredWidth(55);
        resumeB.setPreferredWidth(55);

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
        openB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(download.pubFile);
                } catch (IOException ex) {
                    
                }
            }
        });

        WebButtonGroup buttonGroup1 = new WebButtonGroup ( stopB, pauseB);
        WebButtonGroup buttonGroup2 = new WebButtonGroup ( resumeB, openB);
        WebButtonGroup buttonGroup = new WebButtonGroup ( WebButtonGroup.VERTICAL, true, buttonGroup1, buttonGroup2);
        
        WebOverlay quality = new WebOverlay ();
        quality.setComponent(w1);
        WebLabel overlay = new WebLabel(QUALITY_STRINGS[1]);
        overlay.setDrawShade(true);
        overlay.setShadeColor(new Color(COLORS[1]));
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
        int err=1;
        while (true) {            
            status.setText(Download.STATUSES[download.getStatus()]);
            status.setShadeColor(new Color(COLORS[download.getStatus()]));
            speed.setText(download.formatFileSize(download.getSpeed())+"/s");
            ammount.setText(download.formatFileSize(download.getDownloaded())+" / "+download.formatFileSize(download.getSize()));
            progressBar1.setValue((int) download.getProgress());  
            if(download.getStatus()==Download.ERROR){
                if(err==1)
                    TooltipManager.setTooltip ( status, download.errorText, TooltipWay.trailing, 0 );
                err=-1;
            }
            
            
            try {
                t.sleep(250);
            } catch (InterruptedException ex) {
            }
           
        }
        
        
    }


}
