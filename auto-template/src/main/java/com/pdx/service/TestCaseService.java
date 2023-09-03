package com.pdx.service;

import com.pdx.entity.TestCase;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pdx.modal.vo.CaseInsertVo;
import com.pdx.modal.vo.CaseQueryVo;
import com.pdx.response.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
public interface TestCaseService extends IService<TestCase> {

    Result<?> casePage(CaseQueryVo vo);

    Result<?> insertCase(CaseInsertVo vo);

    Result<?> updateTestCase(TestCase testCase);

    Result<?> caseInfo(String id);

    Result<?> removeCaseInfoById(String id);
}
