const c = require('./callJavaMethod').calljavamethod
window.$ = window.jQuery = require('./js/vendor/jquery.min.js');
function onBtnClick() {
    c.call('method', {
        bean: 'testController',
        method: 'test'
    },(data) =>{
        
        document.getElementById('p').innerHTML = data
    })
}

function onBtnClick2() {
    c.call('method', {
        bean: 'testController',
        method: 'work'
    },(data) =>{
        document.getElementById('p2').innerHTML = JSON.parse(data).res
    })
}
function clearall() {
    var p = document.getElementById('p')
    var p2 = document.getElementById('p2')
    p.innerHTML = ''
    p2.innerHTML = ''
}

