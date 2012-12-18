/*    */ package mineshafter.cache;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.net.CacheResponse;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class SkinsCacheResponse extends CacheResponse
/*    */ {
/*    */   Map<String, List<String>> headers;
/*    */   ByteArrayOutputStream body;
/*    */ 
/*    */   public SkinsCacheResponse(Map<String, List<String>> headers, OutputStream body)
/*    */   {
/* 17 */     this.headers = headers;
/* 18 */     this.body = ((ByteArrayOutputStream)body);
/*    */   }
/*    */ 
/*    */   public InputStream getBody() throws IOException
/*    */   {
/* 23 */     return new ByteArrayInputStream(this.body.toByteArray());
/*    */   }
/*    */ 
/*    */   public Map<String, List<String>> getHeaders()
/*    */     throws IOException
/*    */   {
/* 29 */     return null;
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.cache.SkinsCacheResponse
 * JD-Core Version:    0.6.2
 */