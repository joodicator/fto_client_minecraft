/*    */ package mineshafter.util;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Color;
/*    */ import java.awt.Container;
/*    */ import java.awt.FlowLayout;
/*    */ import javax.swing.ImageIcon;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JProgressBar;
/*    */ import javax.swing.JWindow;
/*    */ import javax.swing.SwingUtilities;
/*    */ 
/*    */ public final class SplashScreen extends JWindow
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*  9 */   BorderLayout borderLayout1 = new BorderLayout();
/* 10 */   JLabel imageLabel = new JLabel();
/* 11 */   JPanel southPanel = new JPanel();
/* 12 */   FlowLayout southPanelFlowLayout = new FlowLayout();
/* 13 */   JProgressBar progressBar = new JProgressBar();
/*    */   ImageIcon imageIcon;
/*    */ 
/*    */   public SplashScreen(ImageIcon imageIcon)
/*    */   {
/* 17 */     this.imageIcon = imageIcon;
/*    */     try {
/* 19 */       jbInit();
/*    */     }
/*    */     catch (Exception localException) {
/*    */     }
/*    */   }
/*    */ 
/*    */   public void jbInit() throws Exception {
/* 26 */     this.imageLabel.setIcon(this.imageIcon);
/* 27 */     getContentPane().setLayout(this.borderLayout1);
/* 28 */     this.southPanel.setLayout(this.southPanelFlowLayout);
/* 29 */     this.southPanel.setBackground(new Color(72, 82, 106));
/* 30 */     getContentPane().add(this.imageLabel, "Center");
/* 31 */     getContentPane().add(this.southPanel, "South");
/* 32 */     this.southPanel.add(this.progressBar, null);
/* 33 */     pack();
/* 34 */     toFront();
/*    */   }
/*    */ 
/*    */   public void setProgressMax(int maxProgress) {
/* 38 */     this.progressBar.setMaximum(maxProgress);
/*    */   }
/*    */ 
/*    */   public void setProgress(int progress) {
/* 42 */     final int theProgress = progress;
/* 43 */     SwingUtilities.invokeLater(new Runnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 47 */         SplashScreen.this.progressBar.setValue(theProgress);
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   public void setProgress(String message, int progress) {
/* 53 */     final int theProgress = progress;
/* 54 */     final String theMessage = message;
/* 55 */     setProgress(progress);
/* 56 */     SwingUtilities.invokeLater(new Runnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 60 */         SplashScreen.this.progressBar.setValue(theProgress);
/* 61 */         SplashScreen.this.setMessage(theMessage);
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   public void setScreenVisible(boolean b) {
/* 67 */     final boolean boo = b;
/* 68 */     SwingUtilities.invokeLater(new Runnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 72 */         SplashScreen.this.setVisible(boo);
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   private void setMessage(String message) {
/* 78 */     if (message == null) {
/* 79 */       message = "";
/* 80 */       this.progressBar.setStringPainted(false);
/*    */     } else {
/* 82 */       this.progressBar.setStringPainted(true);
/*    */     }
/* 84 */     this.progressBar.setString(message);
/*    */   }
/*    */ }

/* Location:           /home/joseph/mc/fto_client_src/
 * Qualified Name:     mineshafter.util.SplashScreen
 * JD-Core Version:    0.6.2
 */