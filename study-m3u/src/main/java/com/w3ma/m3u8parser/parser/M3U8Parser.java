/*
 * Copyright 2016 Emanuele Papa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.w3ma.m3u8parser.parser;

import com.w3ma.m3u8parser.data.ExtInfo;
import com.w3ma.m3u8parser.data.Playlist;
import com.w3ma.m3u8parser.data.Track;
import com.w3ma.m3u8parser.exception.PlaylistParseException;
import com.w3ma.m3u8parser.scanner.M3U8ItemScanner;
import com.w3ma.m3u8parser.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This parser is based on http://ss-iptv.com/en/users/documents/m3u
 * It doesn't take care of all the attributes, it just parsers a playlist like
 * #EXTM3U
 * #EXTINF:0 tvg-id="1" tvg-name="Name1" tvg-logo="http://mylogos.domain/name1logo.png" group-title="Group1", Name1
 * http://server.name/stream/to/video1
 * #EXTINF:0 tvg-id="2" tvg-name="Name2" tvg-logo="http://mylogos.domain/name2logo.png" group-title="Group1", Name2
 * http://server.name/stream/to/video2
 * #EXTINF:0, tvg-id="3" tvg-name="Name3" tvg-logo="http://mylogos.domain/name3logo.png" group-title="Group2", Name3
 * http://server.name/stream/to/video3
 * Created by Emanuele on 31/08/2016.
 */
public class M3U8Parser {

    protected final M3U8ItemScanner.Encoding encoding;
    private final M3U8ItemScanner m3U8ItemScanner;


    public M3U8Parser(final InputStream inputStream, final M3U8ItemScanner.Encoding encoding) {
        this.encoding = encoding;
        this.m3U8ItemScanner = new M3U8ItemScanner(inputStream, encoding);
    }

    public Playlist parse() throws IOException, ParseException, PlaylistParseException {

        final Playlist playlist = new Playlist();
        final ExtInfoParser extInfoParser = new ExtInfoParser();
        Track track;
        ExtInfo extInfo;
        final List<Track> trackList = new LinkedList<>();

        //this is to remove the first #EXTM3U line
        m3U8ItemScanner.next();

        while (m3U8ItemScanner.hasNext()) {
            final String m3UItemString = m3U8ItemScanner.next();

            final String[] m3U8ItemStringArray = m3UItemString.split(Constants.NEW_LINE_CHAR);

            track = new Track();
            extInfo = extInfoParser.parse(getExtInfLine(m3U8ItemStringArray));
            track.setExtInfo(extInfo);
            track.setUrl(getTrackUrl(m3U8ItemStringArray));
            trackList.add(track);
        }

        final Map<String, Set<Track>> trackSetMap = trackList.stream().collect(
                Collectors.groupingBy(t -> t.getExtInfo().getGroupTitle(), Collectors.toSet()));

        playlist.setTrackSetMap(trackSetMap);

        return playlist;

    }

    private String getExtInfLine(final String[] m3uItemStringArray) {
        return m3uItemStringArray[0];
    }

    private String getTrackUrl(final String[] m3uItemStringArray) {
        return m3uItemStringArray[1];
    }

}
