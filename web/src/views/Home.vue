<template>
  <div class="container">
    <div class="create-pina" @click="showCreateWB">
      创建白板
    </div>
    <div class="join-pina" @click="showJoinWB">
      加入会议
    </div>

    <!--  创建会议  -->
    <el-dialog
        v-model="createWBVisible"
        title="Tips"
        width="30%"
        :before-close="handleClose"
    >
      <el-row>
        <el-col :span="6" :offset="6">
          <el-button @click="createWB(true)">创建会议</el-button>
        </el-col>
        <el-col :span="6">
          <el-button @click="createWB">快速创建</el-button>
        </el-col>
      </el-row>
    </el-dialog>

    <!--  加入会议  -->
    <el-dialog
        v-model="joinWBVisible"
        title="Tips"
        width="30%"
        :before-close="handleClose"
    >
      <el-form :model="joinForm" label-width="120px">
        <el-form-item label="邀请码">
          <el-input v-model="joinForm.inviteCode"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="goToWhiteBoard" type="primary">
          加入会议
        </el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {ElMessageBox} from "element-plus";
import {useRouter} from 'vue-router'

const router = useRouter()

const createWBVisible = ref(false)
const joinWBVisible = ref(false)

const joinForm = reactive({
  inviteCode: ''
})

const handleClose = (done) => {
  done()
}
/**
 * 展示对话框方法
 */
const showJoinWB = () => {
  joinWBVisible.value = true
}
const showCreateWB = () => {
  createWBVisible.value = true
}
/**
 * 创建会议
 * @param useDefaultOpt 使用默认配置, 快速创建
 */
const createWB = (useDefaultOpt) => {
  // todo
}

/**
 * 加入会议
 */
const goToWhiteBoard = () => {
  router.push('/classroom')
}
</script>

<style lang="less" scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;

  color: #fff;

  & > div {
    padding: 30px;
    margin: 0 20px;
  }
}

.create-pina {
  background-color: #0db61f;

  &:hover {
    background-color: #34d045;
  }

  &:active {
    background-color: #0a8017;
  }

}

.join-pina {
  background-color: #0055ff;

  &:hover {
    background-color: #4072d5;
  }

  &:active {
    background-color: #093fab;
  }
}
</style>
