<template>
  <div class="content">
    <canvas id="canvas" ref="canvas">
    </canvas>
    <div class="toolbar">
      <div id="allStyleBox">
        <div>
          <div>形状</div>
          <select tag="shape">
            <option value="line">line</option>
            <option value="bight">bight</option>
            <option value="rect">rect</option>
            <option value="circle">circle</option>
            <option value="clear">橡皮擦</option>
            <option value="cleararea">cleararea</option>
          </select>
        </div>
        <div>
          <div>颜色</div>
          <select tag="color">
            <option value="black">black</option>
            <option value="red">red</option>
            <option value="blue">blue</option>
          </select>
        </div>
      </div>
      <div>
        <button @click="undo">撤销</button>
      </div>
      <div>
        <button @click="redo">重做</button>
      </div>
      <div></div>
      <div></div>
    </div>
  </div>

</template>

<script setup>
import {onMounted, ref} from 'vue';

import core from "./js/core.js";

let ctx;
let canvas;
let lineWidth = 10;// 线条宽度
let styleArr = {
  shape: 'line',
  color: '#000',
}

let isPainting = false;// 可以绘画
let toCompleteTheDrawing = false;// 用来判断是否进行过绘图
let dis = {}// 鼠标点击时的坐标点
let imgData = ''// 绘制完要保存canvas图片
let imgCache = [];
let index = -1;


onMounted(() => {

  core.init()

  canvas = core.canvas
  ctx = core.ctx

  // 绑定事件, 更新全局变量
  document.getElementById('allStyleBox').addEventListener('change', (ev) => {
    // 通过tag和属性对应
    styleArr[ev.target.getAttribute('tag')] = ev.target.value;
  })

  bind()
})


function bind() {
  canvas.onmousedown = (e) => {
    isPainting = true;
    toCompleteTheDrawing = false;
    dis = {x: e.offsetX, y: e.offsetY}
  }
  canvas.onmouseup = (e) => {
    // 鼠标移动过则记录
    if (toCompleteTheDrawing) {
      index++
      imgData = ctx.getImageData(0, 0, canvas.width, canvas.height)
      imgCache[index] = imgData
    }
    isPainting = false;
    imgData = ctx.getImageData(0, 0, canvas.width, canvas.height)
    ctx.beginPath()
  }
  canvas.onmousemove = (e) => {
    if (!isPainting) return;

    switch (styleArr.shape) {
      case 'line':
        line(e)
        break;
      case 'bight':
        bight(e)
        break;
    }
    toCompleteTheDrawing = true;
  }

}

/**
 * 自由画笔
 * @param e
 */
function bight(e) {
  ctx.lineWidth = lineWidth;
  ctx.lineCap = 'round';
  ctx.lineTo(e.clientX, e.clientY)
  ctx.stroke()
}

/**
 * 直线
 * @param e
 */
function line(e) {
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  imgData && ctx.putImageData(imgData, 0, 0)
  ctx.beginPath()

  ctx.lineWidth = lineWidth;
  ctx.lineCap = 'round';
  ctx.strokeStyle = styleArr.color
  ctx.moveTo(dis.x, dis.y)
  ctx.lineTo(e.offsetX, e.offsetY);
  ctx.stroke();
}

function clear() {
  ctx.clearRect(0, 0, canvas.width, canvas.height)
}

function undo() {
  index--
  if (index < -1) {
    console.log('撤销失败')
    index++
    // todo 给出提示
    return
  }
  console.log(index)
  ctx.clearRect(0, 0, canvas.width, canvas.height)

  index != -1 && ctx.putImageData(imgCache[index], 0, 0)
  imgData = ctx.getImageData(0, 0, canvas.width, canvas.height)
}

function redo() {
  index++
  if (!imgCache[index]) {
    console.log('重做失败')
    // todo 给出提示
    return
  }
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  ctx.putImageData(imgCache[index], 0, 0)
  imgData = ctx.getImageData(0, 0, canvas.width, canvas.height)
}


</script>

<style scoped>
.content {
  width: 100%;
  height: 100%;
  position: relative;
}

.toolbar {
  width: 80px;
  height: 100vh;
  position: absolute;
  top: 0;
  left: 0;
  border: 1px solid red;
  border-radius: 5px;
}
</style>
