package com.w3ma.m3u8parser;

import com.bage.ChannelUrl;
import com.bage.M3uItem;
import com.google.gson.Gson;
import com.w3ma.m3u8parser.data.ExtInfo;
import com.w3ma.m3u8parser.data.Playlist;
import com.w3ma.m3u8parser.data.Track;
import com.w3ma.m3u8parser.parser.M3U8Parser;
import com.w3ma.m3u8parser.scanner.M3U8ItemScanner;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * "Weather": [{
 * "extInfo": {
 * "duration": "-1",
 * "tvgId": "",
 * "tvgName": "",
 * "tvgLogoUrl": "",
 * "groupTitle": "Weather",
 * "title": "中国气象"
 * },
 * "url": "http://hls.weathertv.cn/tslslive/qCFIfHB/hls/live_sd.m3u8"* 		}],
 * "": [{
 * "extInfo": {
 * "duration": "-1",
 * "tvgId": "",
 * "tvgName": "",
 * "tvgLogoUrl": "",
 * "groupTitle": "",
 * "title": "湖南都市"* 			},
 * "url": "http://hnsd.chinashadt.com:2036/live/stream:hunandushi.stream/playlist.m        "
 * }, ]
 */
public class AppTest {
    public static void main(String[] args) throws Exception {
        // Serialization
        Gson gson = new Gson();
        final InputStream workingPlaylist = new FileInputStream("E:\\Data\\Chrome Download\\cn.m3u");
        final M3U8Parser m3U8Parser = new M3U8Parser(workingPlaylist, M3U8ItemScanner.Encoding.UTF_8);
        try {
            final Playlist playlist = m3U8Parser.parse();
            Map<String, Set<Track>> trackSetMap = playlist.getTrackSetMap();
            Set<Track> tracks = trackSetMap.get("");

            Map<String, M3uItem> groups = new HashMap<>();
            tracks.forEach((track) -> {
                ExtInfo extInfo = track.getExtInfo();
                String key = extInfo.getTitle();
                if (!groups.containsKey(key)) {
                    groups.put(key, new M3uItem(extInfo.getTitle(), extInfo.getTvgLogoUrl(), newArraylist("1", track.getUrl())));
                } else {
                    M3uItem m3uItem = groups.get(key);
                    List<ChannelUrl> channelUrls = m3uItem.getChannelUrls();
                    channelUrls.add(new ChannelUrl("信号源 " + (channelUrls.size() + 1), track.getUrl()));
                }
            });

            System.out.println(gson.toJson(groups));
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(workingPlaylist);
        }


    }

    private static List<ChannelUrl> newArraylist(String append, String url) {
        List<ChannelUrl> items = new ArrayList<>();
        items.add(new ChannelUrl("信号源 " + append, url));
        return items;
    }
}
