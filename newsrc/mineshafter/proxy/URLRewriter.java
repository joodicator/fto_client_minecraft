/*    */ package mineshafter.proxy;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class URLRewriter
/*    */ {
/*    */   public Pattern matcher;
/*    */   public String replacer;
/*    */ 
/*    */   public URLRewriter(String matcher, String replacer)
/*    */   {
/* 13 */     this.matcher = Pattern.compile(matcher);
/* 14 */     this.replacer = replacer;
/*    */   }
/*    */ 
/*    */   public String MatchAndReplace(String url) {
/* 18 */     Matcher m = this.matcher.matcher(url);
/* 19 */     if (m.matches()) {
/* 20 */       int c = m.groupCount();
/*    */ 
/* 22 */       String[] strings = new String[c];
/*    */ 
/* 24 */       for (int i = 0; i < c; i++) {
/* 25 */         strings[i] = m.group(i + 1);
/*    */       }
/*    */ 
/* 28 */       String r = String.format(this.replacer, strings);
/*    */ 
/* 32 */       if (r.contains(".jsp")) {
/* 33 */         r = r.replace(".jsp", ".php");
/*    */       }
/*    */ 
/* 36 */       return r;
/*    */     }
/* 38 */     return null;
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.proxy.URLRewriter
 * JD-Core Version:    0.6.2
 */
