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

package com.w3ma.m3u8parser.writer;

import com.w3ma.m3u8parser.data.ExtInfo;
import com.w3ma.m3u8parser.data.Playlist;
import com.w3ma.m3u8parser.data.Track;
import com.w3ma.m3u8parser.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;


/**
 * This class will write a playlist m3u8 file as the one parsed by {@link com.w3ma.m3u8parser.parser.M3U8Parser}
 * Created by Emanuele on 02/09/2016.
 */
public class M3U8PlaylistWriter {

    public ByteArrayOutputStream getByteArrayOutputStream(final Playlist playlist) {

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final Writer outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
        final PrintWriter printWriter = new PrintWriter(outputStreamWriter);

        //write header
        printWriter.println(Constants.EXT_M3U_HEADER);

        //start writing playlist
        final Map<String, Set<Track>> trackSetMap = playlist.getTrackSetMap();
        final SortedSet<String> categorySet = trackSetMap.keySet().stream().collect(Collectors.toCollection(TreeSet::new));
        for (final String category : categorySet) {

            //skip eventually empty category
            if (category.isEmpty()) {
                continue;
            }

            //write all the tracks in the category
            ExtInfo extInfo;
            final SortedSet<Track> trackSet = trackSetMap.get(category).stream().sorted().collect(Collectors.toCollection(TreeSet::new));

            for (final Track track : trackSet) {
                extInfo = track.getExtInfo();
                printWriter.print(Constants.EXT_INF_TAG + ":");
                printWriter.print(extInfo.getDuration());
                printWriter.print(" " + Constants.TVG_ID_TAG + "=" + wrapWithQuotes(extInfo.getTvgId()));
                printWriter.print(" " + Constants.TVG_NAME_TAG + "=" + wrapWithQuotes(extInfo.getTvgName()));
                printWriter.print(" " + Constants.TVG_LOGO_TAG + "=" + wrapWithQuotes(extInfo.getTvgLogoUrl()));
                printWriter.print(" " + Constants.GROUP_TITLE_TAG + "=" + wrapWithQuotes(extInfo.getGroupTitle()));
                printWriter.println("," + extInfo.getTitle());
                printWriter.println(track.getUrl());
            }

        }

        printWriter.flush();

        return byteArrayOutputStream;
    }

    private String wrapWithQuotes(final String string) {
        return "\"" + string + "\"";
    }
}
