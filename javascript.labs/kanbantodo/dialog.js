YUI.add('my-module', function (Y) {

    // Write your module code here, and make your module available on the Y
    // object if desired.
    Y.MyModule = {

        sayHello: function() {
            console.log("");
        }

    };
}, '0.0.1', {
        requires: ['node', 'event']
    })
