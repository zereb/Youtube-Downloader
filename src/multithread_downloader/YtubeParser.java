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
public class YtubeParser implements Runnable{

    public String vID = null;
    public String video_info = null;
    public String video_gdata = null;
    public String title = null;
    public String UTF_video_info = null;
    public String[] urls = null;
    public String url = null;
    
    public boolean gotem=false;

    public YtubeParser(String url) {
        this.url=url;
        vID = getVID(url);
        video_info=get_videoInfo(vID);    
        if(video_info!=null)Log.putInfo("got'em: "+video_info);
        video_gdata=get_videoGdata(vID);    
        if(video_gdata!=null)Log.putInfo("got'em: "+video_gdata);
        title=getVideoTitle(video_gdata);
        
       Log.putInfo("trying to encode html to UTF8...");
        UTF_video_info= Html2Utf.changeHTMLtoUTF8(video_info);
       Log.putInfo("Got'em: "+UTF_video_info);
       Log.putInfo("trying fucking get this shitty urls/...");
        
        urls = YouTube.getFLVFormatUrls(YouTube.getURLS(vID));
        for (String url1 : urls) {
           Log.putInfo("got'em " + url1);
        }
        gotem=true;
        new Thread(this).start();
    }

    public String getVID(String url) {
        Log.putInfo("trying get video id from: " + url);
        String vId = null;
        Pattern regex = Pattern.compile("http://(?:www\\.)?youtu(?:\\.be/|be\\.com/(?:watch\\?v=|v/|embed/|user/(?:[\\w#]+/)+))([^&#?\n]+)");
        Matcher regexMatcher = regex.matcher(url);
        if (regexMatcher.find()) {
            vId = regexMatcher.group(1);
        }
       Log.putInfo("got'em: " + vId);
        return vId;
    }

    public String get_videoInfo(String video_id) {
       Log.putInfo("trying to get video_info");
        return new GetUrlData().getUrlContents("http://www.youtube.com/get_video_info?video_id="+video_id);
    }
    
    public String get_videoGdata(String video_id) {
       Log.putInfo("trying to get Gdata");
        return new GetUrlData().getUrlContents("http://gdata.youtube.com/feeds/api/videos/" + video_id);
    }
    
    public String getVideoTitle(String video_id) {
       Log.putInfo("trying to get video title...");
        int bi=video_id.indexOf("<title type='text'>");
        int bl=video_id.indexOf("</title>");
        String title=video_id.substring(bi+"<title type='text'>".length(),bl);
       Log.putInfo("got'em: "+title);
        return title;
    }

    public void run() {
        vID = getVID(url);
        video_info=get_videoInfo(vID);    
        if(video_info!=null)Log.putInfo("got'em: "+video_info);
        video_gdata=get_videoGdata(vID);    
        if(video_gdata!=null)Log.putInfo("got'em: "+video_gdata);
        title=getVideoTitle(video_gdata);
        
       Log.putInfo("trying to encode html to UTF8...");
        UTF_video_info= Html2Utf.changeHTMLtoUTF8(video_info);
       Log.putInfo("Got'em: "+UTF_video_info);
       Log.putInfo("trying fucking get this shitty urls/...");
        
        urls = YouTube.getFLVFormatUrls(YouTube.getURLS(vID));
        for (String url1 : urls) {
           Log.putInfo("got'em " + url1);
        }
        gotem=true;
    }
    
    
    
    
}
