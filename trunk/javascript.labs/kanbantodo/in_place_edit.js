/*
 Copyright (c) 2010 Elliot Laster

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
                host.setStyle('backgroundColor', ipe._originalBackground);
                host.addClass(ipe.get('hoverClassName'));
            }
        },

        mouseover: function(ipe){
            if (!ipe._saving) {
                var host = ipe.get('host');
                host.setStyle('backgroundColor', ipe.get('highlightColor'));
                host.removeClass(ipe.get('hoverClassName'));
            }
        }

    }

    // Attribute definitions for the plugin
    InPlaceEditorPlugin.ATTRS = {

        /**
         * @attribute callbacks
         * @description Use when multi-line w/ rows == 1
         * @type String
         */
        autoRows: {

            value: 3,

            validator: Y.Lang.isString

        },

        /**
         * @attribute callbacks
         * @description Object of callbacks to be run at certain interesting moments: mouseover,
         mouseout,
         * @type String
         */
        callbacks: {

            value: DEFAULT_CALLBACKS,

            setter: function(options){
                return Y.merge(DEFAULT_CALLBACKS, options)
            }


        },

        /**
         * @attribute cancelControl
         * @description Type of cancel control to create. Must be 'button' or 'link'
         * @type String
         */
        cancelControl: {

            value: 'button',

            validator: function(value){
                return Y.Lang.isString(value) &&
                    (value === 'button' || value === 'link')
            }

        },

        /**
         * @attribute cancelText
         * @description Text for cancel control.
         * @type String
         */
        cancelControlText: {

            value: 'Cancel',

            validator: Y.Lang.isString

        },

        /**
         * @attribute clickToEditText
         * @description Text set to host's title attribute.
         * @type String
         */
        clickToEditText: {

            value: 'Click to edit',

            validator: Y.Lang.isString

        },

        /**
         * @attribute cols
         * @description cols attr for generated input
         * @type String
         */
        cols: {

            value: 0,

            validator: Y.Lang.isNumber

        },

        /**
         * @attribute editorClassName
         * @description Class name for generated input
         * @type String
         */
        editorClassName: {

            value: 'editor_field',

            validator: Y.Lang.isString

        },

        /**
         * @attribute formClassName
         * @description Class attribute for the generated form
         * @type String
         */
        formClassName: {
            validator: Y.Lang.isString
        },

        /**
         * @attribute formId
         * @description ID attribute for the generated form
         * @type String
         */
        formId: {
            validator: Y.Lang.isString
        },

        /**
         * @attribute highlightColor
         * @description Color to set host's background color to on hover. Defaults to #FFFF99
         * @type String
         */
        highlightColor: {

            value: '#FFFF99',

            validator: Y.Lang.isString

        },

        /**
         * @attribute hoverClassName
         * @description Class name added to host on hover.
         * @type String
         */
        hoverClassName: {

            validator: Y.Lang.isString

        },

        /**
         * @attribute hoverClassName
         * @description Class to be added when the mouse is over the host
         * @type String
         */
        hoverClassName: {

            validator: Y.Lang.isString

        },

        /**
         * @attribute paramName
         * @description Name of param that is sent to the server.
         * @type String
         */
        paramName: {

            value: "value",

            validator: Y.Lang.isString

        },

        /**
         * @attribute rows
         * @description Rows attribute of generated field
         * @type String
         */
        rows: {

            value: 1,

            validator: Y.Lang.isNumber

        },

        /**
         * @attribute saveControl
         * @description Type of save control to create. Must be 'button' or 'link'
         * @type String
         */
        saveControl: {

            value: 'button',

            validator: function(value){
                return Y.Lang.isString(value) &&
                    (value === 'button' || value === 'link')
            }

        },

        /**
         * @attribute saveControlText
         * @description Text for save control.
         * @type String
         */
        saveControlText: {

            value: 'Save',

            validator: Y.Lang.isString

        },

        /**
         * @attribute savingClassName
         * @description Class name applied to element whilst saving
         * @type String
         */
        savingClassName: {

            value: 'editor-saving',

            validator: Y.Lang.isString

        },

        /**
         * @attribute savingText
         * @description Text inserted into element whilst saving
         * @type String
         */
        savingText: {
            value: 'saving...',

            validator: Y.Lang.isString
        },

        /**
         * @attribute size
         * @description Size attribute of generated field
         * @type String
         */
        size: {

            value : 0,

            validator: Y.Lang.isNumber

        },

        /**
         * @attribute textAfterControls
         * @description Text after cancel control
         * @type String
         */
        textAfterControls:{

            value : '',

            validator: Y.Lang.isString

        } ,

        /**
         * @attribute textBeforeControls
         * @description Text before the save control
         * @type String
         */
        textBeforeControls: {

            value: '',

            validator: Y.Lang.isString

        },

        /**
         * @attribute textBetweenControls
         * @description Text between Save and Cancel controls
         * @type String
         */
        textBetweenControls: {

            value: '&nbsp;OR&nbsp;',

            validator: Y.Lang.isString

        },

        /**
         * @attribute url
         * @description URL to mkae request via IO
         * @type String
         */
        url: {
            validator: Y.Lang.isString
        },

        ioOptions: {

            value: {},

            validator: Y.Lang.isObject

        }

    }

    // Extend Plugin.Base
    Y.extend(InPlaceEditorPlugin, Y.Plugin.Base,{

        _controls: {},

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

            // Give the host an ID if it doesn't have one already
            if (!host.get('id')) {
                id = Y.stamp(host)
                host.set('id', id)
            }

            // Set the host's title to the clickToEditText attr
            host.set('title', this.get('clickToEditText'));

            // Get the host's original background color
            this._originalBackground = host.getStyle("backgroundColor") ||
                host.getComputedStyle("backgroundColor")

        },

        /**
         * @method createEditField
         * @description Figures out what type of field to create, and then does so.
         */
        createEditField: function(){
            var host = this.get('host'),
                text = host.get('text'),
                size = host.get('region') || host.ancestor().get('region'),
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
                this.form.appendChild(fld);
            } else {
                var fld = new Y.EditorBase({
                    content: text
                });
                //Add the BiDi plugin
                fld.plug(Y.Plugin.EditorBidi);
                fld.render(this._form);
            }
            // Get the node, and set its attrs to fldConfig
            //fld = Y.one(fld).setAttrs(fldConfig);

            // Set its with to match the host's
            //fld.setStyle('width', size + 'px');

            // Set the field to this._editorfield, and add it to the form
            this._editorField = fld;


        },

        /**
         * @method createControl
         * @description Creates editor controls as needed.
         */
        createControl: function(mode, handler) {

            var control = this.get(mode + 'Control'),
                text = this.get(mode + 'ControlText'),
                element;

            if (control === 'button') {
                element = Y.one(document.createElement('input'));
                element.setAttrs({
                    type: 'submit',
                    value: text,
                    className: 'editor_' + mode + '_button'
                });
            } else if (control === 'link') {
                element = Y.one(document.createElement('a'));
                element.setAttrs({
                    href: 'javascript:;',
                    className: 'editor_' + mode + '_link'
                });
                element.setContent(text);
            }

            element.on('click', handler, this);
            this._form.append(element);
            this._controls[mode] = element;

        },

        /**
         * @method createForm
         * @description Creates the editors form.
         */
        createForm: function(){
            // Create a form.
            this._form = Y.one(document.createElement('form'));

            // Give it an ID, and add any classes specified
            this._form.set('id', this.get('formId') || Y.stamp(this._form));
            this._form.addClass(this.get('formClassName'));

            // Add the content field
            this.createEditField();

            // Add a linebreak
            this._form.append("<br />");

            // Add the controls
            this._form.append(this.get('textBeforeControls'));
            this.createControl('save', this.submitFormHandler);
            this._form.append(this.get('textBetweenControls'));
            this.createControl('cancel', this.handleFormCancellation, 'editor_cancel');

            // Fire the onFormCustomization callback
            this._fireCallback('onFormCustomization');
            // Add submit handler
            this._form.on('submit', this.submitFormHandler);

        },

        /**
         * @method enterEditMode
         * @description Hides the host element, creates the form, and inserts it
         * before the host
         */
        enterEditMode: function(){

            var host = this.get('host');

            if (this._saving || this._editing){
                return;
            }

            this._editing = true;
            host.setStyle('display', 'none')
            this.createForm();
            host.insert(this._form, 'before');
            this._fireCallback('afterEnterEdit');
        },


        /**
         * @method handleFormCancellation
         * @description handles form cancellation
         */
        handleFormCancellation: function(event){
            event.halt();
            this.wrapUp();
        },

        /**
         * @method removeForm
         * @description Removes the form from the DOM
         */
        removeForm: function() {
            if (this._form) {
                this._form.remove();
                this._form = null;
                this._controls = {};
            }
        },

        /**
         * @method leaveEditMode
         * @description exits editing mode
         */
        leaveEditMode: function(){
            var host = this.get('host');
            host.removeClass(this.get('savingClassName'));
            this._fireCallback('mouseout');
            this.removeForm();
            host.setStyle('display', '');

            this._saving = false;
            this._editing = false;
        },

        /**
         * @method showSaving
         * @description Shows the host with its content set to the ipe's savingText
         */
        showSaving: function(){
            var host = this.get('host');
            this._oldInnerHTML = host.get('innerHTML');
            host.setContent(this.get('savingText'))
                .addClass(this.get('savingClassName'))
                .setStyle("display", "");
        },

        /**
         * @private
         * @method _onStart
         * @description Default start callback
         */
        _onStart: function(){
            this._saving = true;
            this.removeForm();
            this._fireCallback('mouseout');
            this.showSaving();
            this._fireCallback('onStart');
        },

        /**
         * @private
         * @method _onSuccess
         * @description Default success callback
         */
        _onSuccess: function(ioId, response){
            var text = response.responseText.replace('<', '&lt;').replace('>', '&gt;');
            this.get('host').setContent(text);
            this.wrapUp();
            this._fireCallback('onSuccess');
        },

        /**
         * @private
         * @method _onFailure
         * @description Default failure callback
         */
        _onFailure: function(ioId, response){
            if (this._oldInnerHTML) {
                this.get('host').setContent(this._oldInnerHTML);
                this._oldInnerHTML = null;
                this._errorMessage = response.responseText;
                this.wrapUp();
                this._fireCallback('onFailure');
            }
        },

        /**
         * @method _onFailure
         * @description Makes the request via IO
         */
        submitFormHandler: function(event){
            event.halt();
            var cfg = {
                context: this,
                form: {id: this._form},
                on: {
                    start: this._onStart,
                    success: this._onSuccess,
                    failure: this._onFailure
                },
                method: "POST"
            };
            cfg = Y.merge(cfg, this.get('ioOptions'));

            Y.io(this.get('url'), cfg);
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

            this.doAfter("mouseenter", function(){
                this.get('host').addClass(this.get('hoverClassName'));
                this.get('callbacks').mouseover(this);
            });

            this.doAfter("mouseleave", function(){
                this.get('host').removeClass(this.get('hoverClassName'));
                this.get('callbacks').mouseout(this);
            });

        }

    })

    // Get the Y.Plugin Namespace
    Y.namespace("Plugin");

    // Set Y.Plugin.InPlaceEditor to the InPlaceEditorPlugin
    Y.Plugin.InPlaceEditor = InPlaceEditorPlugin;

}, '0.0.1' ,{requires:['plugin', 'io-base', 'io-form', 'event-mouseenter', 'editor'], skinnable:false})
