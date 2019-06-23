package com.yyj.bestbase.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class UpdateBean implements Parcelable {

    private Integer versionCode;

    private String versionName;

    private String versionInfo;

    private String downLoadUrl;

    private Integer isForce;


    protected UpdateBean(Parcel in) {
        if (in.readByte() == 0) {
            versionCode = null;
        } else {
            versionCode = in.readInt();
        }
        versionName = in.readString();
        versionInfo = in.readString();
        downLoadUrl = in.readString();
        if (in.readByte() == 0) {
            isForce = null;
        } else {
            isForce = in.readInt();
        }
    }

    public static final Creator<UpdateBean> CREATOR = new Creator<UpdateBean>() {
        @Override
        public UpdateBean createFromParcel(Parcel in) {
            return new UpdateBean(in);
        }

        @Override
        public UpdateBean[] newArray(int size) {
            return new UpdateBean[size];
        }
    };

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public Integer getIsForce() {
        return isForce;
    }

    public void setIsForce(Integer isForce) {
        this.isForce = isForce;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeString(versionInfo);
        dest.writeString(downLoadUrl);
        dest.writeInt(isForce);
    }


}
