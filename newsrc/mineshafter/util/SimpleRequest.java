/*    */ package mineshafter.util;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.Proxy;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class SimpleRequest
/*    */ {
/*    */   public static byte[] get(String url)
/*    */   {
/*    */     try
/*    */     {
/* 15 */       return get(new URL(url)); } catch (MalformedURLException e) {
/*    */     }
/* 17 */     return null;
/*    */   }
/*    */ 
/*    */   public static byte[] get(URL url)
/*    */   {
/*    */     try {
/* 23 */       HttpURLConnection c = (HttpURLConnection)url.openConnection(Proxy.NO_PROXY);
/* 24 */       c.setRequestMethod("GET");
/* 25 */       c.setRequestProperty("Host", url.getHost());
/* 26 */       BufferedInputStream in = new BufferedInputStream(c.getInputStream());
/* 27 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 28 */       Streams.pipeStreams(in, out);
/*    */ 
/* 30 */       return out.toByteArray(); } catch (IOException e) {
/*    */     }
/* 32 */     return new byte[0];
/*    */   }
/*    */ 
/*    */   public static byte[] post(String url, byte[] data)
/*    */   {
/*    */     try {
/* 38 */       return post(new URL(url), data); } catch (MalformedURLException e) {
/*    */     }
/* 40 */     return null;
/*    */   }
/*    */ 
/*    */   public static byte[] post(URL url, byte[] data)
/*    */   {
/*    */     try {
/* 46 */       HttpURLConnection c = (HttpURLConnection)url.openConnection(Proxy.NO_PROXY);
/* 47 */       c.setRequestMethod("POST");
/* 48 */       c.setDoOutput(true);
/* 49 */       c.setRequestProperty("Host", url.getHost());
/* 50 */       c.setRequestProperty("Content-length", Integer.toString(data.length));
/* 51 */       c.getOutputStream().write(data);
/* 52 */       BufferedInputStream in = new BufferedInputStream(c.getInputStream());
/* 53 */       ByteArrayOutputStream returnStream = new ByteArrayOutputStream();
/* 54 */       Streams.pipeStreams(in, returnStream);
/*    */ 
/* 56 */       return returnStream.toByteArray(); } catch (IOException e) {
/*    */     }
/* 58 */     return new byte[0];
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.util.SimpleRequest
 * JD-Core Version:    0.6.2
 */