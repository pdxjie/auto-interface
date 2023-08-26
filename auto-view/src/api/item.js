/**
 * 项目相关接口
 */
import { axios } from '@/utils/request'
import { METHOD_TYPE } from '@/utils/constants'

/**
 * 条件分页查询
 * @param vo
 * @returns {AxiosPromise}
 */
export const homePages = (vo) => {
  return axios({
    url: '/pdx/item/homePages',
    method: METHOD_TYPE.POST,
    data: vo
  })
}

/**
 * 新增项目
 * @param item
 * @returns {AxiosPromise}
 */
export const insertItem = (item) => {
  return axios({
    url: '/pdx/item/insert',
    method: METHOD_TYPE.POST,
    data: item
  })
}

/**
 * 更新项目
 * @param item
 * @returns {AxiosPromise}
 */
export const updateItem = (item) => {
  return axios({
    url: '/pdx/item/update',
    method: METHOD_TYPE.POST,
    data: item
  })
}

/**
 * 获取项目信息
 * @param id
 * @returns {AxiosPromise}
 */
export const itemInfo = (id) => {
  return axios({
    url: '/pdx/item/' + id,
    method: METHOD_TYPE.GET
  })
}

/**
 * 删除项目信息
 * @param id
 * @returns {AxiosPromise}
 */
export const removeItemById = (id) => {
  return axios({
    url: '/pdx/item/remove/' + id,
    method: METHOD_TYPE.DELETE
  })
}
