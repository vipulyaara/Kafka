//
///* Decompiler 58ms, total 263ms, lines 488 */
//package com.kafka.reader;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.content.res.Configuration;
//import android.graphics.DashPathEffect;
//import android.graphics.Paint;
//import android.graphics.PointF;
//import android.graphics.RectF;
//import android.graphics.Paint.Style;
//import android.util.Log;
//import android.view.MotionEvent;
//import androidx.annotation.Keep;
//import androidx.annotation.NonNull;
//import com.pdftron.common.PDFNetException;
//import com.pdftron.pdf.Annot;
//import com.pdftron.pdf.PDFDoc;
//import com.pdftron.pdf.PDFViewCtrl;
//import com.pdftron.pdf.Page;
//import com.pdftron.pdf.Rect;
//import com.pdftron.pdf.PDFViewCtrl.PriorEventMode;
//import com.pdftron.pdf.annots.Line;
//import com.pdftron.pdf.annots.Markup;
//import com.pdftron.pdf.config.ToolStyleConfig;
//import com.pdftron.pdf.model.AnnotStyle;
//import com.pdftron.pdf.model.LineEndingStyle;
//import com.pdftron.pdf.model.LineStyle;
//import com.pdftron.pdf.model.ShapeBorderStyle;
//import com.pdftron.pdf.tools.SimpleShapeCreate.1;
//import com.pdftron.pdf.tools.ToolManager.ToolMode;
//import com.pdftron.pdf.tools.ToolManager.ToolModeBase;
//import com.pdftron.pdf.utils.AnalyticsHandlerAdapter;
//import com.pdftron.pdf.utils.AnnotUtils;
//import com.pdftron.pdf.utils.DrawingUtils;
//import com.pdftron.pdf.utils.Utils;
//
//@Keep
//public abstract class SimpleShapeCreate extends BaseTool {
//   public static final int sTapToCreateHalfWidth = 50;
//   private static final String TAG = SimpleShapeCreate.class.getName();
//   protected PointF mPt1;
//   protected PointF mPt2;
//   protected Paint mPaint;
//   protected Paint mFillPaint;
//   protected int mDownPageNum;
//   protected RectF mPageCropOnClientF;
//   protected float mThickness;
//   protected float mThicknessDraw;
//   protected int mStrokeColor;
//   protected int mFillColor;
//   protected float mOpacity;
//   protected boolean mIsAllPointsOutsidePage;
//   protected boolean mHasFill;
//   protected boolean mHasBorderStyle;
//   protected boolean mHasLineStyle;
//   protected boolean mHasLineStartStyle;
//   protected boolean mHasLineEndStyle;
//   protected double mZoom;
//   protected ShapeBorderStyle mBorderStyle;
//   protected LineStyle mLineStyle;
//   protected LineEndingStyle mLineStartStyle;
//   protected LineEndingStyle mLineEndStyle;
//   protected boolean mPageBoundaryRestricted = true;
//   protected DashPathEffect mDashPathEffect = null;
//   protected boolean mSkipAfterQuickMenuClose;
//   protected int mTapToCreateShapeHalfSize;
//   protected boolean mOnUpCalled;
//
//   public SimpleShapeCreate(@NonNull PDFViewCtrl ctrl) {
//      super(ctrl);
//      this.mZoom = ctrl.getZoom();
//      this.mPt1 = new PointF(0.0F, 0.0F);
//      this.mPt2 = new PointF(0.0F, 0.0F);
//      this.mPaint = new Paint();
//      this.mPaint.setAntiAlias(true);
//      this.mPaint.setColor(-16776961);
//      this.mPaint.setStyle(Style.STROKE);
//      this.mFillPaint = new Paint(this.mPaint);
//      this.mFillPaint.setStyle(Style.FILL);
//      this.mFillPaint.setColor(0);
//      this.mThickness = 1.0F;
//      this.mThicknessDraw = 1.0F;
//      this.mHasFill = false;
//      this.mTapToCreateShapeHalfSize = (int)Utils.convDp2Pix(this.mPdfViewCtrl.getContext(), (float)((ToolManager)this.mPdfViewCtrl.getToolManager()).getTapToCreateShapeHalfWidth());
//      this.mDashPathEffect = DrawingUtils.getDashPathEffect(this.mPdfViewCtrl.getContext());
//   }
//
//   public abstract ToolModeBase getToolMode();
//
//   public abstract int getCreateAnnotType();
//
//   public void setupAnnotProperty(AnnotStyle annotStyle) {
//      super.setupAnnotProperty(annotStyle);
//      this.mStrokeColor = annotStyle.getColor();
//      this.mFillColor = annotStyle.getFillColor();
//      this.mOpacity = annotStyle.getOpacity();
//      this.mThickness = annotStyle.getThickness();
//      this.mHasBorderStyle = annotStyle.hasBorderStyle();
//      this.mHasLineStyle = annotStyle.hasLineStyle();
//      this.mHasLineStartStyle = annotStyle.hasLineStartStyle();
//      this.mHasLineEndStyle = annotStyle.hasLineEndStyle();
//      this.mBorderStyle = annotStyle.getBorderStyle();
//      this.mLineStyle = annotStyle.getLineStyle();
//      this.mLineStartStyle = annotStyle.getLineStartStyle();
//      this.mLineEndStyle = annotStyle.getLineEndStyle();
//      SharedPreferences settings = Tool.getToolPreferences(this.mPdfViewCtrl.getContext());
//      Editor editor = settings.edit();
//      editor.putInt(this.getColorKey(this.getCreateAnnotType()), this.mStrokeColor);
//      editor.putInt(this.getColorFillKey(this.getCreateAnnotType()), this.mFillColor);
//      editor.putFloat(this.getOpacityKey(this.getCreateAnnotType()), this.mOpacity);
//      editor.putFloat(this.getThicknessKey(this.getCreateAnnotType()), this.mThickness);
//      if (this.mBorderStyle != null) {
//         editor.putString(this.getBorderStyleKey(this.getCreateAnnotType()), this.mBorderStyle.name());
//      }
//
//      if (this.mLineStyle != null) {
//         editor.putString(this.getLineStyleKey(this.getCreateAnnotType()), this.mLineStyle.name());
//      }
//
//      if (this.mLineStartStyle != null) {
//         editor.putString(this.getLineStartStyleKey(this.getCreateAnnotType()), this.mLineStartStyle.name());
//      }
//
//      if (this.mLineEndStyle != null) {
//         editor.putString(this.getLineEndStyleKey(this.getCreateAnnotType()), this.mLineEndStyle.name());
//      }
//
//      editor.apply();
//   }
//
//   public boolean isCreatingAnnotation() {
//      return true;
//   }
//
//   public void onConfigurationChanged(Configuration newConfig) {
//      this.resetPts();
//      this.mPdfViewCtrl.invalidate();
//   }
//
//   public boolean onSingleTapConfirmed(MotionEvent e) {
//      ToolModeBase toolMode = ToolManager.getDefaultToolModeBase(this.getToolMode());
//      if (this.tapToSelectAllowed()) {
//         int x = (int)((double)e.getX() + 0.5D);
//         int y = (int)((double)e.getY() + 0.5D);
//         Annot tempAnnot = ((ToolManager)this.mPdfViewCtrl.getToolManager()).getAnnotationAt(x, y);
//         int page = this.mPdfViewCtrl.getPageNumberFromScreenPt((double)x, (double)y);
//         this.setCurrentDefaultToolModeHelper(toolMode);
//
//         try {
//            if (this.isAnnotSupportEdit(tempAnnot)) {
//               ((ToolManager)this.mPdfViewCtrl.getToolManager()).selectAnnot(tempAnnot, page);
//            } else {
//               this.mNextToolMode = this.getToolMode();
//            }
//         } catch (PDFNetException var8) {
//         }
//
//         return false;
//      } else {
//         return super.onSingleTapConfirmed(e);
//      }
//   }
//
//   protected boolean tapToSelectAllowed() {
//      ToolMode toolMode = ToolManager.getDefaultToolMode(this.getToolMode());
//      return this.mForceSameNextToolMode && toolMode != ToolMode.INK_CREATE && toolMode != ToolMode.SMART_PEN_INK && toolMode != ToolMode.INK_ERASER && toolMode != ToolMode.TEXT_ANNOT_CREATE;
//   }
//
//   public boolean onDown(MotionEvent e) {
//      super.onDown(e);
//      this.mOnUpCalled = true;
//      ToolManager toolManager = (ToolManager)this.mPdfViewCtrl.getToolManager();
//      this.mSkipAfterQuickMenuClose = toolManager.isQuickMenuJustClosed();
//      PointF snapPoint = this.snapToNearestIfEnabled(new PointF(e.getX(), e.getY()));
//      this.mPt1.x = snapPoint.x + (float)this.mPdfViewCtrl.getScrollX();
//      this.mPt1.y = snapPoint.y + (float)this.mPdfViewCtrl.getScrollY();
//      this.mDownPageNum = this.mPdfViewCtrl.getPageNumberFromScreenPt((double)e.getX(), (double)e.getY());
//      if (this.mDownPageNum < 1) {
//         this.mIsAllPointsOutsidePage = true;
//         this.mDownPageNum = this.mPdfViewCtrl.getCurrentPage();
//      } else {
//         this.mIsAllPointsOutsidePage = false;
//      }
//
//      if (this.mDownPageNum >= 1) {
//         this.mPageCropOnClientF = Utils.buildPageBoundBoxOnClient(this.mPdfViewCtrl, this.mDownPageNum);
//         if (this.mPageBoundaryRestricted) {
//            Utils.snapPointToRect(this.mPt1, this.mPageCropOnClientF);
//         }
//      }
//
//      this.mPt2.set(this.mPt1);
//      Context context = this.mPdfViewCtrl.getContext();
//      SharedPreferences settings = Tool.getToolPreferences(context);
//      this.mThickness = settings.getFloat(this.getThicknessKey(this.getCreateAnnotType()), ToolStyleConfig.getInstance().getDefaultThickness(context, this.getCreateAnnotType()));
//      this.mStrokeColor = settings.getInt(this.getColorKey(this.getCreateAnnotType()), ToolStyleConfig.getInstance().getDefaultColor(context, this.getCreateAnnotType()));
//      this.mFillColor = settings.getInt(this.getColorFillKey(this.getCreateAnnotType()), ToolStyleConfig.getInstance().getDefaultFillColor(context, this.getCreateAnnotType()));
//      this.mOpacity = settings.getFloat(this.getOpacityKey(this.getCreateAnnotType()), ToolStyleConfig.getInstance().getDefaultOpacity(context, this.getCreateAnnotType()));
//      String lineEndStyle;
//      if (this.mHasBorderStyle) {
//         lineEndStyle = settings.getString(this.getBorderStyleKey(this.getCreateAnnotType()), ToolStyleConfig.getInstance().getDefaultBorderStyle(context, this.getCreateAnnotType()).name());
//         this.mBorderStyle = ShapeBorderStyle.valueOf(lineEndStyle);
//      } else if (this.mHasLineStyle) {
//         lineEndStyle = settings.getString(this.getLineStyleKey(this.getCreateAnnotType()), ToolStyleConfig.getInstance().getDefaultLineStyle(context, this.getCreateAnnotType()).name());
//         this.mLineStyle = LineStyle.valueOf(lineEndStyle);
//      }
//
//      if (this.mHasLineStartStyle) {
//         lineEndStyle = settings.getString(this.getLineStartStyleKey(this.getCreateAnnotType()), ToolStyleConfig.getInstance().getDefaultLineStartStyle(context, this.getCreateAnnotType()).name());
//         this.mLineStartStyle = LineEndingStyle.valueOf(lineEndStyle);
//      }
//
//      if (this.mHasLineEndStyle) {
//         lineEndStyle = settings.getString(this.getLineEndStyleKey(this.getCreateAnnotType()), ToolStyleConfig.getInstance().getDefaultLineEndStyle(context, this.getCreateAnnotType()).name());
//         this.mLineEndStyle = LineEndingStyle.valueOf(lineEndStyle);
//      }
//
//      float zoom = (float)this.mPdfViewCtrl.getZoom();
//      this.mThicknessDraw = this.mThickness * zoom;
//      this.mPaint.setStrokeWidth(this.mThicknessDraw);
//      int color = Utils.getPostProcessedColor(this.mPdfViewCtrl, this.mStrokeColor);
//      this.mPaint.setColor(color);
//      this.mPaint.setAlpha((int)(255.0F * this.mOpacity));
//      if (this.mHasFill) {
//         this.mFillPaint.setColor(Utils.getPostProcessedColor(this.mPdfViewCtrl, this.mFillColor));
//         this.mFillPaint.setAlpha((int)(255.0F * this.mOpacity));
//      }
//
//      this.mAnnotPushedBack = false;
//      return false;
//   }
//
//   public boolean onMove(MotionEvent e1, MotionEvent e2, float x_dist, float y_dist) {
//      super.onMove(e1, e2, x_dist, y_dist);
//      if (this.mAllowTwoFingerScroll) {
//         return false;
//      } else if (this.mAllowOneFingerScrollWithStylus) {
//         return false;
//      } else {
//         if (this.mIsAllPointsOutsidePage && this.mPdfViewCtrl.getPageNumberFromScreenPt((double)e2.getX(), (double)e2.getY()) >= 1) {
//            this.mIsAllPointsOutsidePage = false;
//         }
//
//         this.mSkipAfterQuickMenuClose = false;
//         float x = this.mPt2.x;
//         float y = this.mPt2.y;
//         PointF snapPoint = this.snapToNearestIfEnabled(new PointF(e2.getX(), e2.getY()));
//         this.mPt2.x = snapPoint.x + (float)this.mPdfViewCtrl.getScrollX();
//         this.mPt2.y = snapPoint.y + (float)this.mPdfViewCtrl.getScrollY();
//         if (this.mPageBoundaryRestricted) {
//            Utils.snapPointToRect(this.mPt2, this.mPageCropOnClientF);
//         }
//
//         float min_x = Math.min(Math.min(x, this.mPt2.x), this.mPt1.x) - this.mThicknessDraw;
//         float max_x = Math.max(Math.max(x, this.mPt2.x), this.mPt1.x) + this.mThicknessDraw;
//         float min_y = Math.min(Math.min(y, this.mPt2.y), this.mPt1.y) - this.mThicknessDraw;
//         float max_y = Math.max(Math.max(y, this.mPt2.y), this.mPt1.y) + this.mThicknessDraw;
//         this.mPdfViewCtrl.invalidate((int)min_x, (int)min_y, (int)Math.ceil((double)max_x), (int)Math.ceil((double)max_y));
//         return true;
//      }
//   }
//
//   public boolean onUp(MotionEvent e, PriorEventMode priorEventMode) {
//      super.onUp(e, priorEventMode);
//      if (!this.mOnUpCalled) {
//         return false;
//      } else {
//         this.mOnUpCalled = false;
//         if (this.mAllowTwoFingerScroll) {
//            this.doneTwoFingerScrolling();
//            return false;
//         } else if (this.mSkipAfterQuickMenuClose) {
//            this.resetPts();
//            return true;
//         } else if (priorEventMode == PriorEventMode.PAGE_SLIDING) {
//            return false;
//         } else if (this.mAnnotPushedBack && this.mForceSameNextToolMode) {
//            return true;
//         } else if (this.mIsAllPointsOutsidePage) {
//            return true;
//         } else {
//            this.mAllowOneFingerScrollWithStylus = this.mStylusUsed && e.getToolType(0) != 2;
//            if (this.mAllowOneFingerScrollWithStylus) {
//               return true;
//            } else {
//               boolean canCreate;
//               if (this.mPt1.x == this.mPt2.x && this.mPt1.y == this.mPt2.y) {
//                  canCreate = false;
//                  if (this.canTapToCreate()) {
//                     int x = (int)((double)e.getX() + 0.5D);
//                     int y = (int)((double)e.getY() + 0.5D);
//                     boolean shouldUnlockRead = false;
//
//                     try {
//                        this.mPdfViewCtrl.docLockRead();
//                        shouldUnlockRead = true;
//                        Annot tempAnnot = ((ToolManager)this.mPdfViewCtrl.getToolManager()).getAnnotationAt(x, y);
//                        canCreate = tempAnnot == null || !tempAnnot.isValid();
//                     } catch (Exception var20) {
//                     } finally {
//                        if (shouldUnlockRead) {
//                           this.mPdfViewCtrl.docUnlockRead();
//                        }
//
//                     }
//
//                     if (canCreate) {
//                        PointF var10000 = this.mPt1;
//                        var10000.x -= (float)this.mTapToCreateShapeHalfSize;
//                        var10000 = this.mPt1;
//                        var10000.y -= (float)this.mTapToCreateShapeHalfSize;
//                        var10000 = this.mPt2;
//                        var10000.x += (float)this.mTapToCreateShapeHalfSize;
//                        var10000 = this.mPt2;
//                        var10000.y += (float)this.mTapToCreateShapeHalfSize;
//                        if (this.mPageBoundaryRestricted) {
//                           Utils.snapPointToRect(this.mPt1, this.mPageCropOnClientF);
//                           Utils.snapPointToRect(this.mPt2, this.mPageCropOnClientF);
//                        }
//                     }
//                  }
//
//                  if (!canCreate) {
//                     this.resetPts();
//                     return true;
//                  }
//               }
//
//               this.setNextToolModeHelper();
//               this.setCurrentDefaultToolModeHelper(this.getToolMode());
//               this.addOldTools();
//               canCreate = false;
//
//               try {
//                  this.mPdfViewCtrl.docLock(true);
//                  canCreate = true;
//                  Rect rect = this.getShapeBBox();
//                  if (rect != null) {
//                     Annot markup = this.createMarkup(this.mPdfViewCtrl.getDoc(), rect);
//                     this.setStyle(markup);
//                     markup.refreshAppearance();
//                     Page page = this.mPdfViewCtrl.getDoc().getPage(this.mDownPageNum);
//                     if (page != null) {
//                        page.annotPushBack(markup);
//                        this.mAnnotPushedBack = true;
//                        this.setAnnot(markup, this.mDownPageNum);
//                        this.buildAnnotBBox();
//                        this.mPdfViewCtrl.update(this.mAnnot, this.mAnnotPageNum);
//                        this.raiseAnnotationAddedEvent(this.mAnnot, this.mAnnotPageNum);
//                     }
//                  }
//               } catch (Exception var18) {
//                  this.mNextToolMode = ToolMode.PAN;
//                  ((ToolManager)this.mPdfViewCtrl.getToolManager()).annotationCouldNotBeAdded(var18.getMessage());
//                  AnalyticsHandlerAdapter.getInstance().sendException(var18);
//                  this.onCreateMarkupFailed(var18);
//               } finally {
//                  if (canCreate) {
//                     this.mPdfViewCtrl.docUnlock();
//                  }
//
//               }
//
//               return this.skipOnUpPriorEvent(priorEventMode);
//            }
//         }
//      }
//   }
//
//   protected void doneTwoFingerScrolling() {
//      super.doneTwoFingerScrolling();
//      this.mPt2.set(this.mPt1);
//      this.mPdfViewCtrl.invalidate();
//   }
//
//   public boolean onFlingStop() {
//      if (this.mAllowTwoFingerScroll) {
//         this.doneTwoFingerScrolling();
//      }
//
//      return false;
//   }
//
//   public boolean onScaleBegin(float x, float y) {
//      return false;
//   }
//
//   protected Rect getShapeBBox() {
//      double[] pts1 = this.mPdfViewCtrl.convScreenPtToPagePt((double)(this.mPt1.x - (float)this.mPdfViewCtrl.getScrollX()), (double)(this.mPt1.y - (float)this.mPdfViewCtrl.getScrollY()), this.mDownPageNum);
//      double[] pts2 = this.mPdfViewCtrl.convScreenPtToPagePt((double)(this.mPt2.x - (float)this.mPdfViewCtrl.getScrollX()), (double)(this.mPt2.y - (float)this.mPdfViewCtrl.getScrollY()), this.mDownPageNum);
//
//      try {
//         Rect rect = new Rect(pts1[0], pts1[1], pts2[0], pts2[1]);
//         return rect;
//      } catch (Exception var5) {
//         return null;
//      }
//   }
//
//   protected void setStyle(Annot annot) {
//      this.setStyle(annot, this.mHasFill);
//   }
//
//   protected void setStyle(Annot annot, boolean hasFill) {
//      try {
//         if (annot.isMarkup()) {
//            Markup markup = new Markup(annot);
//            this.setAuthor(markup);
//            double[] dash = DrawingUtils.getShapesDashIntervals();
//            if (this.mHasBorderStyle) {
//               switch(1.$SwitchMap$com$pdftron$pdf$model$ShapeBorderStyle[this.mBorderStyle.ordinal()]) {
//               case 1:
//                  AnnotUtils.setBorderStyle(annot, 1, 0, (double[])null);
//                  break;
//               case 2:
//                  AnnotUtils.setBorderStyle(annot, 0, 1, dash);
//                  break;
//               case 3:
//                  AnnotUtils.setBorderStyle(annot, 0, 0, (double[])null);
//               }
//            } else if (this.mHasLineStyle) {
//               switch(1.$SwitchMap$com$pdftron$pdf$model$LineStyle[this.mLineStyle.ordinal()]) {
//               case 1:
//                  AnnotUtils.setBorderStyle(annot, 0, 1, dash);
//                  break;
//               case 2:
//                  AnnotUtils.setBorderStyle(annot, 0, 0, (double[])null);
//               }
//            }
//
//            if (annot.getType() == 3 || annot.getType() == 7) {
//               Line lineAnnot = new Line(annot);
//               if (this.mHasLineStartStyle) {
//                  AnnotUtils.setLineEndingStyle(lineAnnot, this.mLineStartStyle, true);
//               }
//
//               if (this.mHasLineEndStyle) {
//                  AnnotUtils.setLineEndingStyle(lineAnnot, this.mLineEndStyle, false);
//               }
//            }
//         }
//
//         AnnotUtils.setStyle(annot, hasFill, this.mStrokeColor, this.mFillColor, this.mThickness, this.mOpacity);
//      } catch (PDFNetException var6) {
//         var6.printStackTrace();
//      }
//
//   }
//
//   protected abstract Annot createMarkup(@NonNull PDFDoc doc, Rect bbox) throws PDFNetException;
//
//   protected boolean canTapToCreate() {
//      return false;
//   }
//
//   protected void resetPts() {
//      this.mPt1.set(0.0F, 0.0F);
//      this.mPt2.set(0.0F, 0.0F);
//   }
//
//   protected void setNextToolModeHelper() {
//      ToolManager toolManager = (ToolManager)this.mPdfViewCtrl.getToolManager();
//      if (toolManager.isAutoSelectAnnotation()) {
//         this.mNextToolMode = this.getDefaultNextTool();
//      } else {
//         this.mNextToolMode = (ToolModeBase)(this.mForceSameNextToolMode ? this.getToolMode() : ToolMode.PAN);
//      }
//
//   }
//
//   protected ToolMode getDefaultNextTool() {
//      return ToolMode.ANNOT_EDIT;
//   }
//
//   protected void onCreateMarkupFailed(Exception e) {
//      Log.e(TAG, "onCreateMarkupFailed", e);
//   }
//
//   protected boolean canDrawLoupe() {
//      return false;
//   }
//
//   protected int getLoupeType() {
//      return 1;
//   }
//}
