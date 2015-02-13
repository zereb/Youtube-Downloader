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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import zereb.utils.Log;
import zereb.utils.download.Download;
import zereb.utils.download.DownloadManager;
import zereb.utils.download.DownloadManagerExeption;


    


public class Multithread_downloader implements Observer, Constants {
   
     JFrame frame;
     final WebFileChooserField saveFileField;
     WebTextField urlTextField;
     WebPanel panelMissions;
     WebComboBox qualityComboBox;
     public int totalSpeed=0;
     public WebLabel totalSpeedL;
     public WebLabel statusLabel;
     Choice choice;
     Thread t;
     int currentid=1;
     public DownloadManager dm;
     YtubeParser yp;
    
   public Multithread_downloader(){
        frame = new JFrame(PROGRAMM_NAME);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        
         try {
             dm=new DownloadManager(1);
         } catch (DownloadManagerExeption ex) {
             Log.putError("Неверное максимальное число загрузок");
         }
        WebStatusBar statusBar = new WebStatusBar ();
        totalSpeedL = new WebLabel("");
        statusLabel = new WebLabel("Hi there!");
        WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( memoryBar.getPreferredSize ().width + 20 );
        statusBar.add(totalSpeedL, ToolbarLayout.END);
        statusBar.add(memoryBar, ToolbarLayout.END);
        statusBar.add(statusLabel, ToolbarLayout.START);
        
        WebPanel upPanel = new WebPanel();
        upPanel.setUndecorated ( false );
        upPanel.setLayout ( new HorizontalFlowLayout() );
        upPanel.setPaintSides ( false, false, true, false );
        upPanel.setMargin(5);
        
        
        //----------------------- up panel 
        urlTextField = new WebTextField(20);
        urlTextField.setInputPrompt ( "Enter URL..." );
        upPanel.add(urlTextField);
        
        saveFileField = new WebFileChooserField ( frame );
        saveFileField.setPreferredWidth ( 300 );
        saveFileField.setMultiSelectionEnabled ( false );
        saveFileField.setShowFileShortName ( false );
        saveFileField.setShowRemoveButton ( false );
        saveFileField.setSelectedFile ( File.listRoots ()[ 0 ] );
        upPanel.add(saveFileField);
        
        WebButton okButton = new WebButton ( " OK " );
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                       Log.putInfo("i am trying...");
                        actionAdd(qualityComboBox.getSelectedIndex());
                }
            });
        upPanel.add(okButton);
        
        qualityComboBox = new WebComboBox (QUALITY_STRINGS);
        upPanel.add(qualityComboBox);
        //----------------------- end up panel

        
        
        //----------------------- panel Downloads
        panelMissions = new WebPanel();
        panelMissions.setUndecorated (true);
        panelMissions.setLayout (new VerticalFlowLayout());
        final WebScrollPane webScrollPane = new WebScrollPane(panelMissions, false, false);
        webScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        //----------------------- panel Downloads

        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        JPanel j=new JPanel();
        j.setLayout(new BorderLayout());
        j.add(upPanel, BorderLayout.NORTH);
        j.add(webScrollPane, BorderLayout.CENTER);
        frame.add(j, BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);
        
   
   }
   private void actionAdd(int quality) {
       try {
           
           yp = new YtubeParser(urlTextField.getText());
           
           choice = new Choice(yp);
           choice.t.start();
           choice.addObserver(this);
           
           
       } catch (Exception e) {
           Log.putError(e.toString());
       }
       
       
  }
   
  public void addDownload(int qq) {
       dm.addDownload(yp.urls[qq],saveFileField.getSelectedFiles().get(0).getAbsolutePath(),yp.title);
       panelMissions.add(new DownloadMission(dm.lastAdds));
       
      
  }
//  public void addDownload(Download download) {
//    download.addObserver(this);
//    panelMissions.add(new DownloadMission(download));
//    frame.pack();
//  }
   
  public void createFrames(){
      
  }

  private URL verifyUrl(String url) {
    urlTextField.setText(url);
      
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
                Log.debugSet=true;
            }
        });
    }

    public void update(Observable o, Object arg) {
        System.out.println("loloqop");
        if(choice.down>-1){
            addDownload(choice.down);
        }
        
        totalSpeed=0;
        for (Download download : dm.downloadList) {
            if (download.getStatus() == 0) {
                totalSpeed = totalSpeed + download.getSpeed();
            }
        }
        totalSpeedL.setText("Total speed:"+dm.formatFileSize(totalSpeed)+"/s");
//       
        frame.pack();
    }

}

  



