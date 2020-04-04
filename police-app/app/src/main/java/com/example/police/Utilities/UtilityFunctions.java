package com.example.police.Utilities;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.police.Modals.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class UtilityFunctions {
    public static SharedPrefManager sharedPrefManager;

    public static Bitmap getQRCode(String Id) {
        // Handle Null pointer exception carefully.

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(Id, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void setLoginSession(User user, Context context, String token){
        sharedPrefManager = SharedPrefManager.getInstance(context);
        sharedPrefManager.put(SharedPrefManager.Key.LOGIN_STATUS, true);
        sharedPrefManager.put(SharedPrefManager.Key.USER_NAME, user.getName());
        sharedPrefManager.put(SharedPrefManager.Key.USER_EMAIL, user.getEmail());
        sharedPrefManager.put(SharedPrefManager.Key.USER_PHONE, user.getPhone());
        sharedPrefManager.put(SharedPrefManager.Key.TOKEN, token);
    }

    public static void clearLoginSession( Context context){
        sharedPrefManager = SharedPrefManager.getInstance(context);
        sharedPrefManager.logOut();
    }


}
