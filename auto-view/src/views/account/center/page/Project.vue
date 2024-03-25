<template>
  <div class="user-list">
    <!-- condition -->
    <a-card>
      <div class="condition display-flex align-items">
        <div class="display-flex align-items" style="width: 25%">
          <span>邮箱：</span>
          <a-input v-model="userVo.email" style="width: 80%" placeholder="用户邮箱..." />
        </div>
        <div class="display-flex align-items margin-l-10" style="width: 25%">
          <span>昵称：</span>
          <a-input v-model="userVo.nickName" style="width: 80%" placeholder="用户昵称..." />
        </div>
        <div class="margin-l-14" style="width: 25%">
          <a-range-picker size="default" @change="selectDate"/>
        </div>
        <div style="width: 20%">
          <a-button class="margin-l-10" style="border-radius: 10px" type="primary" @click="searchOperate">搜索</a-button>
          <a-button class="margin-l-10" style="border-radius: 10px" @click="resetVo">重置</a-button>
        </div>
      </div>
    </a-card>
    <a-card style="margin-top: 15px;">
      <a-table
        :loading="loading"
        :columns="UserColumn"
        :pagination="pagination"
        :data-source="data"
        :scroll="{ x: 1800 }"
        @change="handleTableChange"
        :row-key="record => record.id">
        <template slot="avatar" slot-scope="text, record">
          <a-avatar size="large" shape="square" :src="record.avatar" />
        </template>
        <template slot="email" slot-scope="text, record">
          <ellipsis :length="16" :tooltip="true">{{ record.email }}</ellipsis>
        </template>
        <template slot="sex" slot-scope="text, record">
          <a-tag color="green" v-if="record.sex === 0">男</a-tag>
          <a-tag color="orange" v-if="record.sex === 1">女</a-tag>
          <a-tag v-if="record.sex === 2">未知</a-tag>
        </template>
        <template slot="createTime" slot-scope="text, record">
          <span>{{ record.createTime | fromNow }}</span>
        </template>
        <template slot="online" slot-scope="text, record">
          <a-tag color="green" v-if="record.online">在线</a-tag>
          <a-tag v-else>离线</a-tag>
        </template>
        <template slot="role" slot-scope="text, record">
          <a-tag color="green" v-if="record.role === '管理员'">{{ record.role }}</a-tag>
          <a-tag v-else>{{ record.role }}</a-tag>
        </template>
        <template slot="status" slot-scope="text, record">
          <a-switch checked-children="正常" un-checked-children="禁用" :checked="!record.status" @change="changeStatus(record)"/>
        </template>
        <template slot="action" slot-scope="text, record">
          <a-popconfirm
            title="确定要删除该用户信息吗?"
            ok-text="确认"
            cancel-text="取消"
            @confirm="confirm(record.id)"
            @cancel="cancel"
          >
            <a-button type="danger" shape="circle" icon="delete" />
          </a-popconfirm>
          <a-button @click="updateUser(record.id)" type="primary" class="margin-l-10" shape="circle" icon="edit" />
        </template>
      </a-table>
    </a-card>

    <!-- 修改用户信息 -->
    <EditorUserInfo ref="userInfo" :userInfo="userInfo" @updateUserInfo="updateUserInfo"/>
  </div>
</template>

<script>
import moment from 'moment'
import { UserColumn } from '../index'
import { delUser, forbidden, getUserInfo, userPage } from '@/api/system'
import Ellipsis from '@/components/Ellipsis/Ellipsis'
import EditorUserInfo from '../../components/EditorUserInfo.vue'
export default {
  name: 'User',
  components: { EditorUserInfo, Ellipsis },
  data () {
    return {
      userVo: {
        email: '',
        nickName: '',
        startTime: '',
        endTime: '',
        current: 1,
        pageSize: 10
      },
      userInfo: {},
      UserColumn,
      data: [],
      loading: false,
      // 分页参数
      pagination: {
        size: 'large',
        current: 1,
        pageSize: 10,
        total: 0,
        showTotal: (total, range) => {
          return ' 共' + total + '条'
        }
      },
      visible: false
    }
  },
  created () {
    this.searchOperate()
  },
  filters: {
    fromNow (date) {
      return moment(date).format('YYYY-MM-DD')
    }
  },
  computed: {
    userId () {
      return this.$store.getters.userInfo.userInfo.userId
    }
  },
  methods: {
    // 表格改变时触发
    handleTableChange (pagination, filters, sorter) {
      this.pagination = pagination
      this.userVo.current = pagination.current
      this.userVo.pageSize = pagination.pageSize
      this.searchOperate()
    },
    selectDate (val, date) {
      this.userVo.startTime = date[0]
      this.userVo.endTime = date[1]
    },
    cancel () {
      this.$message.info('取消删除！')
    },
    confirm (id) {
      if (id === this.userId) {
        this.$message.error('不能删除自己！')
        return
      }
      this.deleteUser(id)
    },
    async searchOperate () {
      this.loading = true
      const { data } = await userPage(this.userVo)
      this.data = data.users
      this.pagination.total = data.total
      this.loading = false
    },
    async changeStatus (record) {
      const data = await forbidden(record.id)
      if (data.code === 200) {
        this.$message.success('更新成功！')
        this.searchOperate()
      }
    },
    async deleteUser (id) {
      const data = await delUser(id)
      if (data.code === 200) {
        this.$message.success('删除成功！')
        this.searchOperate()
      }
    },
    async updateUser (id) {
      const { data } = await getUserInfo(id)
      this.userInfo = data
      this.$refs.userInfo.visible = true
    },
    setRoleOperate (record) {
    },
    resetVo () {
      this.userVo = {
        email: '',
        nickName: '',
        startTime: '',
        endTime: ''
      }
      this.searchOperate()
    },
    updateUserInfo (code) {
      if (code === 200) {
        this.searchOperate()
      }
    }
  }
}
</script>

<style scoped lang="less">
::v-deep .ant-card {
  border-radius: 10px;
}
::v-deep .ant-card-body {
  padding: 15px!important;
}
</style>