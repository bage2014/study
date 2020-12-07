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

package com.w3ma.m3u8parser.merger;

import com.w3ma.m3u8parser.data.Playlist;
import com.w3ma.m3u8parser.data.Track;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Emanuele on 01/09/2016.
 */
public class PlaylistMerger {

    public Playlist mergePlaylist(final Playlist... playlists) {
        final Playlist playlist = new Playlist();
        final Map<String, Set<Track>> trackSetMap = new HashMap<>();
        for (final Playlist playlist1 : playlists) {
            final Set<Map.Entry<String, Set<Track>>> entries = playlist1.getTrackSetMap().entrySet();
            for (final Map.Entry<String, Set<Track>> entry : entries) {
                final Set<Track> trackSet = trackSetMap.get(entry.getKey());
                if (trackSet != null) {
                    trackSet.addAll(entry.getValue());
                } else {
                    trackSetMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        playlist.setTrackSetMap(trackSetMap);
        return playlist;
    }
}
