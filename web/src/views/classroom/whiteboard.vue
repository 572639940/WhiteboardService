<template>
  <div class="container" ref="container">
    <canvas id="canvas" ref="canvas">
    </canvas>
    <div class="toolbar" v-if="!isMouseDown">
      <el-row>
        <el-col :>

        </el-col>
        <el-radio-group v-model="currentType">
          <el-radio-button label="selection">选择</el-radio-button>
          <el-radio-button label="rectangle">矩形</el-radio-button>
          <el-radio-button label="line">线</el-radio-button>
          <el-radio-button label="circle">圆</el-radio-button>
          <el-radio-button label="brush">画笔</el-radio-button>
        </el-radio-group>
        <el-button @click="testSaveData">save</el-button>
        <el-button @click="testLoadData">load</el-button>
        <el-button @click="undo">undo</el-button>
        <el-button @click="redo">redo</el-button>
        <el-button circle :icon="DeleteFilled" size="small" @click="setEmptyCanvas"/>
      </el-row>
    </div>
    <!-- todo 修改样式时显示 -->
<!--    <el-card class="element-style" v-if="!isMouseDown">-->
<!--      <el-row>-->
<!--        <el-button @click="testSaveData">save</el-button>-->
<!--        <el-button @click="testLoadData">load</el-button>-->
<!--        <el-button @click="undo">undo</el-button>-->
<!--        <el-button @click="redo">redo</el-button>-->
<!--        <el-button circle :icon="DeleteFilled" size="small" @click="setEmptyCanvas"/>-->
<!--      </el-row>-->
<!--    </el-card>-->
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
import {Rectangle} from "./elements/Rectangle.js";
import {loadInStore, saveToStorage} from "../../js/data.js";
import {Line} from "./elements/Line.js";
import {Circle} from "./elements/Circle.js";
import {Brush} from "./elements/Brush.js";

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
// 当前模式: 只读(0) 协作(1)
let currentModel = 1

onMounted(() => {
  initCanvas()
  bindEvent()
  testLoadData()
})

/**
 * 初始化画布
 */
const initCanvas = () => {
  let {width, height} = container.value.getBoundingClientRect();
  canvas.value.width = width
  canvas.value.height = height
  ctx = canvas.value.getContext('2d')
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
};

const onmouseup = (e) => {
  isMouseDown.value = false
  if (currentType.value !== "selection") {
    activeElement = null
  }
  mouseDown.x = {x: 0, y: 0}

  testSaveData()
}

const onmousemove = (e) => {
  // 可以绘制, 且不是选中
  if (!isMouseDown.value || currentType.value === 'selection' || currentModel === 0) {
    return;
  }
  // 当前没有激活元素则创建一个
  if (!activeElement) {
    switch (currentType.value) {
      case 'line':
        activeElement = new Line({x: mouseDown.x, y: mouseDown.y}, {canvas, ctx})
        break;
      case 'rectangle':
        activeElement = new Rectangle({x: mouseDown.x, y: mouseDown.y}, {canvas, ctx})
        break;
      case 'circle':
        activeElement = new Circle({x: mouseDown.x, y: mouseDown.y}, {canvas, ctx})
        break;
      case 'brush':
        activeElement = new Brush({x: [mouseDown.x], y: [mouseDown.y]}, {canvas, ctx})
        break;
      default:
        break;
    }
    // 添加到元素渲染数组
    allElements.push(activeElement)
  }
  // 修改元素大小
  if (activeElement.type === 'brush') {
    activeElement.addCoordinate(e.offsetX, e.offsetY)
  } else {
    activeElement.width = e.offsetX - mouseDown.x
    activeElement.height = e.offsetY - mouseDown.y
  }
  // 渲染所有的元素
  renderAllElements()
}

const setEmptyCanvas = () => {
  allElements = []
  clearCanvas()
  testSaveData()
  renderAllElements()
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
  for (let i = 0; i < allElements.length; i++) {
    allElements[i].render()
  }
}

// 模拟交互存储
let key = "allElement"

const testSaveData = () => {
  saveToStorage(key, JSON.stringify(allElements))
}

const testLoadData = () => {
  let data = loadInStore(key)
  if (data) {
    allElements = JSON.parse(JSON.parse(JSON.stringify(data)))
    for (let i = 0; i < allElements.length; i++) {
      allElements[i] = new Rectangle(allElements[i], {canvas, ctx})
    }
    renderAllElements()
  }
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
  if (!allElements.length) {
    return
  }

  cacheListIndex -= 1
  let element = allElements.pop()
  cacheList.unshift(element)

  renderAllElements()
}
const redo = () => {
  if (!cacheList.length) {
    return
  }
  let element = cacheList.shift()
  allElements.push(element)
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
