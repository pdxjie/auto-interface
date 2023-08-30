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
              <a-button @click="insertModule" type="primary" icon="plus" shape="circle"></a-button>
              <a-button @click="updateModuleInfo" icon="edit" class="margin-l-10" shape="circle"></a-button>
              <a-button
                type="danger"
                @click="removeModule"
                ghost
                icon="delete"
                class="margin-l-10"
                shape="circle"></a-button>
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
    <!-- 添加或编辑模块 -->
    <AddOrEditModule
      ref="module"
      :itemId="$route.query.id"
      :currentParentId="currentParentId"
      @operateResult="operateResult"
      :type="type"
      @updateFun="updateFun"/>
  </div>
</template>

<script>
import { caseColumns } from '@/views/mine'
import { deleteModule, getModuleList, moduleInfo, updateModule } from '@/api/module'
import AddOrEditModule from '@/views/mine/components/AddOrEditModule'

export default {
  name: 'AutoLabInfo',
  components: { AddOrEditModule },
  data () {
    return {
      treeData: [],
      searchVal: '',
      caseColumns,
      caseData: [],
      currentParentId: '',
      type: '',
      currentModule: null
    }
  },
  mounted () {
    this.moduleListByItemId(this.$route.query.id)
  },
  created () {
    this.moduleListByItemId(this.$route.query.id)
  },
  methods: {
    async moduleListByItemId (itemId) {
      const { data } = await getModuleList(itemId)
      const tempData = []
      data.forEach(item => {
        const temp = {
          children: this.dealChildren(item.children),
          title: item.name,
          key: item.id
        }
        tempData.push(temp)
      })
      this.treeData = tempData
    },
    dealChildren (children) {
      let tempData = []
      if (children && children.length > 0) {
        children.forEach(item => {
          const temp = {
            children: this.dealChildren(item.children),
            title: item.name,
            key: item.id
          }
          tempData.push(temp)
        })
      } else {
        tempData = children
      }
      return tempData
    },
    onSelect (keys, event) {
      this.currentParentId = keys[0]
    },
    onExpand () {
      console.log('Trigger Expand')
    },
    handleChangeItem () {},
    insertModule () {
      this.type = 'insert'
      this.$refs.module.visible = true
      this.$nextTick(() => {
        this.$refs.module.form.setFieldsValue({
          name: '',
          sort: 0
        })
      })
    },
    operateResult (data) {
      if (data.code === 200) {
        this.$message.success('操作成功！')
        this.moduleListByItemId(this.$route.query.id)
        this.$refs.module.visible = false
      } else {
        this.$message.warning('操作失败！')
        this.$refs.module.visible = false
      }
    },
    removeModule () {
      if (this.currentParentId === '') {
        this.$message.warning('请选择模块！')
        return
      }
      this.$confirm({
        title: '确定要删除该模块吗?',
        content: '删除该模块后，其下的用例将会自动移入公共模块中，请注意查收！',
        okText: '确认',
        okType: 'danger',
        cancelText: '取消',
        onOk: () => {
          this.removeModuleById(this.currentParentId)
        },
        onCancel: () => {
          this.$message.info('取消删除！')
        }
      })
    },
    removeModuleById () {
      deleteModule(this.currentParentId).then(res => {
        if (res.code === 200) {
          this.$message.success('删除成功！')
          this.moduleListByItemId(this.$route.query.id)
        }
      })
    },
    async updateModuleInfo () {
      if (this.currentParentId === '') {
        this.$message.warning('请选择模块！')
        return
      }
      this.type = 'update'
      const { data } = await moduleInfo(this.currentParentId)
      this.currentModule = data
      this.$refs.module.visible = true
      this.$nextTick(() => {
        this.$refs.module.form.setFieldsValue(data)
      })
    },
    updateFun (data) {
      console.log(data, 'data')
      this.currentModule.name = data.name
      this.currentModule.sort = data.sort
      updateModule(this.currentModule).then(res => {
        if (res.code === 200) {
          this.$message.success('更新成功！')
          this.moduleListByItemId(this.$route.query.id)
          this.$refs.module.visible = false
        }
      })
    }
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
