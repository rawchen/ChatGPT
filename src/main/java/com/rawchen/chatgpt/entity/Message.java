package com.rawchen.chatgpt.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shuangquan.chen
 * @date 2024-01-29 11:47
 */
@Data
@Accessors(chain = true)
public class Message {

    private String role;

    private String content;
}
