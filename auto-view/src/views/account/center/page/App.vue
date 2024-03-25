<template>
  <a-card style='border-radius: 5px;'>
    <a-list item-layout='vertical' size='large' :pagination='pagination' :loading='loading' :data-source='listData'>
      <a-list-item slot='renderItem' key='item.title' slot-scope='item'>
        <template slot='actions'>
            <span style='display: flex;align-items: center;gap: 5px'>
              <a-icon  type='star-o' @click='collectItemInfo(item)' v-if='!item.collect'></a-icon>
              <a-icon type='star-o' @click='unCollectItemInfo(item)' v-if='item.collect' style='color: #f3ca32'></a-icon>
              <span>{{ item.collectCount }}</span>
            </span>
        </template>
        <img
          slot='extra'
          width='272'
          alt='logo'
          :src='item.cover'
          @click='lookDetail(item.id)'
          style='height: 150px;object-fit: cover'/>
        <a-list-item-meta @click='lookDetail(item.id)' :description='item.description'>
          <a slot='title'>{{ item.name }}</a>
          <a-avatar slot='avatar' :src='item.avatar' />
        </a-list-item-meta>
      </a-list-item>
    </a-list>
  </a-card>
</template>
<script>
import { likeItem, myCollectItems, unlikeItem } from '@/api/item'

export default {
  data () {
    return {
      listData: [],
      itemVo: {
        name: '',
        current: 1,
        pageSize: 5
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
      loading: false
    }
  },
  computed: {
    userId () {
      return this.$store.getters.userInfo.userInfo.userId
    }
  },
  async created () {
    await this.getPublishItems()
  },
  methods: {
    // 表格改变时触发
    handleTableChange (pagination, filters, sorter) {
      this.pagination = pagination
      this.itemVo.current = pagination.current
      this.itemVo.pageSize = pagination.pageSize
      this.getPublishItems()
    },
    async getPublishItems (loading = true) {
      this.loading = loading
      const { data } = await myCollectItems(this.itemVo)
      this.listData = data.items
      this.pagination.total = data.total
      this.loading = false
      console.log(data, 'publish items')
    },
    async likeItemInfo (item) {
      const vo = {
        userId: this.userId,
        itemId: item.id,
        type: 1
      }
      const data = await likeItem(vo)
      if (data.code === 200) {
        await this.getPublishItems(false)
      }
      console.log('like item')
    },
    async unlikeItemInfo (item) {
      const vo = {
        userId: this.userId,
        itemId: item.id,
        type: 1
      }
      const data = await unlikeItem(vo)
      if (data.code === 200) {
        await this.getPublishItems(false)
      }
      console.log('unlike item')
    },
    async collectItemInfo (item) {
      const vo = {
        userId: this.userId,
        itemId: item.id,
        type: 2
      }
      const data = await likeItem(vo)
      if (data.code === 200) {
        await this.getPublishItems(false)
      }
      console.log('like item')
    },
    async unCollectItemInfo (item) {
      const vo = {
        userId: this.userId,
        itemId: item.id,
        type: 2
      }
      const data = await unlikeItem(vo)
      if (data.code === 200) {
        await this.getPublishItems(false)
      }
      console.log('unlike item')
    },
    lookDetail (id) {
      this.$router.push({
        path: '/auto',
        query: {
          id: id,
          public: true
        }
      })
    }
  }
}
</script>
<style></style>