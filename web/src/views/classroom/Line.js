export class Line {

    constructor(opt, app) {
        this.x = opt.x || 0
        this.y = opt.y || 0
        this.width = opt.width || 0
        this.height = opt.height || 0
        this.lineWidth = opt.lineWidth || 2
        this.strokeStyle = opt.strokeStyle || '#000'
        this.app = app
    }

    render() {
        let ctx = this.app.ctx
        ctx.moveTo(this.x, this.y)
        ctx.lineTo(this.x + this.width, this.y + this.height)
        ctx.lineWidth = this.lineWidth
        ctx.strokeStyle = this.strokeStyle
        ctx.stroke()
    }
}
