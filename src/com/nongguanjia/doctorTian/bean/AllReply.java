package com.nongguanjia.doctorTian.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AllReply implements Parcelable{
	private String Title;
	private String Image;
	private String Message;
	private String ReplyDate;
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getReplyDate() {
		return ReplyDate;
	}
	public void setReplyDate(String replyDate) {
		ReplyDate = replyDate;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(Title);
		out.writeString(Image);
		out.writeString(Message);
		out.writeString(ReplyDate);
	}
	
	
	public static final Parcelable.Creator<AllReply> CREATOR = new Creator<AllReply>(){

		@Override
		public AllReply createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			AllReply reply = new AllReply();

			reply.Title = in.readString();
			reply.Image = in.readString();
			reply.Message = in.readString();
			reply.ReplyDate = in.readString();
			
			return reply;
		}

		@Override
		public AllReply[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AllReply[size];
		}
		
	};
	
}
