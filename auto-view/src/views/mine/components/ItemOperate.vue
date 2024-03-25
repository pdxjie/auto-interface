<template>
  <div>
    <a-modal
      :title="type === 'insert' ? '创建项目' : '更新项目'"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      width="40%"
      @cancel="handleCancel"
    >
      <a-form :form="itemInfo" :layout="formLayout" >
        <a-form-item label="项目图标" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-upload
            accept=""
            name="avatar"
            list-type="picture-card"
            class="avatar-uploader"
            :show-upload-list="false"
            :beforeUpload="() => false"
          >
            <img v-if="iconUrl" :src="iconUrl" alt="avatar" style="width: 120px;height: 120px"/>
            <div v-else>
              <a-icon :type="loading ? 'loading' : 'plus'" />
              <div @click.prevent class="ant-upload-text">
                上传
              </div>
            </div>
          </a-upload>
          <a-button @click="selectDefaultCover" type="primary">选择图标</a-button>
        </a-form-item>
        <a-form-item label="项目名称" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-input
            v-model="itemInfo.name"
            placeholder="请输入项目名称"
          />
        </a-form-item>
        <a-form-item label="项目描述" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-textarea
            v-model="itemInfo.description"
            placeholder="描述最多不超过100字..."
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item label="项目周期" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <a-range-picker
            showToday
            :placeholder="['开始时间', '结束时间']"
            @change="onChangeDate" />
        </a-form-item>
        <a-form-item label="权限" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
          <div class="display-flex flex-column">
            <a-radio :checked="privateOption" @change="updatePrivateOption">
              <a-icon type="lock" /> 私有
            </a-radio>
            <span style="font-size: 12px;color: #a8a8a8">只有组长和项目开发者可以索引并查看项目信息</span>
          </div>
          <div class="display-flex flex-column">
            <a-radio :checked="publicOption" @change="updatePublicOption">
              <a-icon type="unlock" /> 公共
            </a-radio>
            <span style="font-size: 12px;color: #a8a8a8">只有组长和项目开发者可以索引并查看项目信息</span>
          </div>
        </a-form-item>
      </a-form>
    </a-modal>
    <!-- 选择图标 -->
    <SelectIcons @selectedImage="selectedImageUrl" ref="default"/>
  </div>
</template>

<script>
import SelectIcons from '@/views/mine/components/SelectIcons'
import { insertItem, updateItem } from '@/api/item'
import moment from 'moment'
export default {
  name: 'ItemOperate',
  components: { SelectIcons },
  props: {
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
      formLayout: 'horizontal',
      itemInfo: {
        id: '',
        name: '',
        description: '',
        createTime: '',
        dateLine: '',
        authority: 2,
        cover: '',
        status: 0,
        type: 1
      },
      iconUrl: '',
      loading: false,
      privateOption: true,
      publicOption: false
    }
  },
  methods: {
    async handleOk () {
      this.confirmLoading = true
      if (this.itemInfo.name.trim() === '') {
        this.$message.error('请输入项目名称！')
        return
      }
      if (this.itemInfo.description.trim() === '') {
        this.$message.error('请输入项目描述！')
        return
      }
      if (this.iconUrl.trim() === '') {
        this.$message.error('请选择项目图标！')
        return
      }
      const itemInfo = {
        id: this.itemInfo.id,
        name: this.itemInfo.name,
        description: this.itemInfo.description,
        createTime: this.itemInfo.createTime,
        dateLine: this.itemInfo.dateLine,
        authority: 2,
        cover: this.iconUrl,
        status: 1,
        type: this.itemInfo.type
      }
      let data
      if (this.type === 'insert') {
        data = await insertItem(itemInfo)
      } else {
        data = await updateItem(itemInfo)
      }
      if (data.code === 200) {
        this.$message.success('操作成功！')
        this.visible = false
        this.confirmLoading = false
        this.$emit('operateSuccess', data.code)
      }
    },
    handleCancel () {
      this.visible = false
      this.itemInfo = {}
    },
    handleSelectChange (key) {},
    updatePrivateOption (key) {
      this.privateOption = key.target.checked
      if (this.privateOption) {
        this.publicOption = false
        this.itemInfo.type = 1
      }
    },
    dealDateTime (date) {
      return moment(date).format('YYYY-MM-DD')
    },
    updatePublicOption (key) {
      this.publicOption = key.target.checked
      if (this.publicOption) {
        this.privateOption = false
        this.itemInfo.type = 2
      }
    },
    selectedImageUrl (url) {
      this.itemInfo.cover = url
      this.iconUrl = url
    },
    selectDefaultCover () {
      this.$refs.default.visible = true
    },
    onChangeDate (time, dateStr) {
      this.itemInfo.createTime = dateStr[0]
      this.itemInfo.dateLine = dateStr[1]
    }
  }
}
</script>

<style scoped>

</style>
