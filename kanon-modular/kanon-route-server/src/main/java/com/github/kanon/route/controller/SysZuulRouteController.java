package com.github.kanon.route.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.kanon.common.base.controller.IKanonController;
import com.github.kanon.common.base.model.vo.Pagination;
import com.github.kanon.common.base.model.vo.ResponseParam;
import com.github.kanon.route.cache.SysZuulRouteCacheManager;
import com.github.kanon.route.model.dto.SysZuulRouteDto;
import com.github.kanon.route.model.dto.SysZuulRouteQuery;
import com.github.kanon.route.model.pojo.SysZuulRoute;
import com.github.kanon.route.service.ISysZuulRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>系统路由控制器</p>
 * 增、删、改的同时会去同步刷新配置(非一致性)
 *
 * @author PengCheng
 * @date 2018/11/8
 */
@RestController
@RequestMapping("zuulRoute")
@Api(description = "系统路由",tags = "系统路由")
public class SysZuulRouteController implements IKanonController {

    @Autowired
    private ISysZuulRouteService sysZuulRouteService;

    @Autowired
    private SysZuulRouteCacheManager sysZuulRouteCacheManager;

    @ApiOperation(value="分页查询",tags="系统路由")
    @ApiImplicitParam(name = "sysZuulRouteQuery", required = true, dataType = "SysZuulRouteQuery")
    @PostMapping("query")
    public ResponseParam<List<SysZuulRoute>> query(@RequestBody SysZuulRouteQuery sysZuulRouteQuery){
        Page<SysZuulRoute> page = sysZuulRouteService.queryByPage(sysZuulRouteQuery);
        return ResponseParam.success(
                page.getRecords(),
                new Pagination(page.getTotal(),page.getCurrent(),page.getSize()));
    }

    @ApiOperation(value="获取单个详情",tags="系统路由")
    @GetMapping("view")
    public ResponseParam<SysZuulRoute> view(@RequestParam("id") Long id){
        return ResponseParam.success(sysZuulRouteService.getOne(id));
    }

    @ApiOperation(value="新增",tags="系统路由")
    @ApiImplicitParam(name = "sysZuulRouteDto", required = true, dataType = "SysZuulRouteDto")
    @PostMapping("add")
    public ResponseParam add(@Validated @RequestBody SysZuulRouteDto sysZuulRouteDto){
        sysZuulRouteService.save(sysZuulRouteDto);
        return ResponseParam.success();
    }

    @ApiOperation(value="修改",tags="系统路由")
    @ApiImplicitParam(name = "sysZuulRouteDto", required = true, dataType = "SysZuulRouteDto")
    @PostMapping("modify")
    public ResponseParam modify(@Validated @RequestBody SysZuulRouteDto sysZuulRouteDto){
        sysZuulRouteService.modify(sysZuulRouteDto);
        return ResponseParam.success();
    }

    @ApiOperation(value="删除",tags="系统路由")
    @PostMapping("delete")
    public ResponseParam delete(@RequestParam("id") Long id){
        sysZuulRouteService.remove(id);
        return ResponseParam.success();
    }

    @ApiOperation(value="批量删除",tags="系统路由")
    @PostMapping("deleteBatch")
    public ResponseParam deleteBatch(@RequestParam("ids") @RequestBody List<Long> ids){
        sysZuulRouteService.removeBatch(ids);
        return ResponseParam.success();
    }

    @ApiOperation(value="应用路由配置(刷新配置)",tags="系统路由")
    @GetMapping("apply")
    public ResponseParam apply(){
        sysZuulRouteCacheManager.applyZuulRoute();
        return ResponseParam.success();
    }
}
