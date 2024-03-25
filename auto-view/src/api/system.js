/**
 * 数据中心接口
 */
import { axios } from '@/utils/request'

/* 用户相关功能接口 start */
/**
 * 条件查询用户列表
 * @param userVo
 * @returns {AxiosPromise}
 */
export const userPage = (userVo) => {
  return axios({
    url: '/pdx/user/search',
    method: 'post',
    data: userVo
  })
}

/**
 * 添加用户
 * @param userVo
 * @returns {AxiosPromise}
 */
export const insertUser = (user) => {
  return axios({
    url: '/pdx/user/insert',
    method: 'post',
    data: user
  })
}

/**
 * 获取用户信息
 * @param userVo
 * @returns {AxiosPromise}
 */
export const getUserInfo = (id) => {
  return axios({
    url: '/pdx/user/' + id,
    method: 'get'
  })
}

/**
 * 更新用户基础信息
 * @param userVo
 * @returns {AxiosPromise}
 */
export const updateUser = (userVo) => {
  return axios({
    url: '/pdx/user/update',
    method: 'put',
    data: userVo
  })
}

/**
 * 修改用户头像
 * @param userVo
 * @returns {AxiosPromise}
 */
export const updateAvatar = (id) => {
  return axios({
    url: '/pdx/user/avatar/' + id,
    method: 'get'
  })
}

/**
 * 修改用户密码
 * @param userVo
 * @returns {AxiosPromise}
 */
export const updatePassword = (userVo) => {
  return axios({
    url: '/pdx/user/password',
    method: 'post',
    data: userVo
  })
}

/**
 * 删除用户信息
 * @param userVo
 * @returns {AxiosPromise}
 */
export const delUser = (id) => {
  return axios({
    url: '/pdx/user/delete/' + id,
    method: 'delete'
  })
}

/**
 * 封禁用户账号
 * @param userVo
 * @returns {AxiosPromise}
 */
export const forbidden = (id) => {
  return axios({
    url: '/pdx/user/forbidden/' + id,
    method: 'get'
  })
}

/**
 * 设置角色
 * @param userVo
 * @returns {AxiosPromise}
 */
export const setRole = (userId, roleId) => {
  return axios({
    url: '/pdx/user/set/role?userId=' + userId + '&roleId=' + roleId,
    method: 'put'
  })
}

/* 用户相关功能接口 end */

/* 角色相关功能接口 start */
/**
 * 角色分页列表
 * @param roleVo
 * @returns {AxiosPromise}
 */
export const rolePage = (roleVo) => {
  return axios({
    url: '/pdx/role/roles',
    method: 'post',
    data: roleVo
  })
}

/**
 * 插入角色
 * @param roleVo
 * @returns {AxiosPromise}
 */
export const insertRole = (role) => {
  return axios({
    url: '/pdx/role/insert',
    method: 'post',
    data: role
  })
}

/**
 * 获取角色信息
 * @param roleVo
 * @returns {AxiosPromise}
 */
export const roleInfo = (id) => {
  return axios({
    url: '/pdx/role/' + id,
    method: 'get'
  })
}

/**
 * 修改角色信息
 * @param roleVo
 * @returns {AxiosPromise}
 */
export const updateRole = (roleVo) => {
  return axios({
    url: '/pdx/role/update',
    method: 'put',
    data: roleVo
  })
}

/**
 * 删除角色
 * @param roleVo
 * @returns {AxiosPromise}
 */
export const removeRole = (id) => {
  return axios({
    url: '/pdx/role/' + id,
    method: 'delete'
  })
}

/**
 * 删除角色
 * @param roleVo
 * @returns {AxiosPromise}
 */
export const changeStatus = (roleId) => {
  return axios({
    url: '/pdx/role/status?roleId=' + roleId,
    method: 'get'
  })
}