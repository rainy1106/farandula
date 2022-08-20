//package com.example.demoppdf
//
//import android.content.Context
//import android.os.Bundle
//import android.print.PrintAttributes
//import android.print.PrintManager
//import android.widget.Button
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.itextpdf.text.*
//import com.itextpdf.text.pdf.BaseFont
//import com.itextpdf.text.pdf.PdfWriter
//import com.itextpdf.text.pdf.draw.LineSeparator
//import com.itextpdf.text.pdf.draw.VerticalPositionMark
//import com.mtc.R
//import com.mtc.demoppdf.Common
//import com.mtc.demoppdf.PdfDocumentAdapter
//import java.io.File
//import java.io.FileOutputStream
//
//
//class MainActivity : AppCompatActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        var button = findViewById<Button>(R.id.button)
//        button.setOnClickListener {
//            createPdf(Common.getPath(this@MainActivity) + "test_pdf.pdf")
//        }
//    }
//
//    private fun createPdf(path: String) {
//        if (File(path).exists())
//            File(path).delete()
//
//
//        try {
//            val document = Document()
//            //save
//            PdfWriter.getInstance(document, FileOutputStream(path))
//            //open
//            document.open()
//            //setting
//            document.setPageSize(PageSize.A4)
//            //
//            document.addCreationDate()
//            document.addAuthor("Test")
//            document.addCreator("Test C")
//            //font
//            val baseColor = BaseColor(0, 0, 0, 0)
//            val fontSize = 20f
//            val valueFontSize = 20f
//
//            //custome_font
//            val fontName =
//                BaseFont.createFont("assets/font/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED)
//
//            //title
//            val titleFont = Font(fontName, 22f, Font.NORMAL, BaseColor.BLACK)
//            addNewItem(document, "Order Details", Element.ALIGN_CENTER, titleFont)
//
//
//            //Add item
//            val orderNumberTitle = Font(fontName, fontSize, Font.NORMAL, BaseColor.BLACK)
//            addNewItem(document, "Order Number :", Element.ALIGN_LEFT, orderNumberTitle)
//
//            val orderNumberValueTitle = Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK)
//            addNewItem(document, "323232334", Element.ALIGN_LEFT, orderNumberValueTitle)
//
//            addLineSeperator(document)
//
//            addNewItem(document, "Order Date : ", Element.ALIGN_LEFT, orderNumberTitle)
//            addNewItem(document, "18-07-2022 ", Element.ALIGN_LEFT, orderNumberValueTitle)
//
//            addLineSeperator(document)
//            addLineSpace(document)
//
//            addNewItem(document, "Product Details : ", Element.ALIGN_LEFT, orderNumberTitle)
//
//            addLineSeperator(document)
//
//            //Item 1
//            addNewItemLeftANdRight(document, "Pizza", "(0.0%)", titleFont, orderNumberValueTitle)
//            addNewItemLeftANdRight(document, "25 * 10", "$250", titleFont, orderNumberValueTitle)
//
//            addLineSeperator(document)
//
//            //Item 1
//            addNewItemLeftANdRight(document, "Burger", "(0.0%)", titleFont, orderNumberValueTitle)
//            addNewItemLeftANdRight(document, "20 * 2", "$40", titleFont, orderNumberValueTitle)
//
//
//            addLineSeperator(document)
//            //Item 1
//            addNewItemLeftANdRight(
//                document,
//                "Cold Drink",
//                "(0.0%)",
//                titleFont,
//                orderNumberValueTitle
//            )
//            addNewItemLeftANdRight(document, "10 * 2", "$20", titleFont, orderNumberValueTitle)
//
//            //total
//            addLineSpace(document)
//            addLineSpace(document)
//
//            document.close()
//
//            Toast.makeText(this@MainActivity, "Document generated successs", Toast.LENGTH_SHORT)
//                .show();
//
//            printPdf()
//        } catch (ex: java.lang.Exception) {
//        } finally {
//
//        }
//    }
//
//    private fun printPdf() {
//        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
//        try {
//            val pdfDocumnetAdapter = PdfDocumentAdapter(
//                this@MainActivity, Common.getPath(this) + "test_pdf.pdf"
//            )
//            printManager.print("Document", pdfDocumnetAdapter, PrintAttributes.Builder().build())
//        } catch (ex: java.lang.Exception) {
//
//        }
//    }
//
//    private fun addNewItemLeftANdRight(
//        document: Document,
//        textLeft: String,
//        textRight: String,
//        textLeftFont: Font,
//        textRightFont: Font
//    ) {
//        val chunkTextLeft = Chunk(textLeft, textLeftFont)
//        val chunkTextRight = Chunk(textRight, textRightFont)
//        val p = Paragraph(chunkTextLeft)
//        p.add(Chunk(VerticalPositionMark()))
//        p.add(chunkTextRight)
//        document.add(p)
//    }
//
//    private fun addLineSeperator(document: Document) {
//        val lineSeparator = LineSeparator()
//        lineSeparator.lineColor = BaseColor.BLACK
//        addLineSpace(document)
//        val chunk = Chunk()
//        document.add(chunk)
//        addLineSpace(document)
//
//    }
//
//    private fun addLineSpace(document: Document) {
//        val paragraph = Paragraph("")
//        document.add(paragraph)
//    }
//
//    private fun addNewItem(document: Document, text: String, alignCenter: Int, titleFont: Font) {
//        var chunk = Chunk(text, titleFont)
//        var paragraph = Paragraph(chunk)
//        paragraph.alignment = alignCenter
//        document.add(paragraph)
//    }
//}