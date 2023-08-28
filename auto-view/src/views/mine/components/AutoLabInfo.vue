<template>
  <div class="auto-lab-info" style="margin-top: -20px">
    <div class="margin-t-16 card-layout">
      <div class="margin-b-16">
        <a-button type="primary" style="height: 40px" icon="plus">创建用例</a-button>
        <a-button icon="upload" style="height: 40px;margin-left: 10px">导入</a-button>
      </div>
      <a-card style="border-radius: 8px">
        <a-layout id="components-layout-demo-side" style="min-height: 100%;">
          <a-layout-sider style="background: #fff;padding-right: 10px;border-right: 1px solid #eee;min-width: 220px;position:relative;">
            <div class="margin-t-16">
              <!-- 选择项目 -->
              <div>
                <span class="margin-r-10">项目:</span>
                <a-select default-value="lucy" style="width: 130px" @change="handleChangeItem">
                  <a-select-option value="jack">
                    周计划优化
                  </a-select-option>
                  <a-select-option value="lucy">
                    未读消息发送邮件服务
                  </a-select-option>
                </a-select>
              </div>
              <!-- 模块展示 -->
              <a-directory-tree
                showIcon
                :tree-data="treeData"
                multiple
                default-expand-all
                @select="onSelect"
                @expand="onExpand">
                <template slot="custom" slot-scope="{ selected }">
                  <a-icon style="position:absolute;right: 0px" :type="selected ? 'frown' : 'frown-o'" />
                </template>
              </a-directory-tree>
            </div>
            <div class="bottom-operate display-flex position-absolute" style="bottom: 10px;left: 0px">
              <a-button type="primary" icon="plus" shape="circle"></a-button>
              <a-button type="danger" ghost icon="delete" class="margin-l-10" shape="circle"></a-button>
            </div>
          </a-layout-sider>
          <a-layout>
            <a-layout-content style="background: #fff;padding-left: 10px">
              <div class="display-flex justify-between">
                <div class="margin-t-16">
                  <div class="font-size-18 font-w-500">全部用例（234）</div>
                </div>
                <div class="margin-t-16">
                  <a-input ref="userNameInput" v-model="searchVal" placeholder="通过用例名称检索">
                    <a-icon slot="prefix" type="search" />
                  </a-input>
                </div>
              </div>
              <!-- 用例列表 -->
              <div class="margin-t-10">
                <a-table :columns="caseColumns" :data-source="caseData">
                </a-table>
              </div>
            </a-layout-content>
          </a-layout>
        </a-layout>
      </a-card>
    </div>
  </div>
</template>

<script>
import { caseColumns } from '@/views/mine'

export default {
  name: 'AutoLabInfo',
  data () {
    return {
      treeData: [
        {
          title: '公共用例（3030）',
          key: '0-0',
          scopedSlots: { icon: 'custom' },
          children: [
            {
              title: '公共用例1',
              key: '0-1',
              scopedSlots: { icon: 'custom' }
            }
          ]
        }
      ],
      searchVal: '',
      caseColumns,
      caseData: []
    }
  },
  mounted () {
    console.log(this.$route.query.id)
  },
  methods: {
    onSelect (keys, event) {
      console.log('Trigger Select', keys, event)
    },
    onExpand () {
      console.log('Trigger Expand')
    },
    handleChangeItem () {}
  }
}
</script>

<style scoped lang='less'>
.card-layout {
  height: calc(100vh - 264px);
  ::v-deep .ant-card {
    height: 100%;
    .ant-card-body {
      height: 100% !important;
      padding-top: 0px;
      padding-bottom: 0px;
    }
  }
}
::v-deep .ant-select-selection {
  border: unset;
}

</style>
