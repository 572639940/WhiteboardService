export class Line {

    constructor(opt, app) {
        this.x = opt.x || 0
        this.y = opt.y || 0
        this.width = opt.width || 0
        this.height = opt.height || 0
        this.app = app
    }

    render() {
        let ctx = this.app.ctx
        ctx.beginPath()
        ctx.moveTo(this.x, this.y)
        ctx.lineTo(this.x + this.width, this.y + this.height)
        ctx.stroke()
    }
}
