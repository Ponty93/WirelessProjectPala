"use strict";

module.exports = {
    app: {
        port: process.env.PORT || 3003,
        sslPort: process.env.SSL_PORT || 13003
    },
    logHeaders: {
        in: true,
        out: true
    },
    express: {
        'json spaces': 2, // TODO handle settings
        'x-powered-by': false,
        'etag': false
    }
};
