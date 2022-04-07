package com.example.da_mientay_admin.Common;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.example.da_mientay_admin.Model.Admin;
import com.example.da_mientay_admin.Model.Category;
import com.example.da_mientay_admin.Model.Food;

import java.nio.charset.StandardCharsets;

public class Common {
    public static final String CATEGORY_REF ="Category" ;
    public static final String ORDER_REF ="Order" ;
    public  static Admin currentAdmin;
    public static final int FULL_WIDTH_COLUMN = 1;
    public  static final int DEFAULT_COLUMN_COUNT =0;
    public static Category categorySelected;
    public static Food selectedFood;

    public static void setSpanStringColor(String welcome, String name, TextView textView, int parseColor) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(welcome);
        SpannableString spannableString = new SpannableString(name);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan,0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(parseColor),0,name.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        textView.setText(builder,TextView.BufferType.SPANNABLE);

    }

    public static String covertStatusToString(int orderStatus) {
        switch (orderStatus)
        {
            case 0:
                return  "Đã đặt hàng";

            case -1:
                return "Đã hủy";
            default:
                return "Error";

        }
    }


    public enum ACTION{
        CREATE,
        UPDATE,
        DELETE
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes(StandardCharsets.UTF_8));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }


}
