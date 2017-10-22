package com.receiptshares.user.dao

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Update
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository

import static org.springframework.data.mongodb.core.query.Criteria.where
import static org.springframework.data.mongodb.core.query.Query.query
import static org.springframework.data.mongodb.core.query.Update.update

class RememberMeTokenRepository implements PersistentTokenRepository {

    MongoTemplate mongo

    RememberMeTokenRepository(MongoTemplate mongo) {
        this.mongo = mongo
    }

    @Override
    void createNewToken(PersistentRememberMeToken token) {
        mongo.upsert(query(where("username").is(token.username)), update("username", token.username)
                                                                      .set("series", token.series)
                                                                      .set("tokenValue", token.tokenValue)
                                                                      .set("date", token.date),
        RememberMeTokenEntity)
    }

    @Override
    void updateToken(String series, String tokenValue, Date lastUsed) {
        mongo.update(RememberMeTokenEntity).matching(query(where("series").is(series)))
             .apply(update("tokenValue", tokenValue).set("date", lastUsed))
             .first()
    }

    @Override
    PersistentRememberMeToken getTokenForSeries(String seriesId) {
        def result = mongo.query(RememberMeTokenEntity).matching(query(where("series").is(seriesId)))
                          .first().get()
        return new PersistentRememberMeToken(result.username, result.series, result.tokenValue, result.date)
    }

    @Override
    void removeUserTokens(String username) {
        mongo.remove(query(where("username").is(username)), PersistentRememberMeToken)
    }
}
