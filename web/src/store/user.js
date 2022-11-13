import {defineStore} from 'pinia' //引入pinia
import axios from "axios";
import Request from "../utils/request.js";

export const useUserStore = defineStore("user", {
    state: () => {
        return ({
            userInfo: {},
            token: '',
            tokenHead: ''
        })
    },
    actions: {
        login(user) {
            return Request({
                method: 'post',
                url: 'http://localhost:8080/api/auth/login',
                data: user
            })
        },
        register(user) {
            return axios({
                method: 'post',
                url: '/api/auth/login',
                data: user
            }).then((res) => {
                this.token = res.data.token
                this.tokenHead = res.data.tokenHead
            })
        }
    }
})
