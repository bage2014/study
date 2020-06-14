package com.bage;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;

@RestController
public class StreamController {


    @RequestMapping(value = "/stream/m3u8", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> play1(HttpServletResponse response) throws IOException {
        System.out.println("play1 /video/oculus...");
        final HttpHeaders headers = new HttpHeaders();
        response.setHeader("Content-Disposition", "attachment; filename=playlist.m3u8");
        byte[] byts = IOUtils.toByteArray(URI.create("https://eco.streams.ovh:443/BarazaTV/BarazaTV/playlist.m3u8"));
        return new ResponseEntity<ByteArrayResource>(new ByteArrayResource(byts), headers, HttpStatus.OK);
    }

}
