package com.ityu.linjiaadminapi.controller.cms;


import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.bean.entity.system.FileInfo;

import com.ityu.service.system.FileService;
import com.ityu.utils.StringUtil;
import com.ityu.utils.factory.Page;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fileMgr")
public class FileMgrController extends BaseController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
   // @RequiresPermissions(value = {Permission.FILE})
    public Object list(@RequestParam(required = false) String originalFileName
    ) {
        Page<FileInfo> page = new PageFactory<FileInfo>().defaultPage();
        if (StringUtil.isNotEmpty(originalFileName)) {
            page.addFilter(SearchFilter.build("originalFileName", SearchFilter.Operator.LIKE, originalFileName));
        }
        page = fileService.queryPage(page);
        return Rets.success(page);
    }
}
