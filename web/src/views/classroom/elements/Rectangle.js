import {getPointToLineDistance, getTowPointDistance, checkIsAtSegment, checkPointIsInRectangle, screenToCanvas} from "../../../js/utils.js";

export class Rectangle {

    constructor(opt, app) {
        this.x = opt.x || 0
        this.y = opt.y || 0
        this.width = opt.width || 0
        this.height = opt.height || 0
        this.app = app
    }

    /**
     * 渲染
     */
    render() {
        this.app.ctx.beginPath();
        this.app.ctx.rect(this.x, this.y, this.width, this.height)
        this.app.ctx.stroke()
    }

}
