(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0e4b0c"],{"90fe":function(t,e,o){"use strict";o.r(e);var a=function(){var t=this,e=t.$createElement,o=t._self._c||e;return o("div",{staticClass:"app-container"},[o("el-button",{attrs:{type:"primary"},on:{click:t.addTask}},[t._v("新增任务")]),o("el-dialog",{attrs:{title:"新增任务",visible:t.dialogFormVisible},on:{"update:visible":function(e){t.dialogFormVisible=e}}},[o("el-form",{attrs:{model:t.form}},[o("el-form-item",{attrs:{label:"任务名称","label-width":t.formLabelWidth}},[o("el-input",{attrs:{autocomplete:"off"},model:{value:t.form.taskName,callback:function(e){t.$set(t.form,"taskName",e)},expression:"form.taskName"}})],1),o("el-form-item",{attrs:{label:"class","label-width":t.formLabelWidth}},[o("el-input",{attrs:{autocomplete:"off"},model:{value:t.form.taskClass,callback:function(e){t.$set(t.form,"taskClass",e)},expression:"form.taskClass"}})],1),o("el-form-item",{attrs:{label:"cron","label-width":t.formLabelWidth}},[o("el-input",{attrs:{autocomplete:"off"},model:{value:t.form.cron,callback:function(e){t.$set(t.form,"cron",e)},expression:"form.cron"}})],1),o("el-form-item",{attrs:{label:"taskNum","label-width":t.formLabelWidth}},[o("el-input",{attrs:{autocomplete:"off"},model:{value:t.form.taskNum,callback:function(e){t.$set(t.form,"taskNum",e)},expression:"form.taskNum"}})],1)],1),o("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[o("el-button",{on:{click:function(e){t.dialogFormVisible=!1}}},[t._v("取 消")]),o("el-button",{attrs:{type:"primary"},on:{click:function(e){return t.addOrEditTask(1)}}},[t._v("确 定")])],1)],1),o("el-table",{directives:[{name:"loading",rawName:"v-loading",value:t.listLoading,expression:"listLoading"}],attrs:{data:t.list,"element-loading-text":"Loading",border:"",fit:"","highlight-current-row":"","row-key":"id","default-expand-all":"","tree-props":{children:"children",hasChildren:"hasChildren"}}},[o("el-table-column",{attrs:{align:"center",label:"ID",width:"95"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(e.$index)+" ")]}}])}),o("el-table-column",{attrs:{label:"任务名"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(e.row.taskName)+" ")]}}])}),o("el-table-column",{attrs:{label:"taskClass",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[o("span",[t._v(t._s(e.row.taskClass))])]}}])}),o("el-table-column",{attrs:{label:"cron",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(e.row.cron)+" ")]}}])}),o("el-table-column",{attrs:{"class-name":"status-col",label:"status",align:"center"},scopedSlots:t._u([{key:"default",fn:function(e){return[o("el-tag",[t._v(t._s(t._f("statusFilter")(e.row.status)))])]}}])}),o("el-table-column",{attrs:{label:"操作",align:"center",width:"300px"},scopedSlots:t._u([{key:"default",fn:function(e){return[e.row.isChildren?t._e():o("el-button",{attrs:{type:"primary",size:"small",icon:"el-icon-edit",circle:""},on:{click:function(o){return t.editTask(e.row)}}}),o("el-button",{attrs:{type:0===e.row.status?"success":"warning",size:"small"},on:{click:function(o){return t.operateTask(e.row)}}},[t._v(" "+t._s(t._f("operateButtonFilter")(e.row.status))+" ")]),e.row.isChildren?t._e():o("el-button",{attrs:{type:"danger",size:"small"},on:{click:function(o){return t.deleteTask(e.row)}}},[t._v("删除")]),e.row.isChildren?o("el-button",{attrs:{type:"info",size:"small"},on:{click:function(o){return t.openSocket(e.row)}}},[t._v("查看日志")]):t._e()]}}])})],1),o("el-dialog",{attrs:{title:"展示日志",visible:t.dialogForLog,width:"1400px","before-close":t.handleLogDialogClose},on:{"update:visible":function(e){t.dialogForLog=e}}},[o("div",[o("p",{staticStyle:{"padding-bottom":"20px","padding-top":"5px"},attrs:{slot:"title"},slot:"title"},[t._v("监听程序日志")]),o("Button",{attrs:{type:"primary"},on:{click:t.openSocket}},[t._v("开启日志")]),t._v(" "),o("Button",{attrs:{type:"error"},on:{click:t.closeSocket}},[t._v("关闭日志")]),o("br"),o("br"),o("div",{staticStyle:{height:"400px","overflow-y":"scroll",background:"#333",color:"#aaa",padding:"10px"},attrs:{id:"filelog-container"}},[o("div",{attrs:{id:"aa"}},[t._v(t._s(t.pullFileLogger))])])],1)])],1)},l=[],n=o("b775");function i(t){return Object(n["a"])({url:"/task/list",method:"get",params:t})}function s(t){return Object(n["a"])({url:"/task/add",data:t,method:"post"})}function r(t){return Object(n["a"])({url:"/task/edit",data:t,method:"post"})}function c(t){return Object(n["a"])({url:"/task/operate",data:t,method:"post"})}function d(t){return Object(n["a"])({url:"/task/delete",data:t,method:"post"})}var u={filters:{statusFilter:function(t){var e={0:"已停止",1:"运行中"};return e[t]},operateButtonFilter:function(t){var e={0:"启动",1:"停止"};return e[t]}},data:function(){return{list:[],listLoading:!0,dialogFormVisible:!1,form:{taskName:"",taskClass:"",taskMethod:"",cron:"",taskNum:""},formLabelWidth:"120px",isEdit:!1,dialogForLog:!1,pullLogger:"",pullFileLogger:"正在连接任务节点，请稍后~",webSocket:null,tableData:[{id:1,date:"2016-05-02",name:"王小虎",address:"上海市普陀区金沙江路 1518 弄"},{id:2,date:"2016-05-04",name:"王小虎",address:"上海市普陀区金沙江路 1517 弄"},{id:3,date:"2016-05-01",name:"王小虎",address:"上海市普陀区金沙江路 1519 弄",children:[{id:31,date:"2016-05-01",name:"王小虎",address:"上海市普陀区金沙江路 1519 弄"},{id:32,date:"2016-05-01",name:"王小虎",address:"上海市普陀区金沙江路 1519 弄"}]},{id:4,date:"2016-05-03",name:"王小虎",address:"上海市普陀区金沙江路 1516 弄"}]}},created:function(){this.fetchData()},methods:{fetchData:function(){var t=this;this.listLoading=!0,i().then((function(e){t.list=e.data,t.listLoading=!1}))},addOrEditTask:function(){var t=this;this.isEdit?r(this.form).then((function(e){"success"===e.data?(t.dialogFormVisible=!1,t.fetchData()):alert(e.data)})):s(this.form).then((function(e){"success"===e.data&&(t.dialogFormVisible=!1,t.fetchData())}))},addTask:function(){this.dialogFormVisible=!0,this.isEdit=!1},editTask:function(t){this.form=t,this.isEdit=!0,this.dialogFormVisible=!0},operateTask:function(t){var e=this;c(t).then((function(t){"success"===t.data?e.fetchData():alert(t.data)}))},deleteTask:function(t){var e=this;d(t).then((function(t){"success"===t.data?e.fetchData():alert(t.data)}))},openSocket:function(t){this.dialogForLog=!0;var e=null;function o(t){var e=document.getElementById("aa"),o=document.createElement("p");o.style.wordWrap="break-word",o.style.fontSize="10px",o.style.width="1200px",o.appendChild(document.createTextNode(t)),e.appendChild(o);var a=document.getElementById("filelog-container");a.scrollTop=a.scrollHeight}"WebSocket"in window?(e=new WebSocket("ws://"+window.config.server_ip+":"+window.config.server_port+"/test/one/"+t.taskClass+"-client_"+t.id),this.webSocket=e):alert("Not support websocket"),e.onerror=function(){o("error")},e.onopen=function(t){},e.onmessage=function(t){o(t.data)},e.onclose=function(){o("close")},window.onbeforeunload=function(){e.close()}},handleLogDialogClose:function(t){null!=this.webSocket&&this.webSocket.close(),t()},closeSocket:function(){null!=this.webSocket&&(this.webSocket.disconnect(),this.webSocket=null,console.log("关闭websocket连接"))}}},f=u,m=o("2877"),p=Object(m["a"])(f,a,l,!1,null,null,null);e["default"]=p.exports}}]);