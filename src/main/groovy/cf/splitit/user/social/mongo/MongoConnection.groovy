package cf.splitit.user.social.mongo

import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.hibernate.validator.constraints.NotEmpty
import org.hibernate.validator.constraints.Range
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "connections")
@CompoundIndexes(
    //
    @CompoundIndex(name = "connections_primary_idx", def = "{'userId': 1, 'providerId': 1, 'providerUserId': 1}", unique = true)
)
@CompileStatic
class MongoConnection {
    @Id
    private ObjectId id

    @NotEmpty
    String userId

    @NotEmpty
    String providerId

    String providerUserId

    @Range(min = 1L, max = 9999L)
    int rank //not null
    String displayName
    String profileUrl
    String imageUrl

    @NotEmpty
    String accessToken

    String secret
    String refreshToken
    Long expireTime

    ObjectId getId() {
        return id
    }

    String getUserId() {
        return userId
    }

    void setUserId(String userId) {
        this.userId = userId
    }

    String getProviderId() {
        return providerId
    }

    void setProviderId(String providerId) {
        this.providerId = providerId
    }

    String getProviderUserId() {
        return providerUserId
    }

    void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId
    }

    int getRank() {
        return rank
    }

    void setRank(int rank) {
        this.rank = rank
    }

    String getDisplayName() {
        return displayName
    }

    void setDisplayName(String displayName) {
        this.displayName = displayName
    }

    String getProfileUrl() {
        return profileUrl
    }

    void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl
    }

    String getImageUrl() {
        return imageUrl
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl
    }

    String getAccessToken() {
        return accessToken
    }

    void setAccessToken(String accessToken) {
        this.accessToken = accessToken
    }

    String getSecret() {
        return secret
    }

    void setSecret(String secret) {
        this.secret = secret
    }

    String getRefreshToken() {
        return refreshToken
    }

    void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken
    }

    Long getExpireTime() {
        return expireTime
    }

    void setExpireTime(Long expireTime) {
        this.expireTime = expireTime
    }
}