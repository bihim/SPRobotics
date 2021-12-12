package com.sprobotics.model.courseresponse;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.sprobotics.util.MethodClass;

public class CourseListResponse{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("popular")
	private List<DataItem> popular;

	@SerializedName("response")
	private boolean response;

	@SerializedName("message")
	private String message;

	@SerializedName("error")
	private String error;

	public List<DataItem> getData(){
		return MethodClass.defaultWhenNull(data,new ArrayList<>());
	}

	public List<DataItem> getPopular(){
		return MethodClass.defaultWhenNull(popular,new ArrayList<>());
	}

	public boolean isResponse(){
		return response;
	}

	public String getMessage(){
		return message;
	}

	public String getError(){
		return error;
	}
}