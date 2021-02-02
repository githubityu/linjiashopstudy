package com.ityu.linjiaadminapi.controller.study;



import com.ityu.bean.vo.front.Rets;
import com.ityu.web.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * AccountController
 *
 * @author enilu
 * @version 2018/9/12 0012
 */
@RestController
@RequestMapping("/api/studyone")
public class StudyController extends BaseController {
    @RequestMapping(value = "/getStudyInfo", method = RequestMethod.GET)
    public Object info() {
        return Rets.success("习近平在学习");
    }


}
