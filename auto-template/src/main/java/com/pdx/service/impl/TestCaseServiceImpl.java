package com.pdx.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdx.entity.ModuleCase;
import com.pdx.entity.TestCase;
import com.pdx.exception.BusinessException;
import com.pdx.mapper.ModuleCaseMapper;
import com.pdx.mapper.TestCaseMapper;
import com.pdx.modal.dto.CaseDto;
import com.pdx.modal.vo.CaseInsertVo;
import com.pdx.modal.vo.CaseQueryVo;
import com.pdx.response.ResponseCode;
import com.pdx.response.Result;
import com.pdx.service.TestCaseService;
import com.pdx.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
@Service
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements TestCaseService {

    @Resource
    private ModuleCaseMapper moduleCaseMapper;

    @Override
    public Result<?> casePage(CaseQueryVo vo) {
        // 分页数据
        Integer startPage = (vo.getCurrent() - 1) * vo.getPageSize();
        List<CaseDto> caseDtos = baseMapper.queryCasePage(vo.getModuleId(), vo.getCaseName(), startPage, vo.getPageSize());
        Integer total = baseMapper.queryCount(vo.getModuleId(), vo.getCaseName());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("cases", caseDtos);
        resultMap.put("total", total);
        return Result.success(resultMap);
    }

    @Override
    @Transactional
    public Result<?> insertCase(CaseInsertVo vo) {
        if (StrUtil.isEmpty(vo.getModuleId())) {
            throw new BusinessException(ResponseCode.ERROR_PARAM);
        }
        TestCase testCase = new TestCase();
        BeanUtils.copyProperties(vo, testCase);
        String caseId = UUID.randomUUID().toString();
        testCase.setId(caseId);
        testCase.setCreateTime(new Date());
        testCase.setUpdateTime(new Date());
        testCase.setStatus(1);
        ModuleCase moduleCase = new ModuleCase();
        moduleCase.setCaseId(caseId);
        moduleCase.setModuleId(vo.getModuleId());
        moduleCase.setCreateTime(new Date());
        moduleCase.setUpdateTime(new Date());
        moduleCase.setId(UUID.randomUUID().toString());
        int insert = baseMapper.insert(testCase);
        int result = moduleCaseMapper.insert(moduleCase);
        return (insert + result) == 2 ? Result.success() : Result.fail();
    }

    @Override
    public Result<?> updateTestCase(TestCase testCase) {
        if (StringUtils.isEmpty(testCase.getId())) {
            throw new BusinessException(ResponseCode.ERROR_PARAM);
        }
        int result = baseMapper.updateById(testCase);
        return result > 0 ? Result.success() : Result.fail();
    }

    @Override
    public Result<?> caseInfo(String id) {
        TestCase testCase = baseMapper.selectById(id);
        return Result.success(testCase);
    }

    @Override
    public Result<?> removeCaseInfoById(String id) {
        // 移除关联
        int result = moduleCaseMapper.delete(new QueryWrapper<ModuleCase>().eq("case_id", id));
        int row = baseMapper.deleteById(id);
        return (result > 0 && row > 0) ? Result.success() : Result.fail();
    }

    /**
     * 执行用例
     * @param caseInfo
     */
    @Override
    public Result<?> runCase(TestCase caseInfo) {
        JSONObject requestData = JSONObject.parseObject(caseInfo.getRequestData());
        JSONObject expectData = JSONObject.parseObject(caseInfo.getExpectResponse());
        JSONObject resultJson = HttpUtils.postJson(caseInfo.getRequestUrl(), requestData);
        boolean success = HttpUtils.isSuccess(expectData, resultJson);
        // 如果返回 true 更新用例执行结果
        if (success) {
            caseInfo.setStatus(3);
            caseInfo.setUpdateTime(new Date());
            baseMapper.updateById(caseInfo);
        } else {
            caseInfo.setStatus(4);
            caseInfo.setUpdateTime(new Date());
            baseMapper.updateById(caseInfo);
        }
        return Result.success(success);
    }
}
