/**
 * Created with JetBrains PhpStorm.
 * User: marxma
 * Date: 11.04.13
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */

YUI().use(
    'model', function(Y) {
// Create a new Y.PieModel class that extends Y.Model.

Y.PieModel = Y.Base.create('pieModel', Y.Model, [], {
    // Add prototype methods for your Model here if desired. These methods will be
    // available to all instances of your Model.

    // Returns true if all the slices of the pie have been eaten.
    allGone: function () {
        return this.get('slices') === 0;
    },

    // Consumes a slice of pie, or fires an `error` event if there are no slices
    // left.
    eatSlice: function () {
        if (this.allGone()) {
            this.fire('error', {
                type : 'eat',
                error: "Oh snap! There isn't any pie left."
            });
        } else {
            this.set('slices', this.get('slices') - 1);
            Y.log('You just ate a slice of delicious ' + this.get('type') + ' pie!');
        }
    }
}, {
    ATTRS: {
        // Add custom model attributes here. These attributes will contain your
        // model's data. See the docs for Y.Attribute to learn more about defining
        // attributes.

        slices: {
            value: 6 // default value
        },

        type: {
            value: 'apple'
        }
    }
});
    })

