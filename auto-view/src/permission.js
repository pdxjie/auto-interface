import Vue from 'vue'
import router from './router'
import store from './store'

import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import notification from 'ant-design-vue/es/notification'
import { setDocumentTitle, domTitle } from '@/utils/domUtil'
import { ACCESS_TOKEN } from '@/store/mutation-types'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['login', 'register'] // no redirect whitelist

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  to.meta && (typeof to.meta.title !== 'undefined' && setDocumentTitle(`${to.meta.title} - ${domTitle}`))
  if (Vue.ls.get(ACCESS_TOKEN)) {
    /* has token */
    if (to.path === '/user/login') {
      next({ path: '/mine/card' })
      NProgress.done()
    } else {
      if (store.getters.roles.length === 0) {
        store
          .dispatch('GetInfo')
          .then(res => {
            const roles = res.result && res.result.role
            store.dispatch('GenerateRoutes', { roles }).then(() => {
              // 根据roles权限生成可访问的路由表
              // 动态添加可访问路由表
              router.addRoutes(store.getters.addRouters)
              const redirect = decodeURIComponent(from.query.redirect || to.path)
              if (to.path === redirect) {
                // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
                next({ ...to, replace: true })
              } else {
                // 跳转到目的路由
                next({ path: redirect })
              }
            })
          })
          .catch(() => {
            notification.error({
              message: '错误',
              description: '请求用户信息失败，请重试'
            })
            store.dispatch('Logout').then(() => {
              next({ path: '/user/login', query: { redirect: to.fullPath } })
            })
          })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.includes(to.name)) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next({ path: '/user/login', query: { redirect: to.fullPath } })
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})

const action = Vue.directive('action', {
  bind: function (el, binding, vnode) {
    const actionName = binding.arg
    const roles = store.getters.roles
    const elVal = vnode.context.$route.meta.permission
    const permissionId = elVal instanceof String && [elVal] || elVal
    roles.permissions.forEach(p => {
      if (!permissionId.includes(p.permissionId)) {
        return
      }
      if (p.actionList && !p.actionList.includes(actionName)) {
        el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none')
      }
    })
  }
})

export {
  action
}
