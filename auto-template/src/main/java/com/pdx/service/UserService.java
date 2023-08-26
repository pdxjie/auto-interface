package com.pdx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pdx.entity.User;
import com.pdx.modal.vo.*;
import com.pdx.response.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 派同学
 * @since 2023-07-24
 */
public interface UserService extends IService<User> {

    String currentRoles(String userId);

    Result<?> login(LoginVo loginVo);

    Result<?> registerUser(RegisterVo registerVo);

    Result<?> sendCode(String email);

    Result<?> queryByCondition(UserVo userVo);

    Result<?> insertUser(User user);

    Result<?> getOneById(String id);

    Result<?> updateInfo(ModifyUserVo user);

    Result<?> updateAvatar(String id, MultipartFile file);

    Result<?> updatePassword(UpdatePasswordVo vo);

    Result<?> removeUserById(String id);

    Result<?> forbiddeUser(String id);

    Result<?> logout(String id);

    Result<?> setRole(String userId, String roleId);

    Result<?> getUserInfo();

    Result<?> batchUpload(MultipartFile file);
}
