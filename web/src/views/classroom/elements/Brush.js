export class Brush {

    constructor(opt, app) {
        this.x = opt.x || []
        this.y = opt.y || []
        this.app = app
        this.type = 'brush'
    }

    addCoordinate(x, y) {
        this.x.push(x)
        this.y.push(y)
    }

    render() {
        let ctx = this.app.ctx
        ctx.lineCap = "round";
        ctx.lineJoin = "round";
        ctx.beginPath()
        for (let i = 1; i < this.x.length; i++) {
            ctx.moveTo(this.x[i - 1], this.y[i - 1])
            ctx.lineTo(this.x[i], this.y[i])
            ctx.stroke();
        }
    }
}
