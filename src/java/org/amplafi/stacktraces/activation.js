function XSS(id, url) {
  this.id = id;
  this.url = url + "&noCache=" + (new Date()).getTime();// + "&server="+base_uri;
}

XSS.prototype.done = function(color) {
  this.indicator.style.backgroundColor = color;
  if (this.oncomplete) this.oncomplete(this.status);
};

function dump(o) {
  var s = "";
  for(var p in o) {
    if(!/on.*|inner.*|outer.*|src/.test(p))
    s+=p+"="+o[p]+"\t";
  }
  alert(s);
}

XSS.prototype.doneR = function() {
  if(navigator.userAgent.indexOf('Opera')>=0 || /loaded|complete/.test(this.transport.readyState)) {
    this.status = 1;
    this.doneL();
  }
};

XSS.prototype.doneL = function() {
  this.status = this.status | this.transport.width;
  if(this.status>0) {
    this.done('green');
  } else {
    this.done('orange');
  }
};

XSS.prototype.doneE = function() {
  this.status = -1;
  this.done('silver');
};

XSS.prototype.go = function() {
  this.transport = document.getElementById(this.id);
  this.parent = document.getElementById(Activator.PANELID);
  if (this.transport) {
    this.dispose();
  }
  this.transport = new Image();
  this.transport.id = this.id;
  /*if(navigator.userAgent.indexOf('Opera')>=0 || navigator.userAgent.indexOf('MSIE')>=0 ) {
    this.transport.onreadystatechange = this.doneR.bind(this);
  } else {
    this.transport.onload = this.doneL.bind(this);
  }
  this.transport.onerror = this.doneE.bind(this);
  this.transport.onabort = this.doneE.bind(this);*/
  this.transport.style.display = 'none';
  this.indicator = document.createElement("div");
  this.indicator.className='activationIndicator';
  this.indicator.appendChild(this.transport);
  this.parent.appendChild(this.indicator);

  this.transport.src = this.url; // start loading image
};

XSS.prototype.dispose = function() {
  if(navigator.userAgent.indexOf('Opera')>=0 || navigator.userAgent.indexOf('MSIE')>=0 ) {
    this.transport.onreadystatechange = function() {};
  } else {
    this.transport.onload = function() {};
  }
  this.transport.onerror = function() {};
  this.transport.onabort = function() {};
  if (this.transport.parentNode) {
    this.transport.parentNode.removeChild(this.transport);
  }
  if (this.indicator && this.indicator.parentNode) {
    this.indicator.parentNode.removeChild(this.indicator);
  }
}

function XSSBroadcast(command) {
  this.panel = document.getElementById(Activator.PANELID);
  if(!this.panel) {
    this.parent = document.getElementsByTagName("body").item(0);
    this.panel = document.createElement("div");
    this.panel.className = Activator.PANELID;
    this.panel.setAttribute("id", Activator.PANELID);
    this.parent.appendChild(this.panel);
    this.panel.style.display = 'block';
  }

  this.sequental = (navigator.userAgent.indexOf('WebKit')>0);
  this.id = "id_" + Math.random();

  var START_PORT = 63330;
  var END_PORT = START_PORT + 9;
  var HOST = "http://127.0.0.1";

  this.responseCount = 0;
  this.successes = 0;
  this.connects = 0;
  this.requests = new Array(END_PORT - START_PORT);
  var oncomplete = function(){};//this.done.bind(this);
  for (var port = START_PORT; port <= END_PORT; port++) {
    var uri = HOST + ":" + port + "/" + command;
    var xss = new XSS("r_" + this.id + "_" + port, uri);
    xss.oncomplete = oncomplete;
    this.requests[port - START_PORT] = xss;
    if(!this.sequental) xss.go();
  }
  this.index = 0;
  if(this.sequental) this.go();
}

XSSBroadcast.prototype.go = function() {
  this.requests[this.index++].go();
}

XSSBroadcast.prototype.done = function(status) {
  this._processingResponse = true;
  if (this._timeout) {
     clearTimeout(this._timeout);
  }
  this.responseCount++;
  if (status>=0) {
    this.connects++;
  }
  if (status>0) {
    this.successes++;
  }

  if(this.responseCount >= this.requests.length) {
    this._timeout = setTimeout(this.notify.bind(this), 2000);
  }
  this._processingResponse = false;
  if(this.sequental && (this.index < this.requests.length)) {
    this.go();
  }
};

XSSBroadcast.prototype.notify = function() {
  if (this._processingResponse) return;

  if(this.successes==0) {
    if (this.connects==0) {
      alert("No IDE responded.\n\nMake sure TeamCity plugin is installed, and you are logged in to TeamCity from within the IDE.\n" +
            "You can download the plugin from \"My Settings & Tools\" page.");

    } else {
      alert("IDE cannot locate the requested file.");
    }
  }
  this.panel.parentNode.removeChild(this.panel);
  this._timeout = null;

  for (var i=0; i<this.requests.length; i++) {
    this.requests[i].oncomplete = function() {};
    this.requests[i].dispose();
  }
}

var Activator = {};
Activator.PANELID = "activationPanel";
Activator.doOpen = function (command) {
  new XSSBroadcast(command);
}


