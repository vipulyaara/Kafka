//
///* Decompiler 4ms, total 634ms, lines 59 */
//package com.kafka.reader;
//
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Path;
//import androidx.annotation.Keep;
//import androidx.annotation.NonNull;
//import com.pdftron.common.PDFNetException;
//import com.pdftron.pdf.Annot;
//import com.pdftron.pdf.PDFDoc;
//import com.pdftron.pdf.PDFViewCtrl;
//import com.pdftron.pdf.Rect;
//import com.pdftron.pdf.annots.Square;
//import com.pdftron.pdf.model.ShapeBorderStyle;
//import com.pdftron.pdf.tools.ToolManager.ToolMode;
//import com.pdftron.pdf.tools.ToolManager.ToolModeBase;
//import com.pdftron.pdf.utils.DrawingUtils;
//
//@Keep
//public class RectCreate extends SimpleShapeCreate {
//   private Path mOnDrawPath = new Path();
//
//   public RectCreate(@NonNull PDFViewCtrl ctrl) {
//      super(ctrl);
//      this.mNextToolMode = this.getToolMode();
//      this.mHasFill = true;
//      this.mHasBorderStyle = true;
//   }
//
//   public ToolModeBase getToolMode() {
//      return ToolMode.RECT_CREATE;
//   }
//
//   public int getCreateAnnotType() {
//      return 4;
//   }
//
//   protected Annot createMarkup(@NonNull PDFDoc doc, Rect bbox) throws PDFNetException {
//      return Square.create(doc, bbox);
//   }
//
//   protected boolean canTapToCreate() {
//      return true;
//   }
//
//   public void onDraw(Canvas canvas, Matrix tfm) {
//      if (!this.mAllowTwoFingerScroll) {
//         if (!this.mSkipAfterQuickMenuClose) {
//            if (this.mBorderStyle == ShapeBorderStyle.CLOUDY) {
//               DrawingUtils.drawCloudyRectangle(this.mPdfViewCtrl, this.mDownPageNum, canvas, this.mOnDrawPath, this.mPt1, this.mPt2, this.mFillColor, this.mStrokeColor, this.mFillPaint, this.mPaint, 2.0D);
//            } else {
//               DrawingUtils.drawRectangle(canvas, this.mPt1, this.mPt2, this.mThicknessDraw, this.mFillColor, this.mStrokeColor, this.mFillPaint, this.mPaint, this.mBorderStyle == ShapeBorderStyle.DASHED ? this.mDashPathEffect : null);
//            }
//
//         }
//      }
//   }
//}
