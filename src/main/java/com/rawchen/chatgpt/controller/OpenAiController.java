package com.rawchen.chatgpt.controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rawchen.chatgpt.entity.Message;
import com.rawchen.chatgpt.entity.ModelParam;
import com.rawchen.chatgpt.entity.R;
import com.rawchen.chatgpt.util.FileUtil;
import com.rawchen.chatgpt.util.IpUtil;
import com.rawchen.chatgpt.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/images")
    public String images() {
        return "images";
    }

    @ResponseBody
    @PostMapping("/api/chat")
    public R chat(HttpServletRequest request, String text) {
        String ip = IpUtil.getIpAddr(request);
        String provinceCityArea = IpUtil.getProvinceCityArea(ip);
        String userAgent = request.getHeader("User-Agent");
        log.info("");
        log.info("userAgent: {}", userAgent);
        log.info("IP: {}, provinceCityArea: {}, text: {}",ip , provinceCityArea, text);
        Map<String, Object> param = new HashMap<>();
        Message message = new Message().setRole("user").setContent(text);
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);
        param.put("messages", messages);
//        param.put("model", "gpt-3.5-turbo");
        param.put("model", "gpt-4o-mini");
        param.put("stream", false);
        JSONObject paramJson = new JSONObject(param);
        String body = HttpRequest.post(chatUrl)
                .header("Authorization", "Bearer " + secretKey)
                .body(paramJson.toJSONString())
                .execute()
                .body();
        log.info("Chat Result: {}", StringUtil.subLog(body));
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray choices = jsonObject.getJSONArray("choices");
        if (choices == null || jsonObject.getJSONObject("error") != null) {
            JSONObject error = jsonObject.getJSONObject("error");
            if (error != null) {
                String msg = error.getString("message");
                log.error("Chat Result Error: {}", body);
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
    @PostMapping("/api/images")
    public R images(String text, String password) throws IOException {
        if (!FileUtil.getFileContent("password.txt").equals(password)) {
            return new R().setText("Error: " + "认证失败！");
        }
        Map<String, Object> param = new HashMap<>();
        ModelParam modelParam = new ModelParam().setN(1).setSize("512x512");
        param.put("model", "dall-e-2");
        param.put("model_params", modelParam);
        param.put("prompt", text);
        JSONObject paramJson = new JSONObject(param);
        String body = HttpRequest.post(imagesUrl)
                .header("Authorization", "Bearer " + secretKey)
                .body(paramJson.toJSONString())
                .execute()
                .body();
        log.info("Images Result: {}", body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray data = jsonObject.getJSONArray("data");
        if (data == null || jsonObject.getJSONObject("error") != null) {
            JSONObject error = jsonObject.getJSONObject("error");
            if (error != null) {
                String msg = error.getString("message");
                return new R().setText("Error: " + msg);
            }
        } else {
            JSONObject dataOne = data.getJSONObject(0);
            return new R().setText(StringUtil.wrapperImg(dataOne.getString("url")));
        }
        return new R().setText("Error: 系统错误！");
    }

    @ResponseBody
    @PostMapping("/api/test01")
    public R test01(String text) throws IOException {
        return new R().setText(text);
    }

    @ResponseBody
    @PostMapping("/api/test02")
    public R test02(String text, String password) throws IOException {
        if (!FileUtil.getFileContent("password.txt").equals(password)) {
            return new R().setText("Error: " + "认证失败！");
        }
        return new R().setText("success: " + text);
    }

    @ResponseBody
    @PostMapping("/api/test03")
    public R test03(HttpServletRequest request, String text) throws IOException {
        String ip = IpUtil.getIpAddr(request);
        String provinceCityArea = IpUtil.getProvinceCityArea(ip);
        return new R().setText(text + "  " + provinceCityArea);
    }

}
