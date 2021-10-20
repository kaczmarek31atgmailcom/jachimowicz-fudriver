package com.fungisearch.fudriver.fileGenerator.fedor.view;

import com.fungisearch.fudriver.fileGenerator.fedor.model.PaletteLabel;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.lowagie.text.html.HtmlTags.FONT;

/**
 * Created by marcin on 27.07.16.
 */

public class PaletteLabelView extends AbstractPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
        PdfAction action = new PdfAction(PdfAction.PRINTDIALOG);
        writer.setOpenAction(action);
        PaletteLabel paletteLabel = (PaletteLabel) model.get("command");

        //BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        BaseFont helvetica = BaseFont.createFont(BaseFont.TIMES_ROMAN,"Cp1251",BaseFont.EMBEDDED);

        //Font helvetica12 = new Font(helvetica, 12);
        Font helvetica12 = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, true);
        Font helvetica28 = new Font(helvetica, 28);
        //Font helvetica34 = new Font(helvetica, 34);
        Font helvetica34 = FontFactory.getFont(FONT, "Cp1251", true);



        if (paletteLabel.getCompanyName().length() > 0) {
            Paragraph companyName = new Paragraph();
            companyName.setFont(helvetica12);
            companyName.add(paletteLabel.getCompanyName().toString());
            companyName.setSpacingAfter(1);
            document.add(companyName);
        }

        if (paletteLabel.getCompanyCity().length() > 0) {
            Paragraph companyCity = new Paragraph();
            companyCity.setFont(helvetica12);
            companyCity.add(paletteLabel.getCompanyCity().toString());
            companyCity.setSpacingAfter(1);
            document.add(companyCity);
        }

        if (paletteLabel.getCompanyStreet().length() > 0) {
            Paragraph companyStreet = new Paragraph();
            companyStreet.setFont(helvetica12);
            companyStreet.add(paletteLabel.getCompanyStreet().toString());
            companyStreet.setSpacingAfter(1);
            document.add(companyStreet);
        }

        if (paletteLabel.getCompanyNip().length() > 0) {
            Paragraph companyNip = new Paragraph();
            companyNip.setFont(helvetica12);
            companyNip.add("NIP: " + paletteLabel.getCompanyNip().toString());
            companyNip.setSpacingAfter(1);
            document.add(companyNip);
        }

        if (paletteLabel.getCompanyRegon().length() > 0) {
            Paragraph companyRegon = new Paragraph();
            companyRegon.setFont(helvetica12);
            companyRegon.add("REGON: " + paletteLabel.getCompanyRegon().toString());
            companyRegon.setSpacingAfter(1);
            document.add(companyRegon);
        }

        if (paletteLabel.getCompanyPhoneNo().length() > 0) {
            Paragraph companyPhoneNo = new Paragraph();
            companyPhoneNo.setFont(helvetica12);
            companyPhoneNo.add("tel: " + paletteLabel.getCompanyPhoneNo().toString());
            companyPhoneNo.setSpacingAfter(1);
            document.add(companyPhoneNo);
        }

        if (paletteLabel.getCompanyEmail().length() > 0) {
            Paragraph companyEmail = new Paragraph();
            companyEmail.setFont(helvetica12);
            companyEmail.add(paletteLabel.getCompanyEmail().toString());
            companyEmail.setSpacingAfter(1);
            document.add(companyEmail);
        }


        Paragraph separator = new Paragraph();
        separator.setSpacingAfter(50);
        document.add(separator);


        Paragraph orderId = new Paragraph();
        orderId.setFont(helvetica34);
        orderId.add("Zamówienie nr : " + paletteLabel.getOrderId().toString());
        orderId.setSpacingAfter(50);
        document.add(orderId);

        Paragraph customer = new Paragraph();
        customer.setFont(helvetica34);
        customer.add("Odbiorca: " + paletteLabel.getCustomerName());
        customer.setSpacingAfter(50);
        document.add(customer);

        Paragraph required = new Paragraph();
        required.setFont(helvetica34);
        required.add("Data wysyłki: " + paletteLabel.getRequired().toString());
        required.setSpacingAfter(50);
        document.add(required);

        if ((paletteLabel.getPlate() != null) && (paletteLabel.getPlate().length() > 0)) {
            Paragraph plate = new Paragraph();
            plate.setFont(helvetica34);
            plate.add("Samochód: " + paletteLabel.getPlate());
            plate.setSpacingAfter(50);
            document.add(plate);
        }
        LineSeparator line = new LineSeparator();
        Chunk lineChunk = new Chunk(line);
        document.add(lineChunk);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        Paragraph details = new Paragraph();
        details.setFont(helvetica28);
        details.setSpacingAfter(50);

        for (Map.Entry<String, Long> entry : paletteLabel.getDetails().entrySet()) {
            String name = entry.getKey();
            String amount = entry.getValue().toString();
            Phrase detailLine = new Phrase(name + "       " + amount + " szt.");
            detailLine.add(Chunk.NEWLINE);
            detailLine.add(Chunk.NEWLINE);
            detailLine.add(lineChunk);
            detailLine.add(Chunk.NEWLINE);
            detailLine.add(Chunk.NEWLINE);
            detailLine.add(Chunk.NEWLINE);
            details.add(detailLine);
        }
        document.add(details);

        Paragraph barcode = new Paragraph();
        Barcode128 code128 = new Barcode128();
        code128.setCode(paletteLabel.getBarcode());
        PdfContentByte cb = writer.getDirectContent();

        Image image = code128.createImageWithBarcode(cb, null, null);
        image.scaleAbsoluteWidth(250);
        image.scaleAbsoluteHeight(80);
        barcode.add(image);
        document.add(barcode);

        document.close();
    }
}
