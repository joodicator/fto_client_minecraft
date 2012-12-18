/*    */ package mineshafter.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class Streams
/*    */ {
/*    */   public static int pipeStreams(InputStream in, OutputStream out)
/*    */     throws IOException
/*    */   {
/* 10 */     byte[] b = new byte[8192];
/*    */ 
/* 12 */     int total = 0;
             int read;
/*    */     while (true) {
/*    */       try {
/* 15 */         read = in.read(b);
/* 16 */         if (read == -1) break;
/*    */       }
/*    */       catch (IOException e)
/*    */       {
                    throw new RuntimeException(e);
/*    */       }
/* 22 */       out.write(b, 0, read);
/* 23 */       total += read;
/*    */     }
/* 25 */     out.flush();
/* 26 */     return total;
/*    */   }
/*    */ 
/*    */   public static void pipeStreamsActive(final InputStream in, final OutputStream out) {
/* 30 */     Thread thread = new Thread("Active Pipe Thread")
/*    */     {
/*    */       public void run()
/*    */       {
/* 34 */         byte[] b = new byte[8192];
/*    */         try
/*    */         {
/*    */           while (true) {
/* 38 */             int count = in.read(b);
/* 39 */             if (count == -1) {
/* 40 */               return;
/*    */             }
/* 42 */             out.write(b, 0, count);
/* 43 */             out.flush();
/*    */           }
/*    */         }
/*    */         catch (IOException e)
/*    */         {
/*    */         }
/*    */       }
/*    */     };
/* 50 */     thread.start();
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.util.Streams
 * JD-Core Version:    0.6.2
 */
