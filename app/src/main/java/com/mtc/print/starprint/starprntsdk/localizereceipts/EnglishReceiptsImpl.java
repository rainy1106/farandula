package com.mtc.print.starprint.starprntsdk.localizereceipts;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import com.mtc.R;
import com.mtc.api.APIConstant;
import com.mtc.general.SharedPreference;
import com.mtc.kitchen.FragmentOrderDetails;
import com.mtc.kitchen.OrderListItem;
import com.mtc.print.starprint.starprntsdk.PrinterSettingConstant;
import com.mtc.utils.MyAppContext;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.ICommandBuilder.AlignmentPosition;
import com.starmicronics.starioextension.ICommandBuilder.BarcodeSymbology;
import com.starmicronics.starioextension.ICommandBuilder.BarcodeWidth;
import com.starmicronics.starioextension.ICommandBuilder.CodePageType;
import com.starmicronics.starioextension.ICommandBuilder.InternationalType;
import com.starmicronics.starioextension.StarIoExt.CharacterCode;

import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class EnglishReceiptsImpl extends ILocalizeReceipts {

    public EnglishReceiptsImpl() {
        mLanguageCode = "En";

        mCharacterCode = CharacterCode.Standard;
    }

    @Override
    public void append2inchTextReceiptData(Bitmap icon, ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        OrderListItem.Result order = FragmentOrderDetails.printArray;

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);
        builder.append(("\n\n").getBytes(encoding));
        builder.append(("\n\n").getBytes(encoding));

//        builder.appendBitmapWithAlignment(icon, true, AlignmentPosition.Center);

        builder.appendAlignment(AlignmentPosition.Center);
        builder.appendMultiple(("General Note \n").getBytes(encoding), 2, 2);
        builder.append(("\n").getBytes(encoding));


        builder.appendAlignment(AlignmentPosition.Center);
        if (!order.getGeneral_note().isEmpty())
            builder.appendMultiple((order.getGeneral_note() + "\n").getBytes(encoding), 2, 2);
        else
            builder.appendMultiple(("No notes available" + "\n").getBytes(encoding), 2, 2);

        builder.append(("\n\n").getBytes(encoding));

        Double total = 0.0;
        for (int i = 0; i < order.getCart().size(); i++) {
            OrderListItem.Cart it = order.getCart().get(i);
            double i1 = Integer.parseInt(it.getQuantity()) * Double.valueOf(it.getPrice());
//            uu = it.quantity.toInt().times(it.price.toDouble());
            total = total.doubleValue() + i1;
        }
//        order.getCart().forEach {
//            total = total.plus(it.quantity.toInt().times(it.price.toDouble()))
//        }
        DecimalFormat numberFormat = new DecimalFormat("##.##");
        String roundoff = numberFormat.format(total);
        double tax = ((total * Double.parseDouble(String.valueOf(SharedPreference.INSTANCE.getKitchenTax(Objects.requireNonNull(MyAppContext.Companion.get()))))) / 100.0);
        double full_total = Double.valueOf(roundoff) + tax;
        String _full_total = numberFormat.format(full_total);
        builder.appendAlignment(AlignmentPosition.Center);
        builder.appendMultiple(("$" + _full_total + "\n").getBytes(encoding), 2, 2);
        builder.append(("\n\n").getBytes(encoding));

        for (int i = 0; i < order.getCart().size(); i++) {
            OrderListItem.Cart it = order.getCart().get(i);
            builder.appendAlignment(AlignmentPosition.Right);
            builder.appendEmphasis((it.getProduct_name() + " X " + it.getQuantity() + "        \t" + " \t$ " + String.valueOf(it.getPrice()) + "        \t" + "\n").getBytes(encoding));
            if (!it.getExtra_items().trim().isEmpty())
                builder.append(("Extra Item : " + it.getExtra_items() + "        \t" + "\n").getBytes(encoding));
            if (!it.getRemark().trim().isEmpty())
                builder.append(("Notes : " + it.getRemark() + "        \t" + "\n").getBytes(encoding));
            builder.appendAlignment(AlignmentPosition.Left);
            builder.append(("-----------------------------------------------\n").getBytes(encoding));
        }


        builder.appendAlignment(AlignmentPosition.Left);
        builder.append(("-----------------------------------------------\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Right);
        builder.append(("Purchase SubTotal  :         \t" + " \t$ " + String.valueOf(roundoff) + "        \t").getBytes(encoding));
//        builder.appendAlignment(AlignmentPosition.Right);
//        builder.append((String.valueOf(roundoff)).getBytes(encoding));
        builder.append(("\n\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Right);
        builder.append(("Sales Tax  :         \t" + " \t$ " + numberFormat.format(tax) + "        \t").getBytes(encoding));
//        builder.appendAlignment(AlignmentPosition.Right);
//        builder.append(String.valueOf(tax).getBytes(encoding));
        builder.append(("\n\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);
        builder.append(("-----------------------------------------------\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Right);
        builder.append(("Total :         \t" + " \t$ " + String.valueOf(_full_total) + "        \t" + "\n").getBytes(encoding));
        builder.append("-----------------------------------------------\n".getBytes(encoding));


        builder.appendAlignment(AlignmentPosition.Center);
        builder.append((getCDate() + " at " + getCTime() + "\n").getBytes(encoding));
        builder.append(("\n").getBytes(encoding));


        builder.appendAlignment(AlignmentPosition.Center);
        builder.appendEmphasis(("Farandula Pizza & Restaurant\n").getBytes(encoding));
        builder.append((SharedPreference.INSTANCE.getKitchenAddress(MyAppContext.Companion.getContext())).getBytes(encoding));
//        builder.append(("2846 Palm Ave\n").getBytes(encoding));
//        builder.append(("Hialeah, FL33010\n").getBytes(encoding));
        builder.append(("786-332-3779\n").getBytes(encoding));
        builder.append("-----------------------------------\n".getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);
        builder.append(("Return Policy : No Return\n").getBytes(encoding));
        builder.append("-----------------------------------\n".getBytes(encoding));

//        builder.append(("Star Clothing Boutique\n" + "123 Star Road\n" + "City, State 12345\n" + "\n").getBytes(encoding));
//
//        builder.appendAlignment(AlignmentPosition.Left);
//
//        builder.append(("Date:MM/DD/YYYY    Time:HH:MM PM\n" + "--------------------------------\n" + "\n").getBytes(encoding));

//        builder.append((
//                "SKU         Description    Total\n" +
//                        "300678566   PLAIN T-SHIRT  10.99\n" +
//                        "300692003   BLACK DENIM    29.99\n" +
//                        "300651148   BLUE DENIM     29.99\n" +
//                        "300642980   STRIPED DRESS  49.99\n" +
//                        "300638471   BLACK BOOTS    35.99\n" +
//                        "\n" +
//                        "Subtotal                  156.95\n" +
//                        "Tax                         0.00\n" +
//                        "--------------------------------\n").getBytes(encoding));
//
//        builder.append(("Total     ").getBytes(encoding));
//
//
//        builder.append((
//                "--------------------------------\n" +
//                        "\n" +
//                        "Charge\n" +
//                        "156.95\n" +
//                        "Visa XXXX-XXXX-XXXX-0123\n" +
//                        "\n").getBytes(encoding));
//
//        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));
//
//        builder.append(("Within ").getBytes(encoding));
//
//        builder.appendUnderLine(("30 days").getBytes(encoding));
//
//        builder.append((" with receipt\n").getBytes(encoding));
//
//        builder.append((
//                "And tags attached\n" +
//                        "\n").getBytes(encoding));
//
//        builder.appendAlignment(AlignmentPosition.Center);
//
//        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    private String getCDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(new Date());
    }

    private String getCTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(new Date());
    }

    @Override
    public void append3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY                    Time:HH:MM PM\n" +
                        "------------------------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.appendEmphasis(("SALE\n").getBytes(encoding));

        builder.append((
                "SKU               Description              Total\n" +
                        "300678566         PLAIN T-SHIRT            10.99\n" +
                        "300692003         BLACK DENIM              29.99\n" +
                        "300651148         BLUE DENIM               29.99\n" +
                        "300642980         STRIPED DRESS            49.99\n" +
                        "300638471         BLACK BOOTS              35.99\n" +
                        "\n" +
                        "Subtotal                                  156.95\n" +
                        "Tax                                         0.00\n" +
                        "------------------------------------------------\n").getBytes(encoding));

        builder.append(("Total                       ").getBytes(encoding));

        builder.appendMultiple(("   $156.95\n").getBytes(encoding), 2, 2);

        builder.append((
                "------------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));

        builder.append((
                "And tags attached\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);

        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    @Override
    public void append4inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY                                         Time:HH:MM PM\n" +
                        "---------------------------------------------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.appendEmphasis(("SALE\n").getBytes(encoding));

        builder.append((
                "SKU                        Description                          Total\n" +
                        "300678566                  PLAIN T-SHIRT                        10.99\n" +
                        "300692003                  BLACK DENIM                          29.99\n" +
                        "300651148                  BLUE DENIM                           29.99\n" +
                        "300642980                  STRIPED DRESS                        49.99\n" +
                        "300638471                  BLACK BOOTS                          35.99\n" +
                        "\n" +
                        "Subtotal                                                       156.95\n" +
                        "Tax                                                              0.00\n" +
                        "---------------------------------------------------------------------\n").getBytes(encoding));

        builder.append(("Total                                            ").getBytes(encoding));

        builder.appendMultiple(("   $156.95\n").getBytes(encoding), 2, 2);

        builder.append((
                "---------------------------------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));

        builder.append((
                "And tags attached\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);

        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    @Override
    public Bitmap create2inchRasterReceiptImage() {
        String textToPrint =
                "   Star Clothing Boutique\n" +
                        "        123 Star Road\n" +
                        "      City, State 12345\n" +
                        "\n" +
                        "Date:MM/DD/YYYY Time:HH:MM PM\n" +
                        "-----------------------------\n" +
                        "SALE\n" +
                        "SKU       Description   Total\n" +
                        "300678566 PLAIN T-SHIRT 10.99\n" +
                        "300692003 BLACK DENIM   29.99\n" +
                        "300651148 BLUE DENIM    29.99\n" +
                        "300642980 STRIPED DRESS 49.99\n" +
                        "30063847  BLACK BOOTS   35.99\n" +
                        "\n" +
                        "Subtotal               156.95\n" +
                        "Tax                      0.00\n" +
                        "-----------------------------\n" +
                        "Total                 $156.95\n" +
                        "-----------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "Refunds and Exchanges\n" +
                        "Within 30 days with receipt\n" +
                        "And tags attached\n";

        int textSize = 22;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        return createBitmapFromText(textToPrint, textSize, PrinterSettingConstant.PAPER_SIZE_TWO_INCH, typeface);
    }

    @Override
    public Bitmap create3inchRasterReceiptImage() {
        String textToPrint =
                "        Star Clothing Boutique\n" +
                        "             123 Star Road\n" +
                        "           City, State 12345\n" +
                        "\n" +
                        "Date:MM/DD/YYYY          Time:HH:MM PM\n" +
                        "--------------------------------------\n" +
                        "SALE\n" +
                        "SKU            Description       Total\n" +
                        "300678566      PLAIN T-SHIRT     10.99\n" +
                        "300692003      BLACK DENIM       29.99\n" +
                        "300651148      BLUE DENIM        29.99\n" +
                        "300642980      STRIPED DRESS     49.99\n" +
                        "30063847       BLACK BOOTS       35.99\n" +
                        "\n" +
                        "Subtotal                        156.95\n" +
                        "Tax                               0.00\n" +
                        "--------------------------------------\n" +
                        "Total                          $156.95\n" +
                        "--------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "Refunds and Exchanges\n" +
                        "Within 30 days with receipt\n" +
                        "And tags attached\n";

        int textSize = 25;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        return createBitmapFromText(textToPrint, textSize, PrinterSettingConstant.PAPER_SIZE_THREE_INCH, typeface);
    }

    @Override
    public Bitmap create4inchRasterReceiptImage() {
        String textToPrint =
                "                   Star Clothing Boutique\n" +
                        "                        123 Star Road\n" +
                        "                      City, State 12345\n" +
                        "\n" +
                        "Date:MM/DD/YYYY                             Time:HH:MM PM\n" +
                        "---------------------------------------------------------\n" +
                        "SALE\n" +
                        "SKU                     Description                 Total\n" +
                        "300678566               PLAIN T-SHIRT               10.99\n" +
                        "300692003               BLACK DENIM                 29.99\n" +
                        "300651148               BLUE DENIM                  29.99\n" +
                        "300642980               STRIPED DRESS               49.99\n" +
                        "300638471               BLACK BOOTS                 35.99\n" +
                        "\n" +
                        "Subtotal                                           156.95\n" +
                        "Tax                                                  0.00\n" +
                        "---------------------------------------------------------\n" +
                        "Total                                             $156.95\n" +
                        "---------------------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "Refunds and Exchanges\n" +
                        "Within 30 days with receipt\n" +
                        "And tags attached\n";

        int textSize = 23;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        return createBitmapFromText(textToPrint, textSize, PrinterSettingConstant.PAPER_SIZE_FOUR_INCH, typeface);
    }

    @Override
    public Bitmap createCouponImage(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
    }

    @Override
    public Bitmap createEscPos3inchRasterReceiptImage() {
        String textToPrint =
                "\n" +
                        "      Star Clothing Boutique\n" +
                        "           123 Star Road\n" +
                        "         City, State 12345\n" +
                        "\n" +
                        "Date:MM/DD/YYYY       Time:HH:MM PM\n" +
                        "-----------------------------------\n" +
                        "SALE\n" +
                        "SKU          Description      Total\n" +
                        "300678566    PLAIN T-SHIRT    10.99\n" +
                        "300692003    BLACK DENIM      29.99\n" +
                        "300651148    BLUE DENIM       29.99\n" +
                        "300642980    STRIPED DRESS    49.99\n" +
                        "30063847     BLACK BOOTS      35.99\n" +
                        "\n" +
                        "Subtotal                     156.95\n" +
                        "Tax                            0.00\n" +
                        "-----------------------------------\n" +
                        "Total                       $156.95\n" +
                        "-----------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "Refunds and Exchanges\n" +
                        "Within 30 days with receipt\n" +
                        "And tags attached\n";

        int textSize = 24;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        return createBitmapFromText(textToPrint, textSize, PrinterSettingConstant.PAPER_SIZE_ESCPOS_THREE_INCH, typeface);
    }

    @Override
    public void appendEscPos3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "\n" +
                        "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY              Time:HH:MM PM\n" +
                        "------------------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.appendEmphasis(("SALE \n").getBytes(encoding));

        builder.append((
                "SKU            Description           Total\n" +
                        "300678566      PLAIN T-SHIRT         10.99\n" +
                        "300692003      BLACK DENIM           29.99\n" +
                        "300651148      BLUE DENIM            29.99\n" +
                        "300642980      STRIPED DRESS         49.99\n" +
                        "300638471      BLACK BOOTS           35.99\n" +
                        "\n" +
                        "Subtotal                            156.95\n" +
                        "Tax                                   0.00\n" +
                        "------------------------------------------\n").getBytes(encoding));

        builder.append(("Total                 ").getBytes(encoding));

        builder.appendMultiple(("   $156.95\n").getBytes(encoding), 2, 2);

        builder.append((
                "------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));

        builder.append((
                "And tags attached\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);

        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    @Override
    public void appendDotImpact3inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY              Time:HH:MM PM\n" +
                        "------------------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.appendEmphasis(("SALE \n").getBytes(encoding));

        builder.append((
                "SKU             Description          Total\n" +
                        "300678566       PLAIN T-SHIRT        10.99\n" +
                        "300692003       BLACK DENIM          29.99\n" +
                        "300651148       BLUE DENIM           29.99\n" +
                        "300642980       STRIPED DRESS        49.99\n" +
                        "300638471       BLACK BOOTS          35.99\n" +
                        "\n" +
                        "Subtotal                            156.95\n" +
                        "Tax                                   0.00\n" +
                        "------------------------------------------\n" +
                        "Total                              $156.95\n" +
                        "------------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));
    }

    @Override
    public Bitmap createSk12inchRasterReceiptImage() {
        String textToPrint =
                "     Star Clothing Boutique\n" +
                        "          123 Star Road\n" +
                        "        City, State 12345\n" +
                        "\n" +
                        "Date:MM/DD/YYYY     Time:HH:MM PM\n" +
                        "---------------------------------\n" +
                        "SALE\n" +
                        "SKU         Description     Total\n" +
                        "300678566   PLAIN T-SHIRT   10.99\n" +
                        "300692003   BLACK DENIM     29.99\n" +
                        "300651148   BLUE DENIM      29.99\n" +
                        "300642980   STRIPED DRESS   49.99\n" +
                        "30063847    BLACK BOOTS     35.99\n" +
                        "\n" +
                        "Subtotal                   156.95\n" +
                        "Tax                          0.00\n" +
                        "---------------------------------\n" +
                        "Total                     $156.95\n" +
                        "---------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "Refunds and Exchanges\n" +
                        "Within 30 days with receipt\n" +
                        "And tags attached\n";

        int textSize = 22;
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);

        return createBitmapFromText(textToPrint, textSize, PrinterSettingConstant.PAPER_SIZE_SK1_TWO_INCH, typeface);
    }

    @Override
    public void appendSk12inchTextReceiptData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendAlignment(AlignmentPosition.Center);

        builder.append((
                "Star Clothing Boutique\n" +
                        "123 Star Road\n" +
                        "City, State 12345\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Left);

        builder.append((
                "Date:MM/DD/YYYY        Time:HH:MM PM\n" +
                        "------------------------------------\n" +
                        "\n").getBytes(encoding));

        builder.append((
                "SKU           Description      Total\n" +
                        "300678566     PLAIN T-SHIRT    10.99\n" +
                        "300692003     BLACK DENIM      29.99\n" +
                        "300651148     BLUE DENIM       29.99\n" +
                        "300642980     STRIPED DRESS    49.99\n" +
                        "300638471     BLACK BOOTS      35.99\n" +
                        "\n" +
                        "Subtotal                      156.95\n" +
                        "Tax                             0.00\n" +
                        "------------------------------------\n").getBytes(encoding));

        builder.append(("Total     ").getBytes(encoding));

        builder.appendMultiple(("      $156.95\n").getBytes(encoding), 2, 2);

        builder.append((
                "------------------------------------\n" +
                        "\n" +
                        "Charge\n" +
                        "156.95\n" +
                        "Visa XXXX-XXXX-XXXX-0123\n" +
                        "\n").getBytes(encoding));

        builder.appendInvert(("Refunds and Exchanges\n").getBytes(encoding));

        builder.append(("Within ").getBytes(encoding));

        builder.appendUnderLine(("30 days").getBytes(encoding));

        builder.append((" with receipt\n").getBytes(encoding));

        builder.append((
                "And tags attached\n" +
                        "\n").getBytes(encoding));

        builder.appendAlignment(AlignmentPosition.Center);

        builder.appendBarcode(("{BStar.").getBytes(Charset.forName("US-ASCII")), BarcodeSymbology.Code128, BarcodeWidth.Mode2, 40, true);
    }

    @Override
    public void appendTextLabelData(ICommandBuilder builder, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.appendUnitFeed(20 * 2);

        builder.appendMultipleHeight(2);

        builder.append(("Star Micronics America, Inc.").getBytes(encoding));

        builder.appendUnitFeed(64);

        builder.append(("65 Clyde Road Suite G").getBytes(encoding));

        builder.appendUnitFeed(64);

        builder.append(("Somerset, NJ 08873-3485 U.S.A").getBytes(encoding));

        builder.appendUnitFeed(64);

        builder.appendMultipleHeight(1);
    }

    @Override
    public String createPasteTextLabelString() {
        return "Star Micronics America, Inc.\n" +
                "65 Clyde Road Suite G\n" +
                "Somerset, NJ 08873-3485 U.S.A";
    }

    @Override
    public void appendPasteTextLabelData(ICommandBuilder builder, String pasteText, boolean utf8) {
        Charset encoding;

        if (utf8) {
            encoding = Charset.forName("UTF-8");

            builder.appendCodePage(CodePageType.UTF8);
        } else {
            encoding = Charset.forName("US-ASCII");

            builder.appendCodePage(CodePageType.CP998);
        }

        builder.appendInternational(InternationalType.USA);

        builder.appendCharacterSpace(0);

        builder.append(pasteText.getBytes(encoding));
    }
}
