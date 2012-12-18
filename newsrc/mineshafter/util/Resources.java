/*    */ package mineshafter.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class Resources
/*    */ {
/*    */   public static InputStream load(String filename)
/*    */     throws FileNotFoundException
/*    */   {
/* 12 */     InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
/* 13 */     if (in == null) {
/* 14 */       in = new FileInputStream(filename);
/*    */     }
/* 16 */     return in;
/*    */   }
/*    */ 
/*    */   public static String loadString(String filename) {
/*    */     try {
/* 21 */       char[] b = new char[4096];
/* 22 */       int read = 0;
/* 23 */       StringBuilder builder = new StringBuilder();
/* 24 */       BufferedReader reader = new BufferedReader(new InputStreamReader(load(filename)));
/*    */ 
/* 26 */       while ((read = reader.read(b)) != -1) {
/* 27 */         builder.append(String.valueOf(b, 0, read));
/*    */       }
/*    */ 
/* 30 */       reader.close();
/* 31 */       return builder.toString();
/*    */     } catch (Exception e) {
/* 33 */       System.out.println("load resources:");
/*    */     }
/* 35 */     return null;
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.util.Resources
 * JD-Core Version:    0.6.2
 */