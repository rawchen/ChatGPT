package com.rawchen.chatgpt.controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rawchen.chatgpt.entity.Message;
import com.rawchen.chatgpt.entity.R;
import com.rawchen.chatgpt.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 聊天
 *
 * @author RawChen
 * @date 2022-12-24
 */
@Slf4j
@Controller
public class OpenAiController {

    @Value("${openai.secret-key}")
    private String secretKey;

    @Value("${openai.chat-url}")
    private String chatUrl;

    @Value("${openai.search-url}")
    private String searchUrl;

    @Value("${openai.images-url}")
    private String imagesUrl;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }

    @ResponseBody
    @PostMapping("/api/chat")
    public R chat(String text) {
        Map<String, Object> param = new HashMap<>();
        Message message = new Message().setRole("user").setContent(text);
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);
        param.put("messages", messages);
        param.put("model", "gpt-3.5-turbo");
        param.put("stream", false);
        JSONObject paramJson = new JSONObject(param);
        String body = HttpRequest.post(chatUrl)
                .header("Authorization", "Bearer " + secretKey)
                .body(paramJson.toJSONString())
                .execute()
                .body();
        log.info("Chat Result: {}", body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray choices = jsonObject.getJSONArray("choices");
        if (choices == null || jsonObject.getJSONObject("error") != null) {
            JSONObject error = jsonObject.getJSONObject("error");
            if (error != null) {
                String msg = error.getString("message");
                return new R().setText("Error: " + msg);
            }
        } else {
            JSONObject choiceOne = choices.getJSONObject(0);
            JSONObject messageObject = choiceOne.getJSONObject("message");
            return new R().setText(messageObject.getString("content"));
        }
        return new R().setText("Error: 系统错误！");
    }

    @ResponseBody
    @PostMapping("/api/search")
    public R search(String text, String password) throws IOException {
        if (!FileUtil.getFileContent("password.txt").equals(password)) {
            return new R().setText("Error: " + "认证失败！");
        }
        Map<String, Object> param = new HashMap<>();
        Message message = new Message().setRole("user").setContent(text);
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);
        param.put("messages", messages);
        param.put("stream", false);
        JSONObject paramJson = new JSONObject(param);
        String body = HttpRequest.post(searchUrl)
                .header("Authorization", "Bearer " + secretKey)
                .body(paramJson.toJSONString())
                .execute()
                .body();
        log.info("Search Result: {}", body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray choices = jsonObject.getJSONArray("choices");
        if (choices == null || jsonObject.getJSONObject("error") != null) {
            JSONObject error = jsonObject.getJSONObject("error");
            if (error != null) {
                String msg = error.getString("message");
                return new R().setText("Error: " + msg);
            }
        } else {
            JSONObject choiceOne = choices.getJSONObject(0);
            JSONObject messageObject = choiceOne.getJSONObject("message");
            return new R().setText(messageObject.getString("content"));
        }
        return new R().setText("Error: 系统错误！");
    }

    @ResponseBody
    @PostMapping("/api/test")
    public R search2(String text, String password) throws IOException {
        if (!FileUtil.getFileContent("password.txt").equals(password)) {
            return new R().setText("Error: " + "认证失败！");
        }
        return new R().setText("success: " + text);
    }


}
