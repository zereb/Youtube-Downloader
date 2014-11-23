/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multithread_downloader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author qqqq
 */
public class YtubeParser {

    public String vID = null;
    public String video_info = null;
    public String video_gdata = null;
    public String title = null;
    public String UTF_video_info = null;
    public String[] urls = null;

    public YtubeParser(String url) {
        vID = getVID(url);
        video_info=get_videoInfo(vID);    
        if(video_info!=null) System.out.println("got'em: "+video_info);
        video_gdata=get_videoGdata(vID);    
        if(video_gdata!=null) System.out.println("got'em: "+video_gdata);
        title=getVideoTitle(video_gdata);
        
        System.out.println("trying to encode html to UTF8...");
        UTF_video_info= Html2Utf.changeHTMLtoUTF8(video_info);
        System.out.println("Got'em: "+UTF_video_info);
        System.out.println("trying fucking get this shitty urls/...");
        
        urls = YouTube.getFLVFormatUrls(YouTube.getURLS(vID));
        for (String url1 : urls) {
            System.out.println("got'em " + url1);
        }
        
        

        
        
    }

    public String getVID(String url) {

        System.out.println("trying get video id from: " + url);
        String vId = null;
        Pattern regex = Pattern.compile("http://(?:www\\.)?youtu(?:\\.be/|be\\.com/(?:watch\\?v=|v/|embed/|user/(?:[\\w#]+/)+))([^&#?\n]+)");
        Matcher regexMatcher = regex.matcher(url);
        if (regexMatcher.find()) {
            vId = regexMatcher.group(1);
        }
        System.out.println("got'em: " + vId);
        return vId;
    }

    public String get_videoInfo(String video_id) {
        System.out.println("trying to get video_info");
        return new GetUrlData().getUrlContents("http://www.youtube.com/get_video_info?video_id="+video_id);
       
    }
    
    public String get_videoGdata(String video_id) {
        System.out.println("trying to get Gdata");
        return new GetUrlData().getUrlContents("http://gdata.youtube.com/feeds/api/videos/" + video_id);
       
    }
    
    public String getVideoTitle(String video_id) {
        System.out.println("trying to get video title...");
        int bi=video_id.indexOf("<title type='text'>");
        int bl=video_id.indexOf("</title>");
        String title=video_id.substring(bi+"<title type='text'>".length(),bl);
        System.out.println("got'em: "+title);
        return title;
        
    }
    
    
    
    
}
