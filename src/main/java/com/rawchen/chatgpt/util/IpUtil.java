package com.rawchen.chatgpt.util;

import cn.hutool.core.util.StrUtil;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.rawchen.chatgpt.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.InetAddress;

/**
 * @author shuangquan.chen
 * @date 2024-02-05 09:46
 */
@Slf4j
@Component
public class IpUtil {
    public static String getProvinceCityArea(String ip) {
        try {
            File database = new File(Constants.GEO_LITE_2);
            DatabaseReader reader = new DatabaseReader.Builder(database).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = null;
            response = reader.city(ipAddress);
            return response.getMostSpecificSubdivision().getName() + ", " +
                    response.getCity().getName() + ", " +
                    response.getCountry().getName();
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    /**
     * 获取ip地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (!StrUtil.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StrUtil.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }
}