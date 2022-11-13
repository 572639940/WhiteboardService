<template>
  <div class="container">
    <el-container>
      <el-header>
        <el-menu
            :default-active="1"
            class="el-menu-demo flex items-center "
            mode="horizontal"
            :ellipsis="false"
            @select="handleSelect"
        >
          <el-menu-item index="0" class="hover-bgc-fff">
            首页
          </el-menu-item>
          <el-menu-item index="1" disabled>房间名称</el-menu-item>
          <div class="flex-grow"/>
          <el-menu-item index="2">退出房间</el-menu-item>
        </el-menu>


      </el-header>
      <el-container>
        <el-container>
          <el-main>
            <whiteboard></whiteboard>
          </el-main>
        </el-container>
        <el-aside>
          <el-tabs class="users-table" type="border-card">
            <el-tab-pane label="在线人员">
              <div class="user-info">
                <el-image :src="url" fit="cover">
                  <template #error>
                    <div class="image-slot">
                      <el-icon>
                        <PictureFilled/>
                      </el-icon>
                    </div>
                  </template>
                </el-image>
                <span>
                  用户名
                </span>
                <el-popconfirm title="确定要提出房间?" class="right">
                  <template #reference>
                    <el-button>踢出</el-button>
                  </template>
                </el-popconfirm>
              </div>
            </el-tab-pane>
            <el-tab-pane label="聊天室">TODO</el-tab-pane>
          </el-tabs>
        </el-aside>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import {onMounted, onUpdated, ref} from "vue";
import Whiteboard from "./whiteboard.vue";
import {Picture, PictureFilled} from '@element-plus/icons-vue'

const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

</script>

<style lang="less" scoped>
.container {
  height: 100%;

  & > .el-container {
    height: 100%;
  }
}

header.el-header {
  border-bottom: 1px solid #999;
}

.el-aside {
  border-left: 1px #999 solid;
}

.el-main {
  padding: 0;
}

.el-tabs {
  height: calc(100% - 7px);
}

.el-header {
  display: flex;
  justify-content: start;
  align-items: center;
}

// 展示在房间用户
.user-info {
  vertical-align: center;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 10px;
  position: relative;

  .el-image {
    width: 40px;
    height: 40px;
    border-radius: 8px;
  }

  span {
  }
}

// 头像加载失败
.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 30px;
}

// 头部标签栏
.el-page-header {
  display: flex;
  justify-content: start;
  align-items: center;
}

.el-menu {
  width: 100%;
}

</style>
<style>
.flex {
  display: flex;
}

.items-center {
  align-items: center;
}

.text-large {
  font-size: large;
}

.text-sm {
  font-size: small;
}

.font-600 {
  font-weight: 600;
}

.ml-2 {
  margin-left: 0.5rem;
}

.mr-2 {
  margin-right: 0.5rem;
}

.mr-3 {
  margin-right: 0.75rem;
}

.flex-grow {
  flex-grow: 1;
}

.jus-con-center {
  justify-content: center;
}

.hover-bgc-fff:hover {
  background-color: #fff;
}


</style>
