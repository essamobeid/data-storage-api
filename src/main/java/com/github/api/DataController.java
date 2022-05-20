package com.github.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data")
public class DataController {

    @GetMapping(value = "/{repository}/{objectID}")
    public Object getObject(@PathVariable("repository") String repository, @PathVariable("objectID") String objectID) {
        Object result = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }
}
