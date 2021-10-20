package com.fungisearch.fudriver.common.query;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by marcin on 01.04.16.
 */
@Controller
public class FileController {

    @RequestMapping(value = "/files/error", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getFile() {
        return new FileSystemResource("/files/blackhole.wave");
    }
}
