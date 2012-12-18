/*     */ package mineshafter;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Map;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JOptionPane;
/*     */ import mineshafter.proxy.MineProxy;
/*     */ import mineshafter.util.Resources;
/*     */ import mineshafter.util.SimpleRequest;
/*     */ import mineshafter.util.SplashScreen;
/*     */ import mineshafter.util.Streams;
/*     */ 
/*     */ public class MineClient extends Applet
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  29 */   protected static float VERSION = 2.9F;
/*  30 */   protected static int proxyPort = 8061;
/*  31 */   protected static int proxyHTTPPort = 8062;
/*  32 */   protected static int proxyHTTPSPort = 8063;
/*  33 */   protected static String launcherDownloadURL = "https://s3.amazonaws.com/MinecraftDownload/launcher/minecraft.jar";
/*  34 */   protected static String normalLauncherFilename = "Minecraft.jar";
/*  35 */   protected static String hackedLauncherFilename = "Minecraft_modified.jar";
/*  36 */   protected static boolean portableMode = false;
/*  37 */   public static String authServer = Resources.loadString("auth").trim();
/*     */   private static SplashScreen splash;
/*     */   static final int MAXPROGRESS = 100;
/*     */   static final int PROGRESS_STEPS = 11;
/*     */   static final int PROGRESS_INCREMENT = 9;
/*  42 */   static int progress = 0;
/*  43 */   static boolean launcherModified = false;
/*     */   static String gamePath;
/*     */ 
/*     */   public void init()
/*     */   {
/*  48 */     main(new String[0]); } 
/*  52 */   public static void main(String[] args) { splashScreenInit();
/*     */     String[] updateInfoArray;
/*     */     String verstring;
/*     */     boolean needGameUpdate;
/*     */     try { progress += 9;
/*  56 */       splash.setProgress("Checking for updates", progress);
/*  57 */       String buildNumber = getGameBuildNumber();
/*     */ 
/*  59 */       String updateInfo = new String(SimpleRequest.get("http://" + authServer + "/update.php?name=client&build=" + buildNumber));
/*     */ 
/*  61 */       updateInfoArray = updateInfo.split(":");
/*     */ 
/*  63 */       verstring = updateInfoArray[0];
/*  64 */       needGameUpdate = Boolean.parseBoolean(updateInfoArray[1]);
/*  65 */       String gameVersion = updateInfoArray[2];
/*     */ 
/*  67 */       progress += 9;
/*  68 */       splash.setProgress(progress);
/*     */ 
/*  71 */       if (verstring.isEmpty()) {
/*  72 */         verstring = "0";
/*     */       }
/*     */       float version;
/*     */       try
/*     */       {
/*  77 */         version = Float.parseFloat(verstring);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */         float version;
/*  79 */         version = 0.0F;
/*     */       }
/*     */ 
/*  83 */       System.out.println("Current proxy version: " + VERSION);
/*  84 */       System.out.println("Gotten proxy version: " + version);
/*     */ 
/*  86 */       if (VERSION < version) {
/*  87 */         JOptionPane.showMessageDialog(null, "A new version of Mineshafter is available at http://" + authServer + "/\nGo get it.", "Update Available", -1);
/*  88 */         System.exit(0);
/*     */       }
/*     */ 
/*  92 */       System.out.println("Game Version Found: " + buildNumber);
/*  93 */       System.out.println("Needs Update: " + needGameUpdate);
/*  94 */       System.out.println("Latest Version: " + gameVersion);
/*  95 */       if (needGameUpdate) {
/*  96 */         int answer = JOptionPane.showConfirmDialog(null, "A new version of Minecraft is available, would you like to update to version " + gameVersion + "?", 
/*  97 */           "Game Update Found!", 0, 1);
/*  98 */         if (answer == 0) {
/*  99 */           int areYouSure = JOptionPane.showConfirmDialog(null, "Make sure any online servers you would like to play on are updated to version " + 
/* 100 */             gameVersion + " or else you will not be able to play on them until they do.", 
/* 101 */             "Are you sure?", 0, 2);
/* 102 */           if (areYouSure == 0)
/* 103 */             rDelete(new File(gamePath));
/*     */         }
/*     */       }
/*     */     } catch (Exception e)
/*     */     {
/* 108 */       System.out.println("Error while updating:");
/* 109 */       System.out.println(e);
/* 110 */       System.exit(1);
/*     */     }
/*     */ 
/* 113 */     progress += 9;
/* 114 */     splash.setProgress("Checking start options", progress);
/*     */     try
/*     */     {
/* 117 */       String auth = null;
/* 118 */       needGameUpdate = args; verstring = args.length; for (updateInfoArray = 0; updateInfoArray < verstring; updateInfoArray++) { String arg = needGameUpdate[updateInfoArray];
/* 119 */         if (arg.startsWith("-")) {
/* 120 */           if ((arg.equalsIgnoreCase("-p")) || (arg.equalsIgnoreCase("--portable")))
/* 121 */             portableMode = true;
/*     */         }
/*     */         else {
/* 124 */           auth = arg;
/*     */         }
/*     */       }
/*     */ 
/* 128 */       progress += 9;
/* 129 */       splash.setProgress("Starting Proxy", progress);
/*     */ 
/* 131 */       MineProxy.listen(proxyPort, proxyHTTPPort, proxyHTTPSPort, VERSION, auth);
/*     */ 
/* 134 */       progress += 9;
/* 135 */       splash.setProgress("Starting Launcher", progress);
/* 136 */       startLauncher();
/*     */ 
/* 138 */       System.setProperty("http.proxyHost", "127.0.0.1");
/* 139 */       System.setProperty("http.proxyPort", Integer.toString(proxyPort));
/* 140 */       System.setProperty("https.proxyHost", "127.0.0.1");
/* 141 */       System.setProperty("https.proxyPort", Integer.toString(proxyPort));
/*     */     }
/*     */     catch (Exception e) {
/* 144 */       System.out.println("Something bad happened:");
/* 145 */       System.exit(1);
/*     */     } }
/*     */ 
/*     */   private static void rDelete(File root)
/*     */   {
/* 150 */     if (root.isDirectory()) {
/* 151 */       for (File file : root.listFiles()) {
/* 152 */         rDelete(file);
/*     */       }
/* 154 */       root.delete();
/*     */     } else {
/* 156 */       root.delete();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void splashScreenInit() {
/* 161 */     ImageIcon myImage = new ImageIcon(MineClient.class.getResource("splashimg.png"));
/* 162 */     splash = new SplashScreen(myImage);
/* 163 */     splash.setLocationRelativeTo(null);
/* 164 */     splash.setProgressMax(100);
/* 165 */     splash.setProgress("Loading...", progress);
/* 166 */     splash.setScreenVisible(true);
/*     */   }
/*     */ 
/*     */   private static void splashScreenDestruct() {
/* 170 */     splash.setScreenVisible(false);
/* 171 */     splash = null;
/*     */   }
/*     */ 
/*     */   private static String getGameBuildNumber()
/*     */   {
/* 176 */     String versionPath = null;
/*     */ 
/* 178 */     String os = System.getProperty("os.name").toLowerCase();
/* 179 */     Map enviornment = System.getenv();
/*     */ 
/* 181 */     if (os.contains("windows")) {
/* 182 */       versionPath = (String)enviornment.get("APPDATA") + "\\.minecraft\\bin\\version";
/* 183 */       gamePath = (String)enviornment.get("APPDATA") + "\\.minecraft\\bin";
/* 184 */     } else if (os.contains("mac")) {
/* 185 */       versionPath = "/Users/" + (String)enviornment.get("USER") + "/Library/Application Support/minecraft/bin/version";
/* 186 */       gamePath = "/Users/" + (String)enviornment.get("USER") + "/Library/Application Support/minecraft/bin";
/* 187 */     } else if (os.contains("linux")) {
/* 188 */       versionPath = (String)enviornment.get("HOME") + "/.minecraft/bin/version";
/* 189 */       gamePath = (String)enviornment.get("HOME") + "/.minecraft/bin";
/*     */     }
/*     */ 
/* 193 */     if (new File(versionPath).exists()) {
/* 194 */       return Resources.loadString(versionPath).trim();
/*     */     }
/* 196 */     return "0";
/*     */   }
/*     */ 
/*     */   public static void startLauncher()
/*     */   {
/*     */     try
/*     */     {
/* 203 */       progress += 9;
/* 204 */       splash.setProgress("hack launcher", progress);
/*     */ 
/* 206 */       if ((new File(hackedLauncherFilename).exists()) && (launcherModified)) {
/* 207 */         URL u = new File(hackedLauncherFilename).toURI().toURL();
/* 208 */         URLClassLoader cl = new URLClassLoader(new URL[] { u });
/*     */ 
/* 210 */         Class launcherFrame = cl.loadClass("net.minecraft.LauncherFrame");
/* 211 */         Class utilClass = cl.loadClass("net.minecraft.Util");
/* 212 */         Field portable = utilClass.getDeclaredField("portable");
/* 213 */         portable.setBoolean(null, portableMode);
/* 214 */         Method main = launcherFrame.getMethod("main", new Class[] { [Ljava.lang.String.class });
/*     */ 
/* 216 */         progress += 9;
/* 217 */         splash.setProgress("Starting Launcher", progress);
/* 218 */         main.invoke(launcherFrame, new Object[] { new String[0] });
/*     */ 
/* 220 */         progress += 9;
/* 221 */         splash.setProgress("Don't Login Yet!", progress);
/* 222 */         splash.setAlwaysOnTop(true);
/* 223 */         Thread.sleep(3500L);
/* 224 */         progress = 100;
/* 225 */         splash.setProgress("Okay, Now Login", progress);
/* 226 */         Thread.sleep(500L);
/* 227 */         splashScreenDestruct();
/*     */       }
/* 231 */       else if (new File(hackedLauncherFilename).exists()) {
/* 232 */         progress += 9;
/* 233 */         splash.setProgress(progress);
/* 234 */         new File(hackedLauncherFilename).delete();
/* 235 */         editLauncher();
/* 236 */         launcherModified = true;
/* 237 */         startLauncher();
/*     */       }
/* 240 */       else if (new File(normalLauncherFilename).exists()) {
/* 241 */         progress += 9;
/* 242 */         splash.setProgress(progress);
/* 243 */         editLauncher();
/* 244 */         startLauncher();
/*     */       }
/*     */       else {
/* 247 */         progress += 9;
/* 248 */         splash.setProgress(progress);
/*     */         try {
/* 250 */           InputStream in = new URL(launcherDownloadURL).openStream();
/* 251 */           OutputStream out = new FileOutputStream(normalLauncherFilename);
/* 252 */           Streams.pipeStreams(in, out);
/* 253 */           in.close();
/* 254 */           out.close();
/* 255 */           editLauncher();
/* 256 */           startLauncher();
/*     */         } catch (Exception ex) {
/* 258 */           System.out.println("Error downloading launcher:");
/* 259 */           return;
/*     */         }
/*     */       }
/*     */     } catch (Exception e1) {
/* 263 */       System.out.println("Error starting launcher:");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void editLauncher() {
/*     */     try {
/* 269 */       ZipInputStream in = new ZipInputStream(new FileInputStream(normalLauncherFilename));
/* 270 */       ZipOutputStream out = new ZipOutputStream(new FileOutputStream(hackedLauncherFilename));
/*     */       ZipEntry entry;
/* 274 */       while ((entry = in.getNextEntry()) != null)
/*     */       {
/*     */         ZipEntry entry;
/* 276 */         String n = entry.getName();
/*     */ 
/* 278 */         if ((!n.contains(".svn")) && (!n.equals("META-INF/MOJANG_C.SF")) && (!n.equals("META-INF/MOJANG_C.DSA")) && (!n.equals("net/minecraft/minecraft.key")) && (!n.equals("net/minecraft/Util$OS.class")))
/*     */         {
/* 282 */           out.putNextEntry(entry);
/*     */           InputStream dataSource;
/*     */           InputStream dataSource;
/* 284 */           if (n.equals("META-INF/MANIFEST.MF")) {
/* 285 */             dataSource = Resources.load("manifest.txt");
/*     */           }
/*     */           else
/*     */           {
/*     */             InputStream dataSource;
/* 286 */             if (n.equals("net/minecraft/Util.class"))
/* 287 */               dataSource = Resources.load("Util.class");
/*     */             else {
/* 289 */               dataSource = in;
/*     */             }
/*     */           }
/* 292 */           Streams.pipeStreams(dataSource, out);
/* 293 */           out.flush();
/*     */         }
/*     */       }
/* 296 */       in.close();
/* 297 */       out.close();
/*     */     } catch (Exception e) {
/* 299 */       System.out.println("Editing launcher failed:");
/*     */     }
/*     */   }
/*     */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.MineClient
 * JD-Core Version:    0.6.2
 */