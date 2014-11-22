/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithread_downloader;

import com.alee.laf.WebLookAndFeel;
import javax.swing.*;

    


public class Multithread_downloader{
    
    public int WIDTH = 640;
    public int HEIGHT = 480,
               FIRST_P=0,
               LAST_P=1;
    
    
     JFrame frame;
    
   public Multithread_downloader(){
        frame = new JFrame("pages");
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
   
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
}





//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.print.PrinterException;
//import java.awt.print.PrinterJob;
//import java.io.File;
//import java.io.IOException;
//import java.util.Observable;
//import java.util.Observer;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.BorderFactory;
//import javax.swing.Box;
//import javax.swing.BoxLayout;
//import javax.swing. JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
////import org.apache.pdfbox.pdfviewer.PDFPagePanel;
////import org.apache.pdfbox.pdmodel.PDDocument;
////import org.apache.pdfbox.pdmodel.PDPage;
//
///**
// *
// * @author строевая часть
// */
//public class Multithread_downloader implements  Observer{
//    
//    public int WIDTH = 640;
//    public int HEIGHT = 480,
//               FIRST_P=0,
//               LAST_P=1;
//    
//    
//     JFrame frame;
//    private JTextArea resultArea,
//                      infoArea;
//    private String text=" ";
//
//    //private PDFchoose pdf;
////    private PDDocument pdd;
//    public File pdfFile;
////    private PDFPagePanel pdfpp;
//    public Multithread_downloader() {
//        frame = new JFrame("pages");
//        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
//        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
//        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);
//        
//        
//       
//        JLabel firstPJLabel = new JLabel("first page: ");
//        JLabel lastLJLabel = new JLabel("last page: ");
//        JTextField lastTextField = new JTextField(10);
//        JTextField firstTextField = new JTextField(10);
//        JTextField clustTextField = new JTextField(5);
//        JButton clearButton =new JButton("Clear");
//        clearButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                text=" ";
//                resultArea.setText(text);
//            }
//        });
//        JButton okButton = new JButton("OK");
//        okButton.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                int last=0, first=0, clust=40;
//                try {
//                    last = Integer.parseInt(lastTextField.getText());
//                    first = Integer.parseInt(firstTextField.getText());
//                    clust = Integer.parseInt(clustTextField.getText());
//                    clust *= 4;
//                } catch (NumberFormatException nfe) {
//                    popOut("Не нужны буквы");
//                    
//                }
//                int ammountOfP=(last-first)/clust;
//                int lastestPages=(last-first)%clust;
//                if(lastestPages>0)
//                    ammountOfP+=1;
//                int[][] B=new int[ammountOfP][2];
//                for(int i=0; i<ammountOfP;i++){
//                    if (i==0) {
//                        if (ammountOfP>1) {
//                            B[i][FIRST_P]=first;
//                            B[i][LAST_P]=first+clust-1;
//                        } else {
//                            B[i][FIRST_P]=first;
//                            B[i][LAST_P]=first+lastestPages;
//                        }
//                    } else {
//                        if (lastestPages>0 && i==ammountOfP-1) {
//                            B[i][FIRST_P]=first+i*clust;
//                            B[i][LAST_P]=B[i][FIRST_P]+lastestPages;
//                        } else {
//                            B[i][FIRST_P]=first+i*clust-1;
//                            B[i][LAST_P]=B[i][FIRST_P];
//                        }
//                    }
//                }   System.out.println("ammount of p = "+ammountOfP);
//            for(int i=0; i<ammountOfP;i++){
//                System.err.println(B[i][FIRST_P]+" to "+ B[i][LAST_P]);
//                
//                }
//            }
//        });
//        
//        
//        
//        resultArea = new JTextArea();
//        resultArea.setLineWrap(true);
//        resultArea.setWrapStyleWord(true);
//        resultArea.setEditable(false);
//        resultArea.setFont(Font.getFont(Font.DIALOG));
//        JScrollPane resultScrollPane=new JScrollPane(resultArea);
//        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Result: "));
//     
//        JPanel  inputPanel= new JPanel();
//        inputPanel.setLayout(new FlowLayout());
//        inputPanel.add(firstPJLabel);
//        inputPanel.add(firstTextField);
//        inputPanel.add(lastLJLabel);
//        inputPanel.add(lastTextField);
//        inputPanel.add(clustTextField);
//        inputPanel.add(okButton);
//        inputPanel.add(clearButton);
//        
//       
//        
//       
//        
//        
//        frame.setLayout(new BorderLayout());
//        frame.setResizable(false);
//        frame.add(inputPanel, BorderLayout.NORTH);
//        frame.add(resultScrollPane, BorderLayout.CENTER);
//        
//        frame.pack();
//        frame.setVisible(true);
//    }
//    
//     
//        
//    public static void main(String[] args) {
//         SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                setSystemLookAndFill();
//                new Multithread_downloader();
//            }
//        });
//    }
//
//   
//    public void update(Observable o, Object arg) {
//        resultArea.setText(text);
//        System.out.println("updated");
//    }
//    
//     public void popOut(String msg) {
//        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
//
//    }
//     
//     public static void setSystemLookAndFill() {
//        try {
//
//             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
//        }
//    }
//    
//}