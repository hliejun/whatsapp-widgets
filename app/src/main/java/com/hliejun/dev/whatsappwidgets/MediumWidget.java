package com.hliejun.dev.whatsappwidgets;

import android.app.PendingIntent;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import android.net.Uri;

import android.provider.ContactsContract;

import android.util.TypedValue;

import android.view.View;

import android.widget.RemoteViews;

import com.hliejun.dev.whatsappwidgets.models.Contact;
import com.hliejun.dev.whatsappwidgets.models.PaletteColor;
import com.hliejun.dev.whatsappwidgets.models.WidgetData;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link ConfigurationActivity ConfigurationActivity}
 */
public class MediumWidget extends AppWidgetProvider {

    private static final String URI_PREFIX_CALL = "content://com.android.contacts/data/";
    private static final String PACKAGE_NAME_WHATSAPP = "com.whatsapp";
    private static final String MIMETYPE_VOICE = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call";
    private static final String MIMETYPE_VIDEO = "vnd.android.cursor.item/vnd.com.whatsapp.video.call";
    private static final String MIMETYPE_CHAT = "vnd.android.cursor.item/vnd.com.whatsapp.profile";

    /*** Lifecycle ***/

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.medium_widget);
        WidgetData data = ConfigurationActivity.loadWidgetPref(context, appWidgetId);

        // Stop if no data available to update widget
        if (data == null) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
            return;
        }

        // Get config data
        Contact contact = data.getContact();
        PaletteColor color = data.getColor();

        // Setup
        setupAvatar(context, views, data, contact, color);
        setupTexts(views, data, contact);
        setupColors(context, views, color);
        setupTextSizes(context, views, data);
        setupIntents(context, appWidgetId, views, contact);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            ConfigurationActivity.deleteWidgetPref(context, appWidgetId);
        }
    }

    /*** Setup ***/

    private static void setupAvatar(Context context, RemoteViews views, WidgetData data, Contact contact, PaletteColor color) {
        Uri photoUri = contact.getPhoto();
        if (!data.isShouldUseAvatar()) {
            views.setViewVisibility(R.id.medium_widget_avatar_container, View.GONE);
            views.setViewVisibility(R.id.medium_widget_avatar, View.GONE);
            views.setViewVisibility(R.id.medium_widget_initials, View.GONE);
        } else {
            try {
                views.setViewVisibility(R.id.medium_widget_avatar_container, View.VISIBLE);
                views.setViewVisibility(R.id.medium_widget_avatar, View.VISIBLE);
                views.setViewVisibility(R.id.medium_widget_initials, View.GONE);
                Bitmap bitmap = getCircularBitmap(photoUri, color.getInt(), context);
                views.setImageViewBitmap(R.id.medium_widget_avatar, bitmap);
            } catch (Exception e) {
                views.setViewVisibility(R.id.medium_widget_avatar_container, View.VISIBLE);
                views.setViewVisibility(R.id.medium_widget_avatar, View.VISIBLE);
                views.setViewVisibility(R.id.medium_widget_initials, View.VISIBLE);
                views.setTextViewText(R.id.medium_widget_initials, contact.getInitials());
                views.setImageViewResource(R.id.medium_widget_avatar, R.drawable.cs_circle);
                int backingColorId = color.isLightColor() ? R.color.colorInverseFade : R.color.colorFade;
                views.setInt(R.id.medium_widget_avatar, "setColorFilter",
                        context.getResources().getColor(backingColorId));
                views.setTextColor(R.id.medium_widget_initials,
                        context.getResources().getColor(R.color.colorFadeText));
            }
        }
    }

    private static void setupTexts(RemoteViews views, WidgetData data, Contact contact) {
        // Setup text fields
        views.setTextViewText(R.id.medium_widget_label, data.getLabel());
        views.setTextViewText(R.id.medium_widget_description, data.getDescription());

        // Setup phone number
        if (data.isShouldUseNumber()) {
            views.setViewVisibility(R.id.medium_widget_number, View.VISIBLE);
            views.setTextViewText(R.id.medium_widget_number, contact.getNumber());
        } else {
            views.setViewVisibility(R.id.medium_widget_number, View.GONE);
        }
    }

    private static void setupColors(Context context, RemoteViews views, PaletteColor color) {
        // Setup card style
        views.setInt(R.id.medium_widget_card, "setBackgroundColor", color.getInt());

        // Setup text colors
        int primaryTextColor = color.isLightColor()
                ? context.getResources().getColor(R.color.colorInversePrimaryText)
                : context.getResources().getColor(R.color.colorPrimaryText);
        int hintTextColor = color.isLightColor()
                ? context.getResources().getColor(R.color.colorInverseHintText)
                : context.getResources().getColor(R.color.colorHintText);
        views.setTextColor(R.id.medium_widget_label, primaryTextColor);
        views.setTextColor(R.id.medium_widget_description, hintTextColor);
        views.setTextColor(R.id.medium_widget_overline, primaryTextColor);
        views.setTextColor(R.id.medium_widget_number, primaryTextColor);
        views.setTextColor(R.id.medium_widget_voice, primaryTextColor);
        views.setTextColor(R.id.medium_widget_video, primaryTextColor);
        views.setTextColor(R.id.medium_widget_chat, primaryTextColor);

        // Setup glyph colors
        if (color.isLightColor()) {
            views.setTextViewCompoundDrawables(R.id.medium_widget_voice, 0, R.drawable.ic_phone_widget_black, 0, 0);
            views.setTextViewCompoundDrawables(R.id.medium_widget_video, 0, R.drawable.ic_video_widget_black, 0, 0);
            views.setTextViewCompoundDrawables(R.id.medium_widget_chat, 0, R.drawable.ic_chat_widget_black, 0, 0);
            views.setTextViewCompoundDrawables(R.id.medium_widget_settings, 0, 0, R.drawable.ic_more_widget_black, 0);
        } else {
            views.setTextViewCompoundDrawables(R.id.medium_widget_voice, 0, R.drawable.ic_phone_widget, 0, 0);
            views.setTextViewCompoundDrawables(R.id.medium_widget_video, 0, R.drawable.ic_video_widget, 0, 0);
            views.setTextViewCompoundDrawables(R.id.medium_widget_chat, 0, R.drawable.ic_chat_widget, 0, 0);
            views.setTextViewCompoundDrawables(R.id.medium_widget_settings, 0, 0, R.drawable.ic_more_widget, 0);
        }
    }

    private static void setupTextSizes(Context context, RemoteViews views, WidgetData data) {
        if (data.isShouldUseLargeText()) {
            views.setTextViewTextSize(R.id.medium_widget_label, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_label_large_num));
            views.setTextViewTextSize(R.id.medium_widget_description, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_description_large_num));
            views.setTextViewTextSize(R.id.medium_widget_overline, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_overline_large_num));
            views.setTextViewTextSize(R.id.medium_widget_number, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_overline_large_num));
            views.setTextViewTextSize(R.id.medium_widget_voice, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_button_large_num));
            views.setTextViewTextSize(R.id.medium_widget_video, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_button_large_num));
            views.setTextViewTextSize(R.id.medium_widget_chat, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_button_large_num));
        } else {
            views.setTextViewTextSize(R.id.medium_widget_label, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_label_num));
            views.setTextViewTextSize(R.id.medium_widget_description, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_description_num));
            views.setTextViewTextSize(R.id.medium_widget_overline, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_overline_num));
            views.setTextViewTextSize(R.id.medium_widget_number, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_overline_num));
            views.setTextViewTextSize(R.id.medium_widget_voice, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_button_num));
            views.setTextViewTextSize(R.id.medium_widget_video, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_button_num));
            views.setTextViewTextSize(R.id.medium_widget_chat, TypedValue.COMPLEX_UNIT_SP,
                    context.getResources().getDimension(R.dimen.medium_widget_text_button_num));
        }
    }

    private static void setupIntents(Context context, int appWidgetId, RemoteViews views, Contact contact) {
        // Setup config intent
        Intent configIntent = new Intent(context, ConfigurationActivity.class);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingConfigIntent = PendingIntent.getActivity(context, appWidgetId, configIntent, 0);
        views.setOnClickPendingIntent(R.id.medium_widget_settings, pendingConfigIntent);

        // Setup profile intent
        Intent profileIntent = new Intent(Intent.ACTION_VIEW);
        Uri profileUri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, String.valueOf(contact.getWhatsAppId()));
        profileIntent.setData(profileUri);
        PendingIntent pendingProfileIntent = PendingIntent.getActivity(context, appWidgetId, profileIntent, 0);
        views.setOnClickPendingIntent(R.id.medium_widget_avatar, pendingProfileIntent);

        // Setup voice call intent
        views.setOnClickPendingIntent(R.id.medium_widget_voice,
                getWhatsAppAction(context, MIMETYPE_VOICE, contact.getWhatsAppId()));

        // Setup video call intent
        views.setOnClickPendingIntent(R.id.medium_widget_video,
                getWhatsAppAction(context, MIMETYPE_VIDEO, contact.getWhatsAppId()));

        // Setup chat intent
        views.setOnClickPendingIntent(R.id.medium_widget_chat,
                getWhatsAppAction(context, MIMETYPE_CHAT, contact.getWhatsAppId()));
    }

    /*** Auxiliary ***/

    private static Bitmap getCircularBitmap(Uri photoUri, final int color, Context context) throws Exception {
        if (photoUri == null) {
            throw new Exception("NULL Bitmap path");
        }

        Bitmap bitmap = decodeBitmapUri(photoUri, context, 1024);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private static Bitmap decodeBitmapUri(Uri fileUri, Context context, int requiredSize) throws Exception {
        BitmapFactory.Options sampleOptions = new BitmapFactory.Options();
        sampleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(fileUri), null, sampleOptions);

        int widthTemp = sampleOptions.outWidth;
        int heightTemp = sampleOptions.outHeight;
        int scale = 1;
        while (widthTemp > requiredSize || heightTemp > requiredSize) {
            widthTemp /= 2;
            heightTemp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options renderOptions = new BitmapFactory.Options();
        renderOptions.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(fileUri), null, renderOptions);

        return bitmap;
    }

    private static long getWhatsAppActionId(Context context, String contactId, String mimeType) {
        ContentResolver resolver = context.getContentResolver();

        String selection = ContactsContract.Data.MIMETYPE + " = ? AND "
                         + ContactsContract.Data.RAW_CONTACT_ID + " = ? ";
        String[] selectionArgs = new String[] { mimeType, contactId };

        Cursor cursor = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                null, selection, selectionArgs, null);

        long actionId = 0;
        while (cursor.moveToNext()) {
            actionId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
        }

        return actionId;
    }

    private static PendingIntent getWhatsAppAction(Context context, String mimeType, String contactId) {
        long actionId = getWhatsAppActionId(context, contactId, mimeType);
        String intentPath = URI_PREFIX_CALL + actionId;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(intentPath), mimeType);
        intent.setPackage(PACKAGE_NAME_WHATSAPP);

        return PendingIntent.getActivity(context, 0, intent, 0);
    }

}

