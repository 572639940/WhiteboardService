<template>
  <div class="container" ref="container">
    <canvas id="canvas" ref="canvas">
    </canvas>
    <div class="toolbar" v-if="!isMouseDown">
      <el-row>
        <el-radio-group v-model="currentType">
          <el-radio-button label="selection">选择</el-radio-button>
          <el-radio-button label="rectangle">矩形</el-radio-button>
        </el-radio-group>
      </el-row>
    </div>
    <!-- todo 修改样式时显示 -->
    <el-card class="element-style" v-if="!isMouseDown">
      <el-row>
        <el-button @click="testSaveData">save</el-button>
        <el-button @click="testLoadData">load</el-button>
        <el-button @click="undo">undo</el-button>
        <el-button @click="redo">redo</el-button>
        <el-button circle :icon="DeleteFilled" size="small" @click="clearCanvas"/>
      </el-row>
    </el-card>
    <div class="canvas-page" v-if="!isMouseDown">
      <el-icon>
        <el-tooltip
            class="box-item"
            effect="dark"
            content="上一页"
            placement="top"
        >
          <el-button
              v-if="drawingBoardIndex<=0"
              circle
              size="small"
              disabled
              :icon="CaretLeft"
          />
          <el-button
              v-else
              circle
              size="small"
              :icon="CaretLeft"
              @click="prev"/>
        </el-tooltip>
        <el-button disabled link> {{ drawingBoardIndex + 1 }}</el-button>
        <el-tooltip
            v-if="drawingBoardIndex+1===drawingBoardList.length"
            class="box-item"
            effect="dark"
            content="添加"
            placement="top"
        >
          <el-button
              circle
              size="small"
              :icon="Plus"
              @click="adddrawingBoard"/>
        </el-tooltip>
        <el-tooltip
            v-else
            class="box-item"
            effect="dark"
            content="下一页"
            placement="top"
        >
          <el-button
              circle
              size="small"
              :icon="CaretRight"
              @click="next">
          </el-button>
        </el-tooltip>
      </el-icon>
    </div>
  </div>
</template>

<script setup>
import {onMounted, onUpdated, ref} from "vue";
import {DeleteFilled, CaretLeft, CaretRight, Plus} from "@element-plus/icons-vue"

import {getPointToLineDistance, getTowPointDistance, checkIsAtSegment, checkPointIsInRectangle} from "../../js/utils.js";

import {loadInStore, saveToStorage} from "../../js/data.js";

import _ from 'lodash'


const container = ref(null)
const canvas = ref(null)
let ctx = null

let mouseDown = {x: 0, y: 0}// 鼠标位置
let isMouseDown = ref(false);// 鼠标是否按下

// 当前激活模式
const currentType = ref('rectangle')

let activeElement = null// 当前激活
let allElements = []// 所有元素
// 元素缓存列表
let cacheList = []
let cacheListIndex = -1//当前绘图元素索引

let isAdjustmentElement = false // 是否在调整区域
let hitActiveElementArea = "" // 当前激活的区域

onMounted(() => {
  initCanvas()
  bindEvent()
})

/**
 * 初始化画布
 */
const initCanvas = () => {
  let {width, height} = container.value.getBoundingClientRect();
  canvas.value.width = width
  canvas.value.height = height
  ctx = canvas.value.getContext('2d')
  // ctx.translate(width / 2, height / 2)
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
  // 左键事件
  if (e.button !== 0) {
    return
  }
  // 在 canvas 中的坐标
  mouseDown.x = e.offsetX
  mouseDown.y = e.offsetY
  isMouseDown.value = true

  if (currentType.value === "selection") {
    // 选择模式下进行元素激活检测
    if (activeElement) {
      // 当前存在激活元素则判断是否按住了激活状态的某个区域
      let hitActiveArea = activeElement.isHitActiveArea(mouseDown.x, mouseDown.y);
      console.log(hitActiveArea)
      if (hitActiveArea) {
        // 按住了按住了激活状态的某个区域
        isAdjustmentElement = true;
        hitActiveElementArea = hitActiveArea;
        alert(hitActiveArea);
      } else {
        // 否则进行激活元素的更新操作
        checkIsHitElement(mouseDown.x, mouseDown.y);
      }
    } else {
      checkIsHitElement(mouseDown.x, mouseDown.y);
    }
  }
};

const onmouseup = (e) => {
  isMouseDown.value = false
  if (currentType.value !== "selection") {
    activeElement = null
  }
  mouseDown.x = {x: 0, y: 0}
  cacheListIndex += 1
  cacheList[cacheListIndex] = _.cloneDeep(allElements)
}

const onmousemove = (e) => {
  // 可以绘制, 且不是选中
  if (!isMouseDown.value || currentType.value === 'selection') {
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
  activeElement.width = e.offsetX - mouseDown.x
  activeElement.height = e.offsetY - mouseDown.y
  // 渲染所有的元素
  renderAllElements()
  // testSaveData()
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

    let leftTopPoint = {x: x, y: y,}
    let rightBottomPoint = {x: x + width, y: y + height,}
    let mousePoint = {x: x0, y: y0}

    if (checkPointIsInRectangle(leftTopPoint, rightBottomPoint, mousePoint)) {
      // 在中间的虚线框
      return "body";
    } else if (getTowPointDistance(x0, y0, x + width / 2, y + 6) <= 15) {
      // 在旋转手柄
      return "rotate";
    } else if (checkPointIsInRectangle(rightBottomPoint, {x: rightBottomPoint.x + 10, y: rightBottomPoint.y + 10}, mousePoint)) {
      // 在右下角操作手柄
      return "rightBottom";
    } else if (checkPointIsInRectangle({x: rightBottomPoint.x, y: leftTopPoint.y}, {x: rightBottomPoint.x + 10, y: leftTopPoint.y + 10}, mousePoint)) {
      // 在右上角操作手柄
      return "rightTop";
    } else if (checkPointIsInRectangle({x: leftTopPoint.x - 10, y: leftTopPoint.y - 10}, leftTopPoint, mousePoint)) {
      // 在左上角操作手柄
      return "leftTop";
    } else if (checkPointIsInRectangle({x: leftTopPoint.x - 10, y: rightBottomPoint.y}, {x: leftTopPoint.x + 10, y: rightBottomPoint.y + 10}, mousePoint)) {
      // 在左下角操作手柄
      return "leftBottom";
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

// 屏幕坐标转到画布坐标
const screenToCanvas = (x, y) => {
  return {x, y: y}
}

/**
 * 清除画布
 */
const clearCanvas = () => {
  let width = canvas.value.width;
  let height = canvas.value.height;
  ctx.clearRect(0, 0, width, height)
}

// 渲染元素
const renderAllElements = () => {
  clearCanvas()
  // allElements.forEach((element) => element.render())
  for (let i = 0; i < allElements.length; i++) {
    allElements[i].render()
  }
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


// 模拟交互存储
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
    console.debug('太大了')
    return false
  }
  return true
}
/**
 * 是否允许切换上一个
 */
const canPrev = () => {
  if (drawingBoardIndex.value <= 0) {
    console.debug('太小了')
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

const undo = () => {
  if (cacheList.length <= 0 || cacheListIndex <= -1) {
    return
  }
  cacheListIndex -= 1
  allElements = cacheListIndex !== -1 ? cacheList[cacheListIndex] : []

  renderAllElements()
  console.log(cacheListIndex)
  console.log(cacheList)
}
const redo = () => {
  if (cacheListIndex + 1 >= cacheList.length) {
    return
  }
  cacheListIndex += 1
  allElements = cacheList[cacheListIndex]
  renderAllElements()
}


</script>

<style lang="less" scoped>
.container {
  width: 100%;
  height: 100%;
}

#canvas {
  width: 100%;
  height: 99%;
  //border-bottom: 1px solid #666;
}

// 选择工具栏
.toolbar {
  position: absolute;

  bottom: 70px;
  left: 30px;
}

// 样式调整栏
.element-style {
  position: absolute;

  top: 100px;
  left: 30px;
}

.canvas-page {
  position: absolute;

  right: 360px;
  bottom: 70px;
}
</style>
