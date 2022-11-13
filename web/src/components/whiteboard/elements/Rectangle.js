import {getPointToLineDistance, getTowPointDistance, checkIsAtSegment, checkPointIsInRectangle, screenToCanvas} from "../../../utils/utils.js";
import * as App from "../../../utils/App.js";

export class Rectangle {

    constructor(opt, app) {
        this.x = opt.x || 0
        this.y = opt.y || 0
        this.width = opt.width || 0
        this.height = opt.height || 0
    }

    /**
     * 渲染
     */
    render() {
        let ctx = App.ctx
        ctx.beginPath();
        ctx.rect(this.x, this.y, this.width, this.height)
        ctx.stroke()
    }

}
