package com.rawchen.chatgpt.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shuangquan.chen
 * @date 2024-02-01 18:50
 */
@Data
@Accessors(chain = true)
public class ModelParam {

    private Integer n;

    private String size;
}
