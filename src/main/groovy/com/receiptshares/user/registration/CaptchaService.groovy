package com.receiptshares.user.registration

import rx.Observable

interface CaptchaService {

    Observable<Boolean> verify(String token)

}