<template>
  <div>
    <a-drawer
      placement="right"
      title="输入用例信息"
      :closable="false"
      :visible="visible"
      width="40%"
      :body-style="{ paddingBottom: '80px' }"
      :after-visible-change="afterVisibleChange"
      @close="onClose"
    >
      <a-form :form="form" layout="vertical" hide-required-mark>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="用例名称">
              <a-input
                v-decorator="[
                  'name',
                  {
                    rules: [{ required: true, message: '请输入用例名称！' }],
                  },
                ]"
                placeholder="请输入用例名称"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="请求地址">
              <a-input
                v-decorator="[
                  'requestUrl',
                  {
                    rules: [{ required: true, message: '请输入请求地址！' }],
                  },
                ]"
                placeholder="请描述请求地址"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="用例等级">
              <a-select v-model="caseVo.caseRank" @change="handleChange">
                <a-select-option :value="1">
                  P0
                </a-select-option>
                <a-select-option :value="2">
                  P1
                </a-select-option>
                <a-select-option :value="3">
                  P2
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="业务描述">
              <v-md-editor
                :class="isMobile ? 'custom' : ''"
                v-model="caseVo.description"
                :disabled-menus="[]"
                @upload-image="handleUploadImageAnalysis"
                mode="editable"
                placeholder="点击右上角全屏，编写体验效果更佳"
                height="300px"></v-md-editor>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="请求数据">
              <MonacoEditor ref="initRequestData" height="400px" language="json" :code="initJson"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="响应断言">
              <MonacoEditor ref="responseData" height="400px" language="json" :code="initResponse"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <div
        :style="{
          position: 'absolute',
          right: 0,
          bottom: 0,
          width: '100%',
          borderTop: '1px solid #e9e9e9',
          padding: '10px 16px',
          background: '#fff',
          textAlign: 'right',
          zIndex: 1,
        }"
      >
        <a-button :style="{ marginRight: '8px' }" @click="onClose">
          取消
        </a-button>
        <a-button type="primary" @click="submitCaseInfo">
          确认
        </a-button>
      </div>
    </a-drawer>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { DEVICE_TYPE } from '@/utils/device'
import MonacoEditor from '@/components/MonacoEditor'
import { caseCreate, caseUpdate } from '@/api/case'
export default {
  name: 'InsertOrUpdateCase',
  components: { MonacoEditor },
  props: {
    type: {
      type: String,
      default: () => {
        return ''
      }
    },
    currentModule: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data () {
    return {
      form: this.$form.createForm(this),
      visible: false,
      caseVo: {
        caseRank: 1
      },
      initJson: '',
      initResponse: ''
    }
  },
  computed: {
    ...mapState({
      device: state => state.app.device
    })
  },
  methods: {
    onClose () {
      this.visible = false
    },
    afterVisibleChange () {
    },
    isMobile () {
      return this.device === DEVICE_TYPE.MOBILE
    },
    handleUploadImageAnalysis () {},
    handleChange () {},
    // 提交用例信息
    submitCaseInfo () {
      this.form.validateFields((err, values) => {
        if (!err) {
          // 解构赋值
          const { name, requestUrl } = values
          // 封装数据
          this.caseVo.name = name
          this.caseVo.requestUrl = requestUrl
          this.caseVo.requestData = this.$refs.initRequestData.codeVal
          this.caseVo.expectResponse = this.$refs.responseData.codeVal
          this.caseVo.moduleId = this.currentModule.id
          this.caseVo.businessDesc = this.caseVo.description
          if (this.type === 'insert') {
            caseCreate(this.caseVo).then(res => {
              this.$emit('insertFun', res)
              this.onClose()
            })
          } else {
            caseUpdate(this.caseVo).then(res => {
              this.$emit('insertFun', res)
              this.onClose()
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped lang='less'>
::v-deep .ant-input {
  height: 40px;
}
.custom {
  ::v-deep .v-md-editor__toolbar-left+.v-md-editor__toolbar-right {
    margin-left: unset!important;
  }
  ::v-deep .v-md-editor__toolbar-item:not(:first-child) {
    margin-left: unset!important;
    margin-top: 2px!important;
  }
  ::v-deep .v-md-editor__toolbar-divider {
    height: 2px;
    margin: 0 0px!important;
  }
}
</style>
