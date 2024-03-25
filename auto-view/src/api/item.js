/**
 * 项目相关接口
 */
import { axios } from '@/utils/request'

/**
 * 条件分页查询
 * @param vo
 * @returns {AxiosPromise}
 */
export const homePages = (vo) => {
  return axios({
    url: '/pdx/item/homePages',
    method: 'POST',
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
    method: 'POST',
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
    method: 'POST',
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
    method: 'GET'
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
    method: 'DELETE'
  })
}

/**
 * 克隆项目
 * @param {String} itemId
 * @returns
 */
export const cloneItem = (itemId) => {
  return axios({
    url: '/pdx/item/clone?itemId=' + itemId,
    method: 'GET'
  })
}

/**
 * 公共广场
 * @param {Object} params
 * @returns
 */
export const publishItems = (data) => {
  return axios({
    url: '/pdx/item/public',
    method: 'POST',
    data
  })
}

export const likeItem = (vo) => {
  return axios({
    url: '/like/like',
    method: 'POST',
    data: vo
  })
}

export const unlikeItem = (vo) => {
  return axios({
    url: '/like/unlike',
    method: 'POST',
    data: vo
  })
}

export const myLikeItems = (vo) => {
  return axios({
    url: '/pdx/item/like',
    method: 'POST',
    data: vo
  })
}

export const myCollectItems = (vo) => {
  return axios({
    url: '/pdx/item/collect',
    method: 'POST',
    data: vo
  })
}
