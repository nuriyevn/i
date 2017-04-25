var request = require('request');
var _ = require('lodash');
var nodemailer = require('nodemailer');
var transporter = nodemailer.createTransport('smtp://answer@es.kievcity.gov.ua:M7WpgJI7Ge@mail.kievcity.gov.ua');

var oUtil = require('../../components/activiti');
var oAuth = require('../../components/admin');

function getOptions(req) {
  var config = require('../../config/environment');
  //var config = require('../../config');

  var activiti = config.activiti;

  return {
    protocol: activiti.protocol,
    hostname: activiti.hostname,
    port: activiti.port,
    path: activiti.path,
    username: activiti.username,
    password: activiti.password
  };
}

module.exports.post = function (req, res) {
  //options, callback
  var options = getOptions(req);
  var url = options.protocol + '://' + options.hostname+(options.port?':'+options.port + options.path:options.path) + '/subject/message/setMessage';

  var data = req.body;

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  return request.post({
    'url': url,
    'auth': {
      'username': options.username,
      'password': options.password
    },
    'qs': {
      'sMail': data.sMail,
      'sHead': data.sHead,
      'sBody': data.sBody
    }
  }, callback);
};

module.exports.get = function (req, res) {
  //options, callback
  var options = getOptions(req);
  var url = options.protocol + '://' + options.hostname+(options.port?':'+options.port + options.path:options.path) + '/subject/message/getMessages';

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  return request.get({
    'url': url,
    'auth': {
      'username': options.username,
      'password': options.password
    }
  }, callback);
};

module.exports.getMessageFile = function (req, res) {
  var options = getOptions(req);
  var url = options.protocol + '://' + options.hostname+(options.port?':'+options.port + options.path:options.path) + '/subject/message/getMessageFile?nID_Message=' + req.query.nID;

  var r = request({
    json: true,
    url: url,
    auth: {
      'username': options.username,
      'password': options.password
    }
  });

  req.pipe(r).on('response', function (response) {
    response.headers['content-type'] = 'application/octet-stream';
  }).pipe(res);
};

module.exports.getSubjectMessageData = function (req, res) {
  var options = getOptions(req);
  var url = options.protocol + '://' + options.hostname+(options.port?':'+options.port + options.path:options.path) + '/subject/message/getSubjectMessageData?nID_SubjectMessage=' + req.query.nID;

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  return request.get({
    'url': url,
    'auth': {
      'username': options.username,
      'password': options.password
    }
  }, callback);
};

module.exports.findFeedback = function (req, res) {

  var options = getOptions(req);
  var url = options.protocol + '://'
    + options.hostname
    + (options.port?":"+ options.port+ options.path:options.path)
    + '/subject/message/getMessageFeedbackExtended?sID_Order='
    + req.param('sID_Order')
    + '&sToken=' + req.param('sToken');

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  return request.get({
    'url': url,
    'auth': {
      'username': options.username,
      'password': options.password
    }
  }, callback);
};

module.exports.postFeedback = function (req, res) {
  var options = getOptions(req);
  var url = options.protocol + '://' + options.hostname+(options.port?":"+ options.port+ options.path:options.path) + '/subject/message/setMessageFeedbackExtended';

  var data = req.body;

  var callback = function (error, response, body) {
    res.send(body);
    res.end();
  };

  return request.post({
    'url': url,
    'auth': {
      'username': options.username,
      'password': options.password
    },
    'qs': {
      'sID_Order': data.sID_Order,
      'sToken': data.sToken,
      'sBody': data.sBody
    }
  }, callback);
};

module.exports.postServiceMessage = function (req, res) {
  var oData = req.body;
  var sToken = oData.sToken;
  if (!!req.session.subject.nID || oUtil.bExist(sToken)) {
    var nID_Subject = (oUtil.bExist(req.session) && req.session.hasOwnProperty('subject') && req.session.subject.hasOwnProperty('nID')) ? req.session.subject.nID : null;
    //if (!!req.session.subject.nID){
    //  var nID_Subject = req.session.subject.nID;
    var options = getOptions(req);
    var sURL = options.protocol + '://' + options.hostname+(options.port?":"+ options.port+ options.path:options.path)+ '/subject/message/setServiceMessage';

    var bAdmin = false;
    if (req.session && req.session.subject) {
      bAdmin = oAuth.isAdminInn(req.session.subject.sID);
      console.log("[searchOrderBySID]:bAdmin=" + bAdmin);
    }

    var callback = function (error, response, body) {
      res.send(body);
      res.end();
    };


    var oDateNew = {
      'sID_Order': oData.sID_Order,
      'sBody': oData.sBody,
      'nID_SubjectMessageType': 8
      , 'bAuth': !bAdmin
    };
    if (oUtil.bExist(sToken)) {
      oDateNew = _.extend(oDateNew, {'sToken': sToken});
    }
    if (oUtil.bExist(nID_Subject)) {
      oDateNew = _.extend(oDateNew, {'nID_Subject': nID_Subject});
    }

    if (oUtil.bExist(oData.sID_File) && oUtil.bExist(oData.sFileName)) {
      oDateNew.sID_File = oData.sID_File;
      oDateNew.sFileName = oData.sFileName;
    }

    return request.post({
      'url': sURL,
      'auth': {
        'username': options.username,
        'password': options.password
      },
      'qs': oDateNew/* {
       'sID_Order': oData.sID_Order,
       'sBody': oData.sBody,
       'nID_SubjectMessageType' : 8,
       'nID_Subject' : nID_Subject
       }*/
    }, callback);

  } else {
    res.end();
  }

};

module.exports.findServiceMessages = function (req, res) {
  var sToken = req.param('sToken');
  if (!!req.session.subject.nID || oUtil.bExist(sToken)) {
    //var nID_Subject = (req.session && req.session!==null && req.session.hasOwnProperty('subject') && req.session.subject.hasOwnProperty('nID')) ? req.session.subject.nID : null;
    var nID_Subject = (oUtil.bExist(req.session) && req.session.hasOwnProperty('subject') && req.session.subject.hasOwnProperty('nID')) ? req.session.subject.nID : null;

    //var nID_Subject = req.session.subject.nID;

    var bAdmin = false;
    if (req.session && req.session.subject) {
      bAdmin = oAuth.isAdminInn(req.session.subject.sID);
      console.log("[searchOrderBySID]:bAdmin=" + bAdmin);
    }

    var options = getOptions(req);
    var url = options.protocol + '://'
        + options.hostname
        + (options.port?":"+options.port+ options.path:options.path)
        + '/subject/message/getServiceMessages?'
        + 'sID_Order=' + req.param('sID_Order')
        //+ '&nID_Subject=' + nID_Subject
        + (oUtil.bExist(nID_Subject) ? '&nID_Subject=' + nID_Subject : "")
        + (oUtil.bExist(sToken) ? '&sToken=' + sToken : "")
        + '&bAuth=' + (!bAdmin)
      ;

    var callback = function (error, response, body) {
      var resout = {
        messages: JSON.parse(body)
        //,nID_Subject: req.session.subject.nID
      };
      res.send(resout);
      res.end();
    };

    return request.get({
      'url': url,
      'auth': {
        'username': options.username,
        'password': options.password
      }
    }, callback);
  } else {
    res.end();
  }

};

module.exports.sendMail = function (req, res) {
  return request({
    url:'https://www.google.com/recaptcha/api/siteverify',
    method:'POST',
    form:{
      secret:'6LcsfxIUAAAAANvpJUrlJmHF1k_Fqh5dYbPSTgPZ',
      response:req.headers["g-recapcha-response"]
    },
    json:true
  },function (err, resp, body) {
    if(body.success==false)return res.status(400).send(body["error-codes"]);

    var mailOptions = {
      from: 'answer@es.kievcity.gov.ua', // sender address
      to: 'answer@es.kievcity.gov.ua', // list of receivers
      subject: 'Запит на послугу чи розділ', // Subject line
      text: `Користувач запросив послугу ${req.body.message}`, // plaintext body
      html: `Користувач запросив послугу ${req.body.message}` // html body
    };

// send mail with defined transport object
    transporter.sendMail(mailOptions, function (error, info) {
      if (error) {
        return console.error(error);
      }
      res.send();
    });
    res.send();
  })
};
