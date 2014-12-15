/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithread_downloader;

import java.util.ArrayList;

/**
 *
 * @author qqqq
 */
public class QueueManager {
    
    private int maxDownloads=1,
                currentDownloads=0;
    private int[] downloadIds=new int[maxDownloads];
    
    
    private ArrayList<Download> downloadList = new ArrayList<Download>();
    public QueueManager(ArrayList<Download> downloadList){
        this.downloadList=downloadList;
        DoManage();
    }
    public void updateManager(ArrayList<Download> downloadList){
        this.downloadList=downloadList;
        DoManage();
    }
    
    private void DoManage(){
        currentDownloads=0;
        
        for (Download downloadList1 : downloadList) {
            if(downloadList1.getStatus()==Download.DOWNLOADING){
                currentDownloads++;
            }
//            Log.putDebug("currentDownloads="+currentDownloads+" max="+maxDownloads);
            if (downloadList1.getStatus() == Download.QUEUE) {
                if(currentDownloads<maxDownloads){
                    downloadIds[downloadIds.length-1]=downloadList1.id;
                    downloadList1.resume();
                    currentDownloads++;
                }
            }else{
                if(downloadList1.getStatus()!=Download.DOWNLOADING){
                    currentDownloads--;
                    for(int i=0; i<downloadIds.length; i++){
                        if(downloadIds[i]==downloadList1.id){
                            downloadIds[i]=0;
                        }
                    }
                }
            }
            
        }
       }
    
    public int getCurrentDownloads(){
        return  currentDownloads;
    }
        
    }
    
    
    

