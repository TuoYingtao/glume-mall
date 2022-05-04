package com.glume.glumemall.seckill.service;

import java.util.concurrent.ExecutionException;

/**
@author tuoyingtao
@create 2022-04-01 15:31
*/
public interface SeckillNoticeService {


    void uploadSendNotice() throws ExecutionException, InterruptedException;

    void sendCurrentEmailNotice();
}
