package com.sproboticworks.model;

public class FaqModel {
    private String buttonText;
    private String layoutText;

    public FaqModel(String buttonText, String layoutText) {
        this.buttonText = buttonText;
        this.layoutText = layoutText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getLayoutText() {
        return layoutText;
    }

    public void setLayoutText(String layoutText) {
        this.layoutText = layoutText;
    }
}
