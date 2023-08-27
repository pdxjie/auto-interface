<template>
  <div>
    <a-drawer
      title="选择默认封面"
      placement="right"
      :closable="false"
      width="30%"
      :visible="visible"
      @close="onClose"
    >
      <div class="content">
        <a-list :grid="{ gutter: 16, xs: 1, sm: 3, md: 3, lg: 3, xl: 3, xxl: 4 }" :data-source="defaultImages">
          <a-list-item slot="renderItem" slot-scope="item">
            <a-card @click="selectedImage(item)" :style="{ 'border': item.selected ? '1px solid #1890ff' : ''}">
              <img style="height: 100%;width: 100%" :src="item.image" alt="cover">
            </a-card>
          </a-list-item>
        </a-list>
      </div>
      <div
        :style="{
          position: 'absolute',
          bottom: 0,
          width: '100%',
          borderTop: '1px solid #e8e8e8',
          padding: '10px 16px',
          textAlign: 'right',
          left: 0,
          background: '#fff',
          borderRadius: '0 0 4px 4px',
        }"
      >
        <a-button style="marginRight: 8px;border-radius: 8px" @click="onClose">
          取消
        </a-button>
        <a-button style="border-radius: 8px" type="primary" @click="selectCover">
          确认
        </a-button>
      </div>
    </a-drawer>
  </div>
</template>

<script>
import { defaultImages } from '@/utils/assets_icons'
export default {
  name: 'SelectIcons',
  data () {
    return {
      visible: false,
      defaultImages,
      currentImageUrl: ''
    }
  },
  methods: {
    onClose () {
      this.visible = false
    },
    selectCover () {
      this.$emit('selectedImage', this.currentImageUrl)
      this.visible = false
    },
    selectedImage (item) {
      this.defaultImages.forEach(image => {
        if (image.id === item.id) {
          this.$set(image, 'selected', true)
          this.currentImageUrl = image.image
        } else {
          this.$set(image, 'selected', false)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
