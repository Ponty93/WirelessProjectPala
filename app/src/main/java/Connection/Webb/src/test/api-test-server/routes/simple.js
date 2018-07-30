"use strict";

var SIMPLE_ASCII = 'Hello/World & Co.?';

module.exports = function registerRoutes(app) {

    app.get('/simple.txt', returnFormAsText);
    app.post('/simple.txt', returnFormAsText);

    function returnFormAsText(req, res) {
        res.header('Content-Type', 'text/plain');
        res.send(req.param('p1') + ', ' + req.param('p2'));
    }

    app.get('/simple.json', function (req, res) {
        res.json({p1: req.param('p1'), p2: req.param('p2')});
    });

    app.post('/simple.json', function (req, res) {
        res.status(201).location('http://example.com/4711').send();
    });

    app.put('/simple.json', function (req, res) {
        res.json(req.body);
    });

    app.del('/simple', function (req, res) {
        res.send(204);
    });

    app.get('/no-content', function (req, res) {
        res.send(204);
    });

    app.get('/parameter-types', function (req, res) {
        var ok = req.param('string') === SIMPLE_ASCII &&
            req.param('number') === '4711' &&
            req.param('null') === '' &&
            req.param('empty') === '';

        res.send(ok ? 204 : 500);
    });

    app.get('/multiple-valued-parameter', function (req, res) {
        var m = req.param('m');

        var ok = m.length && m.length === 4 &&
            m[0] === 'abc' && m[1] === '1' && m[2] === 'true' && m[3] === 'abc@abc.com';

        res.send(ok ? 204 : 500);
    });
};
