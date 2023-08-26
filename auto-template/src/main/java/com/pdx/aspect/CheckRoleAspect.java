package com.pdx.aspect;

import com.alibaba.fastjson2.util.AnnotationUtils;
import com.pdx.annotation.CheckRoles;
import com.pdx.annotation.CheckType;
import com.pdx.constants.BasicConstants;
import com.pdx.exception.BusinessException;
import com.pdx.response.ResponseCode;
import com.pdx.service.UserService;
import com.pdx.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/*
 * @Author 派同学
 * @Description 权限校验切面
 * @Date 2023/7/25
 **/

@Slf4j
@Aspect
@Component
public class CheckRoleAspect {

    private static final String POINT_CUT = "pointCut()";

    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;


    @Pointcut(value = "@within(com.pdx.annotation.CheckRoles) || @annotation(com.pdx.annotation.CheckRoles))")
    public void pointCut () {}

    @Around(value = POINT_CUT)
    public Object around (ProceedingJoinPoint point) throws Throwable {
        CheckRoles checkRoles = null;
        Method method = getMethod(point);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if (method.isAnnotationPresent(CheckRoles.class)) {
            checkRoles = getMethodAnnotation(point);
        } else {
            checkRoles = getClassAnnotation(point);
        }
        // 接口需要的角色/权限
        String[] roles = checkRoles.value();
        if (roles == null || roles.length < 1) {
            throw new BusinessException(ResponseCode.ACCESS_DENIED);
        }
        // 拼接方式 OR / AND
        CheckType checkType = checkRoles.checkType();
        // 获取用户的权限
        String token = request.getHeader(BasicConstants.TOKEN);
        // 校验Token是否可用
        boolean isExpired = jwtUtil.isTokenExpired(token);
        if (StringUtils.isEmpty(token) || isExpired) {
            throw new BusinessException(ResponseCode.NEED_LOGIN);
        }
        String userId = "";
        try {
            userId = jwtUtil.getUserIdByJwtToken(token);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.INVALID_LOGIN_INFO);
        }
        String allRoles = userService.currentRoles(userId);
        boolean isAllow = isAllowNext(allRoles, Arrays.asList(roles), checkType);
        if (!isAllow) {
            throw new BusinessException(ResponseCode.ACCESS_DENIED);
        }
        return point.proceed();
    }


    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return methodSignature.getMethod();
    }

    private CheckRoles getMethodAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Method method = getMethod(joinPoint);
        return method.getAnnotation(CheckRoles.class);
    }

    private CheckRoles getClassAnnotation(ProceedingJoinPoint joinPoint) {
        return AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), CheckRoles.class);
    }

    /**
     * 是否有权限执行下一步
     * @param role
     * @param needRole
     * @param checkType
     * @return
     */
    private boolean isAllowNext (String role, List<String> needRole, CheckType checkType) {
        if (checkType.equals(CheckType.OR)) {
            return needRole.contains(role);
        } else if (checkType.equals(CheckType.AND)) {
            return needRole.get(0).equalsIgnoreCase(role);
        }
        return false;
    }
}
