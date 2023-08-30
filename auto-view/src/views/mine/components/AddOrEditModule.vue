<template>
  <div>
    <a-modal
      title="Title"
      width="40%"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <a-form :form="form">
        <a-form-item label="模块名称" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-input
            placeholder="请输入模块名称"
            v-decorator="['name', { rules: [{ required: true, message: '请输入模块名称！' }] }]"
          />
        </a-form-item>
        <a-form-item label="模块排序" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-input-number
            v-decorator="['sort', { rules: [{ required: true, message: '请输入模块排序编号！' }] }]"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
import { insertModule } from '@/api/module'

export default {
  name: 'AddOrEditModule',
  props: {
    currentParentId: {
      type: String,
      default: () => {
        return '0'
      }
    },
    itemId: {
      type: String,
      default: () => {
        return ''
      }
    },
    type: {
      type: String,
      default: () => {
        return ''
      }
    }
  },
  data () {
    return {
      visible: false,
      confirmLoading: false,
      form: this.$form.createForm(this, { name: 'coordinated' })
    }
  },
  methods: {
    handleOk () {
      this.form.validateFields(async (err, values) => {
        this.confirmLoading = true
        if (!err) {
          const name = values.name
          const sort = values.sort
          if (this.type === 'insert') {
            const module = {
              name,
              sort,
              parentId: this.currentParentId,
              itemId: this.itemId
            }
            const data = await insertModule(module)
            this.$emit('operateResult', data)
          } else {
            const module = {
              name,
              sort
            }
            this.$emit('updateFun', module)
          }
          this.confirmLoading = false
        }
      })
    },
    handleCancel () {
      this.visible = false
    }
  }
}
</script>

<style scoped>

</style>
