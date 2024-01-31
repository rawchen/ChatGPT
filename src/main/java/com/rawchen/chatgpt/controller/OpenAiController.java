package com.rawchen.chatgpt.controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rawchen.chatgpt.entity.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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

    @Value("${openai.sess-key}")
    private String sessKey;

    @Value("${openai.completions-url}")
    private String completionsUrl;

    @Value("${openai.code-url}")
    private String codeUrl;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("text", "123");
        return "index";
    }

    @ResponseBody
    @PostMapping("/api/chat")
    public JSONObject query(String text) {
        Map<String, Object> param = new HashMap<>();
//        param.put("prompt", text);
//        param.put("max_tokens", 2048);
//        param.put("temperature", 0.5);
//        param.put("top_p", 1);
//        param.put("frequency_penalty", 0);
//        param.put("presence_penalty", 0);
        Message message = new Message().setRole("user").setContent(text);
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(message);
        param.put("messages", messages);
        param.put("model", "gpt-3.5-turbo");
        param.put("stream", false);
        JSONObject paramJson = new JSONObject(param);
        String body = HttpRequest.post(completionsUrl)
                .header("Authorization", "Bearer " + secretKey)
                .body(paramJson.toJSONString())
                .execute()
                .body();
        System.out.println(body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray choices = jsonObject.getJSONArray("choices");
        if (choices == null || jsonObject.getJSONObject("error") != null) {
            //出错
            JSONObject error = jsonObject.getJSONObject("error");
            if (error != null) {
                String msg = error.getString("message");
                JSONObject obj = new JSONObject();
                obj.put("text", "  Error: " + msg);
                return obj;
            }
        } else {
            JSONObject choiceOne = choices.getJSONObject(0);
            JSONObject messageObject = choiceOne.getJSONObject("message");
            JSONObject obj = new JSONObject();
            obj.put("text", messageObject.getString("content"));
            return obj;
        }
        return (JSONObject) new JSONObject().put("text","  Error: 系统错误！");
    }

    @ResponseBody
    @PostMapping("/api/chat2")
    public JSONObject query2(String text) {
        JSONObject obj = new JSONObject();
        obj.put("text", "可以使用以下方法将列表转化为树结构：\n1. 创建一个空的对象，用于存储树结构。\n2. 遍历列表中的每个元素，将元素的父子关系加入到树结构中。\n3. 使用递归方法将所有层级的父子关系添加到树结构中。\n下面是一个示例代码：\n\n```java\nfunction listToTree(list) {\n  // 创建一个空的对象用于存储树结构\n  var tree = {};\n  \n  // 遍历列表中的每个元素\n  list.forEach(function(item) {\n    // 如果元素没有父节点，则将其作为根节点\n    if (!item.parent) {\n      tree[item.id] = item;\n    } else {\n      // 如果元素有父节点，则将其加入到父节点的子节点列表中\n      if (!tree[item.parent].children) {\n        tree[item.parent].children = [];\n      }\n      tree[item.parent].children.push(item);\n    }\n  });\n  \n  // 定义一个递归函数用于将所有层级的父子关系添加到树结构中\n  function buildTree(node) {\n    if (node.children) {\n      node.children.forEach(function(child) {\n        buildTree(child);\n      });\n    }\n  }\n  \n  // 遍历根节点并调用递归函数来构建完整的树结构\n  Object.keys(tree).forEach(function(key) {\n    buildTree(tree[key]);\n  });\n  \n  // 返回构建完成的树结构\n  return tree;\n}\n```\n\n使用以上方法，可以将列表转化为树结构。当然，具体的实现方式也可以根据实际情况进行调整。");
        return obj;

    }

    @GetMapping("/code")
    public String code(Model model) {
        model.addAttribute("text", "123");
        return "code";
    }

    @ResponseBody
    @PostMapping("/api/code")
    public JSONObject codeQuery(String text) {
        Map<String, Object> param = new HashMap<>();
        param.put("prompt", text);
        param.put("max_tokens", 1000);
        param.put("temperature", 0);
        param.put("top_p", 1);
        param.put("frequency_penalty", 0);
        param.put("presence_penalty", 0);
        param.put("best_of", 1);
        param.put("echo", true);
        param.put("logprobs", 0);
        param.put("stream", false);
        JSONObject paramJson = new JSONObject(param);
        String body = HttpRequest.post(codeUrl)
                .header("Authorization", "Bearer " + sessKey)
                .body(paramJson.toJSONString())
                .execute()
                .body();
//        System.out.println(body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONArray choices = (JSONArray) jsonObject.get("choices");
        if (choices == null) {
            //出错
            JSONObject error = (JSONObject) jsonObject.get("error");
            if (error != null) {
                String message = error.getString("message");
                if (message.startsWith("Rate limit")) {
                    JSONObject obj = new JSONObject();
                    obj.put("text", "Error: 达到每分钟令牌的速率限制，请稍后重试！");
                    return obj;
                } else {
                    JSONObject obj = new JSONObject();
                    obj.put("text", "Error: " + message);
                    return obj;
                }
            }
        } else {
            JSONObject choiceOne = (JSONObject) choices.get(0);
            return choiceOne;
        }
        return (JSONObject) new JSONObject().put("text","Error: 系统错误！");
    }
}
