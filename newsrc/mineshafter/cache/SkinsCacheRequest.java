/*    */ package mineshafter.cache;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.net.CacheRequest;
/*    */ 
/*    */ public class SkinsCacheRequest extends CacheRequest
/*    */ {
/*  9 */   final ByteArrayOutputStream body = new ByteArrayOutputStream();
/*    */ 
/*    */   public OutputStream getBody() throws IOException
/*    */   {
/* 13 */     return this.body;
/*    */   }
/*    */ 
/*    */   public void abort()
/*    */   {
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.cache.SkinsCacheRequest
 * JD-Core Version:    0.6.2
 */