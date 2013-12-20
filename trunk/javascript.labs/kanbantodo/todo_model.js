/**
 * Created with JetBrains PhpStorm.
 * User: marxma
 * Date: 11.04.13
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */

YUI.add('todomodel', function (Y) {



    Y.TodoModel = Y.Base.create('todoModel', Y.Model, [Y.ModelSync.REST], {
        root: 'http://symfony.localhost/web/app_dev.php/todos'

    }, {
        ATTRS: {

            shortDesc: {
                value: ''
            },

            longDesc: {
                value: ''
            }
        }
    });


    Y.TodoModels = Y.Base.create('users', Y.ModelList, [Y.ModelSync.REST], {

        model: Y.TodoModel
    });

}, '0.0.1', {
    requires: ['model', 'model-list', 'model-sync-rest']
});
