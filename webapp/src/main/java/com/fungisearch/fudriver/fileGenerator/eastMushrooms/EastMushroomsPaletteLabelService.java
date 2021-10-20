package com.fungisearch.fudriver.fileGenerator.eastMushrooms;

import com.fungisearch.fudriver.config.AppConfig;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkup;
import com.fungisearch.fudriver.reclassification.query.dao.SkupRodzajDao;
import com.fungisearch.fudriver.reclassification.query.dto.SkupRodzajDto;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.query.dao.EastWarehouseDao;
import com.fungisearch.fudriver.warehouseEastMushrooms.query.dto.CreatedWarehousePaletteDto;
import com.fungisearch.fudriver.warehouseEastMushrooms.query.dto.WarehouseUnitDto;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class EastMushroomsPaletteLabelService {

    private final AppConfig appConfig;
    private final EastWarehouseDao eastWarehouseDao;
    private final SkupRodzajDao skupRodzajDao;
    private final TypeFactory typeFactory;

    public EastMushroomsPaletteLabelService(AppConfig appConfig, EastWarehouseDao eastWarehouseDao, SkupRodzajDao skupRodzajDao, TypeFactory typeFactory) {
        this.appConfig = appConfig;
        this.eastWarehouseDao = eastWarehouseDao;
        this.skupRodzajDao = skupRodzajDao;
        this.typeFactory = typeFactory;
    }

    public void createLabel(long paletteNr) {
        CreatedWarehousePaletteDto palette = eastWarehouseDao.getCreatedWarehousePalette(paletteNr);
        List<String> chambers = findChambers(palette);
        String file = "/tmp/label_" + paletteNr + ".pdf";
        try {

            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font helvetica6 = new Font(Font.HELVETICA, 6);
            Font helvetica8 = new Font(Font.HELVETICA, 8);
            Font helvetica6Bold = new Font(Font.HELVETICA, 6, Font.BOLD);
            Document document = new Document();

            //document.setMargins(10,10,10,10);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.setMargins(120, 0, 450, 0);
            document.open();

            Paragraph paletteNumber = new Paragraph();
            paletteNumber.setFont(new Font(helvetica, 16, Font.BOLD));
            paletteNumber.add("Paleta nr " + paletteNr);
            paletteNumber.setSpacingAfter(1);
            paletteNumber.add("\n");

            Phrase phrase = new Phrase("", helvetica8);
            boolean addComma = false;
            for (String chamber : chambers) {
                if (addComma) {
                    phrase.add(", ");
                }
                phrase.add(chamber);
                addComma = true;
            }
            paletteNumber.add(phrase);

            document.add(paletteNumber);

            Paragraph barcode = new Paragraph();
            Barcode128 code128 = new Barcode128();
            code128.setCode(getBarcode(paletteNr));
            PdfContentByte cb = writer.getDirectContent();
            Image image = code128.createImageWithBarcode(cb, null, null);
            image.scaleAbsoluteWidth(100);
            image.scaleAbsoluteHeight(40);
            barcode.add(image);
            document.add(barcode);

            Map<Long, PaletteType> paletteTypesMap = new HashMap<>();
            List<SkupRodzajDto> skupTypes = skupRodzajDao.getAllTypes();
            for (WarehouseUnitDto warehouseUnitDto : palette.warehouseUnits) {
                PaletteType paletteType;
                if (paletteTypesMap.containsKey(warehouseUnitDto.typeId)) {
                    paletteType = paletteTypesMap.get(warehouseUnitDto.typeId);
                } else {
                    SkupRodzajDto skupRodzajDto = findSkupType(warehouseUnitDto.typeId, skupTypes);
                    paletteType = new PaletteType();
                    paletteType.localTypeId = warehouseUnitDto.typeId;
                    paletteType.localTypeName = warehouseUnitDto.typeName;
                    paletteType.localTypeWeight = warehouseUnitDto.typeWeight;
                    if (skupRodzajDto != null) {
                        paletteType.remoteTypeId = skupRodzajDto.remoteId;
                        paletteType.remoteTypeName = skupRodzajDto.name;
                        paletteType.remoteTypeWeight = skupRodzajDto.weight;
                    }
                    paletteType.totalAmount = 0;
                    paletteTypesMap.put(warehouseUnitDto.typeId, paletteType);
                }
                paletteType.totalAmount++;
            }


            Paragraph localTypesParagraph = new Paragraph();

            localTypesParagraph.setFont(new Font(Font.HELVETICA, 6));


            Table localTypes = new Table(3);
            localTypes.setBorderWidth(1);
            localTypes.setBorderColor(new Color(0, 0, 0));
            localTypes.setPadding(1);
            localTypes.setSpacing(0);
            localTypes.setWidth(60);
            localTypes.setAlignment(Table.ALIGN_LEFT);

            Cell localTypeName = new Cell(new Phrase("Rodzaj", helvetica6Bold));
            localTypeName.setHeader(true);
            localTypes.addCell(localTypeName);

            Cell localTypeWeight = new Cell(new Phrase("Waga jednostkowa", helvetica6Bold));
            localTypeWeight.setHeader(true);
            localTypes.addCell(localTypeWeight);

            Cell localTypeAmount = new Cell(new Phrase("Ile sztuk", helvetica6Bold));
            localTypeAmount.setHeader(true);
            localTypes.addCell(localTypeAmount);


            localTypes.endHeaders();

            Cell cell;

            Integer totalAmount = paletteTypesMap.values().stream().mapToInt(o -> o.totalAmount).sum();
            for (PaletteType paletteType : paletteTypesMap.values()) {
                cell = new Cell(new Phrase(paletteType.localTypeName, helvetica6));
                localTypes.addCell(cell);
                cell = new Cell(new Phrase(paletteType.localTypeWeight.toString(), helvetica6));
                localTypes.addCell(cell);
                cell = new Cell(new Phrase(paletteType.totalAmount.toString(), helvetica6));
                localTypes.addCell(cell);
            }
            localTypesParagraph.add(localTypes);

            Cell totalName = new Cell(new Phrase("Suma", helvetica6Bold));
            totalName.setColspan(2);
            localTypes.addCell(totalName);

            Cell totalAmountCell = new Cell(new Phrase(totalAmount.toString(), helvetica6Bold));
            totalAmountCell.setVerticalAlignment(Cell.ALIGN_RIGHT);
            localTypes.addCell(totalAmountCell);


            localTypesParagraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(localTypesParagraph);


            /*
            Paragraph remoteTypesParagraph = new Paragraph();
            remoteTypesParagraph.setFont(new Font(helvetica,6));
            Table remoteTypes = new Table(3);
            remoteTypes.setBorderWidth(0);
            remoteTypes.setBorderColor(new Color(0, 0, 0));
            remoteTypes.setPadding(0);
            remoteTypes.setSpacing(0);
            Cell eastHeader = new Cell("Rodzaje w EastMushrooms");
            eastHeader.setHeader(true);
            eastHeader.setColspan(3);
            remoteTypes.addCell(eastHeader);

            Cell remoteTypeName = new Cell("Rodzaj");
            remoteTypeName.setHeader(true);
            remoteTypes.addCell(remoteTypeName);

            Cell remoteTypeWeight = new Cell("Waga jednostkowa");
            remoteTypeWeight.setHeader(true);
            remoteTypes.addCell(remoteTypeWeight);

            Cell remoteTypeAmount = new Cell("Ile sztuk");
            remoteTypeAmount.setHeader(true);
            remoteTypes.addCell(remoteTypeAmount);

            remoteTypes.endHeaders();

            for (PaletteType paletteType : paletteTypesMap.values()) {
                cell = new Cell(paletteType.remoteTypeName);
                remoteTypes.addCell(cell);
                cell = new Cell(paletteType.remoteTypeWeight != null ? paletteType.remoteTypeWeight.toString() : "" + " kg");
                remoteTypes.addCell(cell);
                cell = new Cell(paletteType.totalAmount.toString() + " szt.");
                cell.setVerticalAlignment(2);
                remoteTypes.addCell(cell);
            }
            remoteTypesParagraph.add(remoteTypes);
            document.add(remoteTypesParagraph);
*/
            document.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            CupsPrinter printer = new CupsClient().getDefaultPrinter();
            InputStream is = new FileInputStream(file);
            PrintJob pj = new PrintJob.Builder(is).jobName("Naklejka na paletę").userName("marcin").build();

            Map<String, String> attributes = new HashMap<String, String>();
            attributes.put("", "job-name=cups4j print label");
            attributes.put("job-attributes", "job-more-info=");
            attributes.put("job-attributes", "job-originating-user-name=job-originating-user-name");
            attributes.put("job-attributes", "detailed-name=Detailed description of the job.");
            attributes.put("job-attributes", "document-name=cups4j.pdf");
            attributes.put("job-attributes", "document-natural-language=EN");
            pj.setAttributes(attributes);
            printer.print(pj);

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getBarcode(long paletteId) {
        String paletteNr = Long.toString(paletteId);
        StringBuilder sb = new StringBuilder(paletteNr);
        for (int i = paletteNr.length(); i < 10; i++) {
            sb.insert(0, "0");
        }
        StringBuilder supplierNr = new StringBuilder(appConfig.getSupplierNr());
        for (int i = supplierNr.length(); i < 3; i++) {
            supplierNr.insert(0, "0");
        }

        return supplierNr.toString() + sb.toString();
    }


    public static class PaletteType {
        public Long localTypeId;
        public String localTypeName;
        public Double localTypeWeight;
        public Long remoteTypeId;
        public String remoteTypeName;
        public Double remoteTypeWeight;
        public Integer totalAmount;
    }

    private List<String> findChambers(CreatedWarehousePaletteDto palette) {
        List<String> chambers = new ArrayList<>(palette.warehouseUnits.stream().map(o -> o.chamberName).collect(Collectors.toSet()));
        if (!chambers.isEmpty()) {
            Collections.sort(chambers);
        }
        return chambers;
    }

    private SkupRodzajDto findSkupType(long localTypeId, List<SkupRodzajDto> types) {
        Type type = typeFactory.findById(localTypeId);
        for(SkupRodzajDto dto: types){
            if ((dto.id != null) && (dto.id.equals(type.getRemoteTypeId()))) {
                return dto;
            }
        }
        return null;
    }
}
