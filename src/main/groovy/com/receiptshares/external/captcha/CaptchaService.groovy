package com.receiptshares.external.captcha

import rx.Observable

interface CaptchaService {

    Observable<Boolean> verify(String token)

}