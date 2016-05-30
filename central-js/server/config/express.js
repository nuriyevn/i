/**
 * Express configuration
 */

'use strict';

var express = require('express');
var favicon = require('serve-favicon');
var morgan = require('morgan');
var compression = require('compression');
var bodyParser = require('body-parser');
var methodOverride = require('method-override');
var cookieParser = require('cookie-parser');
var errorHandler = require('errorhandler');
var path = require('path');
var config = require('./environment');
//var config = require('./index.js');
var session = require('cookie-session');

module.exports = function (app) {
  app.set('env', config.env);
  var env = app.get('env');

  app.set('views', config.root + '/server/views');
  app.engine('html', require('ejs').renderFile);
  app.set('view engine', 'html');
  app.use(compression());
  app.use(bodyParser.urlencoded({extended: false}));
  app.use(bodyParser.json({limit: '5mb'}));
  app.use(session({
    secret: config.server.session.secret,
    keys: config.server.session.keys,
    secure: config.server.session.secure === "true",
    signed: true,
    maxAge: config.server.session.maxAge
  }));
  app.use(methodOverride());
  app.use(cookieParser());

<<<<<<< HEAD
  //if ('prod' === env || 'test-alpha' === env || 'test-beta' === env) {
  if(config.bCompile){
=======
  if ('prod' === env || 'prod-backup' === env || 'test-alpha' === env || 'test-beta' === env || 'test-delta' === env || 'test-omega' === env) {
  //if(config.bCompile){
>>>>>>> refs/heads/master
    app.use(favicon(path.join(config.root, 'public', 'favicon.ico')));
    app.use(express.static(path.join(config.root, 'public')));
    app.set('appPath', config.root + '/public');
    app.use(morgan('dev'));
  }

  if (!config.bCompile || 'local' === env || 'test' === env) {
  //if (!config.bCompile) {
    app.use(require('connect-livereload')());
    app.use(express.static(path.join(config.root, '.tmp')));
    app.use(express.static(path.join(config.root, 'client')));
    app.use('/public-js', express.static(path.resolve(config.root + '../../public-js')));
    app.set('appPath', path.join(config.root, 'client'));
    app.use(morgan('dev'));
    app.use(errorHandler()); // Error handler - has to be last
  }
};
