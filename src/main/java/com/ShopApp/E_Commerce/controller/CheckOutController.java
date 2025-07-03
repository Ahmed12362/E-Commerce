package com.ShopApp.E_Commerce.controller;

import com.ShopApp.E_Commerce.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class CheckOutController {
        @GetMapping("/success")
        public ResponseEntity<ApiResponse> successCheckOut(){
            return ResponseEntity.ok(new ApiResponse("Success!" , Collections.EMPTY_MAP));
        }

    @GetMapping("/cancel")
    public ResponseEntity<ApiResponse> cancelCheckOut(){
        return ResponseEntity.ok(new ApiResponse("Canceled!" , Collections.EMPTY_MAP));
    }
}
