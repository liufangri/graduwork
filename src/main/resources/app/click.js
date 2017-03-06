window.$ = window.jQuery = require('./js/vendor/jquery.min.js');
function onBtnClick() {
    c.call('method', {
        bean: 'testService',
        method: 'test'
    },(data) =>{
        
        document.getElementById('p').innerHTML = data
    })
}

function onBtnClick2() {
    c.call('method', {
        bean: 'testService',
        method: 'addTest'
    },(data) =>{
        document.getElementById('p2').innerHTML = data
    })
}
function clearall() {
    var p = document.getElementById('p')
    var p2 = document.getElementById('p2')
    p.innerHTML = ''
    p2.innerHTML = ''
}

function onBtnClick3() {
    c.call('method', {
        bean: 'indexService',
        method: 'getContent',
        params:{
            url : 'http://dl.acm.org/'
        }
    },(data) =>{
        document.getElementById('p2').innerHTML = data
    })
}

function onBtnClick4(){
    c.call('method',{
        bean: 'acmService',
        method:'test'
    },(data)=>{
        document.getElementById('p').innerHTML = data
    }

    )
}

function onBtnClick5(){
    c.call('method',{
        bean: 'databaseServiceManager',
        method:'test'
    },(data)=>{
        document.getElementById('p').innerHTML = data
    }

    )
}