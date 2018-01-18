import {Observable} from "rxjs/Rx";

class HttpService {
    post(url) {
        return exchange(() => $.post(url))
    }

    get(url) {
        return exchange(() => $.get(url))
    }
}

function exchange(jqXHRProducer) {
    return Observable.defer(() => Observable.fromPromise(jqXHRProducer()))
                     .retryWhen(errors => {
                         return errors.flatMap(data => {
                             let retryAfterSeconds = data.getResponseHeader("Retry-After");
                             if (data.status === 429 && retryAfterSeconds) {
                                 console.log(`Rate limit exceeded. Retrying after ${retryAfterSeconds}s`);
                                 return Observable.timer(+retryAfterSeconds * 1000);
                             }
                             return Observable.error(data.responseJSON);
                         })
                     })
}


export const httpService = new HttpService();