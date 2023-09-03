<template>
  <div>
    <a-modal
      title="业务场景描述"
      :visible="visible"
      @cancel="handleCancel"
      :footer="null"
    >
      <v-md-editor
        v-model="businessDesc"
        width="50%"
        mode="preview"></v-md-editor>
    </a-modal>
  </div>
</template>

<script>
export default {
  name: 'PreviewBusinessDesc',
  data () {
    return {
      visible: false, // 弹窗是否可见
      businessDesc: ''
    }
  },
  methods: {
    handleCancel () {
      this.visible = false
    }
  }
}
</script>

<style scoped lang='less'>

</style>
