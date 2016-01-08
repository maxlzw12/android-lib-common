package com.aaron.core.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.XMLReader;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Html文件显示在textView中
 * @author lzhiwei
 *
 * 2014-12-19
 */
public class HtmlPrinter {
	private TextView textView;
	private String content;
	
	public HtmlPrinter(TextView textView,String htmlContent) {
		this.textView = textView;
		this.content = htmlContent;
	}
	
	public void show(){
		MyImageGetter getter = new MyImageGetter();  
        CharSequence charSequence = Html.fromHtml(content, getter ,new MyTagHandler());    
        textView.setText(charSequence);    
        textView.setMovementMethod(LinkMovementMethod.getInstance());  
	}
	
	
	class MyTagHandler implements TagHandler {  
		  
        @Override  
        public void handleTag(boolean opening, String tag, Editable output,  
                XMLReader xmlReader) {  
        	 if(tag.toLowerCase().equals("title") && !opening) {  
                 // 清除标题文字  
                 output.clear();  
             }
        }  
          
    }  
      
    class MyImageGetter implements ImageGetter {  
          
        @Override  
        public Drawable getDrawable(String source) {  
            Drawable drawable = null;  
            HttpGet get = new HttpGet(source);  
            // TODO 如果路径没有http头 则直接返回
            if(get.getAllHeaders().length == 0) {
            	return null;
            }
            HttpClient client = new DefaultHttpClient();  
            try {  
                HttpResponse response = client.execute(get);  
                if(response.getStatusLine().getStatusCode() == 200) {  
                    InputStream is = response.getEntity().getContent();  
                    drawable = BitmapDrawable.createFromStream(is, "");  
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable  
                            .getIntrinsicHeight());  
                    is.close();  
                }  
            } catch (ClientProtocolException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            return drawable;  
        }  
          
    }  
}
