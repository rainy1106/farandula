package com.mtc.print.usb.textparser;

import com.mtc.print.usb.EscPosPrinterCommands;
import com.mtc.print.usb.exceptions.EscPosConnectionException;
import com.mtc.print.usb.exceptions.EscPosEncodingException;

public interface IPrinterTextParserElement {
    int length() throws EscPosEncodingException;
    IPrinterTextParserElement print(EscPosPrinterCommands printerSocket) throws EscPosEncodingException, EscPosConnectionException;
}
