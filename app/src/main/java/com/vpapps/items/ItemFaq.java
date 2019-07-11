package com.vpapps.items;

import com.sysdata.widget.accordion.Item;

import java.io.Serializable;

public class ItemFaq implements Serializable {
    private String id;
    private String faq_ques;
    private String faq_ans;


    public ItemFaq(String id, String faq_ques, String faq_ans) {
        this.id = id;
        this.faq_ques = faq_ques;
        this.faq_ans = faq_ans;
    }

    public String getFaq_ques() {
        return faq_ques;
    }

    public String getFaq_ans() {
        return faq_ans;
    }


}
