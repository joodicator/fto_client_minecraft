/*    */ package mineshafter.cache;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.net.CacheRequest;
/*    */ import java.net.CacheResponse;
/*    */ import java.net.ResponseCache;
/*    */ import java.net.URI;
/*    */ import java.net.URL;
/*    */ import java.net.URLConnection;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class SkinsResponseCache extends ResponseCache
/*    */ {
/* 17 */   private static Pattern[] cacheOn = { 
/* 18 */     Pattern.compile("http://s3.amazonaws.com/MinecraftSkins/.*"), 
/* 19 */     Pattern.compile("http://s3.amazonaws.com/MinecraftCloaks/.*"), 
/* 20 */     Pattern.compile("http://mineshafter\\.appspot.com/skin/.*"), 
/* 21 */     Pattern.compile("http://mineshafter\\.appspot.com/cloak/.*") };
/*    */ 
/* 24 */   private Map<URI, CacheResponse> cache = new HashMap();
/*    */ 
/*    */   public CacheResponse get(URI uri, String rqstMethod, Map<String, List<String>> rqstHeaders) throws IOException
/*    */   {
/* 28 */     if (!rqstMethod.equalsIgnoreCase("GET")) {
/* 29 */       return null;
/*    */     }
/*    */ 
/* 32 */     CacheResponse r = (CacheResponse)this.cache.get(uri);
/*    */ 
/* 34 */     if (r != null) {
/* 35 */       System.out.println("Got from cache: " + uri.toString());
/*    */     }
/*    */ 
/* 38 */     return r;
/*    */   }
/*    */ 
/*    */   public CacheRequest put(URI uri, URLConnection conn)
/*    */   {
/*    */     try {
/* 44 */       uri = conn.getURL().toURI();
/*    */ 
/* 46 */       for (Pattern p : cacheOn) {
/* 47 */         Matcher m = p.matcher(uri.toString());
/*    */ 
/* 49 */         if (m.matches()) {
/* 50 */           CacheRequest req = new SkinsCacheRequest();
/* 51 */           Map headers = conn.getHeaderFields();
/* 52 */           CacheResponse resp = new SkinsCacheResponse(headers, req.getBody());
/* 53 */           this.cache.put(uri, resp);
/* 54 */           System.out.println("Put in cache: " + uri.toString());
/*    */ 
/* 56 */           return req;
/*    */         }
/*    */       }
/* 59 */       return null;
/*    */     } catch (Exception e) {
/* 61 */       System.out.println("Skin cache put failed:");
/* 62 */     }return null;
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.cache.SkinsResponseCache
 * JD-Core Version:    0.6.2
 */