<template>
  <div>
    <a-drawer
      placement="right"
      title="输入用例信息"
      :closable="false"
      :visible="visible"
      width="50%"
      :body-style="{ paddingBottom: '80px' }"
      :after-visible-change="afterVisibleChange"
      @close="onClose"
    >
      <a-form :form="form" layout="vertical" hide-required-mark>
        <a-row :gutter="16">
          <a-col :span="24">
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
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="请求地址">
              <a-input
                v-model="caseVo.requestUrl"
                placeholder="请输入请求地址"
              >
                <a-select slot="addonBefore" @change="changeRequestType" v-model="caseVo.requestType" style="width: 90px">
                  <a-select-option :value="1">
                    GET
                  </a-select-option>
                  <a-select-option :value="2">
                    POST
                  </a-select-option>
                  <a-select-option :value="3">
                    PUT
                  </a-select-option>
                  <a-select-option :value="4">
                    DELETE
                  </a-select-option>
                </a-select>
              </a-input>
              <a-tabs default-active-key="1" @change="callback">
                <a-tab-pane key="1" tab="Params">
                  <a-table :columns="ParamsColumns" :data-source="paramsData" :pagination="false">
                    <template slot="paramsKey" slot-scope="text, record">
                      <a-input @input="changeContentKey(record)" style="height: 30px" v-model="record.paramsKey" placeholder="请输入参数名"/>
                    </template>
                    <template slot="paramsVal" slot-scope="text, record">
                      <div class="display-flex align-items">
                        <a-input @input="changeContentVal(record)" style="height: 30px" v-model="record.paramsVal" placeholder="请输入参数名"/>
                        <a-icon v-if="record.id === paramsData.length" @click="addParams" style="margin-left: 5px;cursor: pointer" type="plus"></a-icon>
                        <a-icon v-if="record.id !== paramsData.length" @click="removeParams(record)" style="margin-left: 5px;cursor: pointer;color: #cf1322" type="minus"></a-icon>
                      </div>
                    </template>
                  </a-table>
                </a-tab-pane>
                <a-tab-pane key="2" tab="Body">
                  <MonacoEditor
                    ref="initRequestData"
                    height="400px"
                    language="json"
                    :is-dark="true"
                    :font-size="16"
                    :code="initJson"/>
                </a-tab-pane>
                <a-tab-pane key="3" tab="Header">
                  <a-table :columns="ParamsColumns" :data-source="headersParams" :pagination="false">
                    <template slot="paramsKey" slot-scope="text, record">
                      <a-input style="height: 30px" v-model="record.paramsKey" placeholder="请输入参数名"/>
                    </template>
                    <template slot="paramsVal" slot-scope="text, record">
                      <div class="display-flex align-items">
                        <a-input style="height: 30px" v-model="record.paramsVal" placeholder="请输入参数名"/>
                        <a-icon v-if="record.id === paramsData.length" @click="addHeaders" style="margin-left: 5px;cursor: pointer" type="plus"></a-icon>
                        <a-icon v-if="record.id !== paramsData.length" @click="removeHeaders(record)" style="margin-left: 5px;cursor: pointer;color: #cf1322" type="minus"></a-icon>
                      </div>
                    </template>
                  </a-table>
                </a-tab-pane>
              </a-tabs>
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
import _ from 'lodash'
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
        caseRank: 1,
        requestUrl: 'http://localhost:8080',
        requestType: 1
      },
      initJson: '',
      initResponse: '',
      isPost: true,
      ParamsColumns: [
        {
          title: '参数名',
          dataIndex: 'paramsKey',
          scopedSlots: { customRender: 'paramsKey' }
        },
        {
          title: '参数值',
          dataIndex: 'paramsVal',
          scopedSlots: { customRender: 'paramsVal' }
        }
      ],
      paramsData: [
        {
          id: 1,
          paramsKey: '',
          paramsVal: ''
        }
      ],
      headersParams: [
        {
          id: 1,
          paramsKey: '',
          paramsVal: ''
        }
      ]
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
          if (this.caseVo.requestType === 2 && !this.$refs.initRequestData) {
            this.$message.error('请求方式为 POST 时，请求体不能为空！')
            return
          }
          // 解构赋值
          const { name } = values
          // 封装数据
          this.caseVo.name = name
          this.caseVo.requestData = this.$refs.initRequestData && this.$refs.initRequestData.codeVal
          this.caseVo.expectResponse = this.$refs.responseData && this.$refs.responseData.codeVal
          this.caseVo.moduleId = this.currentModule.id
          this.caseVo.businessDesc = this.caseVo.description
          this.caseVo.headerMap = JSON.stringify(this.headersParams)
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
    },
    // 添加参数
    addParams () {
      if (this.caseVo.requestUrl.trim() === '') {
        this.$message.warning('接口请求地址不能为空！')
        return
      }
      const index = this.paramsData.length
      const param = {
        id: index + 1,
        paramsKey: '',
        paramsVal: ''
      }
      this.paramsData.push(param)
    },
    // 删除参数
    removeParams (param) {
      // 如果删除的是最后一个，直接删除
      if (param.id === this.paramsData.length) {
        this.paramsData = this.paramsData.filter(item => {
          return item.id !== param.id
        })
      } else {
        // 如果删除的不是最后一个，需要重新排序
        const index = param.id
        this.paramsData = this.paramsData.filter(item => {
          return item.id !== param.id
        })
        this.paramsData.forEach(item => {
          if (item.id > index) {
            this.$set(item, 'id', item.id - 1)
          }
        })
      }
      // 截取?前面的链接地址
      let originUrl = this.caseVo.requestUrl.split('?')[0]
      if (param.id === 1 && this.paramsData.length === 2 && this.paramsData[1].paramsKey.trim() === '' && this.paramsData[1].paramsVal.trim() === '') {
        this.caseVo.requestUrl = originUrl
      } else {
        const index = param.id
        const params = this.caseVo.requestUrl.split('?')[1]
        const paramArray = params.split('&')
        // 数组 删除 索引下标为index的元素
        paramArray.splice(index - 1, 1)
        paramArray.forEach((item, index) => {
          if (index === 0) {
            originUrl = `${originUrl}?${item}`
          } else {
            originUrl += `&${item}`
          }
        })
        this.caseVo.requestUrl = originUrl
      }
    },
    // 添加参数
    addHeaders () {
      if (this.caseVo.requestUrl.trim() === '') {
        this.$message.warning('接口请求地址不能为空！')
        return
      }
      const index = this.paramsData.length
      const param = {
        id: index + 1,
        paramsKey: '',
        paramsVal: ''
      }
      this.headersParams.push(param)
    },
    // 删除参数
    removeHeaders (param) {
      // 如果删除的是最后一个，直接删除
      if (param.id === this.headersParams.length) {
        this.headersParams = this.headersParams.filter(item => {
          return item.id !== param.id
        })
      } else {
        // 如果删除的不是最后一个，需要重新排序
        const index = param.id
        this.headersParams = this.headersParams.filter(item => {
          return item.id !== param.id
        })
        this.headersParams.forEach(item => {
          if (item.id > index) {
            this.$set(item, 'id', item.id - 1)
          }
        })
      }
    },
    callback () {},
    changeRequestType (val) {
      this.caseVo.requestType = val
    },
    changeContentKey (record) {
      this.assemblyDataKey(record)
    },
    changeContentVal (record) {
      this.assemblyDataVal(record)
    },
    assemblyDataKey: _.debounce(function (record) {
      if (record.id === 1) {
        this.caseVo.requestUrl += `?${record.paramsKey}=`
      } else {
        this.caseVo.requestUrl += `&${record.paramsKey}=`
      }
    }, 1000),
    assemblyDataVal: _.debounce(function (record) {
      this.caseVo.requestUrl += `${record.paramsVal}`
    }, 1000)
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
