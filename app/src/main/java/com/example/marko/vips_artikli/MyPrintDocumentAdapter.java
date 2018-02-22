package com.example.marko.vips_artikli;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
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

    private String TAG = "REPORT";

    private Paint mPaint = new Paint();
    private Context mContext;


    PrintedPdfDocument mPdfDocument;
    App1Dokumenti dokument;

    private int brojStranica;
    private int visina;
    private int sirina;

    private int zadnjiY = 0;
    private int leftMargin = 20;
    private int rightMargin = 20;
    private int topMargin = 72;
    private int bottomMargin = 20;
    private int sirinaMeduprostora = 0;
    private int margineUkupno;
    private int sirinaIspisa;
    private int proredStavki = 20;

    private int brojDecimala = 1;
    private String formatString;

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
        Activity activity = (Activity) mContext;
        postavkeAplikacije myPostavke = new postavkeAplikacije(activity);
        brojDecimala = myPostavke.getBrojDecimala();
        formatString = "%." + brojDecimala + "f";


        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }
        //po broju artikala bi trebalo izračunati broj stranica zasad 1
        // newAttributes.getColorMode();
        visina = newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
        sirina = newAttributes.getMediaSize().getWidthMils() / 1000 * 72;

        margineUkupno = leftMargin + rightMargin;
        sirinaIspisa = sirina - margineUkupno;
        zadnjiY = topMargin;
        brojStranica = getUkupanBrojStranica();
        PrintDocumentInfo info = new PrintDocumentInfo.Builder("androids.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(brojStranica).build();
        callback.onLayoutFinished(info, true);
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
            if (pageInRange(pages, i)) {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(sirina, visina, i).create();

                PdfDocument.Page page = mPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    mPdfDocument.close();
                    mPdfDocument = null;
                    return;
                }
                drawPage(page.getCanvas(), i);
                mPdfDocument.finishPage(page);
            }
        }
        /*
        PdfDocument.Page page = mPdfDocument.startPage(0);
        if (cancellationSignal.isCanceled()) {
            callback.onWriteCancelled();
            mPdfDocument.close();
            mPdfDocument = null;
            return;
        }
        onDraw(page.getCanvas());
        mPdfDocument.finishPage(page);
        */
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

    private void drawPage(Canvas canvas, int stranicaBroj) {

    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i < pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) && (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }
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

    private int ukupnaVisinaReporta() {
        int ukupnaVisina = ukupnaVisinaTextaZaglavlja() + ukupnaVisinaTextaStavki() + ukupnaVisinaZaglavljaStavki() + topMargin + bottomMargin;
        return ukupnaVisina;
    }

    private int ukupnaVisinaTextaStavki() {
        List<App1Stavke> spisakStavki = dokument.getSpisakStavki();
        int visinaStavki = 0;
        for (int i = 0; i < dokument.getSpisakStavki().size(); i++) {
            visinaStavki += proredStavki;
        }
        return visinaStavki;
    }

    private int ukupnaVisinaZaglavljaStavki() {
        return proredStavki;
    }

    private int ukupnaVisinaTextaZaglavlja() {
        return 80;
    }

    private int getUkupanBrojStranica() {
        int n = 0;
        n = Math.round((ukupnaVisinaTextaZaglavlja() + ukupnaVisinaTextaStavki()) / (visina - topMargin - bottomMargin - ukupnaVisinaZaglavljaStavki()));
        return n;
        /*
        int brojStr=1;//ovo je minimalno
        int ostatak=ukupnaVisinaReporta();
        boolean radi=true;

        while (radi){
            if (brojStr==1){
                ostatak=ostatak-ukupnaVisinaTextaZaglavlja();
            }
            ostatak=ostatak-ukupnaVisinaZaglavljaStavki(); //za zaglvalje stavki
            ostatak=ostatak-topMargin-bottomMargin;
            if (ostatak>visina){
                brojStr+=1;
            }else{
                radi=false;
            }
        }

        return brojStr;
        */
    }

    private void onDraw(Canvas canvas) {
        // units are in points (1/72 of an inch)
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(18);
        canvas.drawText(dokument.getTipDokumentaNaziv(), leftMargin, zadnjiY, paint);

        zadnjiY += 20;
        paint.setTextSize(16);
        canvas.drawText("Datum dokumenta: " + dokument.getDatumDokumentaString(), leftMargin, zadnjiY, paint);
        zadnjiY += 20;
        canvas.drawText("Komitent: " + dokument.getKomitentNaziv(), leftMargin, zadnjiY, paint);
        zadnjiY += 20;
        canvas.drawText("PJ Komitenta: " + dokument.getPjKomitentNaziv(), leftMargin, zadnjiY, paint);
        zadnjiY += 20;

        List<App1Stavke> spisakStavki = dokument.getSpisakStavki();
        //prvo treba nacrtati HEADER STAVKI
        iscrtajZaglavljeStavki(canvas);
        //SADA IDU STAVKE
        for (int i = 0; i < dokument.getSpisakStavki().size(); i++) {
            int lijevo = leftMargin; //početak
            int sirinaTexta = sirinaIspisa * 5 / 100;
            Rect rectRbr = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + 20);
            drawRectText(String.valueOf(i + 1), canvas, rectRbr);

            lijevo = lijevo + sirinaTexta;
            lijevo = lijevo + sirinaMeduprostora;
            sirinaTexta = sirinaIspisa * 60 / 100;
            Rect rectNaziv = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + 20);
            drawRectText(spisakStavki.get(i).getArtiklNaziv(), canvas, rectNaziv);

            lijevo = lijevo + sirinaTexta;
            lijevo = lijevo + sirinaMeduprostora;
            sirinaTexta = sirinaIspisa * 15 / 100;
            Rect rectAtribut = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + 20);
            drawRectText(spisakStavki.get(i).getAtributVrijednost(), canvas, rectAtribut);

            lijevo = lijevo + sirinaTexta;
            lijevo = lijevo + sirinaMeduprostora;
            sirinaTexta = sirinaIspisa * 10 / 100;
            Rect rectJmj = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + 20);
            drawRectText(spisakStavki.get(i).getJmjNaziv(), canvas, rectJmj);

            lijevo = lijevo + sirinaTexta;
            lijevo = lijevo + sirinaMeduprostora;
            sirinaTexta = sirinaIspisa * 10 / 100;
            Rect rectKol = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + 20);
            drawRectText(String.format(formatString, spisakStavki.get(i).getKolicina()), canvas, rectKol);

            zadnjiY = zadnjiY + proredStavki;
        }
    }

    private void iscrtajZaglavljeStavki(Canvas canvas) {

        int lijevo = leftMargin; //početak
        int sirinaTexta = sirinaIspisa * 5 / 100;
        Rect rectRbr = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + proredStavki);
        drawRectText("RBR", canvas, rectRbr);

        lijevo = lijevo + sirinaTexta;
        lijevo = lijevo + sirinaMeduprostora;
        sirinaTexta = sirinaIspisa * 60 / 100;
        Rect rectNaziv = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + proredStavki);
        drawRectText("Naziv artikla", canvas, rectNaziv);

        lijevo = lijevo + sirinaTexta;
        lijevo = lijevo + sirinaMeduprostora;
        sirinaTexta = sirinaIspisa * 15 / 100;
        Rect rectAtribut = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + proredStavki);
        drawRectText("Rok trajanja", canvas, rectAtribut);

        lijevo = lijevo + sirinaTexta;
        lijevo = lijevo + sirinaMeduprostora;
        sirinaTexta = sirinaIspisa * 10 / 100;
        Rect rectJmj = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + proredStavki);
        drawRectText("JMJ", canvas, rectJmj);

        lijevo = lijevo + sirinaTexta;
        lijevo = lijevo + sirinaMeduprostora;
        sirinaTexta = sirinaIspisa * 10 / 100;
        Rect rectKol = new Rect(lijevo, zadnjiY, lijevo + sirinaTexta, zadnjiY + proredStavki);
        drawRectText("Količina", canvas, rectKol);


        zadnjiY = zadnjiY + proredStavki;
    }

    private void drawRectText(String text, Canvas canvas, Rect r) {
        /*
        Paint pn=new Paint();
        pn.setColor(Color.BLACK);
        pn.setStyle(Paint.Style.STROKE);
        canvas.drawRect(r,pn);
        */
        int textPadding = 2;

        if (text == null) {
            return;
        }
        if (text.equals("")) {
            return;
        }
        Paint p = new Paint();
        p.setTextSize(10);
        p.setTextAlign(Paint.Align.LEFT);
        int width = r.width();

        int numOfChars = p.breakText(text, true, width, null);
        int start = (text.length() - numOfChars) / 2;
        //canvas.drawText(text,start,start+numOfChars,r.exactCenterX(),r.exactCenterY(),p);
        canvas.drawText(text, start, start + numOfChars, r.left + textPadding, r.bottom - textPadding, p);


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

    private void printTextCenterX(Canvas canvas, int offsetY, String text, Paint paint) {
        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        int width = 0;
        for (float w : widths) {
            width += w;
        }
        int offsetX = canvas.getWidth() / 2 - width / 2;
        canvas.drawText(text, offsetX, offsetY - paint.getFontMetrics().top, paint);
    }
}
