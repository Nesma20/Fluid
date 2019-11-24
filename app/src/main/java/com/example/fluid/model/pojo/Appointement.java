package com.example.fluid.model.pojo;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointement implements Parcelable {

    @SerializedName("MRN")
    @Expose
    private String mRN;
    @SerializedName("arabic_name")
    @Expose
    private String arabicName;
    @SerializedName("english_name")
    @Expose
    private String englishName;
    @SerializedName("sex_code")
    @Expose
    private String sexCode;
    @SerializedName("scheduled_time")
    @Expose
    private String scheduledTime;
    @SerializedName("expected_time")
    @Expose
    private String expectedTime;
    @SerializedName("checkin_time")
    @Expose
    private String checkinTime;
    @SerializedName("arrival_time")
    @Expose
    private String arrivalTime;
    @SerializedName("calling_time")
    @Expose
    private String callingTime;
    @SerializedName("slot_id")
    @Expose
    private String slotId;
    @SerializedName("is_followup")
    @Expose
    private String isFollowup;

    protected Appointement(Parcel in) {
        mRN = in.readString();
        arabicName = in.readString();
        englishName = in.readString();
        sexCode = in.readString();
        scheduledTime = in.readString();
        expectedTime = in.readString();
        checkinTime = in.readString();
        arrivalTime = in.readString();
        callingTime = in.readString();
        slotId = in.readString();
        isFollowup = in.readString();
    }

    public static final Creator<Appointement> CREATOR = new Creator<Appointement>() {
        @Override
        public Appointement createFromParcel(Parcel in) {
            return new Appointement(in);
        }

        @Override
        public Appointement[] newArray(int size) {
            return new Appointement[size];
        }
    };

    public String getMRN() {
        return mRN;
    }

    public void setMRN(String mRN) {
        this.mRN = mRN;
    }

    public String getArabicName() {
        return arabicName;
    }

    public void setArabicName(String arabicName) {
        this.arabicName = arabicName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getSexCode() {
        return sexCode;
    }

    public void setSexCode(String sexCode) {
        this.sexCode = sexCode;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    public String getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(String checkinTime) {
        this.checkinTime = checkinTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getCallingTime() {
        return callingTime;
    }

    public void setCallingTime(String callingTime) {
        this.callingTime = callingTime;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getIsFollowup() {
        return isFollowup;
    }

    public void setIsFollowup(String isFollowup) {
        this.isFollowup = isFollowup;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRN);
        dest.writeString(arabicName);
        dest.writeString(englishName);
        dest.writeString(sexCode);
        dest.writeString(scheduledTime);
        dest.writeString(expectedTime);
        dest.writeString(checkinTime);
        dest.writeString(arrivalTime);
        dest.writeString(callingTime);
        dest.writeString(slotId);
        dest.writeString(isFollowup);
    }
}