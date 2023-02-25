package com.saggitt.omega.smartspace;

import android.content.Context;
import android.util.Log;

import com.google.protobuf.MessageLite;
import com.saggit.omega.smartspace.SmartspaceProto.CardWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ProtoStore {
    private final Context mContext;

    public ProtoStore(Context context) {
        mContext = context.getApplicationContext();
    }

    public void store(MessageLite messageNano, String name) {
        try {
            FileOutputStream openFileOutput = mContext.openFileOutput(name, 0);
            if (messageNano != null) {
                try {
                    openFileOutput.write(messageNano.toByteArray());
                } catch (Throwable th) {
                    if (openFileOutput != null) {
                        openFileOutput.close();
                    }
                    throw th;
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("deleting ");
                sb.append(name);
                Log.d(name, sb.toString());
                mContext.deleteFile(name);
            }
            if (openFileOutput != null) {
                openFileOutput.close();
            }
        } catch (FileNotFoundException e) {
            Log.d("ProtoStore", "file does not exist " + name);
        } catch (Exception e) {
            Log.e("ProtoStore", "unable to write file " + name, e);
        }
    }

    public <T extends MessageLite> boolean load(String name, CardWrapper wrapper) {
        File fileStreamPath = mContext.getFileStreamPath(name);
        try {
            FileInputStream fileInputStream = new FileInputStream(fileStreamPath);
            byte[] bArr = new byte[((int) fileStreamPath.length())];
            fileInputStream.read(bArr, 0, bArr.length);
            //MessageNano.mergeFrom(wrapper, bArr);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return true;
        } catch (FileNotFoundException ex2) {
            Log.d("ProtoStore", "no cached data");
        } catch (Exception ex) {
            Log.e("ProtoStore", "unable to load data", ex);
        }
        return false;
    }
}