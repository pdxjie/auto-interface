/**
 * 用例相关接口方法
 */
import { axios } from '@/utils/request'
import { METHOD_TYPE } from '@/utils/constants'

// 获取用例列表
export const casePage = (vo) => {
  return axios({
    url: '/case/page',
    method: METHOD_TYPE.POST,
    data: vo
  })
}

// 获取用例详情
export const caseDetail = (id) => {
  return axios({
    url: '/case/' + id,
    method: METHOD_TYPE.GET
  })
}

//  新增用例
export const caseCreate = (vo) => {
  return axios({
    url: '/case/insert',
    method: METHOD_TYPE.POST,
    data: vo
  })
}

// 更新用例
export const caseUpdate = (vo) => {
  return axios({
    url: '/case/update',
    method: METHOD_TYPE.POST,
    data: vo
  })
}

// 删除用例
export const caseDelete = (id) => {
  return axios({
    url: '/case/' + id,
    method: METHOD_TYPE.DELETE
  })
}

// 执行用例
export const caseRun = (caseInfo) => {
  return axios({
    url: '/case/run',
    method: METHOD_TYPE.POST,
    data: caseInfo
  })
}
