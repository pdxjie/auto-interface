<template>
  <div class="card-list" ref="content">
    <a-card style="border-radius: 10px;line-height: 30px;margin-bottom: 15px">
      <div class="display-flex flex-wrap align-items">
        <div class="display-flex flex-wrap align-items justify-content" style="width: 100%">
          <a-input
            style="height: 40px"
            @pressEnter="toSearch"
            allowClear
            v-model="itemVo.name"
            placeholder="根据项目名称检索项目..."
            :style="{ width: isMobile() ? '100%' : '30%' }"></a-input>
          <div :style="{ marginTop: isMobile() ? '10px' : '', width: isMobile() ? '100%' : '', marginLeft: isMobile() ? '' : '20px'}" class="display-flex justify-between">
            <div>
              <a-button @click="toSearch" type="primary" style="border-radius: 10px;height: 40px" :style="{float: isMobile() ? 'right' : ''}">快速查询 </a-button>
            </div>
          </div>
        </div>
      </div>
    </a-card>
    <a-list
      :grid="{gutter: 24, lg: 4, md: 2, sm: 1, xs: 1}"
      :dataSource="dataSource"
      :pagination="pagination"
      :loading="loading"
    >
      <a-list-item slot="renderItem" slot-scope="item">
        <template v-if="item === null">
          <a-button @click="insertItemInfo" class="new-btn" style="height: 361px" type="dashed">
            <a-icon type="plus"/>
            新增项目
          </a-button>
        </template>
        <template v-else>
          <a-card class="ant-pro-pages-list-projects-card custom-card" hoverable>
            <template slot="actions" class="ant-card-actions">
              <span v-if="item.itemOwner">
                <a-tooltip placement="top">
                  <template slot="title">
                    <span>编辑项目</span>
                  </template>
                  <a-icon @click="updateItemInfo(item)" key="edit" type="edit" />
                </a-tooltip>
              </span>
              <span v-if="item.itemOwner">
                <a-popconfirm
                  title="确定要删除该项目吗?"
                  ok-text="确定"
                  cancel-text="取消"
                  @confirm="confirm(item.id)"
                  @cancel="cancel"
                >
                  <a-icon style="color: red" key="delete" type="delete" />
                </a-popconfirm>
              </span>
              <span>
                <a-tooltip placement="top">
                  <template slot="title">
                    <span>克隆项目</span>
                  </template>
                  <a-icon key="copy" type="copy" @click="copyAddress(item)"/>
                </a-tooltip>
              </span>
              <span>
                <span style="color: #fbba1a" v-if="item.status === 1">未开始</span>
                <span style="color: #25b29e" v-if="item.status === 2">进行中</span>
                <span style="color: #a8a8a8" v-if="item.status === 3">已结束</span>
              </span>
            </template>
            <div style="height: 100px;text-align: center">
              <img @click="lookDetail(item.id)" style="border-radius: 50%;width: 100px;height: 100px" slot="cover" :src="item.cover" :alt="item.name" />
            </div>
            <a-card-meta :title="item.name" style="height: 73px!important;" @click="lookDetail(item.id)">
              <template slot="title">
                <ellipsis :length="20">{{ item.name }}</ellipsis>
              </template>
              <template slot="description">
                <ellipsis :length="50">{{ item.description }}</ellipsis>
              </template>
            </a-card-meta>
            <div style="height: 100px;display: flex;flex-direction: column;justify-content: space-around" @click="lookDetail(item.id)">
              <div>
                <a-tooltip placement="topLeft">
                  <template slot="title">
                    <span>自动化测试进度</span>
                  </template>
                  <a-progress showInfo :percent="item.percent" />
                </a-tooltip>
              </div>
              <div class="font-size-14" style="color: #a8a8a8">
                <div>
                  <a-tooltip placement="topLeft">
                    <template slot="title">
                      <span>自动化测试周期</span>
                    </template>
                    <span>{{ item.createTime | fromNow }}</span> ~ <span>{{ item.dateLine | fromNow }}</span>
                  </a-tooltip>
                </div>
              </div>
              <div class="admin">
                <div>
                  <span class="title" style="margin-right: 5px"></span><span>负责人</span> <span class="margin-l-10">派大星</span>
                </div>
              </div>
            </div>
          </a-card>
        </template>
      </a-list-item>
    </a-list>
    <!-- 操作项目 -->
    <ItemOperate @operateSuccess="operateSuccess" :type="type" ref="itemOperate" />
  </div>
</template>

<script>

import { DEVICE_TYPE } from '@/utils/device'
import { mapGetters, mapState } from 'vuex'
import moment from 'moment'
import { TagSelect, StandardFormRow, Ellipsis, AvatarList } from '@/components'
import { homePages, removeItemById } from '@/api/item'
import ItemOperate from '@/views/mine/components/ItemOperate'
const TagSelectOption = TagSelect.Option
const AvatarListItem = AvatarList.Item
export default {
  components: {
    ItemOperate,
    AvatarList,
    AvatarListItem,
    Ellipsis,
    TagSelect,
    TagSelectOption,
    StandardFormRow
  },
  name: 'CardList',
  data () {
    return {
      dataSource: [],
      itemVo: {
        name: '',
        current: 1,
        pageSize: 11
      },
      pagination: {
        size: 'large',
        current: 1,
        pageSize: 10,
        total: 0,
        showTotal: (total, range) => {
          return ' 共' + total + '条'
        }
      },
      loading: false,
      type: '',
      currentId: ''
    }
  },
  filters: {
    fromNow (date) {
      return moment(date).format('YYYY-MM-DD')
    }
  },
  mounted () {
    this.searchOperate()
  },
  methods: {
    isMobile () {
      return this.device === DEVICE_TYPE.MOBILE
    },
    insertItemInfo () {
      this.type = 'insert'
      this.$refs.itemOperate.visible = true
      this.$refs.itemOperate.itemInfo = {}
      this.$refs.itemOperate.iconUrl = ''
    },
    // 表格改变时触发
    handleTableChange (pagination, filters, sorter) {
      this.pagination = pagination
      this.itemVo.current = pagination.current
      this.itemVo.pageSize = pagination.pageSize
      this.searchOperate()
    },
    toSearch () {
      this.searchOperate()
    },
    async searchOperate () {
      this.loading = true
      const { data } = await homePages(this.itemVo)
      const items = []
      items.push(null)
      this.pagination.total = data.total
      this.dataSource = data.items.concat(items)
      this.loading = false
    },
    toRelase () {},
    // 进入项目，查看详情
    lookDetail (id) {
      this.$router.push({
        path: '/auto',
        query: {
          id: id
        }
      })
    },
    copyAddress (item) {},
    updateItemInfo (item) {
      this.type = 'update'
      this.$refs.itemOperate.itemInfo = item
      this.$refs.itemOperate.iconUrl = item.cover
      this.$refs.itemOperate.visible = true
    },
    operateSuccess (code) {
      if (code === 200) {
        this.searchOperate()
      }
    },
    // 移除项目
    async removeItem (id) {
      const data = await removeItemById(id)
      if (data.code === 200) {
        this.$message.success('移除成功')
        this.searchOperate()
      }
    },
    // 确认删除
    confirm (id) {
      this.removeItem(id)
    },
    // 取消删除
    cancel () {
      this.$message.info('已取消')
    }
  },
  computed: {
    ...mapGetters(['userInfo', 'token']),
    ...mapState({
      device: state => state.app.device
    })
  }
}
</script>

<style lang="less" scoped>
.card-avatar {
  width: 48px;
  height: 48px;
  border-radius: 48px;
}

.ant-card-actions {
  background: #f7f9fa;
  li {
    float: left;
    text-align: center;
    margin: 12px 0;
    color: rgba(0, 0, 0, 0.45);
    width: 50%;

    &:not(:last-child) {
      border-right: 1px solid #e8e8e8;
    }

    a {
      color: rgba(0, 0, 0, .45);
      line-height: 22px;
      display: inline-block;
      width: 100%;
      &:hover {
        color: #1890ff;
      }
    }
  }
}

.new-btn {
  background-color: #fff;
  border-radius: 2px;
  width: 100%;
  height: 188px;
}

.meta-content {
  position: relative;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  height: 64px;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.admin {
  position: relative;
  .title::before {
    position: absolute;
    top: 50%;
    transform: translate(-50%);
    content: '';
    display: inline-block;
    width: 4px;
    height: 4px;
    border-radius: 50%;
    background: #d6d6d6;
  }
}
</style>
