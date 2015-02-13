package multithread_downloader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zereb.utils.Log;
import zereb.utils.download.IWannaKnowSize;

/**
 *
 * @author qqqq
 */
public class YtubeParser implements Runnable{
    
    
    
    public static final ArrayList<String> BAD_KEYS = new ArrayList<String>();
    public static final HashMap<Integer, String> FILE_TYPES = new HashMap<Integer, String>();
    public HashMap<String, String> FILE_SIZES = new HashMap<String, String>();

	static {
		BAD_KEYS.add("stereo3d");
		BAD_KEYS.add("type");
		BAD_KEYS.add("fallback_host");
		BAD_KEYS.add("quality");

		
        FILE_TYPES.put(36, "3GP_240p");
        FILE_TYPES.put(13, "3GP_144p");
        FILE_TYPES.put(17, "3GP_144p");
        FILE_TYPES.put(5, "FLV_240p");
        FILE_TYPES.put(6, "FLV_270p");
        FILE_TYPES.put(34, "FLV_360p");
        FILE_TYPES.put(35, "FLV_480p");
        FILE_TYPES.put(18, "MP4_(Max 480p)");
        FILE_TYPES.put(22, "MP4_720p");
        FILE_TYPES.put(37, "MP4_1080p");
        FILE_TYPES.put(160, "MP4_144p (Video Only)");
        FILE_TYPES.put(133, "MP4_240p (Video Only)");
        FILE_TYPES.put(134, "MP4_360p (Video Only)");
        FILE_TYPES.put(135, "MP4_480p (Video Only)");
        FILE_TYPES.put(136, "MP4_720p (Video Only)");
        FILE_TYPES.put(137, "MP4_1080p (Video Only)");
        FILE_TYPES.put(139, "M4A_48Kbps (Video Only)");
        FILE_TYPES.put(140, "M4A_128kbps (Audio Only)");
        FILE_TYPES.put(141, "M4A_256kbps (Audio Only)");
        FILE_TYPES.put(38, "MP4");
        FILE_TYPES.put(83, "MP4_3D 240p");
        FILE_TYPES.put(82, "MP4_3D 360p");
        FILE_TYPES.put(85, "MP4_3D 520p");
        FILE_TYPES.put(84, "MP4_3D 720p");
        FILE_TYPES.put(43, "WEBM_360p");
        FILE_TYPES.put(44, "WEBM_480p");
        FILE_TYPES.put(45, "WEBM_720p");
        FILE_TYPES.put(46, "WEBM_1080p");
        FILE_TYPES.put(100, "WEBM_3D 360p");
        FILE_TYPES.put(101, "WEBM_3D 360p");
        FILE_TYPES.put(102, "WEBM_3D 720p");
	}

    public String vID = null;
    public String video_info = null;
    public String video_gdata = null;
    public String title = null;
    public String UTF_video_info = null;
    public String[] urls = null;
    public String[] sizes = null;
    public String url = null;

    public boolean gotem = false;

    public YtubeParser(String url) throws Exception{
        vID = getVID(url);        
        new Thread(this).start();
                
        }
    

    public String getVID(String url) throws Exception {
        Log.putInfo("trying get video id from: " + url);
        String vId = null;
        Pattern regex = Pattern.compile("http://(?:www\\.)?youtu(?:\\.be/|be\\.com/(?:watch\\?v=|v/|embed/|user/(?:[\\w#]+/)+))([^&#?\n]+)");
        Matcher regexMatcher = regex.matcher(url);
        if (regexMatcher.find()) {
            vId = regexMatcher.group(1);
        }else{
            throw new Exception("Cant get id");
        }
        return vId;
    }

    public String get_videoInfo(String video_id) {
        Log.putInfo("trying to get video_info");
        try {
            return new GetUrlData().getUrlContents("http://www.youtube.com/get_video_info?video_id=" + video_id);
        } catch (IOException ex) {
            Log.putError(ex.toString());
        }
        return null;
    }

    public String get_videoGdata(String video_id) {
        Log.putInfo("trying to get Gdata");
        try {
            return new GetUrlData().getUrlContents("http://gdata.youtube.com/feeds/api/videos/" + video_id);
        } catch (IOException ex) {
            Log.putError(ex.toString());
        }
        return null;
    }

    public String getVideoTitle(String video_id) {
        Log.putInfo("trying to get video title...");
        int bi = video_id.indexOf("<title type='text'>");
        int bl = video_id.indexOf("</title>");
        String title = video_id.substring(bi + "<title type='text'>".length(), bl);
        Log.putInfo("got'em: " + title);
        return title;
    }

    public void run() {
        
        this.url = url;

        video_info = get_videoInfo(vID);
        video_gdata = get_videoGdata(vID);
        if (video_gdata != null && video_info != null) {
            Log.putInfo("got video Gdata: " + video_gdata);
            Log.putInfo("got video info: " + video_info);

            title = getVideoTitle(video_gdata);

            Log.putInfo("trying to encode html to UTF8...");
            UTF_video_info = Html2Utf.changeHTMLtoUTF8(video_info);
            Log.putInfo("Got'em: " + UTF_video_info);
            Log.putInfo("trying fucking get this shitty urls/...");

            urls = getFLVFormatUrls(getURLs(video_info));
            int i = 0;
            for (String url1 : urls) {
                
                    urls[i] = remove(url1, "&title=", "_end_");
                    FILE_SIZES.put( urls[i], new IWannaKnowSize( urls[i], FILE_TYPES.get(getTag(urls[i]))).formatedSize);
                    i++;
                    
                    
                    
                }
            
          
                gotem = true;
            }
        }

    public String[] getURLs(String vinf) {

        LinkedList<String> url1s = new LinkedList<String>();
        for (String splited : vinf.split("&")) {
            Log.putDebug(splited);
            String key = splited.substring(0, splited.indexOf("="));
            String value = splited.substring(splited.indexOf('=') + 1);
            if (key.equals("url_encoded_fmt_stream_map")) {
                value = decode(value);
                for (String u : value.split("url=")) {
                    u = getCorrectURL(decode(u));
                    if (!u.startsWith("http") && !isValid(u)) {
                        continue;
                    }
                    url1s.add(u);
                }
                
            } 
        }

        String[] url_map = url1s.toArray(new String[url1s.size()]);
        for (int i = 0; i < url_map.length; i++) {
            url_map[i] += url_map[i].endsWith("&") ? "title=" + title : "&title=" + title;
        }
        return url_map;
    }

    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    private static boolean isValid(String url) {
        return url.contains("signature=") && url.contains("factor=");
    }

    private static String getCorrectURL(String input) {
        StringBuilder builder = new StringBuilder(input.substring(0, input.indexOf('?') + 1));
        String[] params = input.substring(input.indexOf('?') + 1).split("&");
        LinkedList<String> keys = new LinkedList<String>();
        boolean first = true;
        for (String param : params) {
            String key = param;
            try {
                key = param.substring(0, param.indexOf('='));
            } catch (Exception ex) {
                System.out.println(param);
            }
            if (keys.contains(key) || BAD_KEYS.contains(key)) {
                continue;
            }
            keys.add(key);
            if (key.equals("sig")) {
                builder.append(first ? "" : "&").append("signature=").append(param.substring(4));
            } else {
                if (param.contains(",quality=")) {
                    param = remove(param, ",quality=", "_end_");
                }
                if (param.contains(",type=")) {
                    param = remove(param, ",type=", "_end_");
                }
                if (param.contains(",fallback_host")) {
                    param = remove(param, ",fallback_host", ".com");
                }
                builder.append(first ? "" : "&").append(param);
            }
            if (first) {
                first = false;
            }
        }
        return builder.toString();
    }

    private static String remove(String text, String start, String end) {
        int l = text.indexOf(start);
        return text.replace(text.substring(l, end.equals("_end_") ? text.length() : text.indexOf(end, l)), "");
    }

    public static String[] getFLVFormatUrls(String[] urls) {
        if (urls == null) {
            return null;
        }
        ArrayList<String> u = new ArrayList<String>();
        for (String url : urls) {
            int tag = getTag(url);
            
            for (Integer key : FILE_TYPES.keySet()) {
                 if (tag == key) {
                u.add(url);
            }
            }
           
        }
        Collections.sort(u, new Comparator<String>() {//my first attempt at actually using this class lolol
            public int compare(String url, String url1) {
                int tag = getTag(url), tag1 = getTag(url1);
                if (tag < tag1) {
                    return 1;
                }
                if (tag > tag1) {
                    return -1;
                }
                return 0;
            }
        });
        return u.toArray(new String[u.size()]);
    }

    public static int getTag(String url) {
        int i;
        return Integer.parseInt(url.substring(i = url.indexOf("itag=") + 5, url.indexOf('&', i)));
    }

}
