package com.dudu.wearlauncher.utils;
import android.util.Log;
import com.blankj.utilcode.util.FileIOUtils;
import java.io.File;

public class ILog {
    /*
    *规范log从我做起
    **/
    
    public static void v(String msg) {
    	Log.v(getCallerName(),msg);
    }
    
    public static void d(String msg) {
    	Log.d(getCallerName(),msg);
    }

    public static void i(String msg) {
    	Log.i(getCallerName(),msg);
    }
    
    public static void w(String msg) {
    	Log.w(getCallerName(),msg);
    }
    
    public static void e(String msg) {
    	Log.e(getCallerName(),msg);
    }
    public static void writeThrowableToFile(Throwable throwable,File file) {
        FileIOUtils.writeFileFromString(file,throwable.getMessage()+"\n");
    	StackTraceElement[] elements = throwable.getStackTrace();
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement element : elements) {
        	sb.append(element);
            sb.append("\n");
        }
        FileIOUtils.writeFileFromString(file,sb.toString(),true);
    }
    
    private static String getCallerName() {
        //0 VMStack.getThreadStackTrace
        //1 Thread.getStackTrace
        //2 LogUtil.getCallerInfo
        //3 LogUtil.e
        //4 Caller
    	StackTraceElement st = Thread.currentThread().getStackTrace()[4];
        return st.getClassName() + "." +/*st.getMethodName()+*/"[Line:" + st.getLineNumber() + "]";
    }
    
}
