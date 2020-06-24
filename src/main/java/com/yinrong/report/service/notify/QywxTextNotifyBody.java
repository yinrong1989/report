package com.yinrong.report.service.notify;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname QywxTextNotifyBody
 * @Description
 * @Date 2020/4/14 10:53 上午
 * @Created by yinrong
 */
public class QywxTextNotifyBody  extends NotifyBody{

    private Map<String, String> text = new HashMap<>();

    public QywxTextNotifyBody(String content) {
        this.text.put("content", content);
    }

    private String msgtype = "text";

    @Override
    public String getMsgtype() {
        return msgtype;
    }

    public Map<String, String> getText() {
        return text;
    }

}
