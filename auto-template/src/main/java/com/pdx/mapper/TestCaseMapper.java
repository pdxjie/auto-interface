package com.pdx.mapper;

import com.pdx.entity.TestCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pdx.modal.dto.CaseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 派同学
 * @since 2023-08-26
 */
public interface TestCaseMapper extends BaseMapper<TestCase> {

    List<CaseDto> queryCasePage(@Param("moduleId") String moduleId, @Param("caseName") String caseName, @Param("startPage")Integer startPage, @Param("size")Integer pageSize);

    Integer queryCount(@Param("moduleId") String moduleId, @Param("caseName")String caseName);
}
