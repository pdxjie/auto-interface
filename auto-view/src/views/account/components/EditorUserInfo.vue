<template>
  <a-modal
    title="修改用户信息"
    :visible="visible"
    width="45%"
    :confirm-loading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <a-form :model="userInfo" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="邮箱">
        <a-input disabled v-model="userInfo.email" />
      </a-form-item>
      <a-form-item label="用户昵称">
        <a-input v-model="userInfo.nickName" />
      </a-form-item>
      <a-form-item label="手机号">
        <a-input v-model="userInfo.phone" />
      </a-form-item>
      <a-form-item label="用户密码">
        <a-input v-model="userInfo.password" />
      </a-form-item>
      <a-form-item label="个人简介">
        <a-textarea
          placeholder="说些什么介绍一下自己吧~"
          :auto-size="{ minRows: 2, maxRows: 6 }"
          v-model="userInfo.remark"
        />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { updateUser } from '@/api/system'

export default {
  name: 'EditorUserInfo',
  props: {
    userInfo: {
      type: Object
    }
  },
  data () {
    return {
      confirmLoading: false,
      visible: false,
      labelCol: { span: 4 },
      wrapperCol: { span: 14 }
    }
  },
  methods: {
    async handleOk () {
      const modifyVo = {
        nickName: this.userInfo.nickName,
        password: this.userInfo.password,
        phone: this.userInfo.phone,
        remark: this.userInfo.remark,
        id: this.userInfo.id
      }
      const data = await updateUser(modifyVo)
      if (data.code === 200) {
        this.$message.success('更新成功！')
        this.$emit('updateUserInfo', data.code)
        this.visible = false
      } else {
        this.$message.error('更新失败！')
      }
    },
    handleCancel () {
      this.visible = false
    }
  }
}
</script>

<style scoped lang='less'>

</style>