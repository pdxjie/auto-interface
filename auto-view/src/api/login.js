/**
 * 基础前置操作
 */

import { axios } from '@/utils/request'

/**
 * @param parameter
 * @returns {*}
 */
export function login (parameter) {
  return axios({
    url: '/basic/login',
    method: 'post',
    data: parameter
  })
}

export function getSmsCaptcha (email) {
  return axios({
    url: '/basic/captcha/' + email,
    method: 'get'
  })
}

export function register (parameter) {
  return axios({
    url: '/basic/register',
    method: 'post',
    data: parameter
  })
}

export function getInfo () {
  return axios({
    url: '/basic/info',
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function getCurrentUserNav (token) {
  return axios({
    url: '/user/nav',
    method: 'get'
  })
}

export function logout () {
  return axios({
    url: '/basic/logout',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 聊天记录
 * @returns {AxiosPromise}
 */
export function messageHistory () {
  return axios({
    url: '/pdx/message/history',
    method: 'get'
  })
}

export const aiChat = (message) => {
  return axios({
    url: '/ai/chat?message=' + message,
    method: 'GET'
  })
}
