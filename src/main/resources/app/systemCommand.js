var net = require('net')
const props = require('./properties').properties
props.getPropertiesFromFile('../parameters.properties')
var HOST = '127.0.0.1'
var PORT = props.current_port

var command = {};
command.command = function (str, method, error) {
    var client = new net.Socket()
    var result = 'Failed'
    client.connect(PORT, HOST, () => {
        client.write(str + '\n');
    })
    client.on('data', (data) => {
        client.destroy()
        method(data)
    })
    client.on('close', () => {

    })
    client.on('error', (data) => {
        error(data)
        console.log('error:' + data.message)
    })
}
exports.command = command