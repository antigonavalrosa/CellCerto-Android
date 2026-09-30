package br.com.cellcertobox221.app;

import android.app.Activity;
import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.webkit.WebView;
import android.view.ViewGroup;
import org.json.JSONTokener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AppFlowTest extends InstrumentationTestCase {
    private Activity activity;
    private WebView web;
    private String js(String code) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        String[] result = new String[1];
        getInstrumentation().runOnMainSync(() -> web.evaluateJavascript(code, value -> { result[0] = value; latch.countDown(); }));
        assertTrue("JavaScript callback timed out", latch.await(15, TimeUnit.SECONDS));
        return result[0];
    }
    private boolean ready(String expr) throws Exception { return "true".equals(js(expr)); }
    private void await(String expr, String message) throws Exception {
        long end = System.currentTimeMillis() + 60000;
        while (System.currentTimeMillis() < end) {
            if (ready(expr)) return;
            Thread.sleep(250);
        }
        screenshot("failure");
        fail(message + ": " + js("JSON.stringify({page:document.querySelector('.page.active').id,error:document.getElementById('bookingError').textContent,toast:document.getElementById('toast').textContent,jsError:window._qaError,service:selectedService,time:selectedTime,sending:bookingSending,pending:nativePending.size})"));
    }
    private void screenshot(String name) throws Exception {
        Thread.sleep(500);
        java.io.File dir = new java.io.File(activity.getExternalFilesDir(null), "qa");
        dir.mkdirs();
        android.graphics.Bitmap image = getInstrumentation().getUiAutomation().takeScreenshot();
        try (java.io.FileOutputStream out = new java.io.FileOutputStream(new java.io.File(dir, name + ".png"))) { image.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out); }
        image.recycle();
    }
    public void testRealAndroidBookingAndMedia() throws Exception {
        Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity = getInstrumentation().startActivitySync(intent);
        getInstrumentation().runOnMainSync(() -> web = (WebView)((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0));
        await("typeof show==='function' && document.querySelectorAll('.service').length>0", "App did not load");
        js("window.onerror=function(message){window._qaError=String(message)}");
        screenshot("home");
        js("show('reviews')");
        await("Array.from(document.querySelectorAll('#reviews img')).every(i=>i.complete&&i.naturalWidth>0)", "Review media missing");
        screenshot("reviews");
        js("show('works')");
        await("Array.from(document.querySelectorAll('#works img')).every(i=>i.complete&&i.naturalWidth>0)", "Work media missing");
        screenshot("works");
        js("openCat('Celulares');document.querySelector('.service').click();document.getElementById('bookDate').value='2099-10-01';updateSlots();document.querySelector('.slot').click();goBookingStep(3);document.getElementById('bookName').value='TESTE_AUTOMATICO_CELLCERTO_1_0_3';document.getElementById('bookPhone').value='00000000000';document.getElementById('bookObs').value='Registro temporário criado pelo teste Android e removido após validação';reviewBooking()");
        assertTrue("Review is not visible: " + js("document.getElementById('toast').textContent"), ready("!document.getElementById('bookingReview').classList.contains('hidden')"));
        screenshot("booking-review");
        js("document.getElementById('confirmBooking').click()");
        await("document.getElementById('tracking').classList.contains('active') && localStorage.getItem('cc_last_protocol')", "Real booking failed");
        await("document.getElementById('trackResult').textContent.includes('TESTE_AUTOMATICO_CELLCERTO_1_0_3')", "Real tracking failed");
        android.util.Log.i("CellCertoTest", "PASS_REAL_BOOKING_PROTOCOL=" + new JSONTokener(js("localStorage.getItem('cc_last_protocol')")).nextValue());
        screenshot("tracking");
        js("show('more')");
        screenshot("more");
        assertEquals("\"rgb(245, 251, 255)\"", js("getComputedStyle(document.querySelector('.menuRow b')).color"));
        js("show('home');document.querySelector('.quickStat:last-child').click()");
        assertTrue(ready("document.getElementById('reviews').classList.contains('active')"));
        getInstrumentation().runOnMainSync(() -> activity.finish());
    }
}
