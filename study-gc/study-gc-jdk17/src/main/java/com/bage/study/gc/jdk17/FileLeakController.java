package com.bage.study.gc.jdk17;

import com.bage.study.gc.biz.leak.file.LeakingFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/file")
@RestController
@Slf4j
public class FileLeakController {

    LeakingFileService service = new LeakingFileService();

    @RequestMapping("/leaking")
    public Object leaking(@RequestParam(value = "fileName") String fileName,
                          @RequestParam(value = "close", required = false, defaultValue = "true") Boolean close) {
        service.leak(fileName, close);
        return ";;" + close;
    }


}
