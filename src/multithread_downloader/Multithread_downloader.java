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
import com.alee.extended.layout.WrapFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.statusbar.WebMemoryBar;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.statusbar.WebStatusLabel;
import com.alee.extended.window.WebPopOver;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;


    


public class Multithread_downloader implements Observer {
   
   
    public int WIDTH = 640;
    public int HEIGHT = 480,
               FIRST_P=0,
               LAST_P=1;
    
   
     JFrame frame;
     final WebFileChooserField saveFileField;
     WebTextField urlTextField;
     WebPanel panelMissions;
     WebComboBox comboBox;
     public int totalSpeed=0;
     private ArrayList<Download> downloadList = new ArrayList<Download>();
    
   public Multithread_downloader(){
       
       for (Download downloadList1 : downloadList) {
           if(downloadList1.getStatus() ==  0 ){
              totalSpeed=totalSpeed+downloadList1.getSpeed();
            }
       }
        frame = new JFrame("YouTube downloader");
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        WebStatusBar statusBar = new WebStatusBar ();

        // Simple label
        statusBar.add ( new WebStatusLabel ( "Hello" ) );
        
        WebLabel totalSpeedL=new WebLabel("Total speed:"+totalSpeed+"K/bs");

        // Simple memory bar
        WebMemoryBar memoryBar = new WebMemoryBar ();
        memoryBar.setPreferredWidth ( memoryBar.getPreferredSize ().width + 20 );
        statusBar.add(totalSpeedL, ToolbarLayout.END);
        statusBar.add(memoryBar, ToolbarLayout.END);
        
        WebMenuItem info;
       WebMenuBar menuBar = new WebMenuBar();
       menuBar.setUndecorated ( true );
       menuBar.add(new WebMenu("Recent") {
           {
               for (int i = 0; i < 5; i++) {
                   add(new WebMenuItem("Recent " + i));
               }
           }
       });
       menuBar.add(new WebMenu("Info") {
           {
               add(new WebMenuItem("Help"));
               add(new WebMenuItem("Info"));
               {
                   addActionListener(new ActionListener() {
                       public void actionPerformed(ActionEvent e) {
                           System.out.println("111");
                           final WebPopOver popOver = new WebPopOver(frame);
                           popOver.setCloseOnFocusLoss(true);
                           popOver.setMargin(2);
                           popOver.setLayout(new VerticalFlowLayout());;
                           final WebLabel titleLabel = new WebLabel("Pop-over dialog", WebLabel.CENTER);
                           popOver.add(new GroupPanel(GroupingType.fillMiddle, 4,  titleLabel).setMargin(0, 0, 10, 0));
                           popOver.add(new WebLabel("1. This is a custom detached pop-over dialog"));
                           popOver.add(new WebLabel("2. You can move pop-over by dragging it"));
                           popOver.add(new WebLabel("3. Pop-over will get closed if loses focus"));
                           popOver.add(new WebLabel("4. Custom title added using standard components"));
                           popOver.show(frame);

                       }
                   });
               }
           }
       });
       menuBar.add(new WebMenu("Exit") {
           {
               addActionListener(new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                       System.exit(0);
                   }
               });
           }
       });
        
        
        
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
        String[] items = { "LD", "SD", "HD" };
        
        comboBox = new WebComboBox ( items );
        panel.add(comboBox);
        
        
        
        
     
        panelMissions = new WebPanel();
        panelMissions.setUndecorated (true );
        panelMissions.setLayout ( new VerticalFlowLayout());
        
        final WebScrollPane webScrollPane = new WebScrollPane(panelMissions, false, false);
        
        
        for (int i = 0; i < 9; i++) {
           panelMissions.add(new DownloadMission(new Download(verifyUrl("lol"), "weEEEEEEEEEEEEEEeeEEEeezezezezezeezezeeezezeze", "to long title too long title", i/3)));
           
       
        

        
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.add(menuBar, BorderLayout.NORTH);
        JPanel j=new JPanel();
        j.setLayout(new BorderLayout());
        
        j.add(panel, BorderLayout.NORTH);
        j.add(webScrollPane, BorderLayout.CENTER);
        frame.add(j, BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);
        
   
   }
   }
   private void actionAdd(int hd) {
       YtubeParser yp=new YtubeParser(urlTextField.getText());
       addDownload(new Download(verifyUrl(yp.urls[hd]), saveFileField.getSelectedFiles().get(0).getAbsolutePath(), yp.title, hd));
        urlTextField.setText(null);

  }
   
  public void addDownload(Download download) {
    download.addObserver(this);
    downloadList.add(download);
    panelMissions.add(new DownloadMission(download));
    frame.pack();
  }
   

  // Verify download URL.
  private URL verifyUrl(String url) {
    // Only allow HTTP URLs.
    if (false)
      return null;

    // Verify format of URL.
    URL verifiedUrl = null;
    try {
      verifiedUrl = new URL(url);
    } catch (Exception e) {
      return null;
    }

    // Make sure URL specifies a file.
    

    return verifiedUrl;
  }
   
   
   
   public void createDownload(){
       
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
        frame.pack();
    }

}



