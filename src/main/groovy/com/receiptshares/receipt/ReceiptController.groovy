package com.receiptshares.receipt

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/rec")
class ReceiptController {

    @RequestMapping(value = '/current', method = RequestMethod.GET)
    @ResponseBody
    def current() {
        return "ok"
    }
}
