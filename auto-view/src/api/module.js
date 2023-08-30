/**
 * 模块相关接口方法
 */
import { axios } from '@/utils/request'

export const getModuleList = (itemId) => {
  return axios({
    url: '/pdx/module/modules?itemId=' + itemId,
    method: 'GET'
  })
}
