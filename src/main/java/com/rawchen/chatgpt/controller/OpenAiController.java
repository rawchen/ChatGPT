package com.rawchen.chatgpt.controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 聊天
 *
 * @author RawChen
 * @date 2022-12-24
 */
@Controller
public class OpenAiController {

    @Value("${openai.secret-key}")
    private String secretKey;

    @Value("${openai.completions-url}")
    private String completionsUrl;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("text", "123");
        return "index";
    }

    @ResponseBody
    @PostMapping("/api/chat")
    public JSONObject query(String text) {
        Map<String, Object> param = new HashMap<>();
        param.put("prompt", text);
        param.put("max_tokens", 2048);
        param.put("temperature", 0.5);
        param.put("top_p", 1);
        param.put("frequency_penalty", 0);
        param.put("presence_penalty", 0);
        param.put("model", "text-davinci-003");
        JSONObject paramJson = new JSONObject(param);
        String body = HttpRequest.post(completionsUrl)
                .header("Authorization", "Bearer " + secretKey)
                .body(paramJson.toJSONString())
                .execute()
                .body();
//        System.out.println(body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray choices = (JSONArray) jsonObject.get("choices");
        JSONObject choiceOne = (JSONObject) choices.get(0);
        return choiceOne;
    }

}
