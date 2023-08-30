/**
 * 模块相关接口方法
 */
import { axios } from '@/utils/request'
import { METHOD_TYPE } from '@/utils/constants'

/**
 * 获取模块列表
 *
 * @param itemId
 * @returns {AxiosPromise}
 */
export const getModuleList = (itemId) => {
  return axios({
    url: '/pdx/module/modules?itemId=' + itemId,
    method: METHOD_TYPE.GET
  })
}

/**
 * 添加模块
 * @param data
 * @returns {AxiosPromise}
 */
export const insertModule = (data) => {
  return axios({
    url: '/pdx/module/insert',
    method: METHOD_TYPE.POST,
    data
  })
}

export const updateModule = (data) => {
  return axios({
    url: '/pdx/module/update',
    method: METHOD_TYPE.POST,
    data
  })
}

export const moduleInfo = (id) => {
  return axios({
    url: '/pdx/module/' + id,
    method: METHOD_TYPE.GET
  })
}

export const deleteModule = (id) => {
  return axios({
    url: '/pdx/module/' + id,
    method: METHOD_TYPE.DELETE
  })
}
