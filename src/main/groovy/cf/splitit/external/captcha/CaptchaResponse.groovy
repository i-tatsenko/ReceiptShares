package cf.splitit.external.captcha

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.transform.ToString

@CompileStatic
@ToString
@PackageScope
class CaptchaResponse {
    boolean success
    String challenge_ts
    String hostname
    @JsonProperty("error-codes")
    String[] errorCodes
}
