<template>

    <div class="el-dialog" >
        <!--标题-->
        <el-row class="row-bg" justify="space-evenly">
            <span class="titleUser">用户注册</span>
        </el-row>

        <el-row class="row-bg" justify="space-evenly">
            <el-form ref="ruleFormRef" :model="userItem" :rules="rules" label-width="120px" class="demo-ruleForm" status-icon>

                <el-form-item label="手机号码" prop="phone">
                    <el-input v-model="userItem.phone"  placeholder="请输入手机号码"/>
                </el-form-item>

                <el-form-item label="验证码" prop="code">
                    <el-input v-model="userItem.code"  placeholder="请输入验证码"/>
                </el-form-item>

                <el-form-item label="名称" prop="nickName">
                    <el-input v-model="userItem.nickName"  placeholder="请输入名称"/>
                </el-form-item>

                <el-form-item label="密码" prop="password">
                    <el-input v-model="userItem.passWord" type="password" autocomplete="off" placeholder="请输入密码"/>
                </el-form-item>

                <el-form-item label="确认密码" prop="password">
                    <el-input v-model="userItem.confirmPassWord" type="password" autocomplete="off"  placeholder="请输入密码"/>
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="onSubmit">注册</el-button>
                    <el-button @click="clearFrom">清空</el-button>
                    <el-button @click="sendLogin">去登录</el-button>
                </el-form-item>
            </el-form>
        </el-row>

    </div>

</template>

<script>
    import { reactive, ref } from 'vue'

    const size = ref('default')
    const labelPosition = ref('top')

    export default {
        name: "App1", //定义组件名，默认是不包含扩展名的文件名
        // 定义数据
        data() {
            return {
                userItem:{
                    passWord:null,
                    confirmPassWord:null
                },
                rules: {
                    phone: [
                        { required: true, message: '请输入手机号码' },
                        { min: 11, max: 11, message: '长度必须为 11', trigger: 'blur' }
                    ],
                    code: [
                        { required: true, message: '请输入验证码' },
                        { min: 6, max: 6, message: '长度在 6', trigger: 'blur' }
                    ],
                    nickName: [
                        { required: true, message: '请输入名称', trigger: 'blur' },
                        { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 8, max: 16, message: '长度在 8 到 16 个字符', trigger: 'blur' }
                    ],
                }
            };
        },
        methods:{
            //表单提交
            onSubmit(){
                alert("123456");
            },
            //清空表单
            clearFrom(){
                this.userItem = {}
            },
            //去登录
            sendLogin(){
                this.$router.push("/login")
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
