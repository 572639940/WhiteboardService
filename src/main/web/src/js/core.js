export default {
    canvas: undefined,
    ctx: undefined,
    toolbar: undefined,
    init() {
        // 创建对象
        this.canvas = document.getElementById('canvas')
        this.ctx = this.canvas.getContext('2d')

        // 挂载对象
        document.appendChild(this.canvas)
        document.appendChild(this.toolbar)

    }
}