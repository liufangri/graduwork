const { app, BrowserWindow ,Menu} = require('electron')
const path = require('path')
const url = require('url')
const props = require('./properties').properties
const command = require('./systemCommand').command
props.getPropertiesFromFile('../parameters.properties')
const port = props.port;
console.log(port)
let win

function createWindow() {
    win = new BrowserWindow({ width: 1280, height: 720 })

    win.loadURL(url.format({
        pathname: path.join(__dirname, 'index.html'),
        protocol: 'file',
        slashes: true

    }))
    // win.webContents.openDevTools()
    win.on('closed', () => {
        win = null
    })
    //Set menu
    Menu.setApplicationMenu(null);
}
app.on('ready', createWindow)

app.on('window-all-closed', () => {
    if (process.platform != 'darwin') {
        command.command('\\exit system', 
        //connect success
        (data) => {
            if (data == 0) {
                app.quit()
            } else {

            }
        },
        //connect error
        (data)=>{
            app.quit()
        })
    }
})
app.on('activate', () => {
    if (win == null) {
        createWindow()
    }
})

