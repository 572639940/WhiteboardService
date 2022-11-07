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
