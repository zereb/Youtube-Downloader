/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithread_downloader;

/**
 *
 * @author qqqq
 */
public interface Constants {
    public static final int WIDTH = 660;
    public static final int HEIGHT = 490;
    
    
    
    
    public static final String PROGRAMM_NAME = "YouTube downloader";
    public static final String[] QUALITY_STRINGS = { "LD", "HD", "SD" };
    public static final String STATUSES[] = {"Downloading", "Paused", "Complete", "Cancelled", "Error"};
    public static final int COLORS[] = {0x0B9424, 0x94880B, 0x0EB7C8, 0xC8720E, 0xC8170E};
    public static final int QUALITY_COLORS[] = {0x0B9424, 0x0EB7C8, 0x94880B};
}
