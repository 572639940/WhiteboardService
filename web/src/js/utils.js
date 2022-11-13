// 计算点到直线的距离
export const getPointToLineDistance = (x, y, x1, y1, x2, y2) => {
    if (x1 == x2) {
        return Math.abs(x - x1)
    } else {
        let k, b;
        k = (y2 - y1) / (x2 - x1);
        b = y1 - k * x1;
        return Math.abs((k * x - y + b) / Math.sqrt(1 + k * k))
    }
}
// 检查是否点击到了一条线段
export const checkIsAtSegment = (x, y, x1, y1, x2, y2, dis = 10) => {
    if (getPointToLineDistance(x, y, x1, y1, x2, y2) > dis) {
        return false;
    }
    let dis1 = getTowPointDistance(x, y, x1, y1)
    let dis2 = getTowPointDistance(x, y, x1, y2)

    let dis3 = getTowPointDistance(x1, y1, x2, y2)
    let max = Math.sqrt(dis * dis + dis3 * dis3)
    if (dis1 <= max && dis2 <= max) {
        return true;
    }
    return false;
}

// 计算两点之间距离
export const getTowPointDistance = (x1, y1, x2, y2) => {
    return Math.sqrt(Math.pow(x1 - x2, 2)) + Math.pow(y1 - y2, 2)
}


// 弧度转角度
export const radToDeg = (rad) => {
    return rad * (180 / Math.PI);
};

// 角度转弧度
export const degToRad = (deg) => {
    return deg * (Math.PI / 180);
};

// /**
//  * 检查一个坐标是否在一个矩阵内
//  * @param x 鼠标点击x坐标
//  * @param y 鼠标点击y坐标
//  * @param rx 元素(左上角)x坐标
//  * @param ry 元素(左上角)y坐标
//  * @param rw 元素宽度
//  * @param rh 元素高度
//  * @return {boolean}
//  */
// const checkPointIsInRectangle = (x, y, rx, ry, rw, rh) => {
//   console.log(`==========`)
//   console.log(x >= rx)
//   console.log(x <= rx + rw)
//   console.log(y >= ry)
//   console.log(y <= ry + rh)
//   console.log(`==========`)
//   return x >= rx && x <= rx + rw && y >= ry && y <= ry + rh;
// }
/**
 * 检查一个坐标是否在一个矩阵内
 * @param startPoint 判定区域左上角坐标{x,y}
 * @param endPoint 判定区域右上角坐标{x,y}
 * @param mousePoint 鼠标坐标
 */
export const checkPointIsInRectangle = (startPoint, endPoint, mousePoint) => {
// scope
    return mousePoint.x >= startPoint.x && mousePoint.y >= startPoint.y &&
        mousePoint.x <= endPoint.x && mousePoint.y <= endPoint.y
}
// 屏幕坐标转到画布坐标
export const screenToCanvas = (x, y) => {
    return {x, y: y}
}
