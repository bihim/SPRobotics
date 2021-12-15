package com.sproboticworks.model;

import java.util.List;

public class CourseDetailsStaticModel {
    private String courseDetails;
    private int courseImageId;
    private List<String> whatWillMyChildLearn;
    private List<String> whatWillMyChildLearnTitle;


    public CourseDetailsStaticModel(String courseDetails, int courseImageId, List<String> whatWillMyChildLearn, List<String> whatWillMyChildLearnTitle) {
        this.courseDetails = courseDetails;
        this.courseImageId = courseImageId;
        this.whatWillMyChildLearn = whatWillMyChildLearn;
        this.whatWillMyChildLearnTitle = whatWillMyChildLearnTitle;
    }

    public List<String> getWhatWillMyChildLearnTitle() {
        return whatWillMyChildLearnTitle;
    }

    public void setWhatWillMyChildLearnTitle(List<String> whatWillMyChildLearnTitle) {
        this.whatWillMyChildLearnTitle = whatWillMyChildLearnTitle;
    }

    public int getCourseImageId() {
        return courseImageId;
    }

    public void setCourseImageId(int courseImageId) {
        this.courseImageId = courseImageId;
    }

    public String getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(String courseDetails) {
        this.courseDetails = courseDetails;
    }

    public List<String> getWhatWillMyChildLearn() {
        return whatWillMyChildLearn;
    }

    public void setWhatWillMyChildLearn(List<String> whatWillMyChildLearn) {
        this.whatWillMyChildLearn = whatWillMyChildLearn;
    }
}
