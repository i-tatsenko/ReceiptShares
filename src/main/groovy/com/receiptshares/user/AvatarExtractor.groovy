package com.receiptshares.user

import org.springframework.social.facebook.api.Facebook
import org.springframework.social.facebook.api.ImageType

class AvatarExtractor {

    def api

    def getImage() {
        if (api instanceof Facebook) {
            return extractFromFacebook()
        }
        throw new IllegalArgumentException()
    }

    byte[] extractFromFacebook() {
        api.userOperations().getUserProfileImage(ImageType.ALBUM)
    }
}
