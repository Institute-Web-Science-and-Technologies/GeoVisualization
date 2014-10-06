var app = require('express')();



function Game() {
    this.clients = [];
    this.messages = [];
    this.clientID = 0;
}

Game.prototype.addClient = function(name) {
    if (!name)
        return -1;
    for (var i in this.clients)
        if (this.clients[i].name == name) {
            console.log("name " + name + " already used");
            return -1;
        }
    var id = this.clientID++;
    this.clients.push(new Client(name, id));
    console.log("new client connected : " + name + ", #" + id);
    return id;
}

Game.prototype.addMsg = function(sender, text) {
    if (sender && text) {
        this.messages.push(new Message(sender, text));
        console.log(sender + ": " + text);
    }
}

games = [new Game()];

function Client(name, id) {
    this.id = id;
    this.name = name;
}

function Message(sender, text) {
    this.sender = sender;
    this.text = text;
}

app.get('/createplayer', function(req, res) {
    var id = (games[0].addClient(req.query.name));
    //res.status(id).end();
    res.send(""+id);
});

app.get('/msg', function(req, res) {
    games[0].addMsg(1 * req.query.clientid, req.query.text);
    res.end();
});

app.get('/update', function(req, res) {
    var c = JSON.stringify(games[0]);
    res.send(c);
});

app.get('/', function(req, res) {
	console.log("test");
    res.send("Hello Client!");
});

app.listen(3000);