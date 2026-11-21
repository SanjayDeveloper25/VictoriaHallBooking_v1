package com.gcc.communityHall.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static String generateQRCode(String baseUrl, Long bookingId) throws WriterException, IOException {
        int width = 100; // width of the QR code image
        int height = 100; // height of the QR code image
        String format = "png"; // format of the QR code image
        String charset = "UTF-8"; // character set used to encode the data

        // The URL to be embedded in the QR code
        String data =baseUrl + "/collectionpending?bookingId=" + bookingId;
        
        // create the QR code writer and hints
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, charset);

        // generate the QR code matrix
        BitMatrix matrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

        // create the QR code image as a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, format, baos);
        byte[] imageBytes = baos.toByteArray();

        // encode the image data in base64 format
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);

        return base64EncodedImage; // Fixed return statement
    }
}
