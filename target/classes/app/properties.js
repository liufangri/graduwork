var properties = {}
var fs = require('fs')

properties.getPropertiesFromFile = function (filePath) {
    var data = fs.readFileSync(filePath, 'utf-8')
    var parms = data.split('\n')
    for (var element of parms) {
        if (element !== '') {
            if (!element.startsWith('#')) {
                var e = element.split('=')
                properties[e[0]] = e[1]
            }
        }
    }
}
exports.properties = properties
// properties.getPropertiesFromFile('../parameters.properties')