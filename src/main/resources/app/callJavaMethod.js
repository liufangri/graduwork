var net = require('net')
const props = require('./properties').properties
props.getPropertiesFromFile('../parameters.properties')
var HOST = '127.0.0.1'
var calljavamethod = {}
var PORT = props.current_port

calljavamethod.call = function (kind, data, method, error) {
    var client = new net.Socket()
    var result = 'Failed'
    client.connect(PORT, HOST, () => {
        if (kind == 'method') {
            var message = '@method:' + data.bean + '!' + data.method;
            if (data.params != undefined) {
                message += '?'
                for (var a in data.params) {
                    message += a + '=' + data.params[a] + '&'
                }
                if (message.charAt(message.length - 1) == '&') {
                    message = message.substring(0, message.length - 1)
                }
            }
            client.write(message + '\n');
        } else {
            //TODO: Other kinds of request
        }

    })
    client.on('data', (data) => {
        client.destroy()
        method(data)
    })
    client.on('close', () => {
        
    })
    client.on('error', (data) => {
        console.log('error:' + data.message)
        error(data)
    })
}
exports.calljavamethod = calljavamethod