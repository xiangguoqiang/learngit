package com.baidu.tieba.interviewlive;

import java.util.ArrayList;
import java.util.List;

import com.baidu.tbadk.core.data.AntiData;
import org.json.JSONArray;
import org.json.JSONObject;

import tbclient.GetInterview.DanmuInfo;
import tbclient.GetInterview.DataRes;
import tbclient.GetInterview.InterviewDetail;
import android.content.Context;

import com.baidu.adp.lib.util.BdLog;
import com.baidu.tbadk.core.data.TaskInfoData;


public class InterviewLiveData {
	
	private List<DanmuInfoData> danmuList;
	
	private List<InterviewDetailData> interviewDetail;
	
	private TaskInfoData taskInfoData;

	private AntiData antiData;
	
	public InterviewLiveData() {
		danmuList = new ArrayList<DanmuInfoData>();
		interviewDetail = new ArrayList<InterviewDetailData>();
		taskInfoData = new TaskInfoData();
		antiData = new AntiData();
	}
	
	public List<DanmuInfoData> getDanmuListData() {
		return danmuList;
	}

	public List<InterviewDetailData> getInterviewDetail() {
		return interviewDetail;
	}
	
	public void setInterviewDetail(List<InterviewDetailData> interviewDetail) {
		this.interviewDetail = interviewDetail;
	}

	public TaskInfoData getTaskInfoData() {
		return taskInfoData;
	}

	public AntiData getAntiData() {
		return this.antiData;
	}
	
	public void parserProtoBuf(DataRes data) {
		parserProtoBuf(data, null);
	}
	public void parserProtoBuf(DataRes data, Context context) {
		if (data == null) {
			return;
		}
		
		List<DanmuInfo> danmuList = data.danmu_list;
		if (danmuList != null && danmuList.size() > 0) {
			for (int i = 0; i < danmuList.size(); i++) {
				DanmuInfoData damuInfoData = new DanmuInfoData();
				damuInfoData.parserProtoBuf(danmuList.get(i));
				this.danmuList.add(damuInfoData);
			}
		}
		
		List<InterviewDetail> interviewList = data.interview_list;
		if (interviewList != null && interviewList.size() > 0) {
			for (int i = 0; i < interviewList.size(); i++) {
				InterviewDetailData interviewDetailData = new InterviewDetailData();
				interviewDetailData.parserProtoBuf(interviewList.get(i), context);
				this.interviewDetail.add(interviewDetailData);
			}
		}
		
		taskInfoData = new TaskInfoData();
		taskInfoData.parserProtobuf(data.task_info);
		antiData.parserProtobuf(data.anti);
	}
	
	public void parserJson(String data) {
		try {
			JSONObject json = new JSONObject(data);

			parserJson(json, null);
		} catch (Exception ex) {
			BdLog.detailException(ex);
		}
	}
	
	public void parserJson(JSONObject json, Context context) {
		if (json == null) {
			return;
		}
		
		JSONArray damuList = null;
		damuList = json.optJSONArray("danmu_list");
		if (damuList != null) {
			for (int i = 0; i < damuList.length(); i++) {
				DanmuInfoData danmuInfoData = new DanmuInfoData();
				danmuInfoData.parserJson(damuList.optJSONObject(i));
				this.danmuList.add(danmuInfoData);
			}
		}
		
		JSONArray interviewList = null;
		interviewList = json.optJSONArray("interview_list");
		if (interviewList != null) {
			for (int i = 0; i < interviewList.length(); i++) {
				InterviewDetailData interviewDetailData = new InterviewDetailData();
				interviewDetailData.parserJson(interviewList.optJSONObject(i), context);
				this.interviewDetail.add(interviewDetailData);
			}
		}
		
		JSONObject taskObject = json.optJSONObject("task_info");
		if (taskObject != null) {
			taskInfoData.parserJson(taskObject);
		}

		JSONObject antiObject = json.optJSONObject("anti");
		if(antiObject != null) {
			antiData.parserJson(antiObject);

		}
	}
	
}
