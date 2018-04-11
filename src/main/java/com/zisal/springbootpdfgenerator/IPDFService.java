package com.zisal.springbootpdfgenerator;

/**
 * Created on 4/11/18.
 *
 * @author <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 */
public interface IPDFService {

    void generatePlainPdf(String p_PathAndFileName, String p_Text);

    void generateCustomPageSizePdf(String p_PathAndFileName, String p_Text);

    void generateCustomFontAndColorPdf(String p_PathAndFileName, String p_Text);

    void generateTextWithImagePdf(String p_ImageFileName, String p_PathAndFileName, String p_Text);

    void generateTextWithTablePdf(String p_PathAndFileName, String p_Text);

    void generateEncryptedPdf(String p_InputPathAndFileName, String p_OutputPathAndFileName, String p_Text);
}
