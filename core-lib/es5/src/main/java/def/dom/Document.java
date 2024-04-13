package def.dom;
@jsweet.lang.Extends({GlobalEventHandlers.class,NodeSelector.class,DocumentEvent.class})
public class Document extends Node {
    /**
      * Sets or gets the URL for the current document. 
      */
    public String URL;
    /**
      * Gets the URL for the document, stripped of any character encoding.
      */
    public String URLUnencoded;
    /**
      * Gets the object that has the focus when the parent document has focus.
      */
    public Element activeElement;
    /**
      * Sets or gets the color of all active links in the document.
      */
    public String alinkColor;
    /**
      * Returns a reference to the collection of elements contained by the object.
      */
    public HTMLCollection all;
    /**
      * Retrieves a collection of all a objects that have a name and/or id property. Objects in this collection are in HTML source order.
      */
    public HTMLCollection anchors;
    /**
      * Retrieves a collection of all applet objects in the document.
      */
    public HTMLCollection applets;
    /**
      * Deprecated. Sets or retrieves a value that indicates the background color behind the object. 
      */
    public String bgColor;
    /**
      * Specifies the beginning and end of the document body.
      */
    public HTMLElement body;
    public String characterSet;
    /**
      * Gets or sets the character set used to encode the object.
      */
    public String charset;
    /**
      * Gets a value that indicates whether standards-compliant mode is switched on for the object.
      */
    public String compatMode;
    public String cookie;
    /**
      * Gets the default character set from the current regional language settings.
      */
    public String defaultCharset;
    public Window defaultView;
    /**
      * Sets or gets a value that indicates whether the document can be edited.
      */
    public String designMode;
    /**
      * Sets or retrieves a value that indicates the reading order of the object. 
      */
    public String dir;
    /**
      * Gets an object representing the document type declaration associated with the current document. 
      */
    public DocumentType doctype;
    /**
      * Gets a reference to the root node of the document. 
      */
    public HTMLElement documentElement;
    /**
      * Sets or gets the security domain of the document. 
      */
    public String domain;
    /**
      * Retrieves a collection of all embed objects in the document.
      */
    public HTMLCollection embeds;
    /**
      * Sets or gets the foreground (text) color of the document.
      */
    public String fgColor;
    /**
      * Retrieves a collection, in source order, of all form objects in the document.
      */
    public HTMLCollection forms;
    public Element fullscreenElement;
    public Boolean fullscreenEnabled;
    public HTMLHeadElement head;
    public Boolean hidden;
    /**
      * Retrieves a collection, in source order, of img objects in the document.
      */
    public HTMLCollection images;
    /**
      * Gets the implementation object of the current document. 
      */
    public DOMImplementation implementation;
    /**
      * Returns the character encoding used to create the webpage that is loaded into the document object.
      */
    public String inputEncoding;
    /**
      * Gets the date that the page was last modified, if the page supplies one. 
      */
    public String lastModified;
    /**
      * Sets or gets the color of the document links. 
      */
    public String linkColor;
    /**
      * Retrieves a collection of all a objects that specify the href property and all area objects in the document.
      */
    public HTMLCollection links;
    /**
      * Contains information about the current URL. 
      */
    public Location location;
    public String media;
    public Boolean msCSSOMElementFloatMetrics;
    public Boolean msCapsLockWarningOff;
    public Boolean msHidden;
    public String msVisibilityState;
    /**
      * Fires when the user aborts the download.
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onabort;
    /**
      * Fires when the object is set as the active element.
      * @param ev The event.
      */
    public java.util.function.Function<UIEvent,Object> onactivate;
    /**
      * Fires immediately before the object is set as the active element.
      * @param ev The event.
      */
    public java.util.function.Function<UIEvent,Object> onbeforeactivate;
    /**
      * Fires immediately before the activeElement is changed from the current object to another object in the parent document.
      * @param ev The event.
      */
    public java.util.function.Function<UIEvent,Object> onbeforedeactivate;
    /** 
      * Fires when the object loses the input focus. 
      * @param ev The focus event.
      */
    public java.util.function.Function<FocusEvent,Object> onblur;
    /**
      * Occurs when playback is possible, but would require further buffering. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> oncanplay;
    public java.util.function.Function<Event,Object> oncanplaythrough;
    /**
      * Fires when the contents of the object or selection have changed. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onchange;
    /**
      * Fires when the user clicks the left mouse button on the object
      * @param ev The mouse event.
      */
    public java.util.function.Function<MouseEvent,Object> onclick;
    /**
      * Fires when the user clicks the right mouse button in the client area, opening the context menu. 
      * @param ev The mouse event.
      */
    public java.util.function.Function<PointerEvent,Object> oncontextmenu;
    /**
      * Fires when the user double-clicks the object.
      * @param ev The mouse event.
      */
    public java.util.function.Function<MouseEvent,Object> ondblclick;
    /**
      * Fires when the activeElement is changed from the current object to another object in the parent document.
      * @param ev The UI Event
      */
    public java.util.function.Function<UIEvent,Object> ondeactivate;
    /**
      * Fires on the source object continuously during a drag operation.
      * @param ev The event.
      */
    public java.util.function.Function<DragEvent,Object> ondrag;
    /**
      * Fires on the source object when the user releases the mouse at the close of a drag operation.
      * @param ev The event.
      */
    public java.util.function.Function<DragEvent,Object> ondragend;
    /** 
      * Fires on the target element when the user drags the object to a valid drop target.
      * @param ev The drag event.
      */
    public java.util.function.Function<DragEvent,Object> ondragenter;
    /** 
      * Fires on the target object when the user moves the mouse out of a valid drop target during a drag operation.
      * @param ev The drag event.
      */
    public java.util.function.Function<DragEvent,Object> ondragleave;
    /**
      * Fires on the target element continuously while the user drags the object over a valid drop target.
      * @param ev The event.
      */
    public java.util.function.Function<DragEvent,Object> ondragover;
    /**
      * Fires on the source object when the user starts to drag a text selection or selected object. 
      * @param ev The event.
      */
    public java.util.function.Function<DragEvent,Object> ondragstart;
    public java.util.function.Function<DragEvent,Object> ondrop;
    /**
      * Occurs when the duration attribute is updated. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> ondurationchange;
    /**
      * Occurs when the media element is reset to its initial state. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onemptied;
    /**
      * Occurs when the end of playback is reached. 
      * @param ev The event
      */
    public java.util.function.Function<Event,Object> onended;
    /**
      * Fires when an error occurs during object loading.
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onerror;
    /**
      * Fires when the object receives focus. 
      * @param ev The event.
      */
    public java.util.function.Function<FocusEvent,Object> onfocus;
    public java.util.function.Function<Event,Object> onfullscreenchange;
    public java.util.function.Function<Event,Object> onfullscreenerror;
    public java.util.function.Function<Event,Object> oninput;
    /**
      * Fires when the user presses a key.
      * @param ev The keyboard event
      */
    public java.util.function.Function<KeyboardEvent,Object> onkeydown;
    /**
      * Fires when the user presses an alphanumeric key.
      * @param ev The event.
      */
    public java.util.function.Function<KeyboardEvent,Object> onkeypress;
    /**
      * Fires when the user releases a key.
      * @param ev The keyboard event
      */
    public java.util.function.Function<KeyboardEvent,Object> onkeyup;
    /**
      * Fires immediately after the browser loads the object. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onload;
    /**
      * Occurs when media data is loaded at the current playback position. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onloadeddata;
    /**
      * Occurs when the duration and dimensions of the media have been determined.
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onloadedmetadata;
    /**
      * Occurs when Internet Explorer begins looking for media data. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onloadstart;
    /**
      * Fires when the user clicks the object with either mouse button. 
      * @param ev The mouse event.
      */
    public java.util.function.Function<MouseEvent,Object> onmousedown;
    /**
      * Fires when the user moves the mouse over the object. 
      * @param ev The mouse event.
      */
    public java.util.function.Function<MouseEvent,Object> onmousemove;
    /**
      * Fires when the user moves the mouse pointer outside the boundaries of the object. 
      * @param ev The mouse event.
      */
    public java.util.function.Function<MouseEvent,Object> onmouseout;
    /**
      * Fires when the user moves the mouse pointer into the object.
      * @param ev The mouse event.
      */
    public java.util.function.Function<MouseEvent,Object> onmouseover;
    /**
      * Fires when the user releases a mouse button while the mouse is over the object. 
      * @param ev The mouse event.
      */
    public java.util.function.Function<MouseEvent,Object> onmouseup;
    /**
      * Fires when the wheel button is rotated. 
      * @param ev The mouse event
      */
    public java.util.function.Function<MouseWheelEvent,Object> onmousewheel;
    public java.util.function.Function<UIEvent,Object> onmscontentzoom;
    public java.util.function.Function<MSGestureEvent,Object> onmsgesturechange;
    public java.util.function.Function<MSGestureEvent,Object> onmsgesturedoubletap;
    public java.util.function.Function<MSGestureEvent,Object> onmsgestureend;
    public java.util.function.Function<MSGestureEvent,Object> onmsgesturehold;
    public java.util.function.Function<MSGestureEvent,Object> onmsgesturestart;
    public java.util.function.Function<MSGestureEvent,Object> onmsgesturetap;
    public java.util.function.Function<MSGestureEvent,Object> onmsinertiastart;
    public java.util.function.Function<MSManipulationEvent,Object> onmsmanipulationstatechanged;
    public java.util.function.Function<MSPointerEvent,Object> onmspointercancel;
    public java.util.function.Function<MSPointerEvent,Object> onmspointerdown;
    public java.util.function.Function<MSPointerEvent,Object> onmspointerenter;
    public java.util.function.Function<MSPointerEvent,Object> onmspointerleave;
    public java.util.function.Function<MSPointerEvent,Object> onmspointermove;
    public java.util.function.Function<MSPointerEvent,Object> onmspointerout;
    public java.util.function.Function<MSPointerEvent,Object> onmspointerover;
    public java.util.function.Function<MSPointerEvent,Object> onmspointerup;
    /**
      * Occurs when an item is removed from a Jump List of a webpage running in Site Mode. 
      * @param ev The event.
      */
    public java.util.function.Function<MSSiteModeEvent,Object> onmssitemodejumplistitemremoved;
    /**
      * Occurs when a user clicks a button in a Thumbnail Toolbar of a webpage running in Site Mode.
      * @param ev The event.
      */
    public java.util.function.Function<MSSiteModeEvent,Object> onmsthumbnailclick;
    /**
      * Occurs when playback is paused.
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onpause;
    /**
      * Occurs when the play method is requested. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onplay;
    /**
      * Occurs when the audio or video has started playing. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onplaying;
    public java.util.function.Function<Event,Object> onpointerlockchange;
    public java.util.function.Function<Event,Object> onpointerlockerror;
    /**
      * Occurs to indicate progress while downloading media data. 
      * @param ev The event.
      */
    public java.util.function.Function<ProgressEvent,Object> onprogress;
    /**
      * Occurs when the playback rate is increased or decreased. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onratechange;
    /**
      * Fires when the state of the object has changed.
      * @param ev The event
      */
    public java.util.function.Function<ProgressEvent,Object> onreadystatechange;
    /**
      * Fires when the user resets a form. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onreset;
    /**
      * Fires when the user repositions the scroll box in the scroll bar on the object. 
      * @param ev The event.
      */
    public java.util.function.Function<UIEvent,Object> onscroll;
    /**
      * Occurs when the seek operation ends. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onseeked;
    /**
      * Occurs when the current playback position is moved. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onseeking;
    /**
      * Fires when the current selection changes.
      * @param ev The event.
      */
    public java.util.function.Function<UIEvent,Object> onselect;
    public java.util.function.Function<Event,Object> onselectstart;
    /**
      * Occurs when the download has stopped. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onstalled;
    /**
      * Fires when the user clicks the Stop button or leaves the Web page.
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onstop;
    public java.util.function.Function<Event,Object> onsubmit;
    /**
      * Occurs if the load operation has been intentionally halted. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onsuspend;
    /**
      * Occurs to indicate the current playback position.
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> ontimeupdate;
    public java.util.function.Function<TouchEvent,Object> ontouchcancel;
    public java.util.function.Function<TouchEvent,Object> ontouchend;
    public java.util.function.Function<TouchEvent,Object> ontouchmove;
    public java.util.function.Function<TouchEvent,Object> ontouchstart;
    /**
      * Occurs when the volume is changed, or playback is muted or unmuted.
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onvolumechange;
    /**
      * Occurs when playback stops because the next frame of a video resource is not available. 
      * @param ev The event.
      */
    public java.util.function.Function<Event,Object> onwaiting;
    public java.util.function.Function<Event,Object> onwebkitfullscreenchange;
    public java.util.function.Function<Event,Object> onwebkitfullscreenerror;
    public HTMLCollection plugins;
    public Element pointerLockElement;
    /**
      * Retrieves a value that indicates the current state of the object.
      */
    public String readyState;
    /**
      * Gets the URL of the location that referred the user to the current page.
      */
    public String referrer;
    /**
      * Gets the root svg element in the document hierarchy.
      */
    public SVGSVGElement rootElement;
    /**
      * Retrieves a collection of all script objects in the document.
      */
    public HTMLCollection scripts;
    public String security;
    /**
      * Retrieves a collection of styleSheet objects representing the style sheets that correspond to each instance of a link or style object in the document.
      */
    public StyleSheetList styleSheets;
    /**
      * Contains the title of the document.
      */
    public String title;
    public String visibilityState;
    /** 
      * Sets or gets the color of the links that the user has visited.
      */
    public String vlinkColor;
    public Element webkitCurrentFullScreenElement;
    public Element webkitFullscreenElement;
    public Boolean webkitFullscreenEnabled;
    public Boolean webkitIsFullScreen;
    public String xmlEncoding;
    public Boolean xmlStandalone;
    /**
      * Gets or sets the version attribute specified in the declaration of an XML document.
      */
    public String xmlVersion;
    public HTMLScriptElement currentScript;
    native public Node adoptNode(Node source);
    native public void captureEvents();
    native public void clear();
    /**
      * Closes an output stream and forces the sent data to display.
      */
    native public void close();
    /**
      * Creates an attribute object with a specified name.
      * @param name String that sets the attribute object's name.
      */
    native public Attr createAttribute(String name);
    native public Attr createAttributeNS(String namespaceURI, String qualifiedName);
    native public CDATASection createCDATASection(String data);
    /**
      * Creates a comment object with the specified data.
      * @param data Sets the comment object's data.
      */
    native public Comment createComment(String data);
    /**
      * Creates a new document.
      */
    native public DocumentFragment createDocumentFragment();
    /**
      * Creates an instance of the element for the specified tag.
      * @param tagName The name of an element.
      */
    native public HTMLAnchorElement createElement(jsweet.util.StringTypes.a tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.abbr tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.acronym tagName);
    native public HTMLBlockElement createElement(jsweet.util.StringTypes.address tagName);
    native public HTMLAppletElement createElement(jsweet.util.StringTypes.applet tagName);
    native public HTMLAreaElement createElement(jsweet.util.StringTypes.area tagName);
    native public HTMLAudioElement createElement(jsweet.util.StringTypes.audio tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.b tagName);
    native public HTMLBaseElement createElement(jsweet.util.StringTypes.base tagName);
    native public HTMLBaseFontElement createElement(jsweet.util.StringTypes.basefont tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.bdo tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.big tagName);
    native public HTMLBlockElement createElement(jsweet.util.StringTypes.blockquote tagName);
    native public HTMLBodyElement createElement(jsweet.util.StringTypes.body tagName);
    native public HTMLBRElement createElement(jsweet.util.StringTypes.br tagName);
    native public HTMLButtonElement createElement(jsweet.util.StringTypes.button tagName);
    native public HTMLCanvasElement createElement(jsweet.util.StringTypes.canvas tagName);
    native public HTMLTableCaptionElement createElement(jsweet.util.StringTypes.caption tagName);
    native public HTMLBlockElement createElement(jsweet.util.StringTypes.center tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.cite tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.code tagName);
    native public HTMLTableColElement createElement(jsweet.util.StringTypes.col tagName);
    native public HTMLTableColElement createElement(jsweet.util.StringTypes.colgroup tagName);
    native public HTMLDataListElement createElement(jsweet.util.StringTypes.datalist tagName);
    native public HTMLDDElement createElement(jsweet.util.StringTypes.dd tagName);
    native public HTMLModElement createElement(jsweet.util.StringTypes.del tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.dfn tagName);
    native public HTMLDirectoryElement createElement(jsweet.util.StringTypes.dir tagName);
    native public HTMLDivElement createElement(jsweet.util.StringTypes.div tagName);
    native public HTMLDListElement createElement(jsweet.util.StringTypes.dl tagName);
    native public HTMLDTElement createElement(jsweet.util.StringTypes.dt tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.em tagName);
    native public HTMLEmbedElement createElement(jsweet.util.StringTypes.embed tagName);
    native public HTMLFieldSetElement createElement(jsweet.util.StringTypes.fieldset tagName);
    native public HTMLFontElement createElement(jsweet.util.StringTypes.font tagName);
    native public HTMLFormElement createElement(jsweet.util.StringTypes.form tagName);
    native public HTMLFrameElement createElement(jsweet.util.StringTypes.frame tagName);
    native public HTMLFrameSetElement createElement(jsweet.util.StringTypes.frameset tagName);
    native public HTMLHeadingElement createElement(jsweet.util.StringTypes.h1 tagName);
    native public HTMLHeadingElement createElement(jsweet.util.StringTypes.h2 tagName);
    native public HTMLHeadingElement createElement(jsweet.util.StringTypes.h3 tagName);
    native public HTMLHeadingElement createElement(jsweet.util.StringTypes.h4 tagName);
    native public HTMLHeadingElement createElement(jsweet.util.StringTypes.h5 tagName);
    native public HTMLHeadingElement createElement(jsweet.util.StringTypes.h6 tagName);
    native public HTMLHeadElement createElement(jsweet.util.StringTypes.head tagName);
    native public HTMLHRElement createElement(jsweet.util.StringTypes.hr tagName);
    native public HTMLHtmlElement createElement(jsweet.util.StringTypes.html tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.i tagName);
    native public HTMLIFrameElement createElement(jsweet.util.StringTypes.iframe tagName);
    native public HTMLImageElement createElement(jsweet.util.StringTypes.img tagName);
    native public HTMLInputElement createElement(jsweet.util.StringTypes.input tagName);
    native public HTMLModElement createElement(jsweet.util.StringTypes.ins tagName);
    native public HTMLIsIndexElement createElement(jsweet.util.StringTypes.isindex tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.kbd tagName);
    native public HTMLBlockElement createElement(jsweet.util.StringTypes.keygen tagName);
    native public HTMLLabelElement createElement(jsweet.util.StringTypes.label tagName);
    native public HTMLLegendElement createElement(jsweet.util.StringTypes.legend tagName);
    native public HTMLLIElement createElement(jsweet.util.StringTypes.li tagName);
    native public HTMLLinkElement createElement(jsweet.util.StringTypes.link tagName);
    native public HTMLBlockElement createElement(jsweet.util.StringTypes.listing tagName);
    native public HTMLMapElement createElement(jsweet.util.StringTypes.map tagName);
    native public HTMLMarqueeElement createElement(jsweet.util.StringTypes.marquee tagName);
    native public HTMLMenuElement createElement(jsweet.util.StringTypes.menu tagName);
    native public HTMLMetaElement createElement(jsweet.util.StringTypes.meta tagName);
    native public HTMLNextIdElement createElement(jsweet.util.StringTypes.nextid tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.nobr tagName);
    native public HTMLObjectElement createElement(jsweet.util.StringTypes.object tagName);
    native public HTMLOListElement createElement(jsweet.util.StringTypes.ol tagName);
    native public HTMLOptGroupElement createElement(jsweet.util.StringTypes.optgroup tagName);
    native public HTMLOptionElement createElement(jsweet.util.StringTypes.option tagName);
    native public HTMLParagraphElement createElement(jsweet.util.StringTypes.p tagName);
    native public HTMLParamElement createElement(jsweet.util.StringTypes.param tagName);
    native public HTMLBlockElement createElement(jsweet.util.StringTypes.plaintext tagName);
    native public HTMLPreElement createElement(jsweet.util.StringTypes.pre tagName);
    native public HTMLProgressElement createElement(jsweet.util.StringTypes.progress tagName);
    native public HTMLQuoteElement createElement(jsweet.util.StringTypes.q tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.rt tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.ruby tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.s tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.samp tagName);
    native public HTMLScriptElement createElement(jsweet.util.StringTypes.script tagName);
    native public HTMLSelectElement createElement(jsweet.util.StringTypes.select tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.small tagName);
    native public HTMLSourceElement createElement(jsweet.util.StringTypes.source tagName);
    native public HTMLSpanElement createElement(jsweet.util.StringTypes.span tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.strike tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.strong tagName);
    native public HTMLStyleElement createElement(jsweet.util.StringTypes.style tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.sub tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.sup tagName);
    native public HTMLTableElement createElement(jsweet.util.StringTypes.table tagName);
    native public HTMLTableSectionElement createElement(jsweet.util.StringTypes.tbody tagName);
    native public HTMLTableDataCellElement createElement(jsweet.util.StringTypes.td tagName);
    native public HTMLTextAreaElement createElement(jsweet.util.StringTypes.textarea tagName);
    native public HTMLTableSectionElement createElement(jsweet.util.StringTypes.tfoot tagName);
    native public HTMLTableHeaderCellElement createElement(jsweet.util.StringTypes.th tagName);
    native public HTMLTableSectionElement createElement(jsweet.util.StringTypes.thead tagName);
    native public HTMLTitleElement createElement(jsweet.util.StringTypes.title tagName);
    native public HTMLTableRowElement createElement(jsweet.util.StringTypes.tr tagName);
    native public HTMLTrackElement createElement(jsweet.util.StringTypes.track tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.tt tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.u tagName);
    native public HTMLUListElement createElement(jsweet.util.StringTypes.ul tagName);
    native public HTMLPhraseElement createElement(jsweet.util.StringTypes.Var tagName);
    native public HTMLVideoElement createElement(jsweet.util.StringTypes.video tagName);
    native public MSHTMLWebViewElement createElement(jsweet.util.StringTypes.x_ms_webview tagName);
    native public HTMLBlockElement createElement(jsweet.util.StringTypes.xmp tagName);
    native public HTMLElement createElement(String tagName);
    native public SVGAElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.a qualifiedName);
    native public SVGCircleElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.circle qualifiedName);
    native public SVGClipPathElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.clipPath qualifiedName);
    native public SVGComponentTransferFunctionElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.componentTransferFunction qualifiedName);
    native public SVGDefsElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.defs qualifiedName);
    native public SVGDescElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.desc qualifiedName);
    native public SVGEllipseElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.ellipse qualifiedName);
    native public SVGFEBlendElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feBlend qualifiedName);
    native public SVGFEColorMatrixElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feColorMatrix qualifiedName);
    native public SVGFEComponentTransferElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feComponentTransfer qualifiedName);
    native public SVGFECompositeElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feComposite qualifiedName);
    native public SVGFEConvolveMatrixElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feConvolveMatrix qualifiedName);
    native public SVGFEDiffuseLightingElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feDiffuseLighting qualifiedName);
    native public SVGFEDisplacementMapElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feDisplacementMap qualifiedName);
    native public SVGFEDistantLightElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feDistantLight qualifiedName);
    native public SVGFEFloodElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feFlood qualifiedName);
    native public SVGFEFuncAElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feFuncA qualifiedName);
    native public SVGFEFuncBElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feFuncB qualifiedName);
    native public SVGFEFuncGElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feFuncG qualifiedName);
    native public SVGFEFuncRElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feFuncR qualifiedName);
    native public SVGFEGaussianBlurElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feGaussianBlur qualifiedName);
    native public SVGFEImageElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feImage qualifiedName);
    native public SVGFEMergeElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feMerge qualifiedName);
    native public SVGFEMergeNodeElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feMergeNode qualifiedName);
    native public SVGFEMorphologyElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feMorphology qualifiedName);
    native public SVGFEOffsetElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feOffset qualifiedName);
    native public SVGFEPointLightElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.fePointLight qualifiedName);
    native public SVGFESpecularLightingElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feSpecularLighting qualifiedName);
    native public SVGFESpotLightElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feSpotLight qualifiedName);
    native public SVGFETileElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feTile qualifiedName);
    native public SVGFETurbulenceElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.feTurbulence qualifiedName);
    native public SVGFilterElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.filter qualifiedName);
    native public SVGForeignObjectElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.foreignObject qualifiedName);
    native public SVGGElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.g qualifiedName);
    native public SVGImageElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.image qualifiedName);
    native public SVGGradientElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.gradient qualifiedName);
    native public SVGLineElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.line qualifiedName);
    native public SVGLinearGradientElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.linearGradient qualifiedName);
    native public SVGMarkerElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.marker qualifiedName);
    native public SVGMaskElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.mask qualifiedName);
    native public SVGPathElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.path qualifiedName);
    native public SVGMetadataElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.metadata qualifiedName);
    native public SVGPatternElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.pattern qualifiedName);
    native public SVGPolygonElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.polygon qualifiedName);
    native public SVGPolylineElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.polyline qualifiedName);
    native public SVGRadialGradientElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.radialGradient qualifiedName);
    native public SVGRectElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.rect qualifiedName);
    native public SVGSVGElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.svg qualifiedName);
    native public SVGScriptElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.script qualifiedName);
    native public SVGStopElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.stop qualifiedName);
    native public SVGStyleElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.style qualifiedName);
    native public SVGSwitchElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.Switch qualifiedName);
    native public SVGSymbolElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.symbol qualifiedName);
    native public SVGTSpanElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.tspan qualifiedName);
    native public SVGTextContentElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.textContent qualifiedName);
    native public SVGTextElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.text qualifiedName);
    native public SVGTextPathElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.textPath qualifiedName);
    native public SVGTextPositioningElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.textPositioning qualifiedName);
    native public SVGTitleElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.title qualifiedName);
    native public SVGUseElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.use qualifiedName);
    native public SVGViewElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, jsweet.util.StringTypes.view qualifiedName);
    native public SVGElement createElementNS(jsweet.util.StringTypes.http___www_w3_org_2000_svg namespaceURI, String qualifiedName);
    native public Element createElementNS(String namespaceURI, String qualifiedName);
    native public XPathExpression createExpression(String expression, XPathNSResolver resolver);
    native public XPathNSResolver createNSResolver(Node nodeResolver);
    /**
      * Creates a NodeIterator object that you can use to traverse filtered lists of nodes or elements in a document. 
      * @param root The root element or node to start traversing on.
      * @param whatToShow The type of nodes or elements to appear in the node list
      * @param filter A custom NodeFilter function to use. For more information, see filter. Use null for no filter.
      * @param entityReferenceExpansion A flag that specifies whether entity reference nodes are expanded.
      */
    native public NodeIterator createNodeIterator(Node root, double whatToShow, NodeFilter filter, Boolean entityReferenceExpansion);
    native public ProcessingInstruction createProcessingInstruction(String target, String data);
    /**
      *  Returns an empty range object that has both of its boundary points positioned at the beginning of the document. 
      */
    native public Range createRange();
    /**
      * Creates a text string from the specified value. 
      * @param data String that specifies the nodeValue property of the text node.
      */
    native public Text createTextNode(String data);
    native public Touch createTouch(Object view, EventTarget target, double identifier, double pageX, double pageY, double screenX, double screenY);
    native public TouchList createTouchList(Touch... touches);
    /**
      * Creates a TreeWalker object that you can use to traverse filtered lists of nodes or elements in a document.
      * @param root The root element or node to start traversing on.
      * @param whatToShow The type of nodes or elements to appear in the node list. For more information, see whatToShow.
      * @param filter A custom NodeFilter function to use.
      * @param entityReferenceExpansion A flag that specifies whether entity reference nodes are expanded.
      */
    native public TreeWalker createTreeWalker(Node root, double whatToShow, NodeFilter filter, Boolean entityReferenceExpansion);
    /**
      * Returns the element for the specified x coordinate and the specified y coordinate. 
      * @param x The x-offset
      * @param y The y-offset
      */
    native public Element elementFromPoint(double x, double y);
    native public XPathResult evaluate(String expression, Node contextNode, XPathNSResolver resolver, double type, XPathResult result);
    /**
      * Executes a command on the current document, current selection, or the given range.
      * @param commandId String that specifies the command to execute. This command can be any of the command identifiers that can be executed in script.
      * @param showUI Display the user interface, defaults to false.
      * @param value Value to assign.
      */
    native public Boolean execCommand(String commandId, Boolean showUI, Object value);
    /**
      * Displays help information for the given command identifier.
      * @param commandId Displays help information for the given command identifier.
      */
    native public Boolean execCommandShowHelp(String commandId);
    native public void exitFullscreen();
    native public void exitPointerLock();
    /**
      * Causes the element to receive the focus and executes the code specified by the onfocus event.
      */
    native public void focus();
    /**
      * Returns a reference to the first object with the specified value of the ID or NAME attribute.
      * @param elementId String that specifies the ID value. Case-insensitive.
      */
    native public HTMLElement getElementById(String elementId);
    native public NodeListOf<Element> getElementsByClassName(String classNames);
    /**
      * Gets a collection of objects based on the value of the NAME or ID attribute.
      * @param elementName Gets a collection of objects based on the value of the NAME or ID attribute.
      */
    native public NodeListOf<Element> getElementsByName(String elementName);
    /**
      * Retrieves a collection of objects based on the specified element name.
      * @param name Specifies the name of an element.
      */
    native public NodeListOf<HTMLAnchorElement> getElementsByTagName(jsweet.util.StringTypes.a tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.abbr tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.acronym tagname);
    native public NodeListOf<HTMLBlockElement> getElementsByTagName(jsweet.util.StringTypes.address tagname);
    native public NodeListOf<HTMLAppletElement> getElementsByTagName(jsweet.util.StringTypes.applet tagname);
    native public NodeListOf<HTMLAreaElement> getElementsByTagName(jsweet.util.StringTypes.area tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.article tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.aside tagname);
    native public NodeListOf<HTMLAudioElement> getElementsByTagName(jsweet.util.StringTypes.audio tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.b tagname);
    native public NodeListOf<HTMLBaseElement> getElementsByTagName(jsweet.util.StringTypes.base tagname);
    native public NodeListOf<HTMLBaseFontElement> getElementsByTagName(jsweet.util.StringTypes.basefont tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.bdo tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.big tagname);
    native public NodeListOf<HTMLBlockElement> getElementsByTagName(jsweet.util.StringTypes.blockquote tagname);
    native public NodeListOf<HTMLBodyElement> getElementsByTagName(jsweet.util.StringTypes.body tagname);
    native public NodeListOf<HTMLBRElement> getElementsByTagName(jsweet.util.StringTypes.br tagname);
    native public NodeListOf<HTMLButtonElement> getElementsByTagName(jsweet.util.StringTypes.button tagname);
    native public NodeListOf<HTMLCanvasElement> getElementsByTagName(jsweet.util.StringTypes.canvas tagname);
    native public NodeListOf<HTMLTableCaptionElement> getElementsByTagName(jsweet.util.StringTypes.caption tagname);
    native public NodeListOf<HTMLBlockElement> getElementsByTagName(jsweet.util.StringTypes.center tagname);
    native public NodeListOf<SVGCircleElement> getElementsByTagName(jsweet.util.StringTypes.circle tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.cite tagname);
    native public NodeListOf<SVGClipPathElement> getElementsByTagName(jsweet.util.StringTypes.clipPath tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.code tagname);
    native public NodeListOf<HTMLTableColElement> getElementsByTagName(jsweet.util.StringTypes.col tagname);
    native public NodeListOf<HTMLTableColElement> getElementsByTagName(jsweet.util.StringTypes.colgroup tagname);
    native public NodeListOf<HTMLDataListElement> getElementsByTagName(jsweet.util.StringTypes.datalist tagname);
    native public NodeListOf<HTMLDDElement> getElementsByTagName(jsweet.util.StringTypes.dd tagname);
    native public NodeListOf<SVGDefsElement> getElementsByTagName(jsweet.util.StringTypes.defs tagname);
    native public NodeListOf<HTMLModElement> getElementsByTagName(jsweet.util.StringTypes.del tagname);
    native public NodeListOf<SVGDescElement> getElementsByTagName(jsweet.util.StringTypes.desc tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.dfn tagname);
    native public NodeListOf<HTMLDirectoryElement> getElementsByTagName(jsweet.util.StringTypes.dir tagname);
    native public NodeListOf<HTMLDivElement> getElementsByTagName(jsweet.util.StringTypes.div tagname);
    native public NodeListOf<HTMLDListElement> getElementsByTagName(jsweet.util.StringTypes.dl tagname);
    native public NodeListOf<HTMLDTElement> getElementsByTagName(jsweet.util.StringTypes.dt tagname);
    native public NodeListOf<SVGEllipseElement> getElementsByTagName(jsweet.util.StringTypes.ellipse tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.em tagname);
    native public NodeListOf<HTMLEmbedElement> getElementsByTagName(jsweet.util.StringTypes.embed tagname);
    native public NodeListOf<SVGFEBlendElement> getElementsByTagName(jsweet.util.StringTypes.feBlend tagname);
    native public NodeListOf<SVGFEColorMatrixElement> getElementsByTagName(jsweet.util.StringTypes.feColorMatrix tagname);
    native public NodeListOf<SVGFEComponentTransferElement> getElementsByTagName(jsweet.util.StringTypes.feComponentTransfer tagname);
    native public NodeListOf<SVGFECompositeElement> getElementsByTagName(jsweet.util.StringTypes.feComposite tagname);
    native public NodeListOf<SVGFEConvolveMatrixElement> getElementsByTagName(jsweet.util.StringTypes.feConvolveMatrix tagname);
    native public NodeListOf<SVGFEDiffuseLightingElement> getElementsByTagName(jsweet.util.StringTypes.feDiffuseLighting tagname);
    native public NodeListOf<SVGFEDisplacementMapElement> getElementsByTagName(jsweet.util.StringTypes.feDisplacementMap tagname);
    native public NodeListOf<SVGFEDistantLightElement> getElementsByTagName(jsweet.util.StringTypes.feDistantLight tagname);
    native public NodeListOf<SVGFEFloodElement> getElementsByTagName(jsweet.util.StringTypes.feFlood tagname);
    native public NodeListOf<SVGFEFuncAElement> getElementsByTagName(jsweet.util.StringTypes.feFuncA tagname);
    native public NodeListOf<SVGFEFuncBElement> getElementsByTagName(jsweet.util.StringTypes.feFuncB tagname);
    native public NodeListOf<SVGFEFuncGElement> getElementsByTagName(jsweet.util.StringTypes.feFuncG tagname);
    native public NodeListOf<SVGFEFuncRElement> getElementsByTagName(jsweet.util.StringTypes.feFuncR tagname);
    native public NodeListOf<SVGFEGaussianBlurElement> getElementsByTagName(jsweet.util.StringTypes.feGaussianBlur tagname);
    native public NodeListOf<SVGFEImageElement> getElementsByTagName(jsweet.util.StringTypes.feImage tagname);
    native public NodeListOf<SVGFEMergeElement> getElementsByTagName(jsweet.util.StringTypes.feMerge tagname);
    native public NodeListOf<SVGFEMergeNodeElement> getElementsByTagName(jsweet.util.StringTypes.feMergeNode tagname);
    native public NodeListOf<SVGFEMorphologyElement> getElementsByTagName(jsweet.util.StringTypes.feMorphology tagname);
    native public NodeListOf<SVGFEOffsetElement> getElementsByTagName(jsweet.util.StringTypes.feOffset tagname);
    native public NodeListOf<SVGFEPointLightElement> getElementsByTagName(jsweet.util.StringTypes.fePointLight tagname);
    native public NodeListOf<SVGFESpecularLightingElement> getElementsByTagName(jsweet.util.StringTypes.feSpecularLighting tagname);
    native public NodeListOf<SVGFESpotLightElement> getElementsByTagName(jsweet.util.StringTypes.feSpotLight tagname);
    native public NodeListOf<SVGFETileElement> getElementsByTagName(jsweet.util.StringTypes.feTile tagname);
    native public NodeListOf<SVGFETurbulenceElement> getElementsByTagName(jsweet.util.StringTypes.feTurbulence tagname);
    native public NodeListOf<HTMLFieldSetElement> getElementsByTagName(jsweet.util.StringTypes.fieldset tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.figcaption tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.figure tagname);
    native public NodeListOf<SVGFilterElement> getElementsByTagName(jsweet.util.StringTypes.filter tagname);
    native public NodeListOf<HTMLFontElement> getElementsByTagName(jsweet.util.StringTypes.font tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.footer tagname);
    native public NodeListOf<SVGForeignObjectElement> getElementsByTagName(jsweet.util.StringTypes.foreignObject tagname);
    native public NodeListOf<HTMLFormElement> getElementsByTagName(jsweet.util.StringTypes.form tagname);
    native public NodeListOf<HTMLFrameElement> getElementsByTagName(jsweet.util.StringTypes.frame tagname);
    native public NodeListOf<HTMLFrameSetElement> getElementsByTagName(jsweet.util.StringTypes.frameset tagname);
    native public NodeListOf<SVGGElement> getElementsByTagName(jsweet.util.StringTypes.g tagname);
    native public NodeListOf<HTMLHeadingElement> getElementsByTagName(jsweet.util.StringTypes.h1 tagname);
    native public NodeListOf<HTMLHeadingElement> getElementsByTagName(jsweet.util.StringTypes.h2 tagname);
    native public NodeListOf<HTMLHeadingElement> getElementsByTagName(jsweet.util.StringTypes.h3 tagname);
    native public NodeListOf<HTMLHeadingElement> getElementsByTagName(jsweet.util.StringTypes.h4 tagname);
    native public NodeListOf<HTMLHeadingElement> getElementsByTagName(jsweet.util.StringTypes.h5 tagname);
    native public NodeListOf<HTMLHeadingElement> getElementsByTagName(jsweet.util.StringTypes.h6 tagname);
    native public NodeListOf<HTMLHeadElement> getElementsByTagName(jsweet.util.StringTypes.head tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.header tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.hgroup tagname);
    native public NodeListOf<HTMLHRElement> getElementsByTagName(jsweet.util.StringTypes.hr tagname);
    native public NodeListOf<HTMLHtmlElement> getElementsByTagName(jsweet.util.StringTypes.html tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.i tagname);
    native public NodeListOf<HTMLIFrameElement> getElementsByTagName(jsweet.util.StringTypes.iframe tagname);
    native public NodeListOf<SVGImageElement> getElementsByTagName(jsweet.util.StringTypes.image tagname);
    native public NodeListOf<HTMLImageElement> getElementsByTagName(jsweet.util.StringTypes.img tagname);
    native public NodeListOf<HTMLInputElement> getElementsByTagName(jsweet.util.StringTypes.input tagname);
    native public NodeListOf<HTMLModElement> getElementsByTagName(jsweet.util.StringTypes.ins tagname);
    native public NodeListOf<HTMLIsIndexElement> getElementsByTagName(jsweet.util.StringTypes.isindex tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.kbd tagname);
    native public NodeListOf<HTMLBlockElement> getElementsByTagName(jsweet.util.StringTypes.keygen tagname);
    native public NodeListOf<HTMLLabelElement> getElementsByTagName(jsweet.util.StringTypes.label tagname);
    native public NodeListOf<HTMLLegendElement> getElementsByTagName(jsweet.util.StringTypes.legend tagname);
    native public NodeListOf<HTMLLIElement> getElementsByTagName(jsweet.util.StringTypes.li tagname);
    native public NodeListOf<SVGLineElement> getElementsByTagName(jsweet.util.StringTypes.line tagname);
    native public NodeListOf<SVGLinearGradientElement> getElementsByTagName(jsweet.util.StringTypes.linearGradient tagname);
    native public NodeListOf<HTMLLinkElement> getElementsByTagName(jsweet.util.StringTypes.link tagname);
    native public NodeListOf<HTMLBlockElement> getElementsByTagName(jsweet.util.StringTypes.listing tagname);
    native public NodeListOf<HTMLMapElement> getElementsByTagName(jsweet.util.StringTypes.map tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.mark tagname);
    native public NodeListOf<SVGMarkerElement> getElementsByTagName(jsweet.util.StringTypes.marker tagname);
    native public NodeListOf<HTMLMarqueeElement> getElementsByTagName(jsweet.util.StringTypes.marquee tagname);
    native public NodeListOf<SVGMaskElement> getElementsByTagName(jsweet.util.StringTypes.mask tagname);
    native public NodeListOf<HTMLMenuElement> getElementsByTagName(jsweet.util.StringTypes.menu tagname);
    native public NodeListOf<HTMLMetaElement> getElementsByTagName(jsweet.util.StringTypes.meta tagname);
    native public NodeListOf<SVGMetadataElement> getElementsByTagName(jsweet.util.StringTypes.metadata tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.nav tagname);
    native public NodeListOf<HTMLNextIdElement> getElementsByTagName(jsweet.util.StringTypes.nextid tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.nobr tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.noframes tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.noscript tagname);
    native public NodeListOf<HTMLObjectElement> getElementsByTagName(jsweet.util.StringTypes.object tagname);
    native public NodeListOf<HTMLOListElement> getElementsByTagName(jsweet.util.StringTypes.ol tagname);
    native public NodeListOf<HTMLOptGroupElement> getElementsByTagName(jsweet.util.StringTypes.optgroup tagname);
    native public NodeListOf<HTMLOptionElement> getElementsByTagName(jsweet.util.StringTypes.option tagname);
    native public NodeListOf<HTMLParagraphElement> getElementsByTagName(jsweet.util.StringTypes.p tagname);
    native public NodeListOf<HTMLParamElement> getElementsByTagName(jsweet.util.StringTypes.param tagname);
    native public NodeListOf<SVGPathElement> getElementsByTagName(jsweet.util.StringTypes.path tagname);
    native public NodeListOf<SVGPatternElement> getElementsByTagName(jsweet.util.StringTypes.pattern tagname);
    native public NodeListOf<HTMLBlockElement> getElementsByTagName(jsweet.util.StringTypes.plaintext tagname);
    native public NodeListOf<SVGPolygonElement> getElementsByTagName(jsweet.util.StringTypes.polygon tagname);
    native public NodeListOf<SVGPolylineElement> getElementsByTagName(jsweet.util.StringTypes.polyline tagname);
    native public NodeListOf<HTMLPreElement> getElementsByTagName(jsweet.util.StringTypes.pre tagname);
    native public NodeListOf<HTMLProgressElement> getElementsByTagName(jsweet.util.StringTypes.progress tagname);
    native public NodeListOf<HTMLQuoteElement> getElementsByTagName(jsweet.util.StringTypes.q tagname);
    native public NodeListOf<SVGRadialGradientElement> getElementsByTagName(jsweet.util.StringTypes.radialGradient tagname);
    native public NodeListOf<SVGRectElement> getElementsByTagName(jsweet.util.StringTypes.rect tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.rt tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.ruby tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.s tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.samp tagname);
    native public NodeListOf<HTMLScriptElement> getElementsByTagName(jsweet.util.StringTypes.script tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.section tagname);
    native public NodeListOf<HTMLSelectElement> getElementsByTagName(jsweet.util.StringTypes.select tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.small tagname);
    native public NodeListOf<HTMLSourceElement> getElementsByTagName(jsweet.util.StringTypes.source tagname);
    native public NodeListOf<HTMLSpanElement> getElementsByTagName(jsweet.util.StringTypes.span tagname);
    native public NodeListOf<SVGStopElement> getElementsByTagName(jsweet.util.StringTypes.stop tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.strike tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.strong tagname);
    native public NodeListOf<HTMLStyleElement> getElementsByTagName(jsweet.util.StringTypes.style tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.sub tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.sup tagname);
    native public NodeListOf<SVGSVGElement> getElementsByTagName(jsweet.util.StringTypes.svg tagname);
    native public NodeListOf<SVGSwitchElement> getElementsByTagName(jsweet.util.StringTypes.Switch tagname);
    native public NodeListOf<SVGSymbolElement> getElementsByTagName(jsweet.util.StringTypes.symbol tagname);
    native public NodeListOf<HTMLTableElement> getElementsByTagName(jsweet.util.StringTypes.table tagname);
    native public NodeListOf<HTMLTableSectionElement> getElementsByTagName(jsweet.util.StringTypes.tbody tagname);
    native public NodeListOf<HTMLTableDataCellElement> getElementsByTagName(jsweet.util.StringTypes.td tagname);
    native public NodeListOf<SVGTextElement> getElementsByTagName(jsweet.util.StringTypes.text tagname);
    native public NodeListOf<SVGTextPathElement> getElementsByTagName(jsweet.util.StringTypes.textPath tagname);
    native public NodeListOf<HTMLTextAreaElement> getElementsByTagName(jsweet.util.StringTypes.textarea tagname);
    native public NodeListOf<HTMLTableSectionElement> getElementsByTagName(jsweet.util.StringTypes.tfoot tagname);
    native public NodeListOf<HTMLTableHeaderCellElement> getElementsByTagName(jsweet.util.StringTypes.th tagname);
    native public NodeListOf<HTMLTableSectionElement> getElementsByTagName(jsweet.util.StringTypes.thead tagname);
    native public NodeListOf<HTMLTitleElement> getElementsByTagName(jsweet.util.StringTypes.title tagname);
    native public NodeListOf<HTMLTableRowElement> getElementsByTagName(jsweet.util.StringTypes.tr tagname);
    native public NodeListOf<HTMLTrackElement> getElementsByTagName(jsweet.util.StringTypes.track tagname);
    native public NodeListOf<SVGTSpanElement> getElementsByTagName(jsweet.util.StringTypes.tspan tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.tt tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.u tagname);
    native public NodeListOf<HTMLUListElement> getElementsByTagName(jsweet.util.StringTypes.ul tagname);
    native public NodeListOf<SVGUseElement> getElementsByTagName(jsweet.util.StringTypes.use tagname);
    native public NodeListOf<HTMLPhraseElement> getElementsByTagName(jsweet.util.StringTypes.Var tagname);
    native public NodeListOf<HTMLVideoElement> getElementsByTagName(jsweet.util.StringTypes.video tagname);
    native public NodeListOf<SVGViewElement> getElementsByTagName(jsweet.util.StringTypes.view tagname);
    native public NodeListOf<HTMLElement> getElementsByTagName(jsweet.util.StringTypes.wbr tagname);
    native public NodeListOf<MSHTMLWebViewElement> getElementsByTagName(jsweet.util.StringTypes.x_ms_webview tagname);
    native public NodeListOf<HTMLBlockElement> getElementsByTagName(jsweet.util.StringTypes.xmp tagname);
    native public NodeListOf<Element> getElementsByTagName(String tagname);
    native public NodeListOf<Element> getElementsByTagNameNS(String namespaceURI, String localName);
    /**
      * Returns an object representing the current selection of the document that is loaded into the object displaying a webpage.
      */
    native public Selection getSelection();
    /**
      * Gets a value indicating whether the object currently has focus.
      */
    native public Boolean hasFocus();
    native public Node importNode(Node importedNode, Boolean deep);
    native public NodeList msElementsFromPoint(double x, double y);
    native public NodeList msElementsFromRect(double left, double top, double width, double height);
    /**
      * Opens a new window and loads a document specified by a given URL. Also, opens a new window that uses the url parameter and the name parameter to collect the output of the write method and the writeln method.
      * @param url Specifies a MIME type for the document.
      * @param name Specifies the name of the window. This name is used as the value for the TARGET attribute on a form or an anchor element.
      * @param features Contains a list of items separated by commas. Each item consists of an option and a value, separated by an equals sign (for example, "fullscreen=yes, toolbar=yes"). The following values are supported.
      * @param replace Specifies whether the existing entry for the document is replaced in the history list.
      */
    native public Document open(String url, String name, String features, Boolean replace);
    /** 
      * Returns a Boolean value that indicates whether a specified command can be successfully executed using execCommand, given the current state of the document.
      * @param commandId Specifies a command identifier.
      */
    native public Boolean queryCommandEnabled(String commandId);
    /**
      * Returns a Boolean value that indicates whether the specified command is in the indeterminate state.
      * @param commandId String that specifies a command identifier.
      */
    native public Boolean queryCommandIndeterm(String commandId);
    /**
      * Returns a Boolean value that indicates the current state of the command.
      * @param commandId String that specifies a command identifier.
      */
    native public Boolean queryCommandState(String commandId);
    /**
      * Returns a Boolean value that indicates whether the current command is supported on the current range.
      * @param commandId Specifies a command identifier.
      */
    native public Boolean queryCommandSupported(String commandId);
    /**
      * Retrieves the string associated with a command.
      * @param commandId String that contains the identifier of a command. This can be any command identifier given in the list of Command Identifiers. 
      */
    native public String queryCommandText(String commandId);
    /**
      * Returns the current value of the document, range, or current selection for the given command.
      * @param commandId String that specifies a command identifier.
      */
    native public String queryCommandValue(String commandId);
    native public void releaseEvents();
    /**
      * Allows updating the print settings for the page.
      */
    native public void updateSettings();
    native public void webkitCancelFullScreen();
    native public void webkitExitFullscreen();
    /**
      * Writes one or more HTML expressions to a document in the specified window. 
      * @param content Specifies the text and HTML tags to write.
      */
    native public void write(String... content);
    /**
      * Writes one or more HTML expressions, followed by a carriage return, to a document in the specified window. 
      * @param content The text and HTML tags to write.
      */
    native public void writeln(String... content);
    native public HTMLPictureElement createElement(jsweet.util.StringTypes.picture tagName);
    native public NodeListOf<HTMLPictureElement> getElementsByTagName(jsweet.util.StringTypes.picture tagname);
    native public void addEventListener(jsweet.util.StringTypes.MSContentZoom type, java.util.function.Function<UIEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureChange type, java.util.function.Function<MSGestureEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureDoubleTap type, java.util.function.Function<MSGestureEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureEnd type, java.util.function.Function<MSGestureEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureHold type, java.util.function.Function<MSGestureEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureStart type, java.util.function.Function<MSGestureEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureTap type, java.util.function.Function<MSGestureEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSInertiaStart type, java.util.function.Function<MSGestureEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSManipulationStateChanged type, java.util.function.Function<MSManipulationEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerCancel type, java.util.function.Function<MSPointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerDown type, java.util.function.Function<MSPointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerEnter type, java.util.function.Function<MSPointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerLeave type, java.util.function.Function<MSPointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerMove type, java.util.function.Function<MSPointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerOut type, java.util.function.Function<MSPointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerOver type, java.util.function.Function<MSPointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerUp type, java.util.function.Function<MSPointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.abort type, java.util.function.Function<UIEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.activate type, java.util.function.Function<UIEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.beforeactivate type, java.util.function.Function<UIEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.beforedeactivate type, java.util.function.Function<UIEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.blur type, java.util.function.Function<FocusEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.canplay type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.canplaythrough type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.change type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.click type, java.util.function.Function<MouseEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.contextmenu type, java.util.function.Function<PointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.dblclick type, java.util.function.Function<MouseEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.deactivate type, java.util.function.Function<UIEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.drag type, java.util.function.Function<DragEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.dragend type, java.util.function.Function<DragEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.dragenter type, java.util.function.Function<DragEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.dragleave type, java.util.function.Function<DragEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.dragover type, java.util.function.Function<DragEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.dragstart type, java.util.function.Function<DragEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.drop type, java.util.function.Function<DragEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.durationchange type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.emptied type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.ended type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.error type, java.util.function.Function<ErrorEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.focus type, java.util.function.Function<FocusEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.fullscreenchange type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.fullscreenerror type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.input type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.keydown type, java.util.function.Function<KeyboardEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.keypress type, java.util.function.Function<KeyboardEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.keyup type, java.util.function.Function<KeyboardEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.load type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.loadeddata type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.loadedmetadata type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.loadstart type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.mousedown type, java.util.function.Function<MouseEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.mousemove type, java.util.function.Function<MouseEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.mouseout type, java.util.function.Function<MouseEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.mouseover type, java.util.function.Function<MouseEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.mouseup type, java.util.function.Function<MouseEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.mousewheel type, java.util.function.Function<MouseWheelEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.mssitemodejumplistitemremoved type, java.util.function.Function<MSSiteModeEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.msthumbnailclick type, java.util.function.Function<MSSiteModeEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pause type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.play type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.playing type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pointerlockchange type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pointerlockerror type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.progress type, java.util.function.Function<ProgressEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.ratechange type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.readystatechange type, java.util.function.Function<ProgressEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.reset type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.scroll type, java.util.function.Function<UIEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.seeked type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.seeking type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.select type, java.util.function.Function<UIEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.selectstart type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.stalled type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.stop type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.submit type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.suspend type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.timeupdate type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.touchcancel type, java.util.function.Function<TouchEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.touchend type, java.util.function.Function<TouchEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.touchmove type, java.util.function.Function<TouchEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.touchstart type, java.util.function.Function<TouchEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.volumechange type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.waiting type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.webkitfullscreenchange type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.webkitfullscreenerror type, java.util.function.Function<Event,Object> listener, Boolean useCapture);
    public static Document prototype;
    public Document(){}
    public java.util.function.Function<PointerEvent,Object> onpointercancel;
    public java.util.function.Function<PointerEvent,Object> onpointerdown;
    public java.util.function.Function<PointerEvent,Object> onpointerenter;
    public java.util.function.Function<PointerEvent,Object> onpointerleave;
    public java.util.function.Function<PointerEvent,Object> onpointermove;
    public java.util.function.Function<PointerEvent,Object> onpointerout;
    public java.util.function.Function<PointerEvent,Object> onpointerover;
    public java.util.function.Function<PointerEvent,Object> onpointerup;
    public java.util.function.Function<WheelEvent,Object> onwheel;
    native public void addEventListener(jsweet.util.StringTypes.pointercancel type, java.util.function.Function<PointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pointerdown type, java.util.function.Function<PointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pointerenter type, java.util.function.Function<PointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pointerleave type, java.util.function.Function<PointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pointermove type, java.util.function.Function<PointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pointerout type, java.util.function.Function<PointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pointerover type, java.util.function.Function<PointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.pointerup type, java.util.function.Function<PointerEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(jsweet.util.StringTypes.wheel type, java.util.function.Function<WheelEvent,Object> listener, Boolean useCapture);
    native public void addEventListener(String type, EventListener listener, Boolean useCapture);
    native public Element querySelector(String selectors);
    native public NodeListOf<Element> querySelectorAll(String selectors);
    native public AnimationEvent createEvent(jsweet.util.StringTypes.AnimationEvent eventInterface);
    native public AriaRequestEvent createEvent(jsweet.util.StringTypes.AriaRequestEvent eventInterface);
    native public AudioProcessingEvent createEvent(jsweet.util.StringTypes.AudioProcessingEvent eventInterface);
    native public BeforeUnloadEvent createEvent(jsweet.util.StringTypes.BeforeUnloadEvent eventInterface);
    native public ClipboardEvent createEvent(jsweet.util.StringTypes.ClipboardEvent eventInterface);
    native public CloseEvent createEvent(jsweet.util.StringTypes.CloseEvent eventInterface);
    native public CommandEvent createEvent(jsweet.util.StringTypes.CommandEvent eventInterface);
    native public CompositionEvent createEvent(jsweet.util.StringTypes.CompositionEvent eventInterface);
    native public CustomEvent createEvent(jsweet.util.StringTypes.CustomEvent eventInterface);
    native public DeviceMotionEvent createEvent(jsweet.util.StringTypes.DeviceMotionEvent eventInterface);
    native public DeviceOrientationEvent createEvent(jsweet.util.StringTypes.DeviceOrientationEvent eventInterface);
    native public DragEvent createEvent(jsweet.util.StringTypes.DragEvent eventInterface);
    native public ErrorEvent createEvent(jsweet.util.StringTypes.ErrorEvent eventInterface);
    native public Event createEvent(jsweet.util.StringTypes.Event eventInterface);
    native public Event createEvent(jsweet.util.StringTypes.Events eventInterface);
    native public FocusEvent createEvent(jsweet.util.StringTypes.FocusEvent eventInterface);
    native public GamepadEvent createEvent(jsweet.util.StringTypes.GamepadEvent eventInterface);
    native public HashChangeEvent createEvent(jsweet.util.StringTypes.HashChangeEvent eventInterface);
    native public IDBVersionChangeEvent createEvent(jsweet.util.StringTypes.IDBVersionChangeEvent eventInterface);
    native public KeyboardEvent createEvent(jsweet.util.StringTypes.KeyboardEvent eventInterface);
    native public LongRunningScriptDetectedEvent createEvent(jsweet.util.StringTypes.LongRunningScriptDetectedEvent eventInterface);
    native public MSGestureEvent createEvent(jsweet.util.StringTypes.MSGestureEvent eventInterface);
    native public MSManipulationEvent createEvent(jsweet.util.StringTypes.MSManipulationEvent eventInterface);
    native public MSMediaKeyMessageEvent createEvent(jsweet.util.StringTypes.MSMediaKeyMessageEvent eventInterface);
    native public MSMediaKeyNeededEvent createEvent(jsweet.util.StringTypes.MSMediaKeyNeededEvent eventInterface);
    native public MSPointerEvent createEvent(jsweet.util.StringTypes.MSPointerEvent eventInterface);
    native public MSSiteModeEvent createEvent(jsweet.util.StringTypes.MSSiteModeEvent eventInterface);
    native public MessageEvent createEvent(jsweet.util.StringTypes.MessageEvent eventInterface);
    native public MouseEvent createEvent(jsweet.util.StringTypes.MouseEvent eventInterface);
    native public MouseEvent createEvent(jsweet.util.StringTypes.MouseEvents eventInterface);
    native public MouseWheelEvent createEvent(jsweet.util.StringTypes.MouseWheelEvent eventInterface);
    native public MutationEvent createEvent(jsweet.util.StringTypes.MutationEvent eventInterface);
    native public MutationEvent createEvent(jsweet.util.StringTypes.MutationEvents eventInterface);
    native public NavigationCompletedEvent createEvent(jsweet.util.StringTypes.NavigationCompletedEvent eventInterface);
    native public NavigationEvent createEvent(jsweet.util.StringTypes.NavigationEvent eventInterface);
    native public NavigationEventWithReferrer createEvent(jsweet.util.StringTypes.NavigationEventWithReferrer eventInterface);
    native public OfflineAudioCompletionEvent createEvent(jsweet.util.StringTypes.OfflineAudioCompletionEvent eventInterface);
    native public PageTransitionEvent createEvent(jsweet.util.StringTypes.PageTransitionEvent eventInterface);
    native public PermissionRequestedEvent createEvent(jsweet.util.StringTypes.PermissionRequestedEvent eventInterface);
    native public PointerEvent createEvent(jsweet.util.StringTypes.PointerEvent eventInterface);
    native public PopStateEvent createEvent(jsweet.util.StringTypes.PopStateEvent eventInterface);
    native public ProgressEvent createEvent(jsweet.util.StringTypes.ProgressEvent eventInterface);
    native public SVGZoomEvent createEvent(jsweet.util.StringTypes.SVGZoomEvent eventInterface);
    native public SVGZoomEvent createEvent(jsweet.util.StringTypes.SVGZoomEvents eventInterface);
    native public ScriptNotifyEvent createEvent(jsweet.util.StringTypes.ScriptNotifyEvent eventInterface);
    native public StorageEvent createEvent(jsweet.util.StringTypes.StorageEvent eventInterface);
    native public TextEvent createEvent(jsweet.util.StringTypes.TextEvent eventInterface);
    native public TouchEvent createEvent(jsweet.util.StringTypes.TouchEvent eventInterface);
    native public TrackEvent createEvent(jsweet.util.StringTypes.TrackEvent eventInterface);
    native public TransitionEvent createEvent(jsweet.util.StringTypes.TransitionEvent eventInterface);
    native public UIEvent createEvent(jsweet.util.StringTypes.UIEvent eventInterface);
    native public UIEvent createEvent(jsweet.util.StringTypes.UIEvents eventInterface);
    native public UnviewableContentIdentifiedEvent createEvent(jsweet.util.StringTypes.UnviewableContentIdentifiedEvent eventInterface);
    native public WebGLContextEvent createEvent(jsweet.util.StringTypes.WebGLContextEvent eventInterface);
    native public WheelEvent createEvent(jsweet.util.StringTypes.WheelEvent eventInterface);
    native public Event createEvent(String eventInterface);
    /**
      * Creates a NodeIterator object that you can use to traverse filtered lists of nodes or elements in a document. 
      * @param root The root element or node to start traversing on.
      * @param whatToShow The type of nodes or elements to appear in the node list
      * @param filter A custom NodeFilter function to use. For more information, see filter. Use null for no filter.
      * @param entityReferenceExpansion A flag that specifies whether entity reference nodes are expanded.
      */
    native public NodeIterator createNodeIterator(Node root, double whatToShow, NodeFilter filter);
    /**
      * Creates a NodeIterator object that you can use to traverse filtered lists of nodes or elements in a document. 
      * @param root The root element or node to start traversing on.
      * @param whatToShow The type of nodes or elements to appear in the node list
      * @param filter A custom NodeFilter function to use. For more information, see filter. Use null for no filter.
      * @param entityReferenceExpansion A flag that specifies whether entity reference nodes are expanded.
      */
    native public NodeIterator createNodeIterator(Node root, double whatToShow);
    /**
      * Creates a NodeIterator object that you can use to traverse filtered lists of nodes or elements in a document. 
      * @param root The root element or node to start traversing on.
      * @param whatToShow The type of nodes or elements to appear in the node list
      * @param filter A custom NodeFilter function to use. For more information, see filter. Use null for no filter.
      * @param entityReferenceExpansion A flag that specifies whether entity reference nodes are expanded.
      */
    native public NodeIterator createNodeIterator(Node root);
    /**
      * Creates a TreeWalker object that you can use to traverse filtered lists of nodes or elements in a document.
      * @param root The root element or node to start traversing on.
      * @param whatToShow The type of nodes or elements to appear in the node list. For more information, see whatToShow.
      * @param filter A custom NodeFilter function to use.
      * @param entityReferenceExpansion A flag that specifies whether entity reference nodes are expanded.
      */
    native public TreeWalker createTreeWalker(Node root, double whatToShow, NodeFilter filter);
    /**
      * Creates a TreeWalker object that you can use to traverse filtered lists of nodes or elements in a document.
      * @param root The root element or node to start traversing on.
      * @param whatToShow The type of nodes or elements to appear in the node list. For more information, see whatToShow.
      * @param filter A custom NodeFilter function to use.
      * @param entityReferenceExpansion A flag that specifies whether entity reference nodes are expanded.
      */
    native public TreeWalker createTreeWalker(Node root, double whatToShow);
    /**
      * Creates a TreeWalker object that you can use to traverse filtered lists of nodes or elements in a document.
      * @param root The root element or node to start traversing on.
      * @param whatToShow The type of nodes or elements to appear in the node list. For more information, see whatToShow.
      * @param filter A custom NodeFilter function to use.
      * @param entityReferenceExpansion A flag that specifies whether entity reference nodes are expanded.
      */
    native public TreeWalker createTreeWalker(Node root);
    /**
      * Executes a command on the current document, current selection, or the given range.
      * @param commandId String that specifies the command to execute. This command can be any of the command identifiers that can be executed in script.
      * @param showUI Display the user interface, defaults to false.
      * @param value Value to assign.
      */
    native public Boolean execCommand(String commandId, Boolean showUI);
    /**
      * Executes a command on the current document, current selection, or the given range.
      * @param commandId String that specifies the command to execute. This command can be any of the command identifiers that can be executed in script.
      * @param showUI Display the user interface, defaults to false.
      * @param value Value to assign.
      */
    native public Boolean execCommand(String commandId);
    /**
      * Opens a new window and loads a document specified by a given URL. Also, opens a new window that uses the url parameter and the name parameter to collect the output of the write method and the writeln method.
      * @param url Specifies a MIME type for the document.
      * @param name Specifies the name of the window. This name is used as the value for the TARGET attribute on a form or an anchor element.
      * @param features Contains a list of items separated by commas. Each item consists of an option and a value, separated by an equals sign (for example, "fullscreen=yes, toolbar=yes"). The following values are supported.
      * @param replace Specifies whether the existing entry for the document is replaced in the history list.
      */
    native public Document open(String url, String name, String features);
    /**
      * Opens a new window and loads a document specified by a given URL. Also, opens a new window that uses the url parameter and the name parameter to collect the output of the write method and the writeln method.
      * @param url Specifies a MIME type for the document.
      * @param name Specifies the name of the window. This name is used as the value for the TARGET attribute on a form or an anchor element.
      * @param features Contains a list of items separated by commas. Each item consists of an option and a value, separated by an equals sign (for example, "fullscreen=yes, toolbar=yes"). The following values are supported.
      * @param replace Specifies whether the existing entry for the document is replaced in the history list.
      */
    native public Document open(String url, String name);
    /**
      * Opens a new window and loads a document specified by a given URL. Also, opens a new window that uses the url parameter and the name parameter to collect the output of the write method and the writeln method.
      * @param url Specifies a MIME type for the document.
      * @param name Specifies the name of the window. This name is used as the value for the TARGET attribute on a form or an anchor element.
      * @param features Contains a list of items separated by commas. Each item consists of an option and a value, separated by an equals sign (for example, "fullscreen=yes, toolbar=yes"). The following values are supported.
      * @param replace Specifies whether the existing entry for the document is replaced in the history list.
      */
    native public Document open(String url);
    /**
      * Opens a new window and loads a document specified by a given URL. Also, opens a new window that uses the url parameter and the name parameter to collect the output of the write method and the writeln method.
      * @param url Specifies a MIME type for the document.
      * @param name Specifies the name of the window. This name is used as the value for the TARGET attribute on a form or an anchor element.
      * @param features Contains a list of items separated by commas. Each item consists of an option and a value, separated by an equals sign (for example, "fullscreen=yes, toolbar=yes"). The following values are supported.
      * @param replace Specifies whether the existing entry for the document is replaced in the history list.
      */
    native public Document open();
    native public void addEventListener(jsweet.util.StringTypes.MSContentZoom type, java.util.function.Function<UIEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureChange type, java.util.function.Function<MSGestureEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureDoubleTap type, java.util.function.Function<MSGestureEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureEnd type, java.util.function.Function<MSGestureEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureHold type, java.util.function.Function<MSGestureEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureStart type, java.util.function.Function<MSGestureEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSGestureTap type, java.util.function.Function<MSGestureEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSInertiaStart type, java.util.function.Function<MSGestureEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSManipulationStateChanged type, java.util.function.Function<MSManipulationEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerCancel type, java.util.function.Function<MSPointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerDown type, java.util.function.Function<MSPointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerEnter type, java.util.function.Function<MSPointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerLeave type, java.util.function.Function<MSPointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerMove type, java.util.function.Function<MSPointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerOut type, java.util.function.Function<MSPointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerOver type, java.util.function.Function<MSPointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.MSPointerUp type, java.util.function.Function<MSPointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.abort type, java.util.function.Function<UIEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.activate type, java.util.function.Function<UIEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.beforeactivate type, java.util.function.Function<UIEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.beforedeactivate type, java.util.function.Function<UIEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.blur type, java.util.function.Function<FocusEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.canplay type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.canplaythrough type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.change type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.click type, java.util.function.Function<MouseEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.contextmenu type, java.util.function.Function<PointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.dblclick type, java.util.function.Function<MouseEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.deactivate type, java.util.function.Function<UIEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.drag type, java.util.function.Function<DragEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.dragend type, java.util.function.Function<DragEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.dragenter type, java.util.function.Function<DragEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.dragleave type, java.util.function.Function<DragEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.dragover type, java.util.function.Function<DragEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.dragstart type, java.util.function.Function<DragEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.drop type, java.util.function.Function<DragEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.durationchange type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.emptied type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.ended type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.error type, java.util.function.Function<ErrorEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.focus type, java.util.function.Function<FocusEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.fullscreenchange type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.fullscreenerror type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.input type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.keydown type, java.util.function.Function<KeyboardEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.keypress type, java.util.function.Function<KeyboardEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.keyup type, java.util.function.Function<KeyboardEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.load type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.loadeddata type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.loadedmetadata type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.loadstart type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.mousedown type, java.util.function.Function<MouseEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.mousemove type, java.util.function.Function<MouseEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.mouseout type, java.util.function.Function<MouseEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.mouseover type, java.util.function.Function<MouseEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.mouseup type, java.util.function.Function<MouseEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.mousewheel type, java.util.function.Function<MouseWheelEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.mssitemodejumplistitemremoved type, java.util.function.Function<MSSiteModeEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.msthumbnailclick type, java.util.function.Function<MSSiteModeEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pause type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.play type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.playing type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointercancel type, java.util.function.Function<PointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointerdown type, java.util.function.Function<PointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointerenter type, java.util.function.Function<PointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointerleave type, java.util.function.Function<PointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointerlockchange type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointerlockerror type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointermove type, java.util.function.Function<PointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointerout type, java.util.function.Function<PointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointerover type, java.util.function.Function<PointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.pointerup type, java.util.function.Function<PointerEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.progress type, java.util.function.Function<ProgressEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.ratechange type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.readystatechange type, java.util.function.Function<ProgressEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.reset type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.scroll type, java.util.function.Function<UIEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.seeked type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.seeking type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.select type, java.util.function.Function<UIEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.selectstart type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.stalled type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.stop type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.submit type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.suspend type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.timeupdate type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.touchcancel type, java.util.function.Function<TouchEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.touchend type, java.util.function.Function<TouchEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.touchmove type, java.util.function.Function<TouchEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.touchstart type, java.util.function.Function<TouchEvent,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.volumechange type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.waiting type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.webkitfullscreenchange type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.webkitfullscreenerror type, java.util.function.Function<Event,Object> listener);
    native public void addEventListener(jsweet.util.StringTypes.wheel type, java.util.function.Function<WheelEvent,Object> listener);
    native public void addEventListener(String type, EventListener listener);
    native public void addEventListener(String type, EventListenerObject listener, Boolean useCapture);
    native public void addEventListener(String type, EventListenerObject listener);
}

