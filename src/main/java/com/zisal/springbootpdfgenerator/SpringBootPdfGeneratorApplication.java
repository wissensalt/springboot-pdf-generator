package com.zisal.springbootpdfgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootPdfGeneratorApplication {

	@Autowired
	private IPDFService pdfService;

	private static final String LOREM_IPSUM = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

	public static void main(String[] args) {
		SpringApplication.run(SpringBootPdfGeneratorApplication.class, args);
	}

	@Bean
	CommandLineRunner generateOne() {
		return strings -> pdfService.generatePlainPdf("/home/fauzi/pdf/plain.pdf", "Hello World");
	}

	@Bean
	CommandLineRunner generateTwo() {
		return strings -> pdfService.generateCustomPageSizePdf("/home/fauzi/pdf/custompage.pdf", LOREM_IPSUM);
	}

	@Bean
	CommandLineRunner generateThree() {
		return strings -> pdfService.generateCustomFontAndColorPdf("/home/fauzi/pdf/customfontcolor.pdf", LOREM_IPSUM);
	}

	@Bean
	CommandLineRunner generateFour() {
		return strings -> pdfService.generateTextWithImagePdf("labelstatus.png", "/home/fauzi/pdf/textimage.pdf", LOREM_IPSUM);
	}

	@Bean
	CommandLineRunner generateFive() {
		return strings -> pdfService.generateTextWithTablePdf("/home/fauzi/pdf/texttable.pdf", LOREM_IPSUM);
	}

	@Bean
	CommandLineRunner generateSix() {
		return strings -> pdfService.generateEncryptedPdf("/home/fauzi/pdf/plain.pdf", "/home/fauzi/pdf/encrypted.pdf", LOREM_IPSUM);
	}
}