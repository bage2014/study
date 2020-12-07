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
import com.w3ma.m3u8parser.util.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Emanuele on 31/08/2016.
 */
public class ExtInfoParser {

    private final String DURATION_REGEXP = Constants.EXT_INF_TAG + ":(.*?) ";
    private final String TVG_ID_REGEXP = Constants.TVG_ID_TAG + "=\"(.*?)\"";
    private final String TVG_NAME_REGEXP = Constants.TVG_NAME_TAG + "=\"(.*?)\"";
    private final String TVG_LOGO_URL_REGEXP = Constants.TVG_LOGO_TAG + "=\"(.*?)\"";
    private final String GROUP_TITLE_REGEXP = Constants.GROUP_TITLE_TAG + "=\"(.*?)\"";
    private final String TITLE_REGEXP = "\",";
    private final Pattern durationPattern;
    private final Pattern tvgIdPattern;
    private final Pattern tvgNamePattern;
    private final Pattern tvgLogoUrlPattern;
    private final Pattern groupTitlePattern;
    private final Pattern titlePattern;

    public ExtInfoParser() {
        durationPattern = Pattern.compile(DURATION_REGEXP);
        tvgIdPattern = Pattern.compile(TVG_ID_REGEXP);
        tvgNamePattern = Pattern.compile(TVG_NAME_REGEXP);
        tvgLogoUrlPattern = Pattern.compile(TVG_LOGO_URL_REGEXP);
        groupTitlePattern = Pattern.compile(GROUP_TITLE_REGEXP);
        titlePattern = Pattern.compile(TITLE_REGEXP);
    }

    public ExtInfo parse(final String line) {
        final ExtInfo extInfo = new ExtInfo();
        extInfo.setDuration(getInsideString(durationPattern, line));
        extInfo.setTvgId(getInsideString(tvgIdPattern, line));
        extInfo.setTvgName(getInsideString(tvgNamePattern, line));
        extInfo.setTvgLogoUrl(getInsideString(tvgLogoUrlPattern, line));
        extInfo.setGroupTitle(getInsideString(groupTitlePattern, line));
        extInfo.setTitle(getNextToString(titlePattern, line));
        return extInfo;
    }

    /**
     * Get the string wrapped inside the pattern reg exp
     *
     * @param pattern
     * @param line
     * @return
     */
    private String getInsideString(final Pattern pattern, final String line) {
        final Matcher matcher = pattern.matcher(line);
        String result = "";
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    /**
     * Get the string next to the pattern reg exp
     *
     * @param pattern
     * @param line
     * @return
     */
    private String getNextToString(final Pattern pattern, final String line) {
        final Matcher matcher = pattern.matcher(line);
        String result = "";
        if (matcher.find()) {
            result = line.substring(matcher.end());
        }
        return result;
    }
}
