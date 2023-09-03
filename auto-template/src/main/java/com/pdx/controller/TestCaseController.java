package com.pdx.controller;


import com.pdx.entity.TestCase;
import com.pdx.modal.vo.CaseInsertVo;
import com.pdx.modal.vo.CaseQueryVo;
import com.pdx.response.Result;
import com.pdx.service.TestCaseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
@RestController
@RequestMapping("/case")
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    @ApiOperation(value = "根据模块ID获取用例列表")
    @PostMapping("/page")
    public Result<?> casePage (@RequestBody CaseQueryVo vo) {
        return testCaseService.casePage(vo);
    }

    @ApiOperation(value = "创建用例")
    @PostMapping("/insert")
    private Result<?> insertCase (@RequestBody CaseInsertVo vo) {
        return testCaseService.insertCase(vo);
    }

    @ApiOperation(value = "更新用例信息")
    @PostMapping("/update")
    public Result<?> updateCaseInfo (@RequestBody TestCase testCase) {
        return testCaseService.updateTestCase(testCase);
    }

    @ApiOperation(value = "获取用例信息")
    @GetMapping("/{id}")
    public Result<?> caseInfo (@PathVariable("id") String id) {
        return testCaseService.caseInfo(id);
    }

    @ApiOperation(value = "删除用例")
    @DeleteMapping("/{id}")
    public Result<?> removeCaseById (@PathVariable("id") String id) {
        return testCaseService.removeCaseInfoById(id);
    }

    @ApiOperation(value = "执行用例")
    @PostMapping("/run")
    public Result<?> runCase (@RequestBody TestCase caseInfo) {
        return testCaseService.runCase(caseInfo);
    }
}

