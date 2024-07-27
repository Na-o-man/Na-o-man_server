package com.umc.naoman.domain.notification.service;

import kr.co.yermi.josamoa.Josamoa;

public class JosamoaSingleton {
    private static final Josamoa instance = new Josamoa();
    private JosamoaSingleton(){}

    /*
    * setJosa()는 받침이 있는 경우 앞 글자를, 받침이 없는 경우 뒷 글자를 반환합니다.
     */
    public static String setJosa(String str, String particle){
        return instance.setJosa(str,particle);
    }
}
