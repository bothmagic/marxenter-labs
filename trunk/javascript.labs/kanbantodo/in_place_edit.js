/*
 Copyright (c) 2013 Markus Marx

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
YUI.add('ipe', function(Y){

    // A plugin class designed to create Flickr style in place editing.
    function InPlaceEditorPlugin(config){
        InPlaceEditorPlugin.superclass.constructor.apply(this, arguments);
    }

    /*
     Define Static properties NAME (to identify the class) and NS (to identify
     the namespace)
     */
    InPlaceEditorPlugin.NAME = 'inPlaceEditorPlugin';
    InPlaceEditorPlugin.NS = 'ipe';

    DEFAULT_CALLBACKS = {

        onFailure: function(ipe){
            alert(ipe._errorMessage);
        },

        mouseout: function(ipe){
            if (!ipe._saving) {
                var host = ipe.get('host');
                //host.setStyle('backgroundColor', ipe._originalBackground);
                //host.addClass(ipe.get('hoverClassName'));
            }
        },

        mouseover: function(ipe){
            if (!ipe._saving) {
                var host = ipe.get('host');
                //host.setStyle('backgroundColor', ipe.get('highlightColor'));
                //host.removeClass(ipe.get('hoverClassName'));
            }
        }

    }

    // Attribute definitions for the plugin
    InPlaceEditorPlugin.ATTRS = {

        /**
         * @attribute rows
         * @description Rows attribute of generated field
         * @type String
         */
        rows: {

            value: 1,

            validator: Y.Lang.isNumber

        }

    }

    // Extend Plugin.Base
    Y.extend(InPlaceEditorPlugin, Y.Plugin.Base,{


        /**
         * @private
         * @method initializer
         * @description Private lifecycle method
         */
        initializer: function(){
            this._prepareHost();
            this._addListeners();
        },

        /**
         * @private
         * @method destructor
         * @description Lifecycle destructor, remove listeners
         */
        destructor: function() {
            this.wrapUp();
            this.detachAll();
        },

        /**
         * @private
         * @method _prepareHost
         * @description Prepares the host for Editing
         */
        _prepareHost: function(){
            var host = this.get('host');
        },

        _initMultiLineEdit: function() {
            this._editorField.on('frame:ready', function () {
                this.focus();
            });

            var myipe = this;

            this._editorField.on('dom:keypress', function (e) {
                if (e.keyCode == 13) {
                    myipe.leaveEditMode();
                } else {
                    var size = this.getInstance().one("body").get("region");
                    myipe._editorDiv.setStyle("height", myipe._calculateOptimalHeight(size.height));
                }
            });

            this._editorField.after('dom:focusout', function (e) {
                //console.log("focus out");
                Y.Lang.later(50, Y, function () {
                    myipe.leaveEditMode();
                });
            });
        },

        _removeEditField: function() {
            var text = this._editorField.get('content');
            //this._editorField.destroy();
            this._editorDiv.remove(true);
            return text;
        },

        _calculateOptimalHeight: function(height) {
            return height + 16;
        },

        /**
         * @method createEditField
         * @description Figures out what type of field to create, and then does so.
         */
        createEditField: function(){
            var host = this.get('host'),
                text = host.get('text'),
                size = host.get('region'),

                fldConfig = {
                    className: this.get('editorClassName'),
                    cols: this.get('cols'),
                    //name: this.get('paramName'),
                    size: this.get('size'),
                    value: text.replace(/^\s*|\s*$/g, "")
                },
                fld;

            // Figure out what kind of node to create, and adjust fldConfig as needed.
            if (1 >= this.get('rows') && !/\r|\n/.test(text)) {
                fld =Y.Node.create('<input />');
                fldConfig.type = "text";
                this._editorField = fld;
            } else {
                var fld = new Y.EditorBase({
                    content: text
                });
                var n = Y.Node.create('<div></div>');
                n.setStyle("height", this._calculateOptimalHeight(size.height));
                //Add the BiDi plugin
                fld.plug(Y.Plugin.EditorBidi);
                fld.on('frame:ready', function () {

                    this.focus();
                });

                this._editorDiv = n;
                this._editorField = fld;
                this._initMultiLineEdit();
                host.insert(n, 'before');
                fld.render(n);

            }
            // Get the node, and set its attrs to fldConfig
            //fld = Y.one(fld).setAttrs(fldConfig);

            // Set its with to match the host's
            //fld.setStyle('width', size + 'px');

            // Set the field to this._editorfield, and add it to the form

        },

        /**
         * @method enterEditMode
         * @description Hides the host element, creates the form, and inserts it
         * before the host
         */
        enterEditMode: function(){

            var host = this.get('host');

            if (this._editing){
                return;
            }

            this._editing = true;
            this.createEditField();
            host.setStyle('display', 'none')

        },

        /**
         * @method leaveEditMode
         * @description exits editing mode
         */
        leaveEditMode: function(){
            var host = this.get('host');
            //this._fireCallback('mouseout');
            var text = this._removeEditField();
            host.setHTML(text);
            host.setStyle('display', '');

            this._saving = false;
            this._editing = false;
        },

        /**
         * @private
         * @method _fireCallback
         * @description Fires a given callback if defined. Passes the ipe instance
         * to the callee.
         */
        _fireCallback: function(callback){
            var callBackFunc = this.get('callbacks')[callback];

            if (callBackFunc) {
                callBackFunc(this);
            }

        },

        /**
         * @method wrapUp
         * @description Cleans everything up
         */
        wrapUp: function() {
            this.leaveEditMode();
            this._fireCallback('onComplete');
        },

        /**
         * @private
         * @method _addListeners
         * @description Attaches listeners around the host
         */
        _addListeners: function(){

            this.doAfter("click", function(event){
                event.halt();
                this.enterEditMode();
            });

            /*this.doAfter("mouseenter", function(){
                this.get('host').addClass(this.get('hoverClassName'));
                this.get('callbacks').mouseover(this);
            });

            this.doAfter("mouseleave", function(){
                this.get('host').removeClass(this.get('hoverClassName'));
                this.get('callbacks').mouseout(this);
            });*/

        }


    })



    // Get the Y.Plugin Namespace
    Y.namespace("Plugin");

    // Set Y.Plugin.InPlaceEditor to the InPlaceEditorPlugin
    Y.Plugin.InPlaceEditor = InPlaceEditorPlugin;

}, '0.0.1' ,{requires:['plugin', 'io-base', 'io-form', 'event-mouseenter', 'editor'], skinnable:false})
