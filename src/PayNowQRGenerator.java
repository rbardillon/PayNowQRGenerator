import com.dbs.sgqr.generator.QRGenerator;
import com.dbs.sgqr.generator.QRGeneratorImpl;
import com.dbs.sgqr.generator.io.*;

import java.awt.*;
import java.io.File;
import java.nio.file.*;
import java.util.Base64;

public class PayNowQRGenerator {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java -jar paynowqr.jar <PayNowID> <PaymentReference> <Amount>");
            return;
        }

        try {
            String payNowID = args[0];
            String paymentReference = args[1];
            String amount = args[2];

            QRGenerator qrGenerator = new QRGeneratorImpl();

            // QR code color: PayNow purple
            // Add the PayNow logo in the center
            File logoFile = new File("E:\\Infor\\PayNow\\PayNow.png");
            if (!logoFile.exists()) {
                System.err.println("Logo file not found at: " + logoFile.getAbsolutePath());
                return;
            }
            String logoPath = logoFile.getAbsolutePath();

            QRDimensions qrDetails = qrGenerator.getQRDimensions(500, 500, Color.decode("#7C1A78"), logoPath);


            PayNow payNowObject = qrGenerator.getPayNowObject(
                    "0000",             // Merchant Category Code
                    "702",              // Currency (SGD)
                    "SG",               // Country Code
                    "MDI",              // Merchant Name
                    "Singapore",        // Merchant City
                    "SG.PAYNOW",        // Globally Unique ID
                    "2",                // Proxy Type: UEN
                    payNowID,           // PayNow ID
                    "0",                // editableAmountInd = 0 (no partial payments)
                    null,               // Expiry Date = none
                    "12",               // Point of Initiation Method = dynamic
                    amount,             // Amount
                    paymentReference    // Bill Number / Reference
            );

            // Required by SDK
            payNowObject.setPayloadFormatInd("01");

            QRGeneratorResponse response = qrGenerator.generateSGQR(QRType.PAY_NOW, payNowObject, qrDetails);

            if (response.getSgqrPayload() == null || response.getSgqrPayload().isEmpty()) {
                SGQRError error = response.getSgqrError();
                System.err.println("QR generation failed: " + error.getErrorCode() + " - " + error.getErrorMessage());
                return;
            }

            // Decode Base64 image
            byte[] imageBytes = Base64.getDecoder().decode(response.getImageStream());
            // Define target folder and file name
            String outputFolder = "E:\\Infor\\PayNow\\QR_Result";
            String fileName = "PayNowQR_Result.png";

            // Create the path
            Path outputPath = Paths.get(outputFolder, fileName);

            // Make sure the directory exists
            Files.createDirectories(Paths.get(outputFolder));

            // Write the QR image
            Files.write(outputPath, imageBytes);

            System.out.println("QR Code saved at: " + outputPath.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
