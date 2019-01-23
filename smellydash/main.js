// Modules to control application life and create native browser window
const {app, BrowserWindow} = require('electron')
const nt = require('wpilib-nt-client');

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
var win;
var client;

function createWindow () {
  console.log("Loading splash image");

  let loadingWin = new BrowserWindow({ width: 317, height: 148, frame: false });
  loadingWin.loadFile('splash.png');

  console.log("Loading NetworkTables client");
  ntClient = new nt.Client();
  ntClient.setReconnectDelay(20);

  // Connects the client to the server on team 1983's roborio
  // We do this as early as possible so it has time to connect while the
  // renderer process loads
  ntClient.start((isConnected, err) => {
      // Displays the error and the state of connection
      console.log({ isConnected, err });
  }, 'roborio-1983.local');

  console.log("Creating main window")
  // Create the browser window.
  win = new BrowserWindow({width: 600, height: 600,
    backgroundColor: '#2e2c29', show: false})

  console.log("Loading index.html");
  // and load the index.html of the app.
  win.loadFile('index.html');

  // Open the DevTools.
  //win.webContents.openDevTools()

  win.once('ready-to-show', () => {
    console.log("Window ready to show");
    loadingWin.destroy();
    win.show();
  })

  // Emitted when the window is closed.
  win.on('closed', function () {
    // Dereference the window object, usually you would store windows
    // in an array if your app supports multi windows, this is the time
    // when you should delete the corresponding element.
    win = null
  })
}


// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', createWindow)

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
