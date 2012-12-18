/*    */ package net.minecraft;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class Util
/*    */ {
/* 14 */   private static File workDir = null;
/* 15 */   public static boolean portable = false;
/*    */ 
/*    */   public static File getWorkingDirectory()
/*    */   {
/* 19 */     if (workDir == null)
/*    */     {
/* 21 */       if (portable) workDir = new File("minecraft.files"); else
/* 22 */         workDir = getWorkingDirectory("minecraft");
/*    */     }
/* 24 */     return workDir;
/*    */   }
/*    */ 
/*    */   public static File getWorkingDirectory(String paramString) {
/* 28 */     String str1 = System.getProperty("user.home", ".");
/*    */     File localFile;
/* 30 */     switch (getPlatform())
/*    */     {
/*    */     case 2:
/* 33 */       localFile = new File(str1, "." + paramString + "/");
/* 34 */       break;
/*    */     case 0:
/* 36 */       String str2 = System.getenv("APPDATA");
/* 37 */       if (str2 != null) localFile = new File(str2, "." + paramString + "/"); else
/* 38 */         localFile = new File(str1, "." + paramString + "/");
/* 39 */       break;
/*    */     case 1:
/* 41 */       localFile = new File(str1, "Library/Application Support/" + paramString);
/* 42 */       break;
/*    */     default:
/* 44 */       localFile = new File(str1, paramString + "/");
/*    */     }
/* 46 */     if ((!localFile.exists()) && (!localFile.mkdirs())) throw new RuntimeException("The working directory could not be created: " + localFile);
/* 47 */     return localFile;
/*    */   }
/*    */ 
/*    */   private static int getPlatform()
/*    */   {
/* 52 */     String str = System.getProperty("os.name").toLowerCase();
/* 53 */     if (str.contains("win")) return 0;
/* 54 */     if (str.contains("mac")) return 1;
/* 55 */     if ((str.contains("solaris")) || (str.contains("sunos")) || (str.contains("linux")) || (str.contains("unix"))) return 2;
/* 56 */     return -1;
/*    */   }
/*    */ 
/*    */   public static String excutePost(String paramString1, String paramString2)
/*    */   {
/* 61 */     HttpURLConnection localHttpURLConnection = null;
/* 62 */     if (paramString1.startsWith("https://login.minecraft.net")) paramString1 = "http://www.minecraft.net/game/getversion.jsp";
/* 63 */     System.out.println("executePost: " + paramString1);
/*    */     try {
/* 65 */       byte[] arrayOfByte1 = paramString2.getBytes();
/* 66 */       URL localURL = new URL(paramString1);
/* 67 */       localHttpURLConnection = (HttpURLConnection)localURL.openConnection();
/*    */ 
/* 69 */       localHttpURLConnection.setRequestMethod("POST");
/* 70 */       localHttpURLConnection.setDoOutput(true);
/* 71 */       localHttpURLConnection.setRequestProperty("Host", localURL.getHost());
/* 72 */       localHttpURLConnection.setRequestProperty("Content-Length", Integer.toString(arrayOfByte1.length));
/* 73 */       localHttpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
/* 74 */       localHttpURLConnection.getOutputStream().write(arrayOfByte1);
/* 75 */       InputStream localInputStream = localHttpURLConnection.getInputStream();
/* 76 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 77 */       byte[] arrayOfByte2 = new byte[4096];
/*    */       while (true)
/*    */       {
/*    */         int i;
/*    */         try {
/* 82 */           i = localInputStream.read(arrayOfByte2);
/* 83 */           if (i == -1) break; 
/*    */         } catch (IOException localIOException) { break; }
/* 85 */         localByteArrayOutputStream.write(arrayOfByte2, 0, i);
/*    */       }
/* 87 */       localByteArrayOutputStream.flush();
/* 88 */       return new String(localByteArrayOutputStream.toByteArray()); } catch (Exception localException) {
/* 89 */       System.out.println("executePost failed:"); localException.printStackTrace(); } return null;
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     net.minecraft.Util
 * JD-Core Version:    0.6.2
 */