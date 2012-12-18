/*    */ package mineshafter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.lang.reflect.Method;
/*    */ import java.net.URI;
/*    */ import java.net.URL;
/*    */ import java.net.URLClassLoader;
/*    */ import java.util.jar.Attributes;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.jar.Manifest;
/*    */ import javax.swing.JOptionPane;
/*    */ import mineshafter.proxy.MineProxy;
/*    */ import mineshafter.util.Resources;
/*    */ import mineshafter.util.SimpleRequest;
/*    */ 
/*    */ public class MineServer
/*    */ {
/* 17 */   protected static float VERSION = 2.2F;
/* 18 */   protected static int proxyPort = 8071;
/* 19 */   protected static int proxyHTTPPort = 8072;
/* 20 */   protected static int proxyHTTPSPort = 8073;
/* 21 */   public static String authServer = Resources.loadString("auth").trim();
/*    */ 
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try {
/* 26 */       String verstring = new String(SimpleRequest.get(new URL("http://" + authServer + "/update.php?name=server")));
/*    */ 
/* 29 */       if ((verstring.isEmpty()) || (verstring.equals(""))) {
/* 30 */         verstring = "0";
/*    */       }
/*    */ 
/*    */       float version;
/*    */       try
/*    */       {
/* 36 */         version = Float.parseFloat(verstring);
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/*    */         float version;
/* 38 */         version = 0.0F;
/*    */       }
/*    */ 
/* 42 */       System.out.println("Current proxy version: " + VERSION);
/* 43 */       System.out.println("Gotten proxy version: " + version);
/*    */ 
/* 46 */       if (VERSION < version)
/*    */       {
/* 48 */         JOptionPane.showMessageDialog(null, "A new version of Mineshafter Squared is available at http://" + authServer + "/\nGo get it.", "Update Available", -1);
/* 49 */         System.exit(0);
/*    */       }
/*    */     } catch (Exception e) {
/* 52 */       System.out.println("Error while updating:");
/* 53 */       System.exit(1);
/*    */     }
/*    */ try {
/* 58 */       MineProxy.listen(proxyPort, proxyHTTPPort, proxyHTTPSPort, VERSION);
/*    */ 
/* 60 */       System.setProperty("http.proxyHost", "127.0.0.1");
/* 61 */       System.setProperty("http.proxyPort", Integer.toString(proxyPort));
/* 62 */       System.setProperty("https.proxyHost", "127.0.0.1");
/* 63 */       System.setProperty("https.proxyPort", Integer.toString(proxyPort));
/*    */       String load;
/*    */       try {
/* 69 */         load = args[0];
/*    */       }
/*    */       catch (ArrayIndexOutOfBoundsException e)
/*    */       {
/*    */         String load;
/* 71 */         load = "minecraft_server.jar";
/*    */       }
/*    */ 
/* 74 */       Attributes attributes = new JarFile(load).getManifest().getMainAttributes();
/* 75 */       String name = attributes.getValue("Main-Class");
/*    */ 
/* 89 */       URLClassLoader cl = null;
/* 90 */       Class cls = null;
/* 91 */       Method main = null;
/*    */       try {
/* 93 */         cl = new URLClassLoader(new URL[] { new File(load).toURI().toURL() });
/* 94 */         cls = cl.loadClass(name);
/* 95 */         main = cls.getDeclaredMethod("main", new Class[] { [Ljava.lang.String.class });
/*    */       } catch (Exception e) {
/* 97 */         System.out.println("Error loading class " + name + " from jar " + load + ":");
/* 98 */         System.exit(1);
/*    */       }
/*    */       String[] nargs;
/*    */       try {
/* 102 */         String[] nargs = new String[args.length - 1];
/* 103 */         System.arraycopy(args, 1, nargs, 0, nargs.length);
/*    */       } catch (Exception e) {
/* 105 */         nargs = new String[0];
/*    */       }
/* 107 */       main.invoke(cls, new Object[] { nargs });
/*    */     } catch (Exception e) {
/* 109 */       System.out.println("Something bad happened:");
/* 110 */       System.exit(1);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.MineServer
 * JD-Core Version:    0.6.2
 */