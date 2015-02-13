/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithread_downloader;

import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.radiobutton.WebRadioButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JFrame;
import static multithread_downloader.Constants.HEIGHT;
import static multithread_downloader.Constants.WIDTH;
import zereb.utils.download.IWannaKnowSize;

/**
 *
 * @author qqqq
 */
public class Choice extends Observable implements Runnable{
    private final JFrame frame;
    public Thread t;
    public YtubeParser yp;
    public WebPanel wp;
    public ArrayList<WebRadioButton> checkBoxses=new ArrayList<WebRadioButton>();
    public int down=-1;
    
    public Choice(YtubeParser yp){
        this.yp=yp;
        frame = new JFrame("New download");
        frame.setMinimumSize(new Dimension(WIDTH-250, HEIGHT-150));
        frame.setPreferredSize(new Dimension(WIDTH-250, HEIGHT-150));
        frame.setLocationRelativeTo(null);
        wp=new WebPanel(new VerticalFlowLayout());
        WebProgressBar prbar = new WebProgressBar ();
        prbar.setIndeterminate ( true );
        prbar.setStringPainted ( true );
        prbar.setString ( "Patsing data. Please wait..." );
        wp.add(prbar);
        t=new Thread(this);
        
   

        
        
        
        
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.add(wp,BorderLayout.CENTER);
    }


    public void run() {
        while (!yp.gotem) {
            try {
                t.sleep(100);
            } catch (InterruptedException ex) {
            }
        }

        if (yp.gotem) {
//            frame.remove(0);
            
            wp.remove(0);
            wp.add(new Label(yp.title));
            WebLabel name = new WebLabel(yp.title);
            name.setBoldFont();
            name.setFontSize(10);
            WebPanel coicePanel=new WebPanel(new VerticalFlowLayout());
            coicePanel.setMargin(15);
            WebPanel buttonPanel=new WebPanel();
            
            for (String url : yp.urls) {
                checkBoxses.add( new WebRadioButton ( YtubeParser.FILE_TYPES.get(YtubeParser.getTag(url))+" "+yp.FILE_SIZES.get(url)));
                coicePanel.add(checkBoxses.get(checkBoxses.size()-1));
//                coicePanel.add(new WebLabel(YtubeParser.FILE_TYPES.get(YtubeParser.getTag(url))));
                
            }
            
            
            
              WebButton cancel = new WebButton ("Cancel");
              cancel.addActionListener((ActionEvent e) -> {
                  frame.dispose();
              });
              WebButton ok = new WebButton ("Ok", (ActionEvent e ) ->{
                  for (WebRadioButton checkBoxse : checkBoxses) {
                      if(checkBoxse.isSelected()){
                          down=checkBoxses.indexOf(checkBoxse);
                          stateChanged();
                          frame.dispose();
                      }
                  }
              });
              
            buttonPanel.setUndecorated(false);
            buttonPanel.setLayout(new HorizontalFlowLayout());
            buttonPanel.setPaintSides(true, false, false, false);
            buttonPanel.setMargin(5);

            buttonPanel.add(cancel);
            buttonPanel.add(ok);

            frame.add(wp,BorderLayout.NORTH);
            frame.add(coicePanel,BorderLayout.CENTER);
            frame.add(buttonPanel,BorderLayout.SOUTH);
            frame.pack();
        }

    }
    private void stateChanged() {
    setChanged();
    notifyObservers();
  }
    
    
}
