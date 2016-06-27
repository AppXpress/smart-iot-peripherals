angular.module('app.services').constant('$config', {
    basePath: 'https://platform-preprod.gtnexus.com/rest/310/',
    dataKey: '70213cec5933b3956c9a201e7c9c59b40423ce98',
    customObjects: {
        POLL: '$pollB2',
    },
    log: true,
    push : {
        server : "http://54.197.229.207",
        subscribers_endpoint : "/subscribers",
        unicast_endpoint : "/unicast",
        gcm : {
           // senderId : "AIzaSyAiVJOw0hwlipa5PzJDF64SYdovC1k_8zU",
            senderId : "169925722149"
        }
    },
    messages: {}
});