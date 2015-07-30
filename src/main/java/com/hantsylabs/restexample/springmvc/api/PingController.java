package com.hantsylabs.restexample.springmvc.api;

import com.hantsylabs.restexample.springmvc.Constants;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Constants.URI_API)
public class PingController {

    @RequestMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping(Principal principal) {

        Map<String, Object> map = new HashMap<>();
        map.put("username", principal.getName());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
    

}
