package def.js;
/**
  * Represents a raw buffer of binary data, which is used to store data for the 
  * different typed arrays. ArrayBuffers cannot be read from or written to directly, 
  * but can be passed to a typed array or DataView Object to interpret the raw 
  * buffer as needed. 
  */
public class ArrayBuffer extends def.js.Object {
    /**
      * Read-only. The length of the ArrayBuffer (in bytes).
      */
    public final double byteLength=0;
    /**
      * Returns a section of an ArrayBuffer.
      */
    native public ArrayBuffer slice(double begin, double end);
    public static final ArrayBuffer prototype=null;
    public ArrayBuffer(double byteLength){}
    native public static java.lang.Boolean isView(java.lang.Object arg);
    /**
      * Returns a section of an ArrayBuffer.
      */
    native public ArrayBuffer slice(double begin);
    protected ArrayBuffer(){}
}

