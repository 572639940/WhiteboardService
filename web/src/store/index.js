import { defineStore } from 'pinia' //引入pinia

export const useUser=defineStore("user",{
    state: () =>{
        return  ({
            userInfo
        })
    }
})
