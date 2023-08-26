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
    >
      <a-list-item slot="renderItem" slot-scope="item">
        <template v-if="item === null">
          <a-button class="new-btn" style="height: 361px" type="dashed">
            <a-icon type="plus"/>
            新增项目
          </a-button>
        </template>
        <template v-else>
          <a-card class="ant-pro-pages-list-projects-card custom-card" hoverable>
            <template slot="actions" class="ant-card-actions">
              <span>
                <a-tooltip placement="top">
                  <template slot="title">
                    <span>编辑项目</span>
                  </template>
                  <a-icon key="edit" type="edit" />
                </a-tooltip>
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
              <img @click="lookDetail(item)" style="border-radius: 50%;width: 100px;height: 100px" slot="cover" :src="item.covers" :alt="item.name" />
            </div>
            <a-card-meta :title="item.name" style="height: 73px!important;" @click="lookDetail(item)">
              <template slot="title">
                <ellipsis :length="20">{{ item.name }}</ellipsis>
              </template>
              <template slot="description">
                <ellipsis :length="50">{{ item.description }}</ellipsis>
              </template>
            </a-card-meta>
            <div style="height: 100px;display: flex;flex-direction: column;justify-content: space-around">
              <div>
                <a-tooltip placement="topLeft">
                  <template slot="title">
                    <span>自动化测试进度</span>
                  </template>
                  <a-progress showInfo :percent="60" />
                </a-tooltip>
              </div>
              <div class="font-size-14" style="color: #a8a8a8">
                <div>
                  <a-tooltip placement="topLeft">
                    <template slot="title">
                      <span>自动化测试周期</span>
                    </template>
                    <span>2023-08-23</span> ~ <span>2023-08-24</span>
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
  </div>
</template>

<script>

import { DEVICE_TYPE } from '@/utils/device'
import { mapGetters, mapState } from 'vuex'
import moment from 'moment'
import { TagSelect, StandardFormRow, Ellipsis, AvatarList } from '@/components'
const TagSelectOption = TagSelect.Option
const AvatarListItem = AvatarList.Item
const dataSource = []
dataSource.push(null)
for (let i = 0; i < 11; i++) {
  dataSource.push({
    name: 'Alipay',
    covers: 'https://edu-2330.oss-cn-beijing.aliyuncs.com/icon-images/1.jpg',
    description: '在中台产品的研发过程中，会出现不同的设计规范和实现方式，但其中往往存在很多类似的页面和组件，这些类似的组件会被抽离成一套标准规范。',
    percent: i === 0 ? 15 : (i * 10),
    createTime: '2023-0' + (i + 1) + '-20',
    dateLine: '2023-0' + (i + 1) + '-25',
    status: Math.floor(Math.random() * 3) + 1
  })
}

export default {
  components: {
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
      dataSource,
      itemVo: {}
    }
  },
  methods: {
    isMobile () {
      return this.device === DEVICE_TYPE.MOBILE
    },
    toSearch () {},
    toResetCondition () {
      this.itemVo = {}
    },
    toRelase () {},
    lookDetail (item) {},
    copyAddress (item) {}
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
