package com.ityu.linjiaadminapi.controller.system;


import com.ityu.bean.constant.factory.PageFactory;
import com.ityu.bean.constant.state.BizLogType;
import com.ityu.bean.entity.system.OperationLog;
import com.ityu.service.system.OperationLogService;
import com.ityu.utils.BeanUtil;
import com.ityu.utils.DateUtil;
import com.ityu.utils.factory.Page;
import com.ityu.bean.vo.front.Rets;
import com.ityu.bean.vo.query.SearchFilter;
import com.ityu.warpper.LogWarpper;
import com.ityu.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * LogController
 *
 * @author enilu
 * @version 2018/10/5 0005
 */
@RestController
@RequestMapping("/api/log")
public class LogController extends BaseController {
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询操作日志列表
     */
    @RequestMapping("/list")
    @ResponseBody
    //@RequiresPermissions(value = {Permission.LOG})
    public Object list(@RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) String logName,
                       @RequestParam(required = false) Integer logType) {
        Page<OperationLog> page = new PageFactory<OperationLog>().defaultPage();
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parseDate(beginTime));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parseDate(endTime));
        page.addFilter( "logname", SearchFilter.Operator.LIKE, logName);
        if (logType != null) {
            page.addFilter(SearchFilter.build("logtype", SearchFilter.Operator.EQ, BizLogType.valueOf(logType)));
        }
        page = operationLogService.queryPage(page);
        page.setRecords((List<OperationLog>) new LogWarpper(BeanUtil.objectsToMaps(page.getRecords())).warp());
        return Rets.success(page);
    }

    /**
     * 查询指定用户的操作日志列表
     */
    @RequestMapping("/queryByUser")
    @ResponseBody
    public Object list() {
        Page<OperationLog> page = new Page<OperationLog>();
        page.addFilter(SearchFilter.build("userid", SearchFilter.Operator.EQ, getAdminUser().getId()));
        Page<OperationLog> pageResult = operationLogService.queryPage(page);
        return Rets.success(pageResult.getRecords());
    }

    /**
     * 清空日志
     */
    @RequestMapping(method = RequestMethod.DELETE)
   // @RequiresPermissions(value = {Permission.LOG_CLEAR})
    public Object delLog() {
        operationLogService.clear();
        return Rets.success();
    }
}
