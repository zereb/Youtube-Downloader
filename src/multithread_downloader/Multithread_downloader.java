/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithread_downloader;


import com.alee.extended.filechooser.WebFileChooserField;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.ToolbarLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


    


public class Multithread_downloader implements Observer, Constants {
   
     WebLabel statusLabel;
     JFrame frame;
     final WebFileChooserField saveFileField;
     WebTextField urlTextField;
     WebPanel panelMissions;
     WebComboBox comboBox;
     public int totalSpeed=0;
     public WebLabel totalSpeedL;
     private ArrayList<Download> downloadList = new ArrayList<Download>();
    
   public Multithread_downloader(){
       for (Download downloadList1 : downloadList) {
           if(downloadList1.getStatus() ==  0 ){
              totalSpeed=totalSpeed+downloadList1.getSpeed();
            }
       }
        frame = new JFrame(PROGRAMM_NAME);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        WebStatusBar statusBar = new WebStatusBar ();
         totalSpeedL = new WebLabel("Total speed:"+totalSpeed+"K/bs");
        WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( memoryBar.getPreferredSize ().width + 20 );
        statusBar.add(totalSpeedL, ToolbarLayout.END);
        statusBar.add(statusLabel, ToolbarLayout.START);
        statusBar.add(memoryBar, ToolbarLayout.END);
        
        WebPanel panel = new WebPanel();
        panel.setUndecorated ( false );
        panel.setLayout ( new HorizontalFlowLayout() );
        panel.setPaintSides ( false, false, true, false );
        panel.setMargin(5);
        
        

        urlTextField = new WebTextField(20);
        urlTextField.setInputPrompt ( "Enter URL..." );
        panel.add(urlTextField);
        
        
        
        saveFileField = new WebFileChooserField ( frame );
        saveFileField.setPreferredWidth ( 300 );
        saveFileField.setMultiSelectionEnabled ( false );
        saveFileField.setShowFileShortName ( false );
        saveFileField.setShowRemoveButton ( false );
        saveFileField.setSelectedFile ( File.listRoots ()[ 0 ] );
        panel.add(saveFileField);
        
        WebButton okButton = new WebButton ( " OK " );
        okButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                        System.out.println("i am trying...");
                        actionAdd(comboBox.getSelectedIndex());
                }
            });
        panel.add(okButton);
       
        
        comboBox = new WebComboBox ( QUALITY_STRINGS );
        panel.add(comboBox);
        
        
        
        
     
        panelMissions = new WebPanel();
        panelMissions.setUndecorated (true );
        panelMissions.setLayout ( new VerticalFlowLayout());
        
        final WebScrollPane webScrollPane = new WebScrollPane(panelMissions, false, false);
        
        
       
       
        

        
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        JPanel j=new JPanel();
        j.setLayout(new BorderLayout());
        j.add(panel, BorderLayout.NORTH);
        j.add(webScrollPane, BorderLayout.CENTER);
        frame.add(j, BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);
   
   }
   private void actionAdd(int quality) {
       YtubeParser yp=new YtubeParser(urlTextField.getText());
       addDownload(new Download(verifyUrl(yp.urls[quality]), saveFileField.getSelectedFiles().get(0).getAbsolutePath(), yp.title, quality));
        urlTextField.setText(null);
  }
   
  public void addDownload(Download download) {
    download.addObserver(this);
    downloadList.add(download);
    panelMissions.add(new DownloadMission(download));
    frame.pack();
  }
   
  public void createFrames(){
      
  }

  private URL verifyUrl(String url) {
    URL verifiedUrl = null;
    try {
      verifiedUrl = new URL(url);
    } catch (Exception e) {
      return null;
    }
    return verifiedUrl;
  }
   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WebLookAndFeel.install();
                WebLookAndFeel.initializeManagers();
                new Multithread_downloader();
            }
        });
    }

    public void update(Observable o, Object arg) {
        totalSpeed=0;
        for (Download downloadList1 : downloadList) {
            if (downloadList1.getStatus() == 0) {
                totalSpeed = totalSpeed + downloadList1.getSpeed();
            }
        }
        totalSpeedL.setText("Total speed:"+totalSpeed+"K/bs");
        
        
        frame.pack();
    }

}



