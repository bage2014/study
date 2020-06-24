package com.bage;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@RestController
public class StreamController {



    @RequestMapping(value = "/stream/m3u8", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<ByteArrayResource> play1(HttpServletResponse response) throws IOException {
        System.out.println("play1 /video/oculus...");
        final HttpHeaders headers = new HttpHeaders();
        response.setHeader("Content-Disposition", "attachment; filename=playlist.m3u8");
//        byte[] byts = IOUtils.toByteArray(URI.create("http://117.169.120.140:8080/live/cctv-1/.m3u8"));
//        byte[] byts = IOUtils.toByteArray(URI.create("http://111.40.205.87/PLTV/88888888/224/3221225710/index.m3u8"));
        byte[] byts = IOUtils.toByteArray(URI.create("http://125.210.152.10:8060/live/CCTV1HD_H265.m3u8"));
        return new ResponseEntity<ByteArrayResource>(new ByteArrayResource(byts), headers, HttpStatus.OK);
    }

    // http://localhost:8080/stream/1592955997-1-1583711116.hls.ts
    // http://localhost:8080/stream/CCTV1HD_H265_REAL0000000000884975?fileseq=318591294&startoffset=237158804&endoffset=238043908&isSuma=1&hlsSessionID=9782945875227527582&srchost=125.210.152.10:8060
    // http://125.210.152.10:8060/live/CCTV1HD_H265_REAL0000000000884975?fileseq=318591294&startoffset=237158804&endoffset=238043908&isSuma=1&hlsSessionID=9782945875227527582&srchost=125.210.152.10:8060
    @RequestMapping(value = "/stream/{path}", method = RequestMethod.GET, produces = "video/MP2T")
    public ResponseEntity<ByteArrayResource> ts(@PathVariable("path")String path,
                                                @RequestParam("fileseq")String fileseq,
                                                @RequestParam("startoffset")String startoffset,
                                                @RequestParam("endoffset")String endoffset,
                                                @RequestParam("isSuma")String isSuma,
                                                @RequestParam("hlsSessionID")String hlsSessionID,
                                                @RequestParam("srchost")String srchost,
                                                HttpServletResponse response) throws IOException {
        System.out.println("play1 /video/tststs...");
        final HttpHeaders headers = new HttpHeaders();
        // Content-Type: video/MP2T
        response.setHeader("Content-Type", "video/MP2T");

        String append = "?";
        append = append + "fileseq=" + fileseq + "&";
        append = append + "startoffset=" + startoffset + "&";
        append = append + "endoffset=" + endoffset + "&";
        append = append + "isSuma=" + isSuma + "&";
        append = append + "hlsSessionID=" + hlsSessionID + "&";
        append = append + "srchost=" + srchost;

        byte[] byts = IOUtils.toByteArray(URI.create("http://125.210.152.10:8060/live/" + path + "" + append));
        return new ResponseEntity<ByteArrayResource>(new ByteArrayResource(byts), headers, HttpStatus.OK);
    }

}
