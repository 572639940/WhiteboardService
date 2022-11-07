<template>
  <div class="container">
    <el-container>
      <el-header>头部操作栏</el-header>
      <el-container>
        <el-container>
          <el-main>
            <whiteboard></whiteboard>
          </el-main>
        </el-container>
        <el-aside>房间人员列表/聊天列表</el-aside>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import {onMounted, onUpdated, ref} from "vue";
import Whiteboard from "./whiteboard.vue";

</script>

<style lang="less" scoped>
.container {
  height: 100%;

  &.el-header {
    border-bottom: 1px solid #999;
  }

  & > .el-container {
    height: 100%;


    &.el-aside {
      width: 100px;
    }

    //& > .el-aside {
    //  height: 100%;
    //  background: #545c64;
    //}
    //
    //& > .el-container {
    //  & > .el-header {
    //    padding: 0%;
    //  }
    //}
  }
}

</style>
