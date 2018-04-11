package com.zisal.springbootpdfgenerator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created on 4/11/18.
 *
 * @author <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 */
@Service
public class PDFServiceImpl implements IPDFService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFServiceImpl.class);

    @Override
    public void generatePlainPdf(String p_PathAndFileName, String p_Text) {
        writeFile(new Document(), p_PathAndFileName, p_Text);
    }

    @Override
    public void generateCustomPageSizePdf(String p_PathAndFileName, String p_Text) {
        Rectangle rectangle = new Rectangle(216f, 720f);
        Document document = new Document(rectangle, 36f, 7f, 108f, 180f);
        writeFile(document, p_PathAndFileName, p_Text);
    }

    @Override
    public void generateCustomFontAndColorPdf(String p_PathAndFileName, String p_Text) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream((p_PathAndFileName)));
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error Getting Document Instance");
        }
        document.open();
        try {
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            document.add(new Paragraph(p_Text, font));

            Font font2 = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.RED);
            Chunk chunk = new Chunk(p_Text, font2);
            document.add(chunk);
        } catch (DocumentException e) {
            LOGGER.error("Error Modify Document");
        }
        document.close();
    }

    @Override
    public void generateTextWithImagePdf(String p_ImageFileName, String p_PathAndFileName, String p_Text) {
        Path path = null;
        try {
            path = Paths.get(new ClassPathResource(p_ImageFileName).getURI());
        } catch (IOException e) {
            LOGGER.error("Error handling image file");
        }
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(p_PathAndFileName));
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error Getting Document Instance");
        }
        document.open();
        Image img = null;
        try {
            img = Image.getInstance(path.toAbsolutePath().toString());
        } catch (BadElementException | IOException e) {
            LOGGER.error("Failed handling image insertion");
        }
        try {
            document.add(img);
            document.add(new Paragraph(p_Text));
        } catch (DocumentException e) {
            LOGGER.error("Error Modify Document");
        }
        document.close();
    }

    @Override
    public void generateTextWithTablePdf(String p_PathAndFileName, String p_Text) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream((p_PathAndFileName)));
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error Getting Document Instance");
        }
        document.open();
        PdfPTable table = new PdfPTable(3);
        addTableHeader(table);
        addRows(table);
        try {
            addCustomRows(table);
        } catch (URISyntaxException | BadElementException | IOException e) {
            LOGGER.error("Error create custom rows");
        }
        try {
            document.add(table);
            document.add(new Paragraph(p_Text));
        } catch (DocumentException e) {
            LOGGER.error("Error Modify Document");
        }
        document.close();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Header 1", "Header 2", "Header 3")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table) {
        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");
    }

    private void addCustomRows(PdfPTable table) throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource("labelstatus.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }

    @Override
    public void generateEncryptedPdf(String p_InputPathAndFileName, String p_OutputPathAndFileName, String p_Text) {
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(p_InputPathAndFileName);
        } catch (IOException e) {
            LOGGER.error("File not Found ".concat(e.toString()));
        }
        PdfStamper pdfStamper = null;
        try {
            if (pdfReader != null) {
                pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(p_OutputPathAndFileName));
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        if (pdfStamper != null) {
            try {
                pdfStamper.setEncryption(
                        "userpass".getBytes(),
                        "userpass".getBytes(),
                        0,
                        PdfWriter.ENCRYPTION_AES_256
                );
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        try {
            if (pdfStamper != null) {
                pdfStamper.close();
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

    }

    private void writeFile(Document p_Document, String p_PathAndFileName, String p_Text) {
        try {
            PdfWriter.getInstance(p_Document, new FileOutputStream((p_PathAndFileName)));
        } catch (DocumentException | FileNotFoundException e) {
            LOGGER.error("Error Getting Document Instance");
        }
        p_Document.open();
        try {
            p_Document.add(new Paragraph(p_Text));
        } catch (DocumentException e) {
            LOGGER.error("Error Modify Document");
        }
        p_Document.close();
    }
}
