<template>
  <div class="margin-t-16 h-full">
    <a-spin :spinning="loading">
      <div style="height: 60%!important;">
        <div class="display-flex align-items">
          <a-input
            v-model="caseVo.requestUrl"
            placeholder="请输入请求地址"
          >
            <a-select slot="addonBefore" @change="changeRequestType" v-model="caseVo.requestType" style="width: 95px">
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
          <a-dropdown-button @click="sendRequest" type="primary" style="margin-left: 10px">
            发送
            <a-menu slot="overlay" @click="sendAndDownload">
              <a-menu-item key="1"> 发送并下载 </a-menu-item>
            </a-menu>
            <a-icon slot="icon" type="down" />
          </a-dropdown-button>
          <a-button icon="save" style="margin-left: 10px" size="default">保存用例</a-button>
        </div>
        <div>
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
              <div style="border: 1px solid #eee;padding: 10px 10px 10px 0px;border-radius: 8px;">
                <MonacoEditorNotDark ref="initRequestData" height="350px" language="json" :code="initJson"/>
              </div>
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
        </div>
      </div>
    </a-spin>
    <div class="margin-t-16" style="border: 1px solid #eee;padding: 10px 10px 10px 0px;border-radius: 8px;">
      <span style="margin-left: 35px;margin-bottom: 10px">响应结果</span>
      <MonacoEditorNotDark ref="initResponseData" height="350px" language="json" :code="initResponse" :read-only="false"/>
    </div>
  </div>
</template>

<script>
import MonacoEditorNotDark from '@/components/MonacoEditorNotDark/index.vue'
import _ from 'lodash'
import { caseRun } from '@/api/case'

export default {
  name: 'ExecuteOnline',
  components: { MonacoEditorNotDark },
  data () {
    return {
      caseVo: {
        requestType: 1,
        requestUrl: 'http://localhost:8080/basic/login'
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
      ],
      loading: false
    }
  },
  methods: {
    // 更新请求方式
    changeRequestType (val) {
      this.caseVo.requestType = val
    },
    // 发送请求
    async sendRequest () {
      this.loading = true
      this.caseVo.requestData = this.$refs.initRequestData && this.$refs.initRequestData.codeVal
      this.caseVo.expectResponse = this.$refs.responseData && this.$refs.responseData.codeVal
      this.caseVo.headerMap = JSON.stringify(this.headersParams)
      const { data } = await caseRun(this.caseVo)
      setTimeout(() => {
        this.initResponse = JSON.stringify(data.result)
        this.formatCode()
        this.loading = false
      }, 1500)
    },
    formatJson (value) {
      try {
        const json = JSON.parse(value)
        return JSON.stringify(json, null, 2)
      } catch (err) {
        return value
      }
    },
    formatCode () {
      this.$refs.initResponseData.editor.onDidChangeModelContent(() => {
        this.$refs.initResponseData.editor.setValue(this.formatJson(this.$refs.initResponseData.editor.getValue()))
      })
    },
    // 发送并下载
    sendAndDownload () {
      console.log('sendAndDownload')
    },
    callback () {},
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
    }, 1000),
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
    }
  }
}
</script>

<style scoped lang='less'>
  ::v-deep .ant-input {
    height: 35px!important;
  }
  ::v-deep .ant-tabs {
    height: 45vh;
    overflow-x: hidden;
    overflow-y: auto;
  }
</style>
