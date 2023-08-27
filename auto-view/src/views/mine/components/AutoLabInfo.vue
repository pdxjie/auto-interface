<template>
  <div class="auto-lab-info" style="margin-top: -20px">
    <div class="margin-t-16 card-layout">
      <a-card style="border-radius: 8px">
        <a-layout id="components-layout-demo-side" style="min-height: 100%;">
          <a-layout-sider style="background: #fff;padding-right: 10px;border-right: 1px solid #eee;min-width: 220px">
            <div class="margin-t-16">
              <!-- 选择项目 -->
              <div>
                <span class="margin-r-10">项目:</span>
                <a-select default-value="lucy" style="width: 130px" @change="handleChangeItem">
                  <a-select-option value="jack">
                    Jack
                  </a-select-option>
                  <a-select-option value="lucy">
                    Lucy
                  </a-select-option>
                  <a-select-option value="disabled" disabled>
                    Disabled
                  </a-select-option>
                  <a-select-option value="Yiminghe">
                    yiminghe
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
          </a-layout-sider>
          <a-layout>
            <a-layout-content style="background: #fff;padding-left: 10px">
              <div class="margin-t-16">
                Bill is a cat.
              </div>
            </a-layout-content>
          </a-layout>
        </a-layout>
      </a-card>
    </div>
  </div>
</template>

<script>
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
      ]
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
