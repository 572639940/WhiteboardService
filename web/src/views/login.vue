<template>

    <div class="el-dialog" >
        <!--标题-->
        <el-row class="row-bg" justify="space-evenly">
            <span class="titleUser">用户登录</span>
        </el-row>

        <el-row class="row-bg" justify="space-evenly">
            <el-form ref="ruleFormRef" :model="authUserForm" :rules="rules" label-width="120px" class="demo-ruleForm" status-icon>

                <el-form-item label="用户名" prop="username">
                    <el-input v-model="authUserForm.username" placeholder="请输入账号"/>
                </el-form-item>

                <el-form-item label="密码" prop="password">
                    <el-input v-model="authUserForm.password" type="password" autocomplete="off" placeholder="请输入密码"/>
                </el-form-item>


                <el-form-item>
                    <el-button type="primary" @click="login">登录</el-button>
                    <el-button @click="clearFrom">清空</el-button>
<!--                    <el-button @click="sendRegistered">去注册</el-button>-->
                </el-form-item>
            </el-form>
        </el-row>

    </div>

</template>

<script>
    import { reactive, ref } from 'vue'
    import {useUserStore} from "../store/user.js";

    const size = ref('default')
    const labelPosition = ref('top')

    export default {
        name: "App1", //定义组件名，默认是不包含扩展名的文件名
        // 定义数据
        data() {
            return {
                authUserForm:{
                    username:'',
                    password:null,
                },
                rules: {
                    phone: [
                        { required: true, message: '请输入手机号码' },
                        { min: 11, max: 11, message: '长度必须为 11', trigger: 'blur' }
                    ],
                    // password: [
                    //     { required: true, message: '请输入密码', trigger: 'blur' },
                    //     { min: 8, max: 16, message: '长度在 8 到 16 个字符', trigger: 'blur' }
                    // ],
                }
            };
        },
        methods:{
            //表单提交
            login(){
              try {
                const userStore = useUserStore()
                userStore.login(this.authUserForm).then((res) => {
                  console.log(res)
                  userStore.token = res.data.token
                  userStore.tokenHead = res.data.tokenHead
                })
              }catch (e) {
                console.log('登录失败')
              }
              this.$router.push("/classroom")
            },
            //清空表单
            clearFrom(){
                this.authUserForm = {}
            },
            //去注册
            sendRegistered(){
                this.$router.push("/registered")
            }
        },

    };
</script>


<style>
    .el-radio-group {
        margin-right: 12px;
    }
    .el-dialog .el-dialog__body {
        flex: 1;
        overflow: auto;
    }
    .titleUser{
        padding-top: 30px;
        padding-bottom: 30px;
        font-size: 32px
    }
</style>
