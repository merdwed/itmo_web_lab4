<template>
  <div id ="coord">
    <a href="/logout"><button type="button">logout</button></a>
    <form id="coord-form" v-on:submit.prevent="onSubmit"> 
    <div class="form-input">
      
      <div>
        <label>Координата X:</label>
        <input type="radio" name="coordX" value="-4"  v-model="coordX">-4
        <input type="radio" name="coordX" value="-3" v-model="coordX">-3
        <input type="radio" name="coordX" value="-2" v-model="coordX">-2
        <input type="radio" name="coordX" value="-1" v-model="coordX">-1
        <input type="radio" name="coordX" value="0" v-model="coordX">0
        <input type="radio" name="coordX" value="1" v-model="coordX">1
        <input type="radio" name="coordX" value="2" v-model="coordX">2
        <input type="radio" name="coordX" value="3" v-model="coordX">3
        <input type="radio" name="coordX" value="4" v-model="coordX">4
      </div>  
      <div>
        <label>Координата Y:</label>
        <input type="text" name=coordY placeholder="введите Y" v-model="coordY">
      </div>    
      <input id="submit-button" type="submit" value="Submit">
      
  
          
    </div>
    </form>
     <div v-if="errors">
      <p color="#f00" id="wrong-input-message" >Вы ввели данные некорректно</p>
      <b-badge variant="warning">API call</b-badge> {{ errors }} <b-badge variant="warning">NOT successful</b-badge>
    </div>
    <p>Выбрано:</p>
      <p>R:{{ paramR }}</p>
      <p>X:{{ coordX }}</p>
      <p>Y:{{ coordY }}</p>
    <div>
        <label>Параметр R:</label>
        <input type="radio" name="paramR" value="-4" v-on:change="draw_scene(paramR)" v-model="paramR">-4
        <input type="radio" name="paramR" value="-3" v-on:change="draw_scene(paramR)" v-model="paramR">-3
        <input type="radio" name="paramR" value="-2" v-on:change="draw_scene(paramR)" v-model="paramR">-2
        <input type="radio" name="paramR" value="-1" v-on:change="draw_scene(paramR)" v-model="paramR">-1
        <input type="radio" name="paramR" value="0" v-on:change="draw_scene(paramR)" v-model="paramR">0
        <input type="radio" name="paramR" value="1" v-on:change="draw_scene(paramR)" v-model="paramR">1
        <input type="radio" name="paramR" value="2" v-on:change="draw_scene(paramR)" v-model="paramR">2
        <input type="radio" name="paramR" value="3" v-on:change="draw_scene(paramR)" v-model="paramR">3
        <input type="radio" name="paramR" value="4" v-on:change="draw_scene(paramR)" v-model="paramR">4
      </div>      
         
   
    <center>
      <div>
        <canvas id="canvas" width="400" height="400" v-on:click="onSubmit"></canvas>
      </div>
    </center>
    <!--
    <b-btn variant="primary" @click="getTableTextFromBackend()">Call the secured API</b-btn>
    -->
    <div >
    <table class="coordTable">
			<tbody id="v-for-table" class="coordTable">
        <tr class="coordTableHeader"> 				
            <th>X coord</th>    				
            <th>Y coord</th>
        </tr>
        <tr v-for="item in pointArray" :class="getTableRowClass()">
					<td>{{ item.coordX }}</td>
					<td>{{ item.coordY }}</td>
				</tr>
			</tbody>
		</table>
      <!--
     <p>Full response: {{ backendResponse }} </p>
     --> 
    </div>
    
  </div>

</template>

<script>
  import api from './backend-api'
  import store from './../store'

export default {
  name: 'protected',

  data () {
    return {
      backendResponse: '',
      pointResponse: '',
      securedApiCallSuccess: false,
      errors: null,
      paramR:1,
      coordX:1,
      coordY:'',
      tableRowCounter:0,
      pointArray:[],
      canvas:null,
      ctx : null
    }
  },

  methods: {
    getTableRowClass(){
      this.tableRowCounter=!this.tableRowCounter;
      return this.tableRowCounter?"coordTableOddRow":"coordTableEvenRow";
    },
    getTableTextFromBackend() {
      api.getTable(store.getters.getUserName, store.getters.getUserPass,store.getters.getUserId)
              .then(response => {
                console.log("Response: '" + response.data + "' with Statuscode " + response.status);
                this.securedApiCallSuccess = true;
                this.backendResponse = response.data;
                this.pointArray=response.data.table;
                this.draw_scene(this.paramR);
                this.errors=null;
              })
              .catch(error => {
                console.log("Error: " + error);
                this.errors = error;
              })
    },
    onSubmit(){
      api.putTable(
        store.getters.getUserName,
        store.getters.getUserPass,
        store.getters.getUserId,
        this.coordX,
        this.coordY)
        .then(response =>{
          console.log("Response: '" + response.data + "' with Statuscode " + response.status);
                this.pointResponse = response.data;
          this.getTableTextFromBackend();
        })
        .catch(error => {
                console.log("Error: " + error);
                this.errors = error;
              })
    },
  CanvasToCenterCoord(x,y, r){
    var width=this.canvas.scrollWidth;
    var height=this.canvas.scrollHeight;
    x=x/width*2-1;
    y=-y/height*2+1;
    return {x:x,y:y};
},
CenterToCanvasCoord(x, y, r){
  var width=this.canvas.scrollWidth;
    var height=this.canvas.scrollHeight;
    x=(x/3/r+1/2)*width;
    y=(-y/3/r+1/2)*height;
    return {x:x,y:y};
},
canvas_arrow(context, fromx, fromy, tox, toy) {
    var headlen = 10; // length of head in pixels
    var dx = tox - fromx;
    var dy = toy - fromy;
    var angle = Math.atan2(dy, dx);
    context.moveTo(fromx, fromy);
    context.lineTo(tox, toy);
    context.lineTo(tox - headlen * Math.cos(angle - Math.PI / 6), toy - headlen * Math.sin(angle - Math.PI / 6));
    context.moveTo(tox, toy);
    context.lineTo(tox - headlen * Math.cos(angle + Math.PI / 6), toy - headlen * Math.sin(angle + Math.PI / 6));
},
draw_area(){
    var [w,h]=[this.canvas.scrollWidth,this.canvas.scrollHeight];
    this.ctx.fillStyle = "green";
    this.ctx.strokeStyle = "green";
    this.ctx.lineWidth = 1;
    this.ctx.beginPath();
    this.ctx.moveTo(w/2,h/2);
    this.ctx.lineTo(5*w/6,h/2);
    this.ctx.lineTo(5*w/6,h/3);
    this.ctx.lineTo(w/2,h/3);
    this.ctx.lineTo(w/2,h/2);
    this.ctx.stroke();
    this.ctx.fill();
    this.ctx.beginPath();
    this.ctx.moveTo(w/2,h/2);
    this.ctx.lineTo(w/6,h/2);
    this.ctx.lineTo(w/2,h/6);
    this.ctx.lineTo(w/2,h/2);
    this.ctx.stroke();
    this.ctx.fill();
    this.ctx.beginPath();
    this.ctx.arc(w/2,h/2,w/6,Math.PI/2,-Math.PI/2,true);
    this.ctx.lineTo(w/2,h/2);
    this.ctx.lineTo(w/2,h/3);
    this.ctx.stroke();
    this.ctx.fill();
},
draw_legend(pr){
    var [w,h]=[this.canvas.scrollWidth,this.canvas.scrollHeight];
    this.ctx.beginPath();
    this.ctx.font="20px serif";
    this.ctx.strokeStyle="black";
    this.ctx.lineWidth=2;
    this.ctx.strokeText(pr, 5*w/6-4, h/2-5);//+x
    this.ctx.strokeText(`-${pr}`, w/6-4, h/2-5);//-x
    this.ctx.strokeText(pr, w/2+4, h/6+3);//+y
    this.ctx.strokeText(`-${pr}`, w/2+4, 5*h/6+3);//-y
    this.ctx.fillStyle="black";
    this.ctx.fillRect(5*w/6-1,h/2-3,2,6);//+x
    this.ctx.fillRect(w/6-1,h/2-3,2,6);//-x
    this.ctx.fillRect(w/2-3,h/6-1,6,2);//+y
    this.ctx.fillRect(w/2-3,5*h/6-1,6,2);//-y
},
draw_point(px,py,r){
    if(typeof(px)=="string")px=parseFloat(px);
    if(typeof(py)=="string")py=parseFloat(py);
    if(typeof(r)=="string")r=parseFloat(r);
    if(px == null || py==null || r==null || r==0)return;
    this.ctx.beginPath();
    this.ctx.lineWidth = 2;
    if(px>=0 && py>=0 && px<=r && py<=r/2 ||
      px>=0 && py<=0 && px*px+py*py<r*r/2 ||
      px<=0 && py>=0 && py<px+r)
        this.ctx.strokeStyle='blue';
    else
        this.ctx.strokeStyle="red";
    var {x,y}=this.CenterToCanvasCoord(px,py,r);
    console.log("draw point at ("+ x + ";" + y+")");
    this.ctx.arc(x,y,3,0,2 * Math.PI);
    this.ctx.stroke();
    this.ctx.beginPath();
    this.ctx.lineWidth = 2;
    this.ctx.font="20px serif";
    this.ctx.strokeStyle='black';
    var str=`(${(px).toFixed(2).toString()}; ${(py).toFixed(2).toString()})`;
    var offsetX=this.canvas.scrollWidth * 3 / 4-x;
    var offsetY=this.canvas.scrollHeight / 16-y;
    if(offsetX>=0)offsetX=0;
    if(offsetY<=0)offsetY=0;
    this.ctx.strokeText(str, x+5+offsetX, y-8+offsetY);
},
draw_scene(r){
    
    this.ctx.clearRect(0,0,this.canvas.scrollWidth,this.canvas.scrollHeight);
    this.draw_area();
    this.ctx.beginPath();
    this.ctx.lineWidth = 2;
    this.ctx.strokeStyle='black';
    this.canvas_arrow(this.ctx, 0, this.canvas.scrollHeight/2, this.canvas.scrollWidth, this.canvas.scrollHeight/2);
    this.canvas_arrow(this.ctx, this.canvas.scrollWidth/2, this.canvas.scrollHeight, this.canvas.scrollWidth/2, 0);
    this.ctx.stroke();
    this.draw_legend(r);
    var item=null;
    for(item of this.pointArray){
      console.log("loop of array. item:"+item.coordX+","+item.coordY+","+item.paramR+","+item.hit);
      this.draw_point(item.coordX,item.coordY,r);
    }
  
}
},

 mounted(){
   
   var canvas = document.getElementById("canvas");
  this.canvas = canvas;
  var ctx = canvas.getContext("2d");    
  this.ctx = ctx;
   this.getTableTextFromBackend();
  
 },
 
}

</script>
<style>
div.form-input > div{

    margin: 0 0 30px 50px;
}
#submit-button {
    text-decoration: none;
    display: inline-block;
    width: 140px;
    height: 45px;
    line-height: 45px;
    border-radius: 15px;
    margin: 10px 20px;
    font-family: 'Montserrat', sans-serif;
    font-size: 11px;
    font-weight: 600;
    text-transform: uppercase;
    text-align: center;

    color: #524f4e;
    background: white;
}
#submit-button:hover {
    background: #2EE59D;
    box-shadow: 0 15px 20px rgba(46, 229, 157, .4);
    color: white;
}
.coordTable {   
    border-collapse:collapse;
    border:1px solid #000000;
 }
 
 .coordTableHeader {
    text-align:center;
    background:none repeat scroll 0 0 #B5B5B5;
    border-bottom:1px solid #000000;  
    padding:2px;
 }
 
 .coordTableOddRow {
    text-align:center;
    background:none repeat scroll 0 0 #FFFFFF;	
 }
 
 .coordTableEvenRow {
    text-align:center;
    background:none repeat scroll 0 0 #D3D3D3;
 }
 
.coordTable td,th{
    border: 1px solid black;
}
#canvas {
    height: 400;
    width: 400;
    border: 4px solid #336;
    background: #fc3;
    margin: 40px;
    cursor: none;
}
</style>