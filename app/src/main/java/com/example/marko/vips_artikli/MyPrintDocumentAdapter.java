package com.example.marko.vips_artikli;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by rac157 on 20.2.2018.
 */
@TargetApi(19)
public class MyPrintDocumentAdapter extends PrintDocumentAdapter {

    private Paint mPaint = new Paint();
    private Context mContext;


    PrintedPdfDocument mPdfDocument;
    App1Dokumenti dokument;

    private int brojStranica;
    private int visina;
    private int sirina;

    public MyPrintDocumentAdapter(Context context, App1Dokumenti dokumentZaPrintanje) {
        mContext = context;
        dokument = dokumentZaPrintanje;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes,
                         PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback, Bundle extras) {

        mPdfDocument = new PrintedPdfDocument(mContext, newAttributes);

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }
        brojStranica = getBrojStranica(); //po broju artikala bi trebalo izračunati broj stranica zasad 1
        // newAttributes.getColorMode();
        visina = newAttributes.getMediaSize().getHeightMils();
        sirina = newAttributes.getMediaSize().getWidthMils();

        PrintDocumentInfo info = new PrintDocumentInfo.Builder("androids.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(brojStranica).build();
        callback.onLayoutFinished(info, true);
    }

    private int getBrojStranica() {
        return 1;
    }

    @Override
    public void onWrite(android.print.PageRange[] pages,
                        android.os.ParcelFileDescriptor destination,
                        CancellationSignal cancellationSignal, WriteResultCallback callback) {
        if (mPdfDocument == null) {
            Log.e("print", "error mPdfDocument is null.");
            return;
        }
        for (int i = 0; i < brojStranica; i++) {
            //if (containsPage(pageRanges, i)) {

            //}
        }
        PdfDocument.Page page = mPdfDocument.startPage(0);
        if (cancellationSignal.isCanceled()) {
            callback.onWriteCancelled();
            mPdfDocument.close();
            mPdfDocument = null;
            return;
        }
        onDraw(page.getCanvas());
        mPdfDocument.finishPage(page);

        try {
            mPdfDocument.writeTo(new FileOutputStream(destination
                    .getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            mPdfDocument.close();
            mPdfDocument = null;
        }
        callback.onWriteFinished(pages);
    }

    ;

    public void onDraw1(Canvas canvas) {
        mPaint.setTextSize(12);
        mPaint.setColor(Color.GRAY);
        int offsetX = 50;
        int offsetY = 20;

        canvas.drawText(dokument.getDatumDokumentaString(), offsetX, offsetY - mPaint.getFontMetrics().top, mPaint);
        offsetY += (-mPaint.getFontMetrics().top);
        offsetY += 40; // margin


        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(30);
        printTextCenterX(canvas, offsetY, dokument.getTipDokumentaNaziv(), mPaint);
        offsetY += (-mPaint.getFontMetrics().top);

        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(20);
        printTextCenterX(canvas, offsetY, dokument.getKomitentNaziv() + " - " + dokument.getPjKomitentNaziv(), mPaint);
        offsetY += (-mPaint.getFontMetrics().top);

        int pocetakSTavkiY = offsetY;
        int paddingX = offsetX;
        int maxX = 0;
        for (int i = 0; i < dokument.getSpisakStavki().size(); i++) {
            offsetY += 20; // margin
            mPaint.setTextSize(12);
            int mojX = paddingX;
            int sirinaTXT = printWrapText(canvas, "0" + Integer.toString(i + 1), mojX, offsetY, 18, mPaint);
            if (sirinaTXT > maxX) {
                maxX = sirinaTXT;
            }
        }

        offsetY = pocetakSTavkiY; //vrati na početak stavki
        List<App1Stavke> spisakStavki = dokument.getSpisakStavki();
        for (int i = 0; i < dokument.getSpisakStavki().size(); i++) {
            offsetY += 20; // margin
            mPaint.setTextSize(12);
            int mojX = paddingX + 20;
            int sirinaTXT = printWrapText(canvas, spisakStavki.get(i).getArtiklNaziv(), mojX, offsetY, 18, mPaint);
        }

    }

    private void onDraw(Canvas canvas) {


        // units are in points (1/72 of an inch)
        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(36);
        canvas.drawText(dokument.getTipDokumentaNaziv(), leftMargin, titleBaseLine, paint);

        titleBaseLine += 72;
        List<App1Stavke> spisakStavki = dokument.getSpisakStavki();
        paint.setTextSize(11);
        for (int i = 0; i < dokument.getSpisakStavki().size(); i++) {
            canvas.drawText(Integer.toString(i + 1), leftMargin, titleBaseLine + (i + 1) * 15, paint);
            canvas.drawText(spisakStavki.get(i).getArtiklNaziv(), leftMargin + 15, titleBaseLine + (i + 1) * 15, paint);

        }


    }

    private void printTextCenterX(Canvas canvas, int offsetY, String text,
                                  Paint paint) {
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        int width = 0;
        for (float w : widths) {
            width += w;
        }
        int offsetX = canvas.getWidth() / 2 - width / 2;
        canvas.drawText(text, offsetX, offsetY - paint.getFontMetrics().top,
                paint);
    }

    private int printWrapText(Canvas canvas, String text, int paddingX,
                              int startY, int lineSpace, Paint paint) {
        int renderWidth = canvas.getWidth() - paddingX * 2;
        int textHeight = -(int) paint.getFontMetrics().top;
        int width = 0;
        char[] c = new char[1];
        float[] w = new float[1];
        int index = 0;
        for (int i = 0; i < text.length(); i++) {
            c[0] = text.charAt(i);
            paint.getTextWidths(c, 0, 1, w);
            if (width + w[0] > renderWidth) {
                canvas.drawText(text.substring(index, i), paddingX, startY
                        + textHeight, paint);
                startY += textHeight + lineSpace;
                index = i;
                width = 0;

            } else {
                width += w[0];
            }

            if (i + 1 >= text.length()) {
                // render
                if (index != i) {
                    canvas.drawText(text.substring(index, i + 1), paddingX,
                            startY + textHeight, paint);
                    startY += textHeight + lineSpace;
                }
            }
        }
        return startY;
    }
}
