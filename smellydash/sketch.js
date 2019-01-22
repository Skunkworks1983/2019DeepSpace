const remote = require('electron').remote;
const con = remote.getGlobal('console');
const windowBounds = remote.getCurrentWindow().webContents.getOwnerBrowserWindow().getBounds();
const nt = require('wpilib-nt-client');

const client = new ntClient.Client()

// Connects the client to the server on team 1983's roborio
client.start((isConnected, err) => {
    // Displays the error and the state of connection
    con.log({ isConnected, err });
}, 'roborio-1983.local');

// Adds a listener to the client
client.addListener((key, val, type, id) => {
    con.log({ key, val, type, id });
})

function setup() {
  createCanvas(windowBounds.width, windowBounds.height);
  framerate(50);
  con.log("setup completed");
}

function draw() {
  // background(200);
  ellipse(10,10,10,10);
}
