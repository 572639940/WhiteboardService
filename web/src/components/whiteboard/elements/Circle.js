import * as App from "../../../utils/App.js";

export class Circle {

    constructor(opt, app) {
        this.x = opt.x || 0
        this.y = opt.y || 0
        this.width = opt.width || 0
        this.height = opt.height || 0
        this.lineWidth = opt.lineWidth || 2
        this.strokeStyle = opt.strokeStyle || '#000'
    }

    render() {
        let ctx = App.ctx
        ctx.beginPath();
        ctx.arc(this.x, this.y, Math.abs(this.width), 0, 2 * Math.PI);
        ctx.stroke();
    }
}
