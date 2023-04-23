package com.Task.ChatGPT;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class Controller {
    @Autowired
    Service service;
//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
    @GetMapping("/getmessage")
    public String getMessage(Content content) throws IOException, JSONException {
        String ans = "";

        try {
            ans = service.getMessage(content);
        } catch (Exception e) {
            return e.getMessage();
        }
        return ans;
    }
}
