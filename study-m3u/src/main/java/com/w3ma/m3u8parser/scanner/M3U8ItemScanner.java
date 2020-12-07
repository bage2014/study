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

package com.w3ma.m3u8parser.scanner;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by Emanuele on 31/08/2016.
 */
public class M3U8ItemScanner {

    private final String EXTINF_TAG_PREFIX = "#EXTINF";
    private final Scanner scanner;

    public M3U8ItemScanner(final InputStream inputStream, final Encoding encoding) {
        scanner = new Scanner(inputStream, encoding.getValue()).useLocale(Locale.US).useDelimiter(EXTINF_TAG_PREFIX);
    }

    public boolean hasNext() {
        return scanner.hasNext();
    }

    public String next() throws ParseException {
        final String line = scanner.next();
        return EXTINF_TAG_PREFIX + line;
    }

    public enum Encoding {
        UTF_8("utf-8");

        private final String value;

        Encoding(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
