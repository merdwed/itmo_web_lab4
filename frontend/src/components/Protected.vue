<template>
  <div id ="coord">
    
    <form id="coord-form" onsubmit="return formcheck();"> 
    <div class="form-input">
      <div>
        <label>Параметр R:</label>
        <input type="radio" name="paramR" value="-4" checked v-model="paramR">-4
        <input type="radio" name="paramR" value="-3" v-model="paramR">-3
        <input type="radio" name="paramR" value="-2" v-model="paramR">-2
        <input type="radio" name="paramR" value="-1" v-model="paramR">-1
        <input type="radio" name="paramR" value="0" v-model="paramR">0
        <input type="radio" name="paramR" value="1" v-model="paramR">1
        <input type="radio" name="paramR" value="2" v-model="paramR">2
        <input type="radio" name="paramR" value="3" v-model="paramR">3
        <input type="radio" name="paramR" value="4" v-model="paramR">4
      </div>           
      <div>
        <label>Координата X:</label>
        <input type="radio" name="coordX" value="-4" checked v-model="coordX">-4
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
        <label>Координата X:</label>
        <input type="text" name=coordY placeholder="введите Y" v-model="coordY">
      </div>    
      <p>Выбрано:</p>
      <p>R:{{ paramR }}</p>
      <p>X:{{ coordX }}</p>
      <p>Y:{{ coordY }}</p>
  
          
      <input id="submit-button" type="submit" value="Submit" action="mainPage.xhtml">
    </div>
    </form>
    <p color="#f00" id="wrong-input-message" hidden="true">Вы ввели данные некорректно</p>
    <center>
      <div>
        <canvas id="canvas" width="400" height="400"></canvas>
      </div>
    </center>
    <b-btn variant="primary" @click="getTableTextFromBackend()">Call the secured API</b-btn>
    <p></p>

    <div v-if="securedApiCallSuccess">
    <table class="coordTable">
			<tbody id="v-for-table" class="coordTable">
        <tr class="coordTableHeader"> 				
            <th>X coord</th>    				
            <th>Y coord</th>
            <th>R parameter</th>
            <th>is it hit</th>
        </tr>
        <tr v-for="item in backendResponse.table" :class="getTableRowClass()">
					<td>{{ item.paramR }}</td>
					<td>{{ item.coordX }}</td>
					<td>{{ item.coordY }}</td>
					<td>{{ item.hit }}</td>
				</tr>
			</tbody>
		</table>
       Full response: {{ backendResponse }} 
       test array: {{testarray}}
    </div>
    <div v-if="errors">
      <b-badge variant="warning">API call</b-badge> {{ errors }} <b-badge variant="warning">NOT successful</b-badge>
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
      securedApiCallSuccess: false,
      errors: null,
      paramR:'',
      coordX:'',
      coordY:'',
      tableRowCounter:0,
      testarray:[]
    }
  },
  methods: {
    getTableRowClass(){
      this.tableRowCounter=!this.tableRowCounter;
      return this.tableRowCounter?"coordTableOddRow":"coordTableEvenRow";
    },
    getTableTextFromBackend() {
      api.getTable(store.getters.getUserName, store.getters.getUserPass)
              .then(response => {
                console.log("Response: '" + response.data + "' with Statuscode " + response.status);
                this.securedApiCallSuccess = true;
                this.backendResponse = response.data;
                this.testarray=response.data.table;
              })
              .catch(error => {
                console.log("Error: " + error);
                this.errors = error;
              })
    }
  }
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
</style>