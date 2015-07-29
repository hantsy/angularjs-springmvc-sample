package com.hantsylabs.restexample.springmvc.api;

import com.hantsylabs.restexample.springmvc.Constants;
import com.hantsylabs.restexample.springmvc.model.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.URI_API)
public class PingController {

    @RequestMapping("/ping")
    public ResponseEntity<ResponseMessage> ping() {
        ResponseMessage msg = ResponseMessage.info("authenticated");
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
