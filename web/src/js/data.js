export const saveToStorage = (key, data) => {
    localStorage.setItem(key, data)
    console.debug(`数据已保存`)
    console.debug(JSON.stringify(data))
}

export const loadInStore = (key) => {
    let data = localStorage.getItem(key)
    console.debug(`读取到数据`)
    console.debug(JSON.stringify(data))
    console.debug(JSON.parse(JSON.parse(JSON.stringify(data))))
    return data
}
