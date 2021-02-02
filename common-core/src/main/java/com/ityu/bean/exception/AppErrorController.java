package com.ityu.bean.exception;


import com.ityu.bean.enumeration.BizExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class AppErrorController extends BasicErrorController {
    public AppErrorController(ServerProperties errorAttributes) {
        super(new DefaultErrorAttributes(), errorAttributes.getError());
    }

    /**
     * 覆盖默认的Json响应
     */
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        //输出自定义的Json格式
        Map<String, Object> map = new HashMap<>();
        String msg = body.get("message") + "";
        if (msg.equals(BizExceptionEnum.TOKEN_INVALID.getMessage())) {
            map.put("code", BizExceptionEnum.TOKEN_INVALID.getCode());
        } else {
            map.put("code", -1);
        }
        map.put("msg", msg);
        map.put("status", true);
        map.put("data", "");
        //log.error(String.valueOf(StringUtil.getStrValue(request)));
        return new ResponseEntity<>(map, HttpStatus.valueOf(200));

        // return new ResponseEntity<>(map, status);
    }

    /**
     * 覆盖默认的HTML响应
     */
    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        //请求的状态
        HttpStatus status = getStatus(request);
        response.setStatus(getStatus(request).value());

        Map<String, Object> model = getErrorAttributes(request,
                isIncludeStackTrace(request, MediaType.TEXT_HTML));
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        //指定自定义的视图
        return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
    }
}