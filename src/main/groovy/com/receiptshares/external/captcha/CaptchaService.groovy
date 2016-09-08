package com.receiptshares.external.captcha

interface CaptchaService {

    boolean verify(String token)

}