<template>
  <div class="container" ref="container">
    <canvas id="canvas" ref="canvas">
    </canvas>
    <div class="toolbar">
      <el-row>
        <el-col :span="12">
          <el-radio-group v-model="currentType">
            <el-radio-button label="selection">选择</el-radio-button>
            <el-radio-button label="rectangle">矩形</el-radio-button>
          </el-radio-group>
        </el-col>
        <el-col :span="6">
          <el-button @click="testSaveData">save</el-button>
          <el-button @click="testLoadData">load</el-button>
        </el-col>
        <el-col :span="6">
          <div>
            <el-button v-if="drawingBoardIndex<=0" disabled>上一个</el-button>
            <el-button v-else @click="prev">上一个</el-button>
            <el-button disabled link> {{ drawingBoardIndex + 1 }}</el-button>
            <el-button v-if="drawingBoardIndex+1===drawingBoardList.length" @click="adddrawingBoard">添加</el-button>
            <el-button v-else @click="next">下一个</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import {onMounted, onUpdated, ref} from "vue";

import {getPointToLineDistance, getTowPointDistance, checkIsAtSegment} from "../../js/utils.js";

import {loadInStore, saveToStorage} from "../../js/data.js";

const container = ref(null)
const canvas = ref(null)
let ctx = null

let mouseDown = {x: 0, y: 0}// 鼠标位置
let isMouseDown = false;// 鼠标是否按下

// 当前激活模式
const currentType = ref('rectangle')
// const currentType = ref('selection')

let activeElement = null// 当前激活
let allElements = []// 所有元素

let isAdjustmentElement = false // 是否在调整区域
let hitActiveElementArea = "" // 当前激活的区域

onMounted(() => {
  initCanvas()
  bindEvent()
  // testLoadData()
})

/**
 * 初始化画布
 */
const initCanvas = () => {
  let {width, height} = container.value.getBoundingClientRect();
  console.log(width)
  console.log(height)
  canvas.value.width = width
  canvas.value.height = height*0.9
  ctx = canvas.value.getContext('2d')
  ctx.translate(width / 2, height / 2)
}

/**
 * 绑定事件
 */
const bindEvent = () => {
  canvas.value.addEventListener("mousedown", onmousedown)
  canvas.value.addEventListener("mouseup", onmouseup)
  canvas.value.addEventListener("mousemove", onmousemove)
}

const onmousedown = (e) => {
  mouseDown.x = e.clientX
  mouseDown.y = e.clientY
  isMouseDown = true

  if (currentType.value === 'selection') {
    // 选择模式下进行元素激活检测
    // checkIsHitElement(mouseDown.x, mouseDown.y);
    if (activeElement) {
      let hitActiveArea = activeElement.isHitActiveArea(mouseDown.x, mouseDown.y)
      if (hitActiveArea) {
        isAdjustmentElement = true
        hitActiveArea = hitArea
      }
    }
  }
}

const onmouseup = (e) => {
  isMouseDown = false
  if (currentType.value !== "selection") {
    activeElement = null
  }
  mouseDown.x = {x: 0, y: 0}
}

const onmousemove = (e) => {
  // 可以绘制, 且不是选中
  if (!isMouseDown || currentType.value === 'selection') {
    return;
  }
  if (!activeElement) {
    activeElement = new Rectangle({
      x: mouseDown.x,
      y: mouseDown.y,
    })
    // 渲染所有元素
    allElements.push(activeElement)
  }
  // 更改矩阵的大小
  activeElement.width = e.clientX - mouseDown.x
  activeElement.height = e.clientY - mouseDown.y
  // 渲染所有的元素
  renderAllElements()
  testSaveData()
}

// 矩阵
class Rectangle {

  constructor(opt) {
    this.x = opt.x || 0
    this.y = opt.y || 0
    this.width = opt.width || 0
    this.height = opt.height || 0
    this.isActive = false
  }

  /**
   * 渲染
   */
  render() {
    // ctx.beginPath();
    ctx.save()
    let canvasPos = screenToCanvas(this.x, this.y)
    ctx.rect(canvasPos.x, canvasPos.y, this.width, this.height)
    drawRect(canvasPos.x, canvasPos.y, this.width, this.height)
    this.renderActiveState()
    // ctx.stroke()
  }

  /**
   * 渲染选中框
   */
  renderActiveState() {
    if (!this.isActive) {
      return;
    }
    let canvasPos = screenToCanvas(this.x, this.y);
    // 选中框应当比矩阵大一些
    let x = canvasPos.x - 5;
    let y = canvasPos.y - 5;
    let width = this.width + 10;
    let height = this.height + 10;

    ctx.save();
    ctx.setLineDash([5])
    drawRect(x, y, width, height)
    ctx.restore()
    drawRect(x - 10, y - 10, 10, 10)// 左上角操作图标
    drawRect(x + width, y - 10, 10, 10)// 右上角操作图标
    drawRect(x + width, y + height, 10, 10)// 左下角操作图标
    drawRect(x - 10, y + height, 10, 10)// 右下角操作图标
    drawCircle(x + width / 2, y - 10, 10)// 旋转操作图标
  }

  /**
   * 是否选中
   * @param x0
   * @param y0
   * @return {boolean}
   */
  isHit(x0, y0) {
    let {x, y, width, height} = this;
    let segments = [
      [x, y, x + width, y],
      [x + width, y, x + width, y + height],
      [x + width, y + height, x, y + height],
      [x, y + height, x, y],
    ]
    for (let i = 0; i < segments.length; i++) {
      let segment = segments[i];
      if (checkIsAtSegment(x0, y0, segment[0], segment[1], segment[2], segment[3])) {
        return true;
      }
    }
    return false
  }

  /**
   * 是否选中了 激活状态 的某个区域
   * @param x0
   * @param y0
   * @return {string}
   */
  isHitActiveArea(x0, y0) {
    let x = this.x - 5;
    let y = this.y - 5;

    let width = this.width + 10;
    let height = this.height + 10;
    // todo 还有3个选中没有写
    if (checkPointIsInRectangle(x0, y0, x, y, width, height)) {
      // 在中间的虚线区域
      return 'body'
    } else if (getTowPointDistance(x0, y0, x + width / 2, y - 10) <= 10) {
      // 旋转手柄
      return 'rotate'
    } else if (checkPointIsInRectangle(x0, y0, x + width, y + height, 10, 10)) {
      // 右下角
      return 'bottomRight'
    }

  }
}

// 绘制矩形
const drawRect = (x, y, w, h) => {
  ctx.beginPath();
  ctx.rect(x, y, w, h);
  ctx.stroke();
};

// 绘制圆形
const drawCircle = (x, y, r) => {
  ctx.beginPath();
  ctx.arc(x, y, r, 0, 2 * Math.PI);
  ctx.stroke();
};

// 鼠标,画布圆点不一致
const screenToCanvas = (x, y) => {
  return {
    x: x - canvas.value.width / 2,
    y: y - canvas.value.height / 2
  }
}

/**
 * 清除画布
 */
const clearCanvas = () => {
  let width = canvas.value.width;
  let height = canvas.value.height;
  ctx.clearRect(-width / 2, -height / 2, width, height)
}

// 渲染元素
const renderAllElements = () => {
  clearCanvas()
  allElements.forEach((element) => element.render())
}

// 检查是否选中某个元素
const checkIsHitElement = (x, y) => {
  let hitElement = null
  for (let i = allElements.length - 1; i >= 0; i--) {
    if (allElements[i].isHit(x, y)) {
      hitElement = allElements[i]
      break;
    }
  }
  // 如果当前已经有激活元素先取消
  if (activeElement) {
    activeElement.isActive = false
  }
  // 更改为当前选中元素
  activeElement = hitElement
  if (hitElement) {
    hitElement.isActive = true
  }
  // 重新渲染元素
  renderAllElements()
}

// 检查一个坐标是否在一个矩阵内
const checkPointIsInRectangle = (x, y, rx, ry, rw, rh) => {
  return x => rx && x <= rx + rw && y >= ry && y <= ry + rh;
}

let key = "allElement"

const testSaveData = () => {
  saveToStorage(key, JSON.stringify(allElements))
}

const testLoadData = () => {
  allElements = JSON.parse(JSON.parse(JSON.stringify(loadInStore(key))))
  for (let i = 0; i < allElements.length; i++) {
    allElements[i] = new Rectangle(allElements[i])
  }
  renderAllElements()
}

// 画布相关
let drawingBoardIndex = ref(0)
let drawingBoardList = [[]]

/**
 * 是否允许切换下一个
 */
const canNext = () => {
  // 不能超出最大画板数
  if (drawingBoardIndex.value + 1 >= drawingBoardList.length) {
    console.log('太大了')
    return false
  }
  return true
}
/**
 * 是否允许切换上一个
 */
const canPrev = () => {
  if (drawingBoardIndex.value <= 0) {
    console.log('太小了')
    return false
  }
  return true
}

/**
 * 上一个
 * 画布切换
 */
const prev = () => {
  if (!canPrev()) return
  // 切换画板前, 保存数据
  drawingBoardList[drawingBoardIndex.value] = allElements
  // testSaveData()
  //
  drawingBoardIndex.value--
  console.log(drawingBoardIndex.value)
  console.log(drawingBoardList[drawingBoardIndex.value])
  allElements = drawingBoardList[drawingBoardIndex.value]
  renderAllElements()
}
/**
 * 是否能切换上一个
 * @return {boolean}
 */
const canDrawingBoard = () => {
  return drawingBoardIndex + 1 === drawingBoardList.length
}

/**
 * 下一个
 * 画布切换
 */
const next = () => {
  if (!canNext()) return
  // 切换前保存数据
  drawingBoardList[drawingBoardIndex.value] = allElements
  // testSaveData()

  drawingBoardIndex.value++
  console.log(drawingBoardIndex.value)
  allElements = drawingBoardList[drawingBoardIndex.value]
  renderAllElements()
}

/**
 * 添加画布
 */
const adddrawingBoard = () => {
  if (canDrawingBoard()) return
  // 保存当前画布
  drawingBoardList[drawingBoardIndex.value] = allElements
  // testSaveData()

  // 添加画布
  drawingBoardIndex.value += 1
  drawingBoardList[drawingBoardIndex.value] = []

  allElements = []
  renderAllElements()
}


</script>

<style lang="less" scoped>
.container {
  /*position: relative;*/
  width: 100%;
  height: 100%;
  /*position: fixed;*/
  /*left: 0;*/
  /*top: 0;*/
  /*width: 100%;*/
  /*height: 80%;*/
}
#canvas{
  /*height: 600px;*/
}
</style>
