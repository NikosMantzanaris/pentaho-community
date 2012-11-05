/* automatically generated by JSCoverage - do not edit */
try {
  if (typeof top === 'object' && top !== null && typeof top.opener === 'object' && top.opener !== null) {
    // this is a browser window that was opened from another window

    if (! top.opener._$jscoverage) {
      top.opener._$jscoverage = {};
    }
  }
}
catch (e) {}

try {
  if (typeof top === 'object' && top !== null) {
    // this is a browser window

    try {
      if (typeof top.opener === 'object' && top.opener !== null && top.opener._$jscoverage) {
        top._$jscoverage = top.opener._$jscoverage;
      }
    }
    catch (e) {}

    if (! top._$jscoverage) {
      top._$jscoverage = {};
    }
  }
}
catch (e) {}

try {
  if (typeof top === 'object' && top !== null && top._$jscoverage) {
    _$jscoverage = top._$jscoverage;
  }
}
catch (e) {}
if (typeof _$jscoverage !== 'object') {
  _$jscoverage = {};
}
if (! _$jscoverage['dojo/pentaho/common/Select.js']) {
  _$jscoverage['dojo/pentaho/common/Select.js'] = [];
  _$jscoverage['dojo/pentaho/common/Select.js'][1] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][3] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][4] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][5] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][6] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][8] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][15] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][16] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][17] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][18] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][19] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][21] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][22] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][23] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][24] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][25] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][26] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][27] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][34] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][36] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][49] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][50] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][51] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][55] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][62] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][71] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][72] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][73] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][79] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][81] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][82] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][83] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][86] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][87] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][89] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][96] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][98] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][101] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][102] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][108] = 0;
  _$jscoverage['dojo/pentaho/common/Select.js'][109] = 0;
}
_$jscoverage['dojo/pentaho/common/Select.js'].source = ["dojo<span class=\"k\">.</span>provide<span class=\"k\">(</span><span class=\"s\">\"pentaho.common.Select\"</span><span class=\"k\">);</span>","","dojo<span class=\"k\">.</span>require<span class=\"k\">(</span><span class=\"s\">\"dijit.form.Select\"</span><span class=\"k\">);</span>","dojo<span class=\"k\">.</span>require<span class=\"k\">(</span><span class=\"s\">\"pentaho.common.Menu\"</span><span class=\"k\">);</span>","dojo<span class=\"k\">.</span>require<span class=\"k\">(</span><span class=\"s\">\"pentaho.common.ListItem\"</span><span class=\"k\">);</span>","dojo<span class=\"k\">.</span>require<span class=\"k\">(</span><span class=\"s\">\"pentaho.common.MenuSeparator\"</span><span class=\"k\">);</span>","","dojo<span class=\"k\">.</span>declare<span class=\"k\">(</span><span class=\"s\">\"pentaho.common._SelectMenu\"</span><span class=\"k\">,</span> pentaho<span class=\"k\">.</span>common<span class=\"k\">.</span>Menu<span class=\"k\">,</span> <span class=\"k\">{</span>","\t<span class=\"c\">// summary:</span>","\t<span class=\"c\">//\t\tAn internally-used menu for dropdown that allows us a vertical scrollbar</span>","\tbuildRendering<span class=\"k\">:</span> <span class=\"k\">function</span><span class=\"k\">()</span><span class=\"k\">{</span>","\t\t<span class=\"c\">// summary:</span>","\t\t<span class=\"c\">//\t\tStub in our own changes, so that our domNode is not a table</span>","\t\t<span class=\"c\">//\t\totherwise, we won't respond correctly to heights/overflows</span>","\t\t<span class=\"k\">this</span><span class=\"k\">.</span>inherited<span class=\"k\">(</span>arguments<span class=\"k\">);</span>","\t\t<span class=\"k\">var</span> o <span class=\"k\">=</span> <span class=\"k\">(</span><span class=\"k\">this</span><span class=\"k\">.</span>menuTableNode <span class=\"k\">=</span> <span class=\"k\">this</span><span class=\"k\">.</span>domNode<span class=\"k\">);</span>","\t\t<span class=\"k\">var</span> n <span class=\"k\">=</span> <span class=\"k\">(</span><span class=\"k\">this</span><span class=\"k\">.</span>domNode <span class=\"k\">=</span> dojo<span class=\"k\">.</span>create<span class=\"k\">(</span><span class=\"s\">\"div\"</span><span class=\"k\">,</span> <span class=\"k\">{</span>style<span class=\"k\">:</span> <span class=\"k\">{</span>overflowX<span class=\"k\">:</span> <span class=\"s\">\"hidden\"</span><span class=\"k\">,</span> overflowY<span class=\"k\">:</span> <span class=\"s\">\"scroll\"</span><span class=\"k\">}}</span><span class=\"k\">));</span>","\t\t<span class=\"k\">if</span><span class=\"k\">(</span>o<span class=\"k\">.</span>parentNode<span class=\"k\">)</span><span class=\"k\">{</span>","\t\t\to<span class=\"k\">.</span>parentNode<span class=\"k\">.</span>replaceChild<span class=\"k\">(</span>n<span class=\"k\">,</span> o<span class=\"k\">);</span>","\t\t<span class=\"k\">}</span>","\t\tdojo<span class=\"k\">.</span>removeClass<span class=\"k\">(</span>o<span class=\"k\">,</span> <span class=\"s\">\"dijitMenuTable\"</span><span class=\"k\">);</span>","\t\tn<span class=\"k\">.</span>className <span class=\"k\">=</span> o<span class=\"k\">.</span>className <span class=\"k\">+</span> <span class=\"s\">\" dijitSelectMenu\"</span><span class=\"k\">;</span>","\t\tn<span class=\"k\">.</span>className <span class=\"k\">=</span> <span class=\"s\">\"pentaho-listbox\"</span><span class=\"k\">;</span>","\t\to<span class=\"k\">.</span>className <span class=\"k\">=</span> <span class=\"s\">\"dijitReset dijitMenuTable\"</span><span class=\"k\">;</span>","\t\tdijit<span class=\"k\">.</span>setWaiRole<span class=\"k\">(</span>o<span class=\"k\">,</span><span class=\"s\">\"listbox\"</span><span class=\"k\">);</span>","\t\tdijit<span class=\"k\">.</span>setWaiRole<span class=\"k\">(</span>n<span class=\"k\">,</span><span class=\"s\">\"presentation\"</span><span class=\"k\">);</span>","\t\tn<span class=\"k\">.</span>appendChild<span class=\"k\">(</span>o<span class=\"k\">);</span>","\t<span class=\"k\">}</span><span class=\"k\">,</span>","","\tpostCreate<span class=\"k\">:</span> <span class=\"k\">function</span><span class=\"k\">()</span><span class=\"k\">{</span>","\t\t<span class=\"c\">// summary:</span>","\t\t<span class=\"c\">//              stop mousemove from selecting text on IE to be consistent with other browsers</span>","","\t\t<span class=\"k\">this</span><span class=\"k\">.</span>inherited<span class=\"k\">(</span>arguments<span class=\"k\">);</span>","","\t\t<span class=\"k\">this</span><span class=\"k\">.</span>connect<span class=\"k\">(</span><span class=\"k\">this</span><span class=\"k\">.</span>domNode<span class=\"k\">,</span> <span class=\"s\">\"onmousemove\"</span><span class=\"k\">,</span> dojo<span class=\"k\">.</span>stopEvent<span class=\"k\">);</span>","        ","\t<span class=\"k\">}</span><span class=\"k\">,</span>","","\tresize<span class=\"k\">:</span> <span class=\"k\">function</span><span class=\"k\">(</span><span class=\"c\">/*Object*/</span> mb<span class=\"k\">)</span><span class=\"k\">{</span>","\t\t<span class=\"c\">// summary:</span>","\t\t<span class=\"c\">//\t\tOverridden so that we are able to handle resizing our</span>","\t\t<span class=\"c\">//\t\tinternal widget.  Note that this is not a \"full\" resize</span>","\t\t<span class=\"c\">//\t\timplementation - it only works correctly if you pass it a</span>","\t\t<span class=\"c\">//\t\tmarginBox.</span>","\t\t<span class=\"c\">//</span>","\t\t<span class=\"c\">// mb: Object</span>","\t\t<span class=\"c\">//\t\tThe margin box to set this dropdown to.</span>","\t\t<span class=\"k\">if</span><span class=\"k\">(</span>mb<span class=\"k\">)</span><span class=\"k\">{</span>","\t\t\tdojo<span class=\"k\">.</span>marginBox<span class=\"k\">(</span><span class=\"k\">this</span><span class=\"k\">.</span>domNode<span class=\"k\">,</span> mb<span class=\"k\">);</span>","\t\t\t<span class=\"k\">if</span><span class=\"k\">(</span><span class=\"s\">\"w\"</span> <span class=\"k\">in</span> mb<span class=\"k\">)</span><span class=\"k\">{</span>","\t\t\t\t<span class=\"c\">// We've explicitly set the wrapper &lt;div&gt;'s width, so set &lt;table&gt; width to match.</span>","\t\t\t\t<span class=\"c\">// 100% is safer than a pixel value because there may be a scroll bar with</span>","\t\t\t\t<span class=\"c\">// browser/OS specific width.</span>","\t\t\t\t<span class=\"k\">this</span><span class=\"k\">.</span>menuTableNode<span class=\"k\">.</span>style<span class=\"k\">.</span>width <span class=\"k\">=</span> <span class=\"s\">\"100%\"</span><span class=\"k\">;</span>","\t\t\t<span class=\"k\">}</span>","\t\t<span class=\"k\">}</span>","\t<span class=\"k\">}</span>","<span class=\"k\">}</span><span class=\"k\">);</span>","","","dojo<span class=\"k\">.</span>declare<span class=\"k\">(</span><span class=\"s\">\"pentaho.common.Select\"</span><span class=\"k\">,</span>","\tdijit<span class=\"k\">.</span>form<span class=\"k\">.</span>Select<span class=\"k\">,</span>","\t<span class=\"k\">{</span>","","        templateString<span class=\"k\">:</span> dojo<span class=\"k\">.</span>cache<span class=\"k\">(</span><span class=\"s\">\"pentaho.common\"</span><span class=\"k\">,</span> <span class=\"s\">\"Select.html\"</span><span class=\"k\">),</span>","","        _setDisplay<span class=\"k\">:</span> <span class=\"k\">function</span><span class=\"k\">(</span><span class=\"c\">/*String*/</span> newDisplay<span class=\"k\">)</span><span class=\"k\">{</span>","            <span class=\"c\">// summary:</span>","            <span class=\"c\">//\t\tsets the display for the given value (or values)</span>","            <span class=\"k\">var</span> lbl <span class=\"k\">=</span> newDisplay <span class=\"k\">||</span> <span class=\"k\">this</span><span class=\"k\">.</span>emptyLabel<span class=\"k\">;</span>","            <span class=\"k\">this</span><span class=\"k\">.</span>containerNode<span class=\"k\">.</span>innerHTML <span class=\"k\">=</span> <span class=\"s\">'&lt;span class=\"dijitReset dijitInline label\"&gt;'</span> <span class=\"k\">+</span> lbl <span class=\"k\">+</span> <span class=\"s\">'&lt;/span&gt;'</span><span class=\"k\">;</span>","            dijit<span class=\"k\">.</span>setWaiState<span class=\"k\">(</span><span class=\"k\">this</span><span class=\"k\">.</span>focusNode<span class=\"k\">,</span> <span class=\"s\">\"valuetext\"</span><span class=\"k\">,</span> lbl<span class=\"k\">);</span>","        <span class=\"k\">}</span><span class=\"k\">,</span>","        ","        _fillContent<span class=\"k\">:</span> <span class=\"k\">function</span><span class=\"k\">()</span><span class=\"k\">{</span>","            <span class=\"c\">// summary:</span>","            <span class=\"c\">//\t\tSet the value to be the first, or the selected index</span>","            <span class=\"k\">this</span><span class=\"k\">.</span>inherited<span class=\"k\">(</span>arguments<span class=\"k\">);</span>","            <span class=\"c\">// set value from selected option</span>","            <span class=\"k\">if</span><span class=\"k\">(</span><span class=\"k\">this</span><span class=\"k\">.</span>options<span class=\"k\">.</span>length <span class=\"k\">&amp;&amp;</span> <span class=\"k\">!</span><span class=\"k\">this</span><span class=\"k\">.</span>value <span class=\"k\">&amp;&amp;</span> <span class=\"k\">this</span><span class=\"k\">.</span>srcNodeRef<span class=\"k\">)</span><span class=\"k\">{</span>","                <span class=\"k\">var</span> si <span class=\"k\">=</span> <span class=\"k\">this</span><span class=\"k\">.</span>srcNodeRef<span class=\"k\">.</span>selectedIndex <span class=\"k\">||</span> <span class=\"s\">0</span><span class=\"k\">;</span> <span class=\"c\">// || 0 needed for when srcNodeRef is not a SELECT</span>","                <span class=\"k\">this</span><span class=\"k\">.</span>value <span class=\"k\">=</span> <span class=\"k\">this</span><span class=\"k\">.</span>options<span class=\"k\">[</span>si <span class=\"k\">&gt;=</span> <span class=\"s\">0</span> <span class=\"k\">?</span> si <span class=\"k\">:</span> <span class=\"s\">0</span><span class=\"k\">].</span>value<span class=\"k\">;</span>","            <span class=\"k\">}</span>","            <span class=\"c\">// Create the dropDown widget</span>","            <span class=\"k\">this</span><span class=\"k\">.</span>dropDown<span class=\"k\">.</span>destroy<span class=\"k\">();</span>","            <span class=\"k\">this</span><span class=\"k\">.</span>dropDown <span class=\"k\">=</span> <span class=\"k\">new</span> pentaho<span class=\"k\">.</span>common<span class=\"k\">.</span>_SelectMenu<span class=\"k\">(</span><span class=\"k\">{</span>id<span class=\"k\">:</span> <span class=\"k\">this</span><span class=\"k\">.</span>id <span class=\"k\">+</span> <span class=\"s\">\"_menu\"</span><span class=\"k\">}</span><span class=\"k\">);</span>","<span class=\"c\">//            dojo.addClass(this.dropDown.domNode, this.baseClass + \"Menu\");</span>","            dojo<span class=\"k\">.</span>addClass<span class=\"k\">(</span><span class=\"k\">this</span><span class=\"k\">.</span>dropDown<span class=\"k\">.</span>domNode<span class=\"k\">,</span> <span class=\"s\">\"pentaho-listbox\"</span><span class=\"k\">);</span>","        <span class=\"k\">}</span><span class=\"k\">,</span>","        ","        _getMenuItemForOption<span class=\"k\">:</span> <span class=\"k\">function</span><span class=\"k\">(</span><span class=\"c\">/*dijit.form.__SelectOption*/</span> option<span class=\"k\">)</span><span class=\"k\">{</span>","            <span class=\"c\">// summary:</span>","            <span class=\"c\">//\t\tFor the given option, return the menu item that should be</span>","            <span class=\"c\">//\t\tused to display it.  This can be overridden as needed</span>","            <span class=\"k\">if</span><span class=\"k\">(!</span>option<span class=\"k\">.</span>value <span class=\"k\">&amp;&amp;</span> <span class=\"k\">!</span>option<span class=\"k\">.</span>label<span class=\"k\">)</span><span class=\"k\">{</span>","                <span class=\"c\">// We are a separator (no label set for it)</span>","                <span class=\"k\">return</span> <span class=\"k\">new</span> pentaho<span class=\"k\">.</span>common<span class=\"k\">.</span>MenuSeparator<span class=\"k\">();</span>","            <span class=\"k\">}</span><span class=\"k\">else</span><span class=\"k\">{</span>","                <span class=\"c\">// Just a regular menu option</span>","                <span class=\"k\">var</span> click <span class=\"k\">=</span> dojo<span class=\"k\">.</span>hitch<span class=\"k\">(</span><span class=\"k\">this</span><span class=\"k\">,</span> <span class=\"s\">\"_setValueAttr\"</span><span class=\"k\">,</span> option<span class=\"k\">);</span>","                <span class=\"k\">var</span> item <span class=\"k\">=</span> <span class=\"k\">new</span> pentaho<span class=\"k\">.</span>common<span class=\"k\">.</span>ListItem<span class=\"k\">(</span><span class=\"k\">{</span>","                    option<span class=\"k\">:</span> option<span class=\"k\">,</span>","                    label<span class=\"k\">:</span> option<span class=\"k\">.</span>label <span class=\"k\">||</span> <span class=\"k\">this</span><span class=\"k\">.</span>emptyLabel<span class=\"k\">,</span>","                    onClick<span class=\"k\">:</span> click<span class=\"k\">,</span>","                    disabled<span class=\"k\">:</span> option<span class=\"k\">.</span>disabled <span class=\"k\">||</span> <span class=\"k\">false</span>","                <span class=\"k\">}</span><span class=\"k\">);</span>","                dijit<span class=\"k\">.</span>setWaiRole<span class=\"k\">(</span>item<span class=\"k\">.</span>focusNode<span class=\"k\">,</span> <span class=\"s\">\"listitem\"</span><span class=\"k\">);</span>","                <span class=\"k\">return</span> item<span class=\"k\">;</span>","            <span class=\"k\">}</span>","        <span class=\"k\">}</span>","        ","    <span class=\"k\">}</span>","<span class=\"k\">);</span>","",""];
_$jscoverage['dojo/pentaho/common/Select.js'][1]++;
dojo.provide("pentaho.common.Select");
_$jscoverage['dojo/pentaho/common/Select.js'][3]++;
dojo.require("dijit.form.Select");
_$jscoverage['dojo/pentaho/common/Select.js'][4]++;
dojo.require("pentaho.common.Menu");
_$jscoverage['dojo/pentaho/common/Select.js'][5]++;
dojo.require("pentaho.common.ListItem");
_$jscoverage['dojo/pentaho/common/Select.js'][6]++;
dojo.require("pentaho.common.MenuSeparator");
_$jscoverage['dojo/pentaho/common/Select.js'][8]++;
dojo.declare("pentaho.common._SelectMenu", pentaho.common.Menu, {buildRendering: (function () {
  _$jscoverage['dojo/pentaho/common/Select.js'][15]++;
  this.inherited(arguments);
  _$jscoverage['dojo/pentaho/common/Select.js'][16]++;
  var o = (this.menuTableNode = this.domNode);
  _$jscoverage['dojo/pentaho/common/Select.js'][17]++;
  var n = (this.domNode = dojo.create("div", {style: {overflowX: "hidden", overflowY: "scroll"}}));
  _$jscoverage['dojo/pentaho/common/Select.js'][18]++;
  if (o.parentNode) {
    _$jscoverage['dojo/pentaho/common/Select.js'][19]++;
    o.parentNode.replaceChild(n, o);
  }
  _$jscoverage['dojo/pentaho/common/Select.js'][21]++;
  dojo.removeClass(o, "dijitMenuTable");
  _$jscoverage['dojo/pentaho/common/Select.js'][22]++;
  n.className = (o.className + " dijitSelectMenu");
  _$jscoverage['dojo/pentaho/common/Select.js'][23]++;
  n.className = "pentaho-listbox";
  _$jscoverage['dojo/pentaho/common/Select.js'][24]++;
  o.className = "dijitReset dijitMenuTable";
  _$jscoverage['dojo/pentaho/common/Select.js'][25]++;
  dijit.setWaiRole(o, "listbox");
  _$jscoverage['dojo/pentaho/common/Select.js'][26]++;
  dijit.setWaiRole(n, "presentation");
  _$jscoverage['dojo/pentaho/common/Select.js'][27]++;
  n.appendChild(o);
}), postCreate: (function () {
  _$jscoverage['dojo/pentaho/common/Select.js'][34]++;
  this.inherited(arguments);
  _$jscoverage['dojo/pentaho/common/Select.js'][36]++;
  this.connect(this.domNode, "onmousemove", dojo.stopEvent);
}), resize: (function (mb) {
  _$jscoverage['dojo/pentaho/common/Select.js'][49]++;
  if (mb) {
    _$jscoverage['dojo/pentaho/common/Select.js'][50]++;
    dojo.marginBox(this.domNode, mb);
    _$jscoverage['dojo/pentaho/common/Select.js'][51]++;
    if (("w" in mb)) {
      _$jscoverage['dojo/pentaho/common/Select.js'][55]++;
      this.menuTableNode.style.width = "100%";
    }
  }
})});
_$jscoverage['dojo/pentaho/common/Select.js'][62]++;
dojo.declare("pentaho.common.Select", dijit.form.Select, {templateString: dojo.cache("pentaho.common", "Select.html"), _setDisplay: (function (newDisplay) {
  _$jscoverage['dojo/pentaho/common/Select.js'][71]++;
  var lbl = (newDisplay || this.emptyLabel);
  _$jscoverage['dojo/pentaho/common/Select.js'][72]++;
  this.containerNode.innerHTML = ("<span class=\"dijitReset dijitInline label\">" + lbl + "</span>");
  _$jscoverage['dojo/pentaho/common/Select.js'][73]++;
  dijit.setWaiState(this.focusNode, "valuetext", lbl);
}), _fillContent: (function () {
  _$jscoverage['dojo/pentaho/common/Select.js'][79]++;
  this.inherited(arguments);
  _$jscoverage['dojo/pentaho/common/Select.js'][81]++;
  if ((this.options.length && (! this.value) && this.srcNodeRef)) {
    _$jscoverage['dojo/pentaho/common/Select.js'][82]++;
    var si = (this.srcNodeRef.selectedIndex || 0);
    _$jscoverage['dojo/pentaho/common/Select.js'][83]++;
    this.value = this.options[((si >= 0)? si: 0)].value;
  }
  _$jscoverage['dojo/pentaho/common/Select.js'][86]++;
  this.dropDown.destroy();
  _$jscoverage['dojo/pentaho/common/Select.js'][87]++;
  this.dropDown = new (pentaho.common._SelectMenu)({id: (this.id + "_menu")});
  _$jscoverage['dojo/pentaho/common/Select.js'][89]++;
  dojo.addClass(this.dropDown.domNode, "pentaho-listbox");
}), _getMenuItemForOption: (function (option) {
  _$jscoverage['dojo/pentaho/common/Select.js'][96]++;
  if (((! option.value) && (! option.label))) {
    _$jscoverage['dojo/pentaho/common/Select.js'][98]++;
    return new (pentaho.common.MenuSeparator)();
  }
  else {
    _$jscoverage['dojo/pentaho/common/Select.js'][101]++;
    var click = dojo.hitch(this, "_setValueAttr", option);
    _$jscoverage['dojo/pentaho/common/Select.js'][102]++;
    var item = new (pentaho.common.ListItem)({option: option, label: (option.label || this.emptyLabel), onClick: click, disabled: (option.disabled || false)});
    _$jscoverage['dojo/pentaho/common/Select.js'][108]++;
    dijit.setWaiRole(item.focusNode, "listitem");
    _$jscoverage['dojo/pentaho/common/Select.js'][109]++;
    return item;
  }
})});