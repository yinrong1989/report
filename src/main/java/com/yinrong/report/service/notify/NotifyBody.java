package com.yinrong.report.service.notify;

import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public abstract class NotifyBody {

    abstract String getMsgtype();

    public RequestBody toRequestBody() {
        String body = JSON.toJSONString(this);
        return RequestBody.create(body, MediaType.get("application/json; charset=utf-8"));
    }
}
